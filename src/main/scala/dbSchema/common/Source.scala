package dbSchema.common

/**
  * Created by vvasuki on 5/26/17.
  */
case class Source(name: Text, authors: Option[List[Text]] = None,
                  categories: Option[List[String]] = None, license: Option[String] = None,
                  canonicalSource: Option[String] = None, issuePage: Option[String] = None) {
  def getKey: String = {

    var key = s"${name.getKey}"
    if (authors.nonEmpty && authors.get.nonEmpty) {
      key = key + s"__${authors.get.sortBy(_.scriptRenderings.head.text).map(_.getKey).mkString("_")}"
    }
    key
  }

  // The below causes a mysterious failure (as of 20170326):
  // lift json serialization stops working.
  // Hence commenting out.
  //  def this(nameIn: String, author: String, script: String, language: String) =
  //    this(name= new QuoteText(text=nameIn, script = script, language = Language(language)),
  //      authors = List(author).filter(_.nonEmpty).map(x =>
  //        new QuoteText(text=x, script = script, language = Language(language))) )
}
