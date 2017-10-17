package dbSchema.archive

import dbSchema.rss.{Podcast, PodcastItem}

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

  def getPodcastItems(filePattern: String, useArchiveOrder: Boolean = true, itemPublishTime: Option[Long] = None): List[PodcastItem] = {
    val itemPublishTime = if (useArchiveOrder) metadata.getModificationTime1970Secs() else None
    val itemFiles = files.filter(fileInfo => {
      val fileName = fileInfo.name.get
      fileName.matches(filePattern)
    })

    itemFiles.zipWithIndex.map( {case (itemFile: FileInfo, index: Int) =>
      val ordinal = if (useArchiveOrder) Some(index) else None
      itemFile.toPodcastItem(itemMetadata = metadata, publishTime = itemPublishTime, ordinal = ordinal)})
  }

  def toPodcast(filePattern: String, useArchiveOrder: Boolean = true, podcast: Podcast): Podcast = {
    val url = s"https://archive.org/details/${metadata.identifier}"
    val author = if (metadata.creator.isDefined) metadata.creator else metadata.uploader.map(_.replaceFirst("@.+", ""))

    val titleFinal = if (podcast.title.isEmpty) metadata.title.getOrElse(metadata.identifier) else podcast.title

    val items = getPodcastItems(filePattern=filePattern, useArchiveOrder = useArchiveOrder)

    // Archive items seem to be available under variants of the Creative Commons license, chosen at upload-time. (Deduced from seeing https://archive.org/editxml.php?type=audio&edit_item=CDAC-tArkShya-shAstra-viShayaka-bhAShaNAni )
    podcast.copy(title = titleFinal, description = s"A podcast created using https://github.com/vedavaapi/scala-akka-http-server from the archive item: $url, with description:\n${metadata.description.getOrElse("")}.", websiteUrl = Some(url), author = author,  timeSecs1970 = metadata.getModificationTime1970Secs(), keywords = metadata.subject.getOrElse(Seq()), items=items)
  }

}