package dbSchema.rss

import java.net.{URI, URLEncoder}
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import dbSchema.archive.{FileInfo, ItemInfo, ItemMetadata}
import org.slf4j.LoggerFactory

import scala.xml.{Comment, Elem, Node}

object timeHelper {
  val log = LoggerFactory.getLogger(this.getClass)

  def getTimeRfc2822(timeSecs1970: Long = 0): String = {
    //    https://tools.ietf.org/html/rfc2822#section-3.3
    val pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
    val format = new SimpleDateFormat(pattern)
    var time: Date = null
    if (timeSecs1970 == 0) {
      time = Calendar.getInstance().getTime()
    } else {
      time = new Date(timeSecs1970 * 1000)
    }
    return format.format(time)
  }
}

//  Example: view-source:http://feeds.feedburner.com/SS-bAlamodinI
//  Feed validators:
//    https://validator.w3.org/feed/
//    http://www.feedvalidator.org/check.cgi?url=http%3A%2F%2Ffeeds.feedburner.com%2FSS-bAlamodinI
//  Template: https://resourcecenter.odee.osu.edu/digital-media-production/how-write-podcast-rss-xml
//  Best practices: https://github.com/gpodder/podcast-feed-best-practice/blob/master/podcast-feed-best-practice.md
// ItunesU guide: http://mediaserver.sewanee.edu/itunesu/docs/iTunesUAdministrationGuide.pdf

case class PodcastItem(val title: String, val enclosureUrl: String, val lengthInSecs: Int, var description: String = null, var shortDescription: String = null, val timeSecs1970: Long = 0,
                       val itunesCategoryCode: Long = 107) {
  val log = LoggerFactory.getLogger(this.getClass)
  if (description == null) {
    description = title
  }

  // Max length is 255, but a UTF-8 character can consume multiple bytes.
  if (shortDescription == null) {
    shortDescription = description.substring(0, math.min(150, description.length - 1))
  }

  val enclosureUriEncoded = new URI(enclosureUrl.replaceFirst("://.+", ""), enclosureUrl.replaceFirst(".+://", "//"), null)

  def getNode(): Node = {
    val feed =
      <item>
        <title>
          {title}
        </title>
        <description>
          {description}
        </description>
        <itunes:summary>Duplicate of above verbose description.</itunes:summary>
        <itunes:subtitle>
          {shortDescription}
        </itunes:subtitle>
        <itunesu:category itunesu:code={f"$itunesCategoryCode"}/>
        <enclosure url={f"$enclosureUriEncoded"} type="audio/mpeg" length="1"/>
        <guid>
          {enclosureUriEncoded}
        </guid>
        {Comment("Expected format: H:MM:SS")}
        <itunes:duration>
        {f"${lengthInSecs / 3600 }%02d:${lengthInSecs / 60   % 60}%02d:${lengthInSecs % 60}%02d"}
      </itunes:duration>
        <pubDate>
          {timeHelper.getTimeRfc2822(timeSecs1970 = timeSecs1970)}
        </pubDate>
      </item>
    return feed
  }
}

case class Podcast(val title: String, val description: String,
                   val website_url: String = "http://archive.org",
                   val subtitle: Option[String] = None,
                   var publisher: Option[String] = None, var author: Option[String] = None, val publisherEmail: String,
                   val keywords: Seq[String] = Seq(),
                   val items: Seq[PodcastItem], val feedUrl:Option[String] = None) {
  val log = LoggerFactory.getLogger(this.getClass)

  if (publisher == None) {
    publisher = Some(publisherEmail)
  }

  if (author == None) {
    author = publisher
  }

  def add(n: Node, c: Node): Node = n match {
    case e: Elem => e.copy(child = e.child ++ c)
  }

  def getNode(): Node = {
    var feed =
      <rss xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd" xmlns:itunesu="http://www.itunesu.com/feed" xmlns:atom="http://www.w3.org/2005/Atom" version="2.0">
        {Comment("The namespace definitions point to non-existent dtd-s and online validators complain, but that's fine. Can't help it.")}

        <channel>
          {if (feedUrl.isDefined) <atom:link href={f"${feedUrl.get}"} rel="self" type="application/rss+xml" />}

          <title>
            {title}
          </title>
          {Comment("Various description fields.")}
          <description>
          {description}
        </description>
          <itunes:subtitle>
            {subtitle.getOrElse(title)}
          </itunes:subtitle>
          <itunes:summary>
            {description}
          </itunes:summary>{Comment("The podcast website.")}<link>
          {website_url}
        </link>

          <language>en-us</language>
          <copyright>None</copyright>

          <lastBuildDate>
            {timeHelper.getTimeRfc2822()}
          </lastBuildDate>
          <pubDate>
            {timeHelper.getTimeRfc2822()}
          </pubDate>{Comment("Publisher details.")}<webMaster>
          {publisher.get}
        </webMaster>
          <itunes:author>
            {author.get}
          </itunes:author>
          <itunes:owner>
            <itunes:name>
              {publisher.get}
            </itunes:name>
            <itunes:email>
              {publisherEmail}
            </itunes:email>
          </itunes:owner>{Comment("Category fields.")}<itunes:explicit>No</itunes:explicit>
          <itunes:category text="Education">
          </itunes:category>
          <itunes:keywords>
            {keywords.mkString(",")}
          </itunes:keywords>{items.map(_.getNode())}
        </channel>

      </rss>
    return feed
  }
}

object podcastTest {
  val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val podcastItems = Seq(PodcastItem(title = "xyz", enclosureUrl = "http://enclosure.mp3", lengthInSecs = 601))
    val podcast = new Podcast(title = "संस्कृतशास्त्राणि: shastras in sanskrit", description = "", publisherEmail = "sanskrit-programmers@googlegroups.com", items = podcastItems)
    print(podcast.getNode())
  }
}