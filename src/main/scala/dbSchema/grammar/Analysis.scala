package dbSchema.grammar

case class Qualification(category: String, value: String)
case class SclAnalysis(qualifications: Option[Seq[Qualification]] = None)
