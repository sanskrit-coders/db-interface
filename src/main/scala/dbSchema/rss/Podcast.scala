package dbSchema.rss

import java.net.URI
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

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

/**
  * See comments associated with Podcast class for some common references.
  *
  * @param title
  * @param enclosureUrl
  * @param lengthInSecs
  * @param description
  * @param shortDescription
  * @param timeSecs1970
  * @param itunesCategoryCode
  */
case class PodcastItem(val title: String, val enclosureUrl: String, val lengthInSecs: Int, var description: String = null, var shortDescription: String = null, val timeSecs1970: Long = 0,
                       val itunesCategoryCode: Option[Int] = None, val ordinal: Option[Int] = None) {
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
        <itunes:summary>
          {description}
        </itunes:summary>
        <itunes:subtitle>
          {shortDescription}
        </itunes:subtitle>

        {Comment("iTunesU Category Codes: http://sitemanager.itunes.apple.com/help/#itu337EEAE0-035A-4660-B53D-46A13A7721E5")}
        {if (itunesCategoryCode.isDefined)
          <itunesu:category itunesu:code={f"$itunesCategoryCode"}/>
        }
        <guid>
          {enclosureUriEncoded}
        </guid>

        {Comment("'The <itunes:order> tag can be used to override the default ordering of episodes on the iTunes Store by populating it with the number value in which you would like the episode to appear. For example, if you would like an <item> to appear as the first episode of the podcast, you would populate the <itunes:order> tag with “1.” If conflicting order values are present in multiple episodes, the store will order by <pubDate>.'")}
        {if (ordinal.isDefined)
        <itunes:order> {ordinal.get} </itunes:order>
        }


      <enclosure url={f"$enclosureUriEncoded"} type="audio/mpeg" length="1"/>

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

/**
  *
  * General References:
  * Example: view-source:http://feeds.feedburner.com/SS-bAlamodinI
  *
  * Feed validators:
  * https://validator.w3.org/feed/
  * http://www.feedvalidator.org/check.cgi?url=http%3A%2F%2Ffeeds.feedburner.com%2FSS-bAlamodinI
  * Template: https://resourcecenter.odee.osu.edu/digital-media-production/how-write-podcast-rss-xml

  * Best practices: https://github.com/gpodder/podcast-feed-best-practice/blob/master/podcast-feed-best-practice.md
  * ItunesU guide: http://mediaserver.sewanee.edu/itunesu/docs/iTunesUAdministrationGuide.pdf
  * Google Play required tags: https://support.google.com/googleplay/podcasts/answer/6260341#rpt

  * @param title
  * @param description
  * @param imageUrl
  * @param languageCode
  * @param websiteUrl
  * @param subtitle
  * @param publisher
  * @param author
  * @param publisherEmail
  * @param keywords
  * @param items
  * @param feedUrl
  * @param isExplicitYesNo
  * @param categories
  */
case class Podcast(val title: String, val description: String,
                   val imageUrl: String, val languageCode: String,
                   val websiteUrl: Option[String] = None,
                   val copyright: Option[String] = None,
                   val subtitle: Option[String] = None,
                   var publisher: Option[String] = None, var author: Option[String] = None, val publisherEmail: String, timeSecs1970: Option[Long] = None,
                   val keywords: Seq[String] = Seq(),
                   val items: Seq[PodcastItem], val feedUrl:Option[String] = None,
                   val isExplicitYesNo: Option[String] = None, val categories: Seq[String] = Seq("Society & Culture")) {
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
    // XML encoding happens automatically when the node is rendered, no need to specially encode.
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
          </itunes:summary>
          {Comment("The podcast website.")}
          {
          if (websiteUrl.isDefined)
          <link> {websiteUrl.get}</link>
          }

          {Comment("The language should be specified according to RFC 3066, RFC 4647 and RFC 5646. List: http://www.loc.gov/standards/iso639-2/php/code_list.php ")}
          <language>{languageCode}</language>
          {
          if (copyright.isDefined)
            <copyright>{copyright.get}</copyright>
          }


          <lastBuildDate>
            {timeHelper.getTimeRfc2822(timeSecs1970.getOrElse(0))}
          </lastBuildDate>
          <pubDate>
            {timeHelper.getTimeRfc2822(timeSecs1970.getOrElse(0))}
          </pubDate>
          {Comment("Publisher details.")}
          <webMaster>
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
          </itunes:owner>

          {Comment("Image must be square and over 1200 x 1200 for Google Play, and over 1400 x 1400 for ITunes.")}
          <image>
            <url>{imageUrl}</url>
            <title>{title}</title>
            {
            if (websiteUrl.isDefined)
              <link> {websiteUrl.get}</link>
            }
          </image>
          <itunes:image href={imageUrl} />


          {Comment("Category fields.")}
          {Comment("itunes:explicit: Valid values: Yes or No. Google Play says: If a tag isn’t included, the content will not be considered explicit. ITunes requires this.")}
          {if (isExplicitYesNo.isDefined)
          <itunes:explicit> {isExplicitYesNo.get} </itunes:explicit>
          }

        {Comment("Only certain categories are valid, see: https://support.google.com/googleplay/podcasts/answer/6260341#rpt for Google Play and https://www.seriouslysimplepodcasting.com/itunes-podcast-category-list/ for ITunes.")}
          {
          categories.map(category => <itunes:category text={category}/>)
          }
          <itunes:keywords>
            {keywords.mkString(",")}
          </itunes:keywords>

          {items.map(_.getNode())}
        </channel>
      </rss>
    return feed
  }
}

object podcastTest {
  val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val podcastItems = Seq(PodcastItem(title = "xyz", enclosureUrl = "http://enclosure.mp3", lengthInSecs = 601))
    val podcast = new Podcast(title = "संस्कृतशास्त्राणि: shastras in sanskrit", description = "", publisherEmail = "sanskrit-programmers@googlegroups.com", items = podcastItems, imageUrl = "https://i.imgur.com/dQjPQYi.jpg", languageCode = "en")
    print(podcast.getNode())
  }
}