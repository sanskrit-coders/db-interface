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
  def toPodcastItem(itemMetadata: ItemMetadata,  publishTime: Option[Long] = None, ordinal: Option[Int] = None): PodcastItem = {
    val albumTag = album.getOrElse("") + " "
    var timeSecs1970 = mtime.getOrElse("0").toLong
    if (publishTime.isDefined) {
      // doggcatcher only uses date, not time - http://www.doggcatcher.com/node/6804 !
      timeSecs1970 = publishTime.get + 24*3600*ordinal.getOrElse(0)
    }
    val finalTitle = albumTag.trim +  title.getOrElse(name.get)

    // Not passing , ordinal=ordinal below: see PodcastItem comments for reasons.
    PodcastItem(title = finalTitle, enclosureUrlUnencoded = s"https://archive.org/download/${itemMetadata.identifier}/${name.get}",
      lengthInSecs = length.getOrElse("10").toFloat.toInt, timeSecs1970 = timeSecs1970)
  }

}