package dbSchema.archive

import dbSchema.rss.PodcastItem

// Generated using https://json2caseclass.cleverapps.io/ while referring to http://jsoneditoronline.org/?id=e031ab3cecf3cd6e0891eb9f303cd963
case class FileInfo(
                 name: Option[String],
                 source: Option[String],
                 mtime: Option[String],
                 size: Option[String],
                 md5: Option[String],
                 crc32: Option[String],
                 sha1: Option[String],
                 format: Option[String],
                 length: Option[String],
                 height: Option[String],
                 width: Option[String],
                 title: Option[String],
                 album: Option[String]
               ) {
  def toPodcastItem(itemMetadata: ItemMetadata,  publishTime: Long): PodcastItem = {
    val albumTag = album.getOrElse("") + " "
    val finalTitle = albumTag.trim +  title.getOrElse(name.get)
    PodcastItem(title = finalTitle, enclosureUrl = s"https://archive.org/download/${itemMetadata.identifier}/${name}",
      lengthInSecs = length.getOrElse("10").toFloat.toInt, timeUsecs1970 = publishTime )
  }

}