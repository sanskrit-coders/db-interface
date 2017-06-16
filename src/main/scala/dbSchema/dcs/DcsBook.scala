package dbSchema.dcs

// A part of śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]
case class DcsWord(val root: String, val dcsId: Int, val dcsGrammarHint: Option[String] = None)

// iti śrīḍalhaṇaviracitāyāṃ suśrutavyākhyāyāṃ nibandhasaṃgrahākhyāyāṃ sūtrasthāne caturviṃśatitamo'dhyāyaḥ // (8.0)
// iti [indecl.]  śrī [comp.]-ḍalhaṇa [comp.]-viracay [PPP]  suśruta [comp.]-vyākhyā [l.s.f.]  nibandhasaṃgraha [comp.]-ākhyā [l.s.f.]  sūtrasthāna [l.s.n.]  caturviṃśatitama [n.s.m.]-adhyāya [n.s.m.]
case class DcsSentence(val text: String, val dcsId: Int, val decomposition: Option[Seq[Seq[DcsWord]]] = None) {
  def getKey: String = s"sentence_$dcsId"
}

case class DcsChapter(val text: String, val dcsId: Int, val sentenceIds: Option[Seq[Int]] = None)

case class DcsBook (val title: String, val dcsId: Int, val chapterIds: Option[Seq[DcsChapter]] = None) {
  def getKey: String = s"book_$dcsId"
}