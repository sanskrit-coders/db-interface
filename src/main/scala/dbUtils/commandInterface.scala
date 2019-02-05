package dbUtils


import dbSchema.archive.ItemInfo
import dbSchema.rss.ArchivePodcastRequest
import org.slf4j.LoggerFactory

/**
  * Example invocation:
  * java -jar bin/artifacts/dict-tools.jar install /home/vvasuki/sanskrit-coders/stardict-dicts-installed/ https://raw.githubusercontent.com/sanskrit-coders/stardict-dictionary-updater/master/dictionaryIndices.md
  */
case class CommandConfig(mode: Option[String]=None, requestJsonPath: Option[String]=None, outputPath: Option[String]=None)

object commandInterface {
  private val log = LoggerFactory.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    log.info(args.mkString(" "))
    val parser = new scopt.OptionParser[CommandConfig]("cli") {
      cmd("podcastFromRequest")
        .action((_, c) => c.copy(mode = Some("podcastFromRequest")))
        .text("Generate a podcast feed into a specified file.")
        .children(
          opt[String]("requestJsonPath")
            .action((x, c) => c.copy(requestJsonPath = Some(x)))
            .required(),
          opt[String]("outputPath")
            .action((x, c) => c.copy(outputPath = Some(x)))
            .required(),
        )
    }
    parser.parse(args, CommandConfig()) match {
      case Some(commandConfig) => {
        log.debug(commandConfig.toString)
        if (commandConfig.mode.contains("podcastFromRequest")) {
          val podcastRequest = jsonHelper.fromUrlOrFile[ArchivePodcastRequest](commandConfig.requestJsonPath.get)
          val podcast = podcastRequest.getPodcast
          val feedNode = podcast.getFeedNode
          reflect.io.File(commandConfig.outputPath.get).writeAll(feedNode.toString())
        }
      } 
      case None =>
        log.error("Failed to parse args")
    }
  }
}
