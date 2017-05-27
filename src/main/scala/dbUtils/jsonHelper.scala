package dbUtils

import dbSchema.quote.{QuoteText, _}
import dbSchema.common.Text
import dbSchema.dictionary._
import org.json4s.native.Serialization
import org.json4s.{Extraction, ShortTypeHints}

import scala.collection.mutable

/**
  * Created by vvasuki on 5/26/17.
  */
object jsonHelper {
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
        classOf[DictLocation]
    )))


  def getJsonMap(caseObj: Any): Map[String,Object] = {
    implicit val formats = jsonHelper.formats
    val jobj = Extraction.decompose(caseObj)
    return jobj.values.asInstanceOf[Map[String,Object]]
  }

  def fromJsonMap(jsonMap: mutable.Map[String, _]): Any = {
    implicit val formats = jsonHelper.formats
    //    log debug (jsonMap.toString)
    val jsonClass = jsonMap.get("jsonClass").get
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
    }
  }

}
