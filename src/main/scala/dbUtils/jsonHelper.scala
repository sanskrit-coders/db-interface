package dbUtils

import dbSchema.archive.{FileInfo, ItemInfo, ItemMetadata}
import dbSchema.common.Text
import dbSchema.dcs.{DcsBook, DcsChapter, DcsOldBook, DcsSentence}
import dbSchema.dictionary._
import dbSchema.grammar._
import dbSchema.quote.{QuoteText, _}
import dbSchema.vedavaapi._
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Extraction, ShortTypeHints}

import scala.reflect.Manifest


class JsonHelper {
  val JSON_CLASS_FIELD_NAME = "jsonClass"
  implicit val formats = new DefaultFormats {
    override val typeHintFieldName = JSON_CLASS_FIELD_NAME
    override val typeHints = ShortTypeHints(
      List(
        classOf[Text],
        classOf[QuoteText],
        classOf[OriginAnnotation],
        classOf[DescriptionAnnotation],
        classOf[TopicAnnotation],
        classOf[RatingAnnotation],
        classOf[RequestAnnotation],
        classOf[ReferenceAnnotation],
        classOf[MemorableBitsAnnotation],
        classOf[DictEntry],
        classOf[DictLocation],
        classOf[DcsBook],
        classOf[DcsOldBook],
        classOf[DcsChapter],
        classOf[DcsSentence],
        classOf[ItemInfo],
        classOf[ItemMetadata],
        classOf[FileInfo],
        classOf[SupVibhakti],
        classOf[Subanta],
        classOf[Subantaavalii],
        classOf[Praatipadika],
        classOf[Dhaatu],
        classOf[TinVivaxaa],
        classOf[BookPortion],
        classOf[BookPositionTarget],
        classOf[JsonObjectNode],
        classOf[Target],
        classOf[ImageAnnotation],
        classOf[TextAnnotation],
        classOf[TextTarget],
        classOf[ImageTarget],
        classOf[AnnotationSource],
        classOf[SamaasaAnnotation],
        classOf[PadaAnnotation]
      ))
  }.skippingEmptyValues


  def getJsonMap(caseObj: Any): Map[String,Object] = {
    val jobj = Extraction.decompose(caseObj)
    val returnMap = jobj.values.asInstanceOf[Map[String,Object]]
    return returnMap
  }

  def asString(caseObj: AnyRef): String = {
    Serialization.writePretty(caseObj)
  }

  def fromString[T](jsonStr: String)(implicit mf: Manifest[T]): T = {
    // Alternative method: JsonMethods.parse(responseString).extract[ItemInfo]
    Serialization.read[T](jsonStr)
  }

  def fromJsonMap[T](jsonMap: collection.Map[String, _])(implicit mf: Manifest[T]): T = {
    val jobj = Extraction.decompose(jsonMap)
    jobj.extract[T]
  }
}

object jsonHelper extends JsonHelper {

}
