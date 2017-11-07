package dbSchema.common

import dbSchema.quote.QuoteText
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Text(script_renderings: List[ScriptRendering],
                language_code: Option[String] = None) {
  def getKey: String = {
    if (script_renderings.nonEmpty) {
      val canonicalRendering = script_renderings.filter(
        x => language_code.nonEmpty && x.encoding_scheme == Some(Language(language_code.get).canonicalScript)).headOption
      val rendering = canonicalRendering.getOrElse(script_renderings.head)
      return rendering.getKey
    } else {
      return null
    }
  }

}

object textHelper {
  val emptyText = fromOnlyText("")
  val log = LoggerFactory.getLogger(getClass.getName)

  // Constructors can mess with JSON (de)serialization
  def fromDetails(text: String, script: Option[String], language_code: Option[String]) =
    Text(script_renderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, encoding_scheme = script)),
      language_code = language_code)

  def fromOnlyText(text: String) = fromDetails(text = text, script = None, language_code = None)

  def getSanskritDevangariiText(text: String): Text =
    fromDetails(text = text, script = Some(transliterator.scriptDevanAgarI), language_code = Some("sa"))
}

