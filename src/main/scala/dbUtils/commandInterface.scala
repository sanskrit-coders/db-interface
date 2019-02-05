package dbUtils


import dbSchema.archive.ItemInfo
import dbSchema.rss.ArchivePodcastRequest
import org.slf4j.LoggerFactory

/**
  * Example invocation:
  * java -jar bin/artifacts/dict-tools.jar install /home/vvasuki/sanskrit-coders/stardict-dicts-installed/ https://raw.githubusercontent.com/sanskrit-coders/stardict-dictionary-updater/master/dictionaryIndices.md
  */
object commandInterface {
  private val log = LoggerFactory.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    assert(args.length > 0)
    val command = args(0)
    log.info(args.mkString(" "))
    command match {
      case "podcastFromRequest" => {
//        val podcastRequest = jsonHelper.fromUrlOrFile[ArchivePodcastRequest](args(1))
//        podcastRequest.getPodcast(filePattern = "*.mp3", useArchiveOrder = )
      }
      case unknownCommand => log.error(s"Do not recognize $unknownCommand")
    }
  }
}
