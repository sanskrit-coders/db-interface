package dbSchema.archive

case class ItemMetadata(
                     identifier: String,
                     mediatype: Option[String],
                     collection: Option[List[String]],
                     description: Option[String],
                     scanner: Option[String],
                     subject: Option[List[String]],
                     title: Option[String],
                     publicdate: Option[String],
                     uploader: Option[String],
                     addeddate: Option[String],
                     curation: Option[String],
                     backup_location: Option[String]
                   )