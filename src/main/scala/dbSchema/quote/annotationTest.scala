package dbSchema.quote

import dbSchema.common.{Language, ScriptRendering, Source, sourceHelper, textHelper, Text}
import dbUtils.jsonHelper
import org.json4s.native.Serialization
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
object annotationTest {
  val log = LoggerFactory.getLogger(getClass.getName)

  def quoteTest: Unit = {
    implicit val formats = jsonHelper.formats

    val text = Text(
      scriptRenderings = List(
        ScriptRendering(text = "दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।। \tदण्डः\t",
          scheme = Some(transliterator.scriptDevanAgarI))),
      language = Some(Language("sa")))
    // implicit val formats = Serialization.formats(NoTypeHints)
    log info Serialization.writePretty(text)
    val source = Source(name = textHelper.emptyText)
    log info Serialization.writePretty(source)

    val origin = OriginAnnotation(textKey = text.getKey,
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
