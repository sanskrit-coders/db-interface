package dbSchema.archive

import dbSchema.rss.{Podcast, PodcastItem}
import org.slf4j.LoggerFactory

// Example data: http://jsoneditoronline.org/?id=e031ab3cecf3cd6e0891eb9f303cd963
case class ItemInfo( created: Option[Double],
                     d1: Option[String],
                     dir: Option[String],
                     files: List[FileInfo],
                     files_count: Option[Double],
                     item_size: Option[Double],
                     metadata: ItemMetadata,
                     server: Option[String],
                     uniq: Option[Double],
                     workable_servers: Option[List[String]]
                   ) {
  private val log = LoggerFactory.getLogger(this.getClass)
  
  def getPodcastFileInfos(filePattern:String) =  files.filter(fileInfo => {
    val fileName = fileInfo.name.get
    fileName.matches(filePattern)
  })


  def getPodcastItems(itemFiles: List[FileInfo], useArchiveOrder: Boolean = true, itemPublishTime: Option[Long] = None): List[PodcastItem] = {
    val intervalBetweenItems = 1

    itemFiles.sortBy(_.name.get).zipWithIndex.map( {case (itemFile: FileInfo, index: Int) =>
      val itemPublishTimeFinal: Option[Long] = if (useArchiveOrder) itemPublishTime.map(_ + intervalBetweenItems * index) else None
      itemFile.toPodcastItem(archiveItemMetadata = metadata, publishTime = itemPublishTimeFinal)})
  }

  def toPodcast(filePattern: String, useArchiveOrder: Boolean = true, podcast: Podcast): Podcast = {
    val itemUrl = s"https://archive.org/details/${metadata.identifier}"
    val websiteUrlFinal = if (podcast.websiteUrl.isDefined) podcast.websiteUrl else Some(itemUrl)

    val itemAuthor = if (metadata.creator.isDefined) metadata.creator else metadata.uploader.map(_.replaceFirst("@.+", ""))
    val authorFinal = if (podcast.author.isDefined ) podcast.author else itemAuthor

    val titleFinal = if (podcast.title.isEmpty) metadata.title.getOrElse(metadata.identifier) else podcast.title

    val descriptionFinal = if (podcast.description.isEmpty) s"A podcast created using https://github.com/vedavaapi/scala-akka-http-server from the archive item: $itemUrl, with description:\n${metadata.description.getOrElse("")}." else podcast.description

    val itemFileInfos = getPodcastFileInfos(filePattern=filePattern)
    var itemPublishTimeFinal: Option[Long] = None
    if (itemFileInfos.length == 0) {
      log.error("Did not get any item files!")
    } else {
      itemPublishTimeFinal = if (podcast.timeSecs1970.isDefined) podcast.timeSecs1970 else  Some(itemFileInfos.map(_.mtime.getOrElse("0").toLong).max)
    }

    val items = getPodcastItems(itemFiles = itemFileInfos, useArchiveOrder = useArchiveOrder, itemPublishTime = itemPublishTimeFinal)

    // Archive items seem to be available under variants of the Creative Commons license, chosen at upload-time. (Deduced from seeing https://archive.org/editxml.php?type=audio&edit_item=CDAC-tArkShya-shAstra-viShayaka-bhAShaNAni )
    podcast.copy(title = titleFinal, description = descriptionFinal, websiteUrl = websiteUrlFinal, author = authorFinal,  timeSecs1970 = itemPublishTimeFinal, keywords = Seq.concat(podcast.keywords, metadata.subject.getOrElse(Seq())), items=items)
  }

}