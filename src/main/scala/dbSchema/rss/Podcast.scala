package dbSchema.rss

import java.net.URI
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.slf4j.{Logger, LoggerFactory}

import scala.xml.{Comment, Elem, Node}

object timeHelper {
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def getTimeRfc2822(timeSecs1970: Long = 0): String = {
    //    https://tools.ietf.org/html/rfc2822#section-3.3
    var pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
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
  * @param enclosureUrlUnencoded
  * @param lengthInSecs
  * @param description
  * @param shortDescription
  * @param timeSecs1970
  * @param itunesCategoryCode
  */
case class PodcastItem(var title: String, var enclosureUrlUnencoded: String, var lengthInSecs: Int, var description: String = null, var shortDescription: String = null, var timeSecs1970: Long = 0,
                       var itunesCategoryCode: Option[Int] = None, var ordinal: Option[Int] = None) {
  private val log = LoggerFactory.getLogger(this.getClass)
  if (description == null) {
    description = title
  }

  // Max length is 255, but a UTF-8 character can consume multiple bytes.
  if (shortDescription == null) {
    shortDescription = description.substring(0, math.min(150, description.length - 1))
  }

  // Shorter constructors mess up on certain URI-s.
  private val enclosureUriFinal = new URI(enclosureUrlUnencoded.replaceFirst("://.+", "") /*scheme*/, enclosureUrlUnencoded.replaceFirst(".+://", "").replaceFirst("/.+", "") /*host*/, enclosureUrlUnencoded.replaceFirst(".+://", "").replaceFirst("[^/]+/", "/") /* path */, null).toASCIIString

  def getNode: Node = {
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

        {
        // iTunesU Category Codes: http://sitemanager.itunes.apple.com/help/#itu337EEAE0-035A-4660-B53D-46A13A7721E5)
        }
        {if (itunesCategoryCode.isDefined)
          <itunesu:category itunesu:code={f"$itunesCategoryCode"}/>
        }
        <guid>
          {enclosureUriFinal}
        </guid>

        {/*
        'The <itunes:order> tag can be used to override the default ordering of episodes on the iTunes Store by populating it with the number value in which you would like the episode to appear. For example, if you would like an <item> to appear as the first episode of the podcast, you would populate the <itunes:order> tag with “1.” If conflicting order values are present in multiple episodes, the store will order by <pubDate>.'  +
        But itunes itself doesnt seem to respect it in the podcast submission page! Same with several podcast players.
        Also, podcast players like Doggcatcher show episodes in the feed order, which is suboptimal. Also, doggcatcher only uses date, not time - http://www.doggcatcher.com/node/6804 !
        Conclusion : use date to set order.*/
        }
        {if (ordinal.isDefined)
        <itunes:order> {ordinal.get} </itunes:order>
        }


      <enclosure url={f"$enclosureUriFinal"} type="audio/mpeg" length="1"/>

        {/*Comment("Expected format: H:MM:SS")*/}
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
  *
  * https://cyber.harvard.edu/rss/rss.html
  * Best practices: https://github.com/gpodder/podcast-feed-best-practice/blob/master/podcast-feed-best-practice.md
  * ItunesU guide: http://mediaserver.sewanee.edu/itunesu/docs/iTunesUAdministrationGuide.pdf
  * Google Play required tags: https://support.google.com/googleplay/podcasts/answer/6260341#rpt
  *
  *
  * Template: https://resourcecenter.odee.osu.edu/digital-media-production/how-write-podcast-rss-xml
  * Example: view-source:http://feeds.feedburner.com/SS-bAlamodinI
  *
  * Feed validators:
  * https://validator.w3.org/feed/
  * http://www.feedvalidator.org/check.cgi?url=http%3A%2F%2Ffeeds.feedburner.com%2FSS-bAlamodinI
  * https://podba.se/validate/?url=http://vedavaapi.org:9090/podcasts/v1/archiveItems/shaiva_tantra_mt?publisherEmail=podcast-bhaaratii%40googlegroups.com&languageCode=en&categoriesCsv=Society%20%26%20Culture&imageUrl=https%3A%2F%2Fi.imgur.com%2FdQjPQYi.jpg&isExplicitYesNo=no
  *
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
//noinspection ScalaDocMissingParameterDescription
case class Podcast(var title: String, var description: String,
                   var imageUrl: String, var languageCode: String,
                   var websiteUrl: Option[String] = None,
                   var copyright: Option[String] = None,
                   var subtitle: Option[String] = None,
                   var publisher: Option[String] = None, var author: Option[String] = None, var publisherEmail: String, var timeSecs1970: Option[Long] = None,
                   var keywords: Seq[String] = Seq(),
                   var items: Seq[PodcastItem], var feedUrl:Option[String] = None,
                   var isExplicitYesNo: Option[String] = None, var categories: Seq[String] = Seq("Society & Culture")) {
  private val log = LoggerFactory.getLogger(this.getClass)

  if (publisher == None) {
    publisher = Some(publisherEmail)
  }

  if (author == None) {
    author = publisher
  }

  def add(n: Node, c: Node): Node = n match {
    case e: Elem => e.copy(child = e.child ++ c)
  }

  def getNode: Node = {
    // XML encoding happens automatically when the node is rendered, no need to specially encode.
    var feed =
      <rss xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd" xmlns:itunesu="http://www.itunesu.com/feed" xmlns:atom="http://www.w3.org/2005/Atom" version="2.0">
        {/*The namespace definitions point to non-existent dtd-s and online validators complain, but that's fine. Can't help it.*/}

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

          {/*The language should be specified according to RFC 3066, RFC 4647 and RFC 5646. List: http://www.loc.gov/standards/iso639-2/php/code_list.php */}
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

          {/*Image must be square and over 1200 x 1200 for Google Play, and over 1400 x 1400 for ITunes.*/}
          <image>
            <url>{imageUrl}</url>
            <title>{title}</title>
            {
            if (websiteUrl.isDefined)
              <link> {websiteUrl.get}</link>
            }
          </image>
          <itunes:image href={imageUrl} />


          {/*Category fields.*/}
          {/*itunes:explicit: Valid values: yes or no or clean. Google Play says: If a tag isn’t included, the content will not be considered explicit. ITunes requires this.*/}
          {if (isExplicitYesNo.isDefined)
          <itunes:explicit>{isExplicitYesNo.get}</itunes:explicit>
          }

        {/*Only certain categories are valid, see: https://support.google.com/googleplay/podcasts/answer/6260341#rpt for Google Play and https://www.seriouslysimplepodcasting.com/itunes-podcast-category-list/ for ITunes.*/}
          {
          categories.map(category => <itunes:category text={category}/>)
          }
          <itunes:keywords>
            {keywords.mkString(",")}
          </itunes:keywords>


          {
          // Some podcast players blithely follow feed order without rearranging.
          items.sorted(Ordering.by((x: PodcastItem) => x.timeSecs1970).reverse).map(_.getNode)
          }
        </channel>
      </rss>
    return feed
  }
}

object Podcast {
  def merge(podcast1: Podcast, podcast2: Podcast): Podcast = {
    val podcast = podcast1.copy()
    podcast.timeSecs1970 = Some(Math.max(podcast1.timeSecs1970.getOrElse(0L), podcast2.timeSecs1970.getOrElse(0L)))
    podcast.keywords = Seq.concat(podcast1.keywords, podcast2.keywords).distinct
    podcast.categories = Seq.concat(podcast1.categories, podcast2.categories).distinct
    podcast.author = Some(Seq(podcast1.author, podcast2.author).filterNot(_.isDefined).map(_.get).mkString(", "))
    if (podcast1.isExplicitYesNo.contains("yes") || podcast2.isExplicitYesNo.contains("yes")) {
      podcast.isExplicitYesNo = Some("yes")
    }
    podcast.items = Seq.concat(podcast1.items, podcast2.items)
    // We deliberately don't merge title, description, subtitle, language, publisher
    podcast
  }
}

object podcastTest {
  private val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val podcastItems = Seq(PodcastItem(title = "xyz", enclosureUrlUnencoded = "http://enclosure.mp3", lengthInSecs = 601))
    val podcast = Podcast(title = "संस्कृतशास्त्राणि: shastras in sanskrit", description = "", publisherEmail = "sanskrit-programmers@googlegroups.com", items = podcastItems, imageUrl = "https://i.imgur.com/dQjPQYi.jpg", languageCode = "en")
    print(podcast.getNode)
  }
}