package dbSchema.grammar

// prakaara could be avyaya too.
case class Praatipadika(root: String, prakaara: Option[String] = None, linga: Option[String] = None)
case class SupVibhakti(vibhaktiNum: Int, prakaara: Option[String] = None)
case class Subanta(pada: String, vibhakti: Option[SupVibhakti] = None,
                   vachana: Option[Int] = None, praatipadika: Option[Praatipadika] = None)
case class Subantaavalii(praatipadika: Option[Praatipadika] = None, subantaLists: Option[Seq[Seq[Subanta]]])

