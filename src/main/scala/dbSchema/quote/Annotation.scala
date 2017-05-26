package dbSchema.quote

import dbSchema.common.{Language, ScriptRendering}

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
