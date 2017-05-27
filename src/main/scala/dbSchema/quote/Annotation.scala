package dbSchema.quote

import dbSchema.common.{Language, ScriptRendering, Source, sourceHelper, Text}

/*
{
"_id": "amarakosha",
"name": "अमरकोशः",
"authors": ["अमरसिंहः"],
"licenseLink": "http://some-link",
"canonicalSource": "http://some-link",
"categories": ["sanskrit to sanskrit", "thesaurus"],
"issuePage": "https://github.com/sanskrit-coders/stardict-sanskrit/issues"
}
 */


case class Rating(rating: Int)

abstract class Annotation(val textKey: String, val source: Source) {
  def getKey(): String = s"${this.getClass.getSimpleName}__${textKey}__${source.getKey}"
}

case class Topic(text: Text)

case class TopicAnnotation(override val textKey: String, override val source: Source, topics: Seq[Topic]) extends Annotation(textKey = textKey, source = source)

case class MemorableBitsAnnotation(override val textKey: String, override val source: Source, memorableBits: List[QuoteText]) extends Annotation(textKey = textKey, source = source)

case class RatingAnnotation(override val textKey: String, override val source: Source, overall: Rating) extends Annotation(textKey = textKey, source = source)

case class OriginAnnotation(override val textKey: String, override val source: Source, origin: Source = sourceHelper.emptySource) extends Annotation(textKey = textKey, source = source)

// The below is usable for translations as well.
case class DescriptionAnnotation(override val textKey: String, override val source: Source, description: Text) extends Annotation(textKey = textKey, source = source)

case class RequestAnnotation(override val textKey: String, override val source: Source, request: Text) extends Annotation(textKey = textKey, source = source)

case class ReferenceAnnotation(override val textKey: String, override val source: Source, reference: Text) extends Annotation(textKey = textKey, source = source)

case class QuoteWithInfo(quoteText: QuoteText,
                         originAnnotations: List[OriginAnnotation] = List(),
                         topicAnnotations: List[TopicAnnotation] = List(),
                         ratingAnnotations: List[RatingAnnotation] = List(),
                         descriptionAnnotations: List[DescriptionAnnotation] = List(),
                         requestAnnotations: List[RequestAnnotation] = List(),
                         referenceAnnotations: List[ReferenceAnnotation] = List()
                        )



