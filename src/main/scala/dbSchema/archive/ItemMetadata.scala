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
                     creator: Option[String],
                     addeddate: Option[String],
                     curation: Option[String],
                     backup_location: Option[String]
                   ) {

  def getModificationTime1970Secs(): Option[Long] = {
    var modTimeString = addeddate
    if (!addeddate.isDefined) {
      modTimeString = publicdate
    }
    modTimeString.map(dateString => {
      if (addeddate == None && publicdate == None) {
        0.toLong
      } else {
        val dateString = addeddate.getOrElse(publicdate.get)
        // Example value: 2016-10-17 00:34:49
        import java.text.DateFormat
        import java.text.SimpleDateFormat
        import java.util.Locale
        val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        df.parse(dateString).getTime/1000
      }
    })
  }
}