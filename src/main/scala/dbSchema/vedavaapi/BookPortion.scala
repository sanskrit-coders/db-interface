package dbSchema.vedavaapi

import dbSchema.common.{NamedEntity, Text}

case class PublicationDetails(release_time: Option[String], publisher: Option[NamedEntity], issue_page: Option[String])

case class CreationDetails(override val names: Seq[Text], authors: Seq[NamedEntity]) extends NamedEntity(names=names)

case class BookPortion(creation_details: Option[CreationDetails], path: Option[String], thumbnail_path: Option[String], base_data: Option[String], portion_class: Option[String], curated_content: Option[Text], val targets: Option[Seq[BookPositionTarget]],  publication_details: Option[PublicationDetails], var _id: Option[String] = None) extends JsonObjectWithTarget(targets = targets)
