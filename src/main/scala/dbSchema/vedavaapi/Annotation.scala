package dbSchema.vedavaapi

import dbSchema.common.{NamedEntity, Text}

case class AnnotationSource(source_type: Option[String], id: Option[String])

abstract class Annotation(source: Option[AnnotationSource], val targets: Option[Seq[AbstractTarget]], _id: Option[String] = None) extends JsonObjectWithTarget(targets = targets){}

case class ImageAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[ImageTarget]], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class TextAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], content: Option[Text], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class CommentAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], override val content: Option[Text], override var _id: Option[String] = None) extends TextAnnotation(source = source, targets = targets, content = content, _id = _id)

case class TranslationAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], override val content: Option[Text], override var _id: Option[String] = None) extends TextAnnotation(source = source, targets = targets, content = content, _id = _id)

case class QuoteAnnotation(override val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], override val content: Option[Text], override var _id: Option[String] = None) extends TextAnnotation(source = source, targets = targets, content = content, _id = _id)

case class Topic(override val names: Seq[Text]) extends NamedEntity(names = names)

case class TopicAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], topic: Option[Topic], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class RatingAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], rating: Option[Float], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class Metre(override val names: Seq[Text]) extends NamedEntity(names = names)

case class MetreAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[Target]], metre: Option[Metre], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class PadaAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], word: Option[Text], root: Option[Text], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)

case class SamaasaAnnotation(val source: Option[AnnotationSource], override val targets: Option[Seq[TextTarget]], component_padas: Option[Seq[Target]], var _id: Option[String] = None) extends Annotation(source = source, targets = targets, _id = _id)