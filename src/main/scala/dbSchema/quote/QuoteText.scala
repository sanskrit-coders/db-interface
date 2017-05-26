package dbSchema.quote

import dbSchema.common.{Language, ScriptRendering, Text}
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class QuoteText(override val scriptRenderings: List[ScriptRendering],
                     override var key: String = "",
                     override val language: Language = Language("UNK"),
                     val metre: String = "UNK") extends Text(scriptRenderings = scriptRenderings, key = key, language = language) {
  val log = LoggerFactory.getLogger(getClass.getName)
  // Helps make primary keys (aka ids) for storing these case classes in databases.
  if (scriptRenderings.nonEmpty) {
    val canonicalRendering = scriptRenderings.filter(_.scheme == language.canonicalScript).headOption
    val rendering = canonicalRendering.getOrElse(scriptRenderings.head)
    key = rendering.getKey
  }

  def this(text: String, script: String, language: Language) =
    this(scriptRenderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, scheme = script)),
      language = language)

  def this(text: String) = this(text = text, script = transliterator.scriptUnknown, language = Language("UNK"))
}

object quoteTextHelper {
  val emptyText = new QuoteText("")
  val log = LoggerFactory.getLogger(getClass.getName)

  def getSanskritDevangariiQuote(text: String): QuoteText =
    new QuoteText(text = text, script = transliterator.scriptDevanAgarI, language = Language("sa"))
}


