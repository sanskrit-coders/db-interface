package dbSchema.common

import dbSchema.quote.QuoteText
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Text(scriptRenderings: List[ScriptRendering],
                language: Language = null) {
  def this(text: String, script: String, language: Language) =
    this(scriptRenderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, scheme = script)),
      language = language)

  def this(text: String) = this(text = text, script = transliterator.scriptUnknown, language = Language("UNK"))

  def getKey: String = {
    if (scriptRenderings.nonEmpty) {
      val canonicalRendering = scriptRenderings.filter(_.scheme == language.canonicalScript).headOption
      val rendering = canonicalRendering.getOrElse(scriptRenderings.head)
      return rendering.getKey
    } else {
      return null
    }
  }

}

object textHelper {
  val emptyText = new Text("")
  val log = LoggerFactory.getLogger(getClass.getName)

  def getSanskritDevangariiText(text: String): Text =
    new Text(text = text, script = transliterator.scriptDevanAgarI, language = Language("sa"))
}

