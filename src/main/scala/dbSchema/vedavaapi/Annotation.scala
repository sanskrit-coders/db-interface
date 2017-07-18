package dbSchema.vedavaapi

case class AnnotationSource(source_type: Option[String], id: Option[String], _id: Option[String] = None)

case class Annotation(source: Option[AnnotationSource], override val targets: Option[Seq[Target]], _id: Option[String] = None) extends JsonObjectWithTarget(targets = targets)

case class ImageAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[ImageTarget]], override var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class TextAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], content: TextContent, override var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class PadaAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], word: Option[String], root: Option[String], override var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class SamaasaAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], component_padas: Option[Seq[Target]], override var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)