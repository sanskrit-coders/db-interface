package dbUtils

import dbSchema.quote.{QuoteText, _}
import dbSchema.common.Text
import dbSchema.dcs.{DcsBook, DcsChapter, DcsSentence, DcsOldBook}
import dbSchema.dictionary._
import org.json4s.native.Serialization
import org.json4s.{Extraction, ShortTypeHints}

import scala.collection.mutable

/**
  * Created by vvasuki on 5/26/17.
  */
object jsonHelper {
  val JSON_CLASS_FIELD_NAME = "jsonClass"
  val formats = Serialization.formats(ShortTypeHints(
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
      classOf[DcsSentence]
    )))


  def getJsonMap(caseObj: Any): Map[String,Object] = {
    implicit val formats = jsonHelper.formats.skippingEmptyValues
    val jobj = Extraction.decompose(caseObj)
    val returnMap = jobj.values.asInstanceOf[Map[String,Object]]
    return returnMap
  }

  def fromJsonMap(jsonMap: mutable.Map[String, _]): Any = {
    implicit val formats = jsonHelper.formats
    //    log debug (jsonMap.toString)
    val jsonClass = jsonMap.get(JSON_CLASS_FIELD_NAME).get
    val jsonStr = Serialization.writePretty(jsonMap)
    //    log debug jsonStr
    jsonClass match {
      case "Text" => Serialization.read[Text](jsonStr)
      case "QuoteText" => Serialization.read[QuoteText](jsonStr)
      case "OriginAnnotation" => Serialization.read[OriginAnnotation](jsonStr)
      case "DescriptionAnnotation" => Serialization.read[DescriptionAnnotation](jsonStr)
      case "TopicAnnotation" => Serialization.read[TopicAnnotation](jsonStr)
      case "RatingAnnotation" => Serialization.read[RatingAnnotation](jsonStr)
      case "RequestAnnotation" => Serialization.read[RequestAnnotation](jsonStr)
      case "ReferenceAnnotation" => Serialization.read[ReferenceAnnotation](jsonStr)
      case "DictEntry" => Serialization.read[ReferenceAnnotation](jsonStr)
      case "DictLocation" => Serialization.read[ReferenceAnnotation](jsonStr)
      case "DcsChapter" => Serialization.read[DcsChapter](jsonStr)
      case "DcsBook" => Serialization.read[DcsBook](jsonStr)
      case "DcsOldBook" => Serialization.read[DcsOldBook](jsonStr)
      case "DcsSentence" => Serialization.read[DcsSentence](jsonStr)
    }
  }

}
