package dbSchema.common

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

/**
  * Created by vvasuki on 5/26/17.
  */
case class Language(code: String) {
  val log = LoggerFactory.getLogger(getClass.getName)
  var canonicalScript: String = transliterator.scriptUnknown
  code match {
    case "sa" => canonicalScript = transliterator.scriptDevanAgarI
    case "en" => canonicalScript = "en"
    case _ => canonicalScript = transliterator.scriptUnknown
  }

}
