package dbSchema.common

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Text(script_renderings: List[ScriptRendering],
                language_code: Option[String] = None) {
  def getKey: String = {
    if (script_renderings.nonEmpty) {
      val canonicalRendering = script_renderings.find(x => language_code.nonEmpty && x.encoding_scheme == Some(Language(language_code.get).canonicalScript))
      val rendering = canonicalRendering.getOrElse(script_renderings.head)
      return rendering.getKey
    } else {
      return null
    }
  }

}

object textHelper {
  val emptyText: Text = fromOnlyText("")
  private val log = LoggerFactory.getLogger(getClass.getName)

  // Constructors can mess with JSON (de)serialization
  def fromDetails(text: String, script: Option[String], language_code: Option[String]) =
    Text(script_renderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, encoding_scheme = script)),
      language_code = language_code)

  def fromOnlyText(text: String): Text = fromDetails(text = text, script = None, language_code = None)

  def getSanskritDevangariiText(text: String): Text =
    fromDetails(text = text, script = Some(transliterator.scriptDevanAgarI), language_code = Some("sa"))
}

