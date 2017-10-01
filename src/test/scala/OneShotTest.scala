import java.net.URI

import org.slf4j.LoggerFactory

object OneShotTest {
  val logger = LoggerFactory.getLogger(this.getClass)
  def main(args: Array[String]): Unit = {
    val enclosureUrl = "https://archive.org/download/Sribhashya-of-Sri-Ramanuja-taught-by-M-A-Alwar/shrIbhAShyam-upl-20130825-Sribhashyam%20Class%2013%20-%20The%20Heritage%20of%20Bodhayana%20Maharshi%20[sūtra%201.1.1%20part%201]%20(Sri%20M.A.%20Alwar).mp3"
//    val enclosureUriFinal = new URI(enclosureUrl.replaceFirst("://.+", ""), enclosureUrl.replaceFirst(".+://", "//"), null)
    val enclosureUriFinal = new URI(enclosureUrl.replaceFirst("://.+", ""), enclosureUrl.replaceFirst(".+://", "").replaceFirst("/.+", ""), enclosureUrl.replaceFirst(".+://", "").replaceFirst("[^/]+/", "/"), null)
    logger.info(enclosureUriFinal.toASCIIString)
  }

}
