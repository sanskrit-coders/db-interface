package dbSchema.dcs

class DcsObject(val dcsId: Int) {
  def getKey: String = s"${this.getClass.getSimpleName}_$dcsId"
}

// A part of śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]
case class DcsWord(val root: String, override val dcsId: Int, var dcsGrammarHint: Option[String] = None) extends DcsObject(dcsId = dcsId)

// iti śrīḍalhaṇaviracitāyāṃ suśrutavyākhyāyāṃ nibandhasaṃgrahākhyāyāṃ sūtrasthāne caturviṃśatitamo'dhyāyaḥ // (8.0)
// iti [indecl.]  śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]  suśruta [comp.]-vyākhyā [l.s.f.]  nibandhasaṃgraha [comp.]-ākhyā [l.s.f.]  sūtrasthāna [l.s.n.]  caturviṃśatitama [n.s.m.]-adhyāya [n.s.m.]
case class DcsSentence(val text: String, override val dcsId: Int, var dcsAnalysis: Option[String] = None, var dcsAnalysisDecomposition: Option[Seq[Seq[DcsWord]]] = None) extends DcsObject(dcsId = dcsId) {
  override def getKey: String = s"sentence_$dcsId"
}

case class DcsChapter(override val dcsId: Int, val dcsName: Option[String] = None, var sentenceIds: Option[Seq[Int]] = None) extends DcsObject(dcsId = dcsId)

case class DcsBook (val title: String, override val dcsId: Int, var chapterIds: Option[Seq[Int]] = None) extends DcsObject(dcsId = dcsId)

case class DcsOldBook (val title: String, override val dcsId: Int, var chapters: Option[Seq[DcsChapter]] = None) extends DcsObject(dcsId = dcsId)
