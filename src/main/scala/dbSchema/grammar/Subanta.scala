package dbSchema.grammar

// prakaara could be avyaya too.
case class Praatipadika(var root: Option[String] = None,
                        var prakaara: Option[String] = None,
                        var linga: Option[String] = None,
                        var rootAnalysis: Option[RootAnalysis] = None,
                       )
case class SupVibhakti(vibhaktiNum: Int, prakaara: Option[String] = None)
case class Subanta(pada: Option[String] = None, vibhakti: Option[SupVibhakti] = None,
                   vachana: Option[Int] = None, praatipadika: Option[Praatipadika] = None)
case class Subantaavalii(praatipadika: Option[Praatipadika] = None, subantaLists: Option[Seq[Seq[Subanta]]])

