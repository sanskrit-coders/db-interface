package dbSchema.vedavaapi

abstract class AbstractTarget(container_id: Option[String]){}

class Target(container_id: Option[String]) extends AbstractTarget(container_id = container_id)

class BookPositionTarget(val container_id: Option[String], position: Option[Float]) extends AbstractTarget(container_id = container_id)

// Incomplete
class Rectangle(x: Option[Float], y: Option[Float], w: Option[Float], h: Option[Float], score: Option[Float])
class ImageTarget(val container_id: Option[String], rectangle: Option[Rectangle]) extends AbstractTarget(container_id = container_id)

class TextOffsetAddress(start: Option[Int], end: Option[Int])

class TextTarget(val container_id: Option[String], shabda_id: Option[String], offset_address: Option[TextOffsetAddress]) extends AbstractTarget(container_id = container_id)

abstract class JsonObjectWithTarget(targets: Option[Seq[AbstractTarget]]){}

case class JsonObjectNode(content: JsonObjectWithTarget, children: Option[Seq[JsonObjectWithTarget]])