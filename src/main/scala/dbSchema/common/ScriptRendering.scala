package dbSchema.common

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator


case class ScriptRendering(text: String, scheme: Option[String] = None) {
  val log = LoggerFactory.getLogger(getClass.getName)

  // A unique identifier for a text rendering.
  def getKey: String = {
    scheme match {
      case Some(transliterator.scriptDevanAgarI) => {
        return transliterator.transliterate(
          text.replaceAll("\\P{IsDevanagari}", "").replaceAll("[।॥०-९]+", "").replaceAll("\\s", ""), "dev", "optitrans")
          .replaceAll("[MNn]", "m")
      }
      case unknownScript => {
        log warn (s"got script $unknownScript for text [$text]")
        return text.replaceAll("\\s", "")
      }
    }
  }
}
