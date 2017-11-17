package dbSchema.grammar

case class Dhaatu(var root: Option[String] = None,
                  var rootAnalysis: Option[RootAnalysis] = None,
                  var gaNas: Option[Seq[String]] = None,
                  var arthas: Option[Seq[String]] = None,
                  var sclCode: Option[String] = None)

case class TinVivaxaa(var prayoga: Option[String] = None,
                      var kimpadI: Option[String] = None,
                      var lakaara: Option[String] = None,
                      var puruSha: Option[String] = None,
                      var vachana: Option[Int] = None)

case class Tinanta(var pada: Option[String] = None, var dhaatu: Option[Dhaatu] = None, var vivaxaa: Option[TinVivaxaa] = None)

