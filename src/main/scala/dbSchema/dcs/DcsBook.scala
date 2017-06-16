package dbSchema.dcs

// A part of śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]
case class DcsWord(val root: String, val dcsId: Int, var dcsGrammarHint: Option[String] = None)

// iti śrīḍalhaṇaviracitāyāṃ suśrutavyākhyāyāṃ nibandhasaṃgrahākhyāyāṃ sūtrasthāne caturviṃśatitamo'dhyāyaḥ // (8.0)
// iti [indecl.]  śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]  suśruta [comp.]-vyākhyā [l.s.f.]  nibandhasaṃgraha [comp.]-ākhyā [l.s.f.]  sūtrasthāna [l.s.n.]  caturviṃśatitama [n.s.m.]-adhyāya [n.s.m.]
case class DcsSentence(val text: String, val dcsId: Int, var dcsAnalysis: Option[String] = None, var dcsAnalysisDecomposition: Option[Seq[Seq[DcsWord]]] = None) {
  def getKey: String = s"sentence_$dcsId"
}

case class DcsChapter(val dcsId: Int, val dcsName: Option[String] = None, var sentenceIds: Option[Seq[Int]] = None)

case class DcsBook (val title: String, val dcsId: Int, var chapters: Option[Seq[DcsChapter]] = None) {
  def getKey: String = s"book_$dcsId"
}