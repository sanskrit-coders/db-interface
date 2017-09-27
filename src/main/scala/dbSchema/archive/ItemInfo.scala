package dbSchema.archive

import java.io.FilenameFilter

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

  def toPodcast(fileExtensions: Seq[String], publisherEmail: String, imageUrl: String, languageCode: String = "en", categories: Seq[String], isExplicitYesNo: Option[String] = None): Podcast = {
    val url = s"https://archive.org/details/${metadata.identifier}"
    val itemFiles = files.filter(fileInfo => {
      val fileName = fileInfo.name.get
      fileName.contains(".") && fileExtensions.contains(fileName.drop(fileName.lastIndexOf('.')).replace(".", ""))
    })
    val itemPublishTimes = itemFiles.zipWithIndex.map(created.getOrElse[Double](0).toLong + _._2)
    var items = itemFiles.zipWithIndex.map( {case (itemFile: FileInfo, index: Int) => itemFile.toPodcastItem(itemMetadata = metadata, ordinal = Some(index))})


    Podcast(title = metadata.title.getOrElse(metadata.identifier), description = s"A podcast created using https://github.com/vedavaapi/scala-akka-http-server from the archive item: $url, with description:\n${metadata.description.getOrElse("")}.",
      websiteUrl = Some(url), languageCode = languageCode, imageUrl =imageUrl, author = metadata.uploader, categories = categories,
      publisherEmail = publisherEmail, keywords = metadata.subject.getOrElse(Seq()), items=items, isExplicitYesNo=isExplicitYesNo
    )
  }

}