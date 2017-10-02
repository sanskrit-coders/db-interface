package dbSchema.archive

import dbSchema.rss.Podcast

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

  def toPodcast(filePattern: String, publisherEmail: String, imageUrl: String, languageCode: String = "en", useArchiveOrder: Boolean = true, title: Option[String] = None, categories: Seq[String], isExplicitYesNo: Option[String] = None): Podcast = {
    val url = s"https://archive.org/details/${metadata.identifier}"
    val itemFiles = files.filter(fileInfo => {
      val fileName = fileInfo.name.get
      fileName.matches(filePattern)
    })

    val itemPublishTime = if (useArchiveOrder) metadata.getModificationTime1970Secs else None
    var items = itemFiles.zipWithIndex.map( {case (itemFile: FileInfo, index: Int) =>
       val ordinal = if (useArchiveOrder) Some(index) else None
      itemFile.toPodcastItem(itemMetadata = metadata, publishTime = itemPublishTime, ordinal = ordinal)})

    val author = if (metadata.creator.isDefined) metadata.creator else metadata.uploader.map(_.replaceFirst("@.+", ""))
    val titleFinal = if (title.isDefined) title.get else metadata.title.getOrElse(metadata.identifier)

    // Archive items seem to be available under variants of the Creative Commons license, chosen at upload-time. (Deduced from seeing https://archive.org/editxml.php?type=audio&edit_item=CDAC-tArkShya-shAstra-viShayaka-bhAShaNAni )
    Podcast(title = titleFinal, description = s"A podcast created using https://github.com/vedavaapi/scala-akka-http-server from the archive item: $url, with description:\n${metadata.description.getOrElse("")}.",
      websiteUrl = Some(url), languageCode = languageCode, imageUrl =imageUrl, author = author, categories = categories, timeSecs1970 = itemPublishTime,
      publisherEmail = publisherEmail, keywords = metadata.subject.getOrElse(Seq()), items=items, isExplicitYesNo=isExplicitYesNo
    )
  }

}