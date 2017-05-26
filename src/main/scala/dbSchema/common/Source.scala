package dbSchema.common

/**
  * Created by vvasuki on 5/26/17.
  */
case class Source(name: Text, authors: List[Text] = List[Text](),
                  categories: List[String] = null, license: String = null,
                  canonicalSource: String = null, issuePage: String = null) {
  def getKey: String =
    return s"${name.getKey}__${authors.sortBy(_.scriptRenderings.head.text).map(_.getKey).mkString("_")}"

  // The below causes a mysterious failure (as of 20170326):
  // lift json serialization stops working.
  // Hence commenting out.
  //  def this(nameIn: String, author: String, script: String, language: String) =
  //    this(name= new QuoteText(text=nameIn, script = script, language = Language(language)),
  //      authors = List(author).filter(_.nonEmpty).map(x =>
  //        new QuoteText(text=x, script = script, language = Language(language))) )
}
