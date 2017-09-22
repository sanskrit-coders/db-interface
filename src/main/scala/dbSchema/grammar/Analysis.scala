package dbSchema.grammar

import sun.reflect.generics.reflectiveObjects.NotImplementedException

case class Qualification(category: String, value: String)
case class SclAnalysis(qualifications: Option[Seq[Qualification]] = None)
case class Analysis(subanta: Option[Subanta], tinanta: Option[Tinanta])

object analysisHelper {
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