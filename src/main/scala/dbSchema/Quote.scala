package dbSchema

import org.json4s.native.Serialization
import org.json4s.ShortTypeHints
import org.json4s.Extraction
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

import scala.collection.mutable

case class Language(code: String) {
  val log = LoggerFactory.getLogger(getClass.getName)
  var canonicalScript: String = transliterator.scriptUnknown
  code match {
    case "sa" => canonicalScript = transliterator.scriptDevanAgarI
    case "en" => canonicalScript = "en"
    case _ => canonicalScript = transliterator.scriptUnknown
  }

}
case class ScriptRendering(text: String, scheme: String = transliterator.scriptUnknown,
                           var startLetter: String = "") {
  startLetter = text.toList.head.toString
  val log = LoggerFactory.getLogger(getClass.getName)

  // A unique identifier for a text rendering.
  def getKey: String = {
    scheme match {
      case transliterator.scriptDevanAgarI => {
        return transliterator.transliterate(
          text.replaceAll("\\P{IsDevanagari}", "").replaceAll("[।॥०-९]+", "").replaceAll("\\s", ""), "dev", "optitrans")
          .replaceAll("[MNn]", "m")
      }
      case unknownScript => {
        log warn (s"got script $unknownScript for text [$text]")
        return text.replaceAll("\\s", "")
      }
    }
  }
}

case class QuoteText(scriptRenderings: List[ScriptRendering],
                     var key: String = "",
                     language: Language = Language("UNK"),
                     metre: String = "UNK") {
  val log = LoggerFactory.getLogger(getClass.getName)
  // Helps make primary keys (aka ids) for storing these case classes in databases.
  if (scriptRenderings.nonEmpty) {
    val canonicalRendering = scriptRenderings.filter(_.scheme == language.canonicalScript).headOption
    val rendering = canonicalRendering.getOrElse(scriptRenderings.head)
    key = rendering.getKey
  }
  def this(text: String, script: String, language: Language) =
    this(scriptRenderings=List(text).filter(_.nonEmpty).map(x => ScriptRendering(text=x,scheme=script)),
      language=language)
  def this(text: String) = this(text=text, script=transliterator.scriptUnknown, language = Language("UNK"))
}


/*
{
"_id": "amarakosha",
"name": "अमरकोशः",
"authors": ["अमरसिंहः"],
"licenseLink": "http://some-link",
"canonicalSource": "http://some-link",
"categories": ["sanskrit to sanskrit", "thesaurus"],
"issuePage": "https://github.com/sanskrit-coders/stardict-sanskrit/issues"
}
 */

case class Source(name: QuoteText, authors: List[QuoteText] = List[QuoteText](), var key: String = "",
                  categories: List[String] = null, license: String = null,
                  canonicalSource: String = null, issuePage: String = null) {
  key = s"${name.key}__${authors.sortBy(_.scriptRenderings.head.text).map(_.key).mkString("_")}"

  // The below causes a mysterious failure (as of 20170326):
  // lift json serialization stops working.
  // Hence commenting out.
//  def this(nameIn: String, author: String, script: String, language: String) =
//    this(name= new QuoteText(text=nameIn, script = script, language = Language(language)),
//      authors = List(author).filter(_.nonEmpty).map(x =>
//        new QuoteText(text=x, script = script, language = Language(language))) )
}

case class Rating(rating: Int)

abstract class Annotation(val textKey: String, val source: Source) {
  def getKey(): String = s"${this.getClass.getSimpleName}__${textKey}__${source.key}"
}
case class Topic(scriptRendering: ScriptRendering, language: Language = Language("UNK"))
case class TopicAnnotation(override val textKey: String, override val source: Source, topics: List[Topic]) extends Annotation(textKey = textKey, source = source)
case class MemorableBitsAnnotation(override val textKey: String, override val source: Source, memorableBits: List[QuoteText]) extends Annotation(textKey = textKey, source = source)
case class RatingAnnotation(override val textKey: String, override val source: Source, overall: Rating) extends Annotation(textKey = textKey, source = source)
case class OriginAnnotation(override val textKey: String, override val source: Source, origin: Source = sourceHelper.emptySource) extends Annotation(textKey = textKey, source = source)
// The below is usable for translations as well.
case class DescriptionAnnotation(override val textKey: String, override val source: Source, description: QuoteText) extends Annotation(textKey = textKey, source = source)
case class RequestAnnotation(override val textKey: String, override val source: Source, request: QuoteText) extends Annotation(textKey = textKey, source = source)
case class ReferenceAnnotation(override val textKey: String, override val source: Source, reference: QuoteText) extends Annotation(textKey = textKey, source = source)

