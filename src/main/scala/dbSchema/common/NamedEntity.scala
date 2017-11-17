package dbSchema.common

abstract class AbstractNamedEntity(val names: Seq[Text])

case class NamedEntity(override val names: Seq[Text]) extends AbstractNamedEntity(names = names)
