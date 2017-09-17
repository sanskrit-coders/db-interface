package dbSchema.dcs

import sanskritnlp.transliteration.transliterator

class DcsObject(val dcsId: Int) {
  def getKey: String = s"${this.getClass.getSimpleName}_$dcsId"
}

/**
  * A part of śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]
  *
  * @param root
  * @param dcsId
  * @param dcsGrammarHint
  * Incomplete decypherment from observations.
  *
  * Noun (nAmasUchaka) subanta-s:
  * [comp.] : Compound - non-terminal samAsa component
  * [indecl.] : Indeclinable, avyaya
  *
  * [n.d.m.] : Nominal dual masculine - 1-vibhakti, 2-vachana
  * [n.s.f.] : Nominal singluar feminine - 1-vibhakti, 1-vachana
  * [n.s.n.] : Nominal singluar neuter - 1-vibhakti, 1-vachana
  * [n.p.a.] : Nominal plural adjective - vayam - 1-vibhakti, 3-vachana
  * [ac.s.m.] : Accusative singular masculine - 2-vibhakti, 1-vachana
  * [i.s.m.] : Instrumental singular masculine - 3-vibhakti, 1-vachana
  * [d.s.m.] : Dative singular masculine - 4-vibhakti, 1-vachana
  * [ab.s.m.] : Ablative singular masculine - 5-vibhakti, 1-vachana
  * [g.s.m.] : Genitive singular masculine - 6-vibhakti, 1-vachana
  * [g./o.p.m.] : Genitive plural masculine - 6-vibhakti, 3-vachana
  * [l.s.n.] : Locative singular masculine - 7-vibhakti, 1-vachana
  *
  * Verb (kriyAsUchaka) tiNanta-s:
  * [3. sg. Opt. Pr.] : prathama-puruSha, 1-vachana, Li~N
  * [3. du. Perf.] : prathama-puruSha, 2-vachana, Perfect (liT)
  * [2. sg. Imper. Pr.] : madhyama-puruSha, 1-vachana, Imperative (loT)
  *
  * Verb (kriyAsUchaka) kRdanta-s:
  * [PPP] : Past participle - smRtaH -
  * [Abs.] : dṛṣṭvA
  *
  * References:
  * https://en.wikipedia.org/wiki/Sanskrit_nouns
  * https://en.wikipedia.org/wiki/Sanskrit_verbs
  *
  */
case class DcsWord(val root: String, override val dcsId: Int, var dcsGrammarHint: Option[String] = None) extends DcsObject(dcsId = dcsId) {
  def transliterate(destScheme: String): DcsWord =
    new DcsWord(root = transliterator.transliterate(in_str = root, sourceScheme = "iastDcs", destScheme = destScheme), dcsId = dcsId, dcsGrammarHint = dcsGrammarHint)
}

/**
  * Example source:
  * iti śrīḍalhaṇaviracitāyāṃ suśrutavyākhyāyāṃ nibandhasaṃgrahākhyāyāṃ sūtrasthāne caturviṃśatitamo'dhyāyaḥ // (8.0)
  * iti [indecl.]  śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]  suśruta [comp.]-vyākhyā [l.s.f.]  nibandhasaṃgraha [comp.]-ākhyā [l.s.f.]  sūtrasthāna [l.s.n.]  caturviṃśatitama [n.s.m.]-adhyāya [n.s.m.]
  *
  * @param text
  * @param dcsId
  * @param dcsAnalysis
  * @param dcsAnalysisDecomposition
  */
case class DcsSentence(val text: String, override val dcsId: Int, var dcsAnalysis: Option[String] = None, var dcsAnalysisDecomposition: Option[Seq[Seq[DcsWord]]] = None) extends DcsObject(dcsId = dcsId) {
  override def getKey: String = s"sentence_$dcsId"

  def transliterate(destScheme: String): DcsSentence = DcsSentence(text = transliterator.transliterate(in_str = text, sourceScheme = "iastDcs", destScheme = destScheme), dcsId = dcsId, dcsAnalysis = dcsAnalysis, dcsAnalysisDecomposition = dcsAnalysisDecomposition.map(value => value.map(wordSeq => wordSeq.map(word => word.transliterate(destScheme = destScheme)))))
}

case class DcsChapter(override val dcsId: Int, val dcsName: Option[String] = None, var sentenceIds: Option[Seq[Int]] = None) extends DcsObject(dcsId = dcsId)

case class DcsBook(val title: String, override val dcsId: Int, var chapterIds: Option[Seq[Int]] = None) extends DcsObject(dcsId = dcsId)

case class DcsOldBook(val title: String, override val dcsId: Int, var chapters: Option[Seq[DcsChapter]] = None) extends DcsObject(dcsId = dcsId)
