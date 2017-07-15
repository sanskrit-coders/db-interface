package dbUtils

import dbSchema.quote.{QuoteText, _}
import dbSchema.common.Text
import dbSchema.dcs.{DcsBook, DcsChapter, DcsOldBook, DcsSentence}
import dbSchema.dictionary._
import dbSchema.grammar.{Praatipadika, Subanta, Subantaavalii, SupVibhakti}
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Extraction, ShortTypeHints}

import scala.collection.mutable


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
        classOf[SupVibhakti],
        classOf[Subanta],
        classOf[Subantaavalii],
        classOf[Praatipadika]
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

  def fromString(jsonStr: String, jsonClassIn: String = null): Any = {
    var jsonClass = jsonClassIn
    if (jsonClassIn == null) {
      val docMap = Serialization.read[Map[String, _]](jsonStr)
      jsonClass = docMap.get(JSON_CLASS_FIELD_NAME).get.asInstanceOf[String]
    }
    //    log debug jsonStr
    Serialization.read(jsonStr)
  }

  def fromJsonMap(jsonMap: mutable.Map[String, _]): Any = {
    //    log debug (jsonMap.toString)
    val jsonClass = jsonMap.get(JSON_CLASS_FIELD_NAME).get
    val jsonStr = Serialization.writePretty(jsonMap)
    fromString(jsonStr = jsonStr, jsonClassIn = jsonClass.asInstanceOf[String])
  }
}

object jsonHelper extends JsonHelper {

}

object pyJsonHelper  extends JsonHelper {
  override val JSON_CLASS_FIELD_NAME = "py/object"
}