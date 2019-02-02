package dbSchema.archive

import dbSchema.rss.PodcastItem
import org.slf4j.LoggerFactory

import scala.util.Try

// Metadata example at source: https://archive.org/metadata/mahAbhArata-mUla-paThanam-GP .
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
                 album: Option[String],
                 album_artist: Option[String],
                 artist: Option[String],
                 creator: Option[String]
               ) {
  private val log = LoggerFactory.getLogger(this.getClass)
  private final val defaultLengthSecs = 600

  def getLengthSecs: Int = {
    // Rare cases: Failed to parse length 30:58 for file Some(ra ganesh upanyasa on Ramayana - chandana tv.mp3)
    val lengthString = length.getOrElse(defaultLengthSecs.toString)
    val lengthParts = lengthString.split(":").reverse.toList
    var intLength = lengthParts.head.toFloat.toInt
    if (lengthParts.size > 1) {
      intLength = intLength + lengthParts(1).toInt * 60
    }
    if (lengthParts.size > 2) {
      intLength = intLength + lengthParts(2).toInt * 60 * 60
    }
    intLength
  }

  // archive item is not a podcast item.
  def toPodcastItem(archiveItemMetadata: ItemMetadata, publishTime: Option[Long] = None): PodcastItem = {
    val albumTag = album.getOrElse("") + " "
    var timeSecs1970 = publishTime.getOrElse(mtime.getOrElse("0").toLong)
    val finalTitle = s"${albumTag.trim} ${title.getOrElse(name.get)} ${artist.getOrElse("")}"

    val lengthTry = Try(getLengthSecs)
    if (lengthTry.isFailure) {
      log.warn(s"Failed to parse length ${length.get} for file $name.")
    }

    // Not passing , ordinal=ordinal below: see PodcastItem comments for reasons.
    PodcastItem(title = finalTitle, enclosureUrlUnencoded = s"https://archive.org/download/${archiveItemMetadata.identifier}/${name.get}", author=artist,
      lengthInSecs = lengthTry.getOrElse(defaultLengthSecs), timeSecs1970 = timeSecs1970)
  }

}