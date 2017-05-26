package dbSchema.quote

import dbSchema.common.{Language, ScriptRendering, Text}
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class QuoteText(val text: Text,
                     val metre: String = null){
  val log = LoggerFactory.getLogger(getClass.getName)
  // Helps make primary keys (aka ids) for storing these case classes in databases.
  def this(textStr: String, script: String, language: Language) =
    this(text = Text(scriptRenderings = List(textStr).filter(_.nonEmpty).map(x => ScriptRendering(text = x, scheme = script)),
      language = language))

  def this(text: String) = this(textStr = text, script = transliterator.scriptUnknown, language = Language("UNK"))
}

