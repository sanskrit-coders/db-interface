package dbSchema.rss

import dbSchema.archive.ItemInfo
import dbUtils.commandInterface.getClass
import dbUtils.jsonHelper
import org.slf4j.LoggerFactory

case class ArchivePodcastRequest(archiveIds: Seq[String], useArchiveOrder: Boolean = true, filePattern: String, podcastTemplate: Podcast) {
  private val log = LoggerFactory.getLogger(getClass.getName)
  def getPodcast: Podcast = {
    val podcasts = archiveIds.map(archiveId => {
      val uri = f"https://archive.org/metadata/${archiveId}"
      val archiveItem = jsonHelper.fromUrlOrFile[ItemInfo](uri)
      log.info(s"Connecting to ${uri}")
      archiveItem.toPodcast(filePattern = filePattern, useArchiveOrder = useArchiveOrder, podcast = podcastTemplate)
    })
    val finalPodcast: Podcast = podcasts.foldLeft(null.asInstanceOf[Podcast]){
      (p1: Podcast, p2: Podcast) => Podcast.merge(podcast1 = p1, podcast2 = p2)
    }
    finalPodcast
  }

}

case class ArchivePodcastRequestUri(requestUri: String)
