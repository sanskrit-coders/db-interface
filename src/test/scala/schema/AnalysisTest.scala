package schema

import dbSchema.grammar.SclAnalysis
import dbUtils.jsonHelper
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

import scala.io.Source

class AnalysisTest  extends FlatSpec {
  private val log = LoggerFactory.getLogger(this.getClass)
  // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>/
  private val tinantaAnalysis = SclAnalysis(Map("root" -> "cur1", "prayogaH" -> "karwari", "lakAraH" -> "lat", "puruRaH" -> "pra", "vacanam" -> "bahu", "paxI" -> "parasmEpaxI", "XAwuH" -> "curaz", "gaNaH" -> "curAxiH")).toAnalysis

  "toAnalysis" should "be json-serializable" in {
    log.debug(jsonHelper.asString(tinantaAnalysis))

  }

}