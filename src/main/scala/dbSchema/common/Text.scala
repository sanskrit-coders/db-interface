package dbSchema.common

import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Text(scriptRenderings: List[ScriptRendering],
                var key: String = "",
                language: Language = Language("UNK")) {
  def this(text: String, script: String, language: Language) =
    this(scriptRenderings = List(text).filter(_.nonEmpty).map(x => ScriptRendering(text = x, scheme = script)),
      language = language)

  def this(text: String) = this(text = text, script = transliterator.scriptUnknown, language = Language("UNK"))

}
