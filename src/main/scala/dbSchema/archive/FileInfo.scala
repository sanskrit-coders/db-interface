package dbSchema.archive

import dbSchema.rss.PodcastItem
import org.slf4j.LoggerFactory

import scala.util.Try

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
  private val log = LoggerFactory.getLogger(this.getClass)
  def toPodcastItem(itemMetadata: ItemMetadata,  publishTime: Option[Long] = None, ordinal: Option[Int] = None): PodcastItem = {
    val albumTag = album.getOrElse("") + " "
    var timeSecs1970 = mtime.getOrElse("0").toLong
    if (publishTime.isDefined) {
      // doggcatcher allegedly only uses date, not time - http://www.doggcatcher.com/node/6804 !
      // So might need to do 24*3600 below.
      val intervalBetweenItems = 1
      timeSecs1970 = publishTime.get + intervalBetweenItems * ordinal.getOrElse(0)
    }
    val finalTitle = s"${albumTag.trim} ${title.getOrElse(name.get)} ${name.get}"

    val defaultLengthSecs = 600
    // Rare cases: Failed to parse length 30:58 for file Some(ra ganesh upanyasa on Ramayana - chandana tv.mp3)
    val lengthTry = Try(length.getOrElse(defaultLengthSecs.toString).toFloat.toInt)
    if (lengthTry.isFailure) {
      log.warn(s"Failed to parse length ${length.get} for file $name.")
    }

    // Not passing , ordinal=ordinal below: see PodcastItem comments for reasons.
    PodcastItem(title = finalTitle, enclosureUrlUnencoded = s"https://archive.org/download/${itemMetadata.identifier}/${name.get}",
      lengthInSecs = lengthTry.getOrElse(defaultLengthSecs), timeSecs1970 = timeSecs1970)
  }

}