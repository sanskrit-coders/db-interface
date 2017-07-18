package dbSchema.vedavaapi

case class TextContent(text: Option[String], language: Option[String], encoding: Option[String], var _id: Option[String] = None)

case class BookPortion(title: Option[String], path: Option[String], authors: Option[Seq[String]], base_data: Option[String], portion_class: Option[String], curated_content: Option[String], val targets: Option[Seq[BookPositionTarget]], var _id: Option[String] = None) extends JsonObjectWithTarget(targets = targets)
