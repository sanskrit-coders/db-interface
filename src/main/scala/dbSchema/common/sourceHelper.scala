package dbSchema.common

/**
  * Created by vvasuki on 5/26/17.
  */
object sourceHelper {
  val emptySource = new Source(name = textHelper.fromOnlyText(""))
  def getSanskritDevanaagariiSource(title: String, authors: List[String]): Source = {
    return Source(name=textHelper.getSanskritDevangariiText(title),
      authors=Some(authors.filter(_.nonEmpty).map(textHelper.getSanskritDevangariiText(_)))
    )
  }
  def fromAuthor(author: String): Source = {
    return new Source(name = textHelper.emptyText,
      authors = Some(List(author).filter(_.nonEmpty).map(textHelper.fromOnlyText(_))))
  }
}
