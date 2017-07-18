package dbSchema.vedavaapi

case class AnnotationSource(source_type: Option[String], id: Option[String], _id: Option[String] = None)

abstract class Annotation(source: Option[AnnotationSource], val targets: Option[Seq[AbstractTarget]], _id: Option[String] = None) extends JsonObjectWithTarget(targets = targets){}

case class ImageAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[ImageTarget]], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class TextAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], content: TextContent, var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class PadaAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], word: Option[String], root: Option[String], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class SamaasaAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], component_padas: Option[Seq[Target]], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)