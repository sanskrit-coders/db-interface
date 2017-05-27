package dbSchema.common

import dbSchema.quote.QuoteText
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Text(scriptRenderings: List[ScriptRendering],
                language: Option[Language] = None) {
  def getKey: String = {
    if (scriptRenderings.nonEmpty) {
      val canonicalRendering = scriptRenderings.filter(
        x => language.nonEmpty && x.scheme == Some(language.get.canonicalScript)).headOption
      val rendering = canonicalRendering.getOrElse(scriptRenderings.head)
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
  def fromDetails(text: String, script: Option[String], language: Option[Language]) =
    Text(scriptRenderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, scheme = script)),
      language = language)

  def fromOnlyText(text: String) = fromDetails(text = text, script = None, language = None)

  def getSanskritDevangariiText(text: String): Text =
    fromDetails(text = text, script = Some(transliterator.scriptDevanAgarI), language = Some(Language("sa")))
}

