package dbUtils

import dbSchema.rss.ArchivePodcastRequest
import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory

class JsonHelperTest extends FlatSpec{
  private val log = LoggerFactory.getLogger(this.getClass)
  "jsonHelper" should "parse ArchivePodcastRequest string correctly " in {
    val jsonStr =
      """
        |{
        |  "archiveIds": [
        |    "mahAbhArata-mUla-paThanam-GP"
        |    ],
        |    "useArchiveOrder": true,
        |    "filePattern": ".*\\.mp3",
        |    "podcastTemplate": {
        |      "title": "mahAbhArata-mUla-paThanam - updates महाभारतम्",
        |      "description": "महाभारतम्",
        |      "imageUrl": "https://i.imgur.com/dQjPQYi.jpg",
        |      "languageCode": "sa",
        |      "publisherEmail": "podcast-bhaaratii@googlegroups.com",
        |      "author": "Various sanskrit scholars",
        |      "isExplicitYesNo": "no",
        |      "categories": ["Society & Culture"]
        |    }
        |}
      """.stripMargin
    val podcastRequest = jsonHelper.fromString[ArchivePodcastRequest](jsonStr)
    log.debug(jsonHelper.asString(podcastRequest))
    assert(podcastRequest.archiveIds == List("mahAbhArata-mUla-paThanam-GP"))
  }
}
