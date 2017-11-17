package dbSchema.grammar

import sanskritnlp.transliteration.transliterator
import sun.reflect.generics.reflectiveObjects.NotImplementedException

case class RootAnalysis(var root: Option[String], var pratyayas: Option[Seq[String]] = None)

case class SclAnalysis(qualifications: Map[String, String]) {
  // echo 'corayanwi' | /usr/bin/lt-proc -ct /home/vvasuki/scl/build/morph_bin/all_morf.bin
  // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:1><vacanam:bahu><level:2>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:2><vacanam:bahu><level:2>/cur1<sanAxi_prawyayaH:Nic><prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>

  def getTinanta: Option[Tinanta] = {
    if (!qualifications.contains("lakAraH")) {
      None
    } else {
      val gaNa = qualifications.get("gaNaH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev"))
      val dhaatu = Dhaatu(
        root = qualifications.get("XAwuH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        gaNas = gaNa.map(Seq(_)),
        sclCode=qualifications.get("root"))
      val tinVivaxaa = TinVivaxaa(
        prayoga = qualifications.get("prayogaH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        lakaara = qualifications.get("lakAraH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        puruSha = qualifications.get("puruRaH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        vachana = qualifications.get("vacanam").map(analysisHelper.getVachanaIntFromString(_, scheme = "wx")),
      )
      Some(Tinanta(dhaatu = Some(dhaatu), vivaxaa = Some(tinVivaxaa)))
    }
  }

  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:2><vacanam:bahu><level:2>
  def getSubanta: Option[Subanta] = {
    if (!qualifications.contains("viBakwiH")) {
      None
    } else {
      val gaNa = qualifications.get("gaNaH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev"))
      val praatipadika = Praatipadika(
        root = qualifications.get("root").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        linga = qualifications.get("lifgam").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")),
        prakaara = qualifications.get("vargaH").map(transliterator.transliterate(_, sourceScheme = "wx", destScheme = "dev")).map{
          case "ना" => "साधारणम्"
          case x: String => x
         },
      )
      val vibhakti = SupVibhakti(
        vibhaktiNum = qualifications.get("viBakwiH").map(_.toInt).get,
      )
      Some(Subanta(
        vibhakti = Some(vibhakti),
        vachana = qualifications.get("vacanam").map(analysisHelper.getVachanaIntFromString(_, scheme = "wx")),
      ))
    }
  }

  def toAnalysis : Analysis = {
    Analysis(
      subanta = getSubanta,
      tinanta = getTinanta,
      sclAnalysis = Some(this)
    )
  }
}

case class Analysis(subanta: Option[Subanta] = None, tinanta: Option[Tinanta] = None, sclAnalysis: Option[SclAnalysis])

object analysisHelper {

  def getVachanaIntFromString(vachanaStr: String, scheme: String): Int = {
    transliterator.transliterate(vachanaStr, sourceScheme = scheme, destScheme = "dev") match {
      case "एक" => 1
      case "द्वि" => 2
      case "बहु" => 3
    }
  }

  /**
    *
    * @param dcsGrammarHint Format details: Please see documentation for :class:DcsWord
    *
    * @return
    */
  def fromDcsGrammarHint(dcsGrammarHint: String): Analysis = {
    throw new NotImplementedException()
  }
}