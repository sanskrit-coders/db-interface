package dbSchema.rss

import java.net.URI

import org.slf4j.LoggerFactory

import scala.xml.Node

/**
  * See comments associated with Podcast class for some common references.
  *
  */
case class PodcastItem(var title: String, var enclosureUrlUnencoded: String, var lengthInSecs: Int, var description: String = null, var shortDescription: String = null, var timeSecs1970: Long = 0,
                       var itunesCategoryCode: Option[Int] = None, var ordinal: Option[Int] = None, author: Option[String] = None) {
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
        {
        // Services such as IFTTT use this, though podcast services don't require it. 
        }
        <link>{enclosureUriFinal}</link>
        <itunes:summary>
          {description}
        </itunes:summary>
        <itunes:subtitle>
          {shortDescription}
        </itunes:subtitle>

        {
        if (author.isDefined)
          <author>{author.get}</author>
        }

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
