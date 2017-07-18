package dbSchema.vedavaapi

case class Target(container_id: Option[String])

case class BookPositionTarget(override val container_id: Option[String], position: Option[Float]) extends Target(container_id = container_id)

// Incomplete
case class Rectangle()
case class ImageTarget(override val container_id: Option[String], rectangle: Option[Rectangle]) extends Target(container_id = container_id)

case class TextTarget(override val container_id: Option[String], shabda_id: Option[String]) extends Target(container_id = container_id)

case class JsonObjectWithTarget(targets: Option[Seq[Target]])

case class JsonObjectNode(content: JsonObjectWithTarget, children: Option[Seq[JsonObjectWithTarget]])