case class QuoteWithInfo(quoteText: QuoteText,
                         originAnnotations: List[OriginAnnotation] = List(),
                         topicAnnotations: List[TopicAnnotation] = List(),
                         ratingAnnotations: List[RatingAnnotation] = List(),
                         descriptionAnnotations: List[DescriptionAnnotation] = List(),
                         requestAnnotations: List[RequestAnnotation] = List(),
                         referenceAnnotations: List[ReferenceAnnotation] = List()
                        )
object jsonHelper {
  val formats = Serialization.formats(ShortTypeHints(
    List(
      classOf[QuoteText],
      classOf[OriginAnnotation],
      classOf[DescriptionAnnotation],
      classOf[TopicAnnotation],
      classOf[RatingAnnotation],
      classOf[RequestAnnotation],
      classOf[ReferenceAnnotation],
      classOf[MemorableBitsAnnotation]
    )))


  def getJsonMap(caseObj: Any): Map[String,Object] = {
    implicit val formats = jsonHelper.formats
    val jobj = Extraction.decompose(caseObj)
    return jobj.values.asInstanceOf[Map[String,Object]]
  }

  def fromJsonMap(jsonMap: mutable.Map[String, _]): Any = {
    implicit val formats = jsonHelper.formats
    //    log debug (jsonMap.toString)
    val jsonClass = jsonMap.get("jsonClass").get
    val jsonStr = Serialization.writePretty(jsonMap)
    //    log debug jsonStr
    jsonClass match {
      case "QuoteText" => Serialization.read[QuoteText](jsonStr)
      case "OriginAnnotation" => Serialization.read[OriginAnnotation](jsonStr)
      case "DescriptionAnnotation" => Serialization.read[DescriptionAnnotation](jsonStr)
      case "TopicAnnotation" => Serialization.read[TopicAnnotation](jsonStr)
      case "RatingAnnotation" => Serialization.read[RatingAnnotation](jsonStr)
      case "RequestAnnotation" => Serialization.read[RequestAnnotation](jsonStr)
      case "ReferenceAnnotation" => Serialization.read[ReferenceAnnotation](jsonStr)
    }
  }

}

object quoteTextHelper {
  val emptyText = new QuoteText("")
  val log = LoggerFactory.getLogger(getClass.getName)
  def getSanskritDevangariiQuote(text: String): QuoteText =
    new QuoteText(text = text, script=transliterator.scriptDevanAgarI, language = Language("sa"))
}

object sourceHelper {
  val emptySource = new Source(name = quoteTextHelper.emptyText)
  def getSanskritDevanaagariiSource(title: String, authors: List[String]): Source = {
    return Source(name=quoteTextHelper.getSanskritDevangariiQuote(title),
      authors=authors.filter(_.nonEmpty).map(quoteTextHelper.getSanskritDevangariiQuote(_))
    )
  }
  def fromAuthor(author: String): Source = {
    return new Source(name = quoteTextHelper.emptyText,
      authors = List(author).filter(_.nonEmpty).map(new QuoteText(_)))
  }
}


object quoteTest {
  val log = LoggerFactory.getLogger(getClass.getName)
  def quoteTest: Unit = {
    implicit val formats = jsonHelper.formats

    val quoteText = QuoteText(
      scriptRenderings = List(
        ScriptRendering(text = "दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।। \tदण्डः\t",
          scheme = transliterator.scriptDevanAgarI)),
      language = Language("sa"))
    // implicit val formats = Serialization.formats(NoTypeHints)
    log info Serialization.writePretty(quoteText)
    val source = Source(name=quoteTextHelper.emptyText)
    log info Serialization.writePretty(source)

    val origin = OriginAnnotation(textKey = quoteText.key,
      source = sourceHelper.getSanskritDevanaagariiSource("विश्वाससङ्ग्रहः", List("विश्वासः")),
      origin = sourceHelper.getSanskritDevanaagariiSource("क्वचित्", List("कश्चित्"))
    )
    log info Serialization.writePretty(origin)
  }

  def main(args: Array[String]): Unit = {
    // SanskritSubhashitaTest
    quoteTest
  }
}
