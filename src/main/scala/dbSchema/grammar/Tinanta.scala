package dbSchema.grammar

case class Dhaatu(aupadeshikaDhaatu: Option[String] = None, gaNa: Option[String] = None, artha: Option[String] = None, sclCode: Option[String] = None)

case class TinVivaxaa(prayoga: Option[String], kimpadI: Option[String], lakaara: Option[String], puruSha: Option[String], vachana: Option[String])

case class Tinanta(pada: Option[String], dhaatu: Option[Dhaatu] = None, vivaxaa: Option[TinVivaxaa] = None)

