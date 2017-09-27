package dbSchema.archive

import dbSchema.rss.Podcast

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
  def toPodcast(audioFileExtension: String, publisherEmail: String, imageUrl: String = "https://i.imgur.com/yAR8bWh.png"): Podcast = {
    val url = s"https://archive.org/details/${metadata.identifier}"
    val itemFiles = files.filter(fileInfo => fileInfo.name.get.endsWith(s".$audioFileExtension"))
    val itemPublishTimes = itemFiles.zipWithIndex.map(created.getOrElse[Double](0).toLong + _._2)
    var items = itemFiles.zip(itemPublishTimes).map( {case (itemFile: FileInfo, publishTime: Long) => itemFile.toPodcastItem(itemMetadata = metadata, publishTime = publishTime)})
    Podcast(title = metadata.title.getOrElse(metadata.identifier), description = s"A podcast automatically created from the archive item: $url, with description: ${metadata.description.getOrElse("")}",
      websiteUrl = Some(url), imageUrl =imageUrl, author = metadata.uploader,
      publisherEmail = publisherEmail, keywords = metadata.subject.getOrElse(Seq()), items=items
    )
  }

}