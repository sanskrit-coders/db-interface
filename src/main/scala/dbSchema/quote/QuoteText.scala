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
}

