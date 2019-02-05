package dbUtils

import dbSchema.archive.{FileInfo, ItemInfo, ItemMetadata}
import dbSchema.common.{NamedEntity, ScriptRendering, Text}
import dbSchema.dcs.{DcsBook, DcsChapter, DcsOldBook, DcsSentence}
import dbSchema.dictionary._
import dbSchema.grammar._
import dbSchema.vedavaapi._
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Extraction, Formats, ShortTypeHints}
import org.slf4j.LoggerFactory

import scala.reflect.Manifest


class JsonHelper {
  private val log = LoggerFactory.getLogger(this.getClass)
  val JSON_CLASS_FIELD_NAME = "jsonClass"
  implicit val formats: Formats = new DefaultFormats {
    override val typeHintFieldName: String = JSON_CLASS_FIELD_NAME
    override val typeHints: ShortTypeHints = ShortTypeHints(
      List(
        // dictionary/*.scala
        classOf[DictEntry],
        classOf[DictLocation],

        // dcs/*.scala
        classOf[DcsBook],
        classOf[DcsOldBook],
        classOf[DcsChapter],
        classOf[DcsSentence],

        // archive/*.scala
        classOf[ItemInfo],
        classOf[ItemMetadata],
        classOf[FileInfo],

        // grammar/*.scala
        classOf[SupVibhakti],
        classOf[Subanta],
        classOf[Subantaavalii],
        classOf[Praatipadika],
        classOf[Dhaatu],
        classOf[TinVivaxaa],
        classOf[Analysis],
        classOf[RootAnalysis],

        // common/*.scala
        classOf[Text],
        classOf[NamedEntity],
        classOf[ScriptRendering],
        classOf[JsonObjectNode],

        // vedavaapi/BookPortion.scala
        classOf[BookPortion],
        classOf[PublicationDetails],
        classOf[CreationDetails],

        // vedavaapi/Target.scala
        classOf[Target],
        classOf[BookPositionTarget],
        classOf[TextOffsetAddress],
        classOf[TextTarget],
        classOf[Rectangle],
        classOf[ImageTarget],

        // vedavaapi/Annotation.scala
        classOf[ImageAnnotation],
        classOf[TextAnnotation],
        classOf[AnnotationSource],
        classOf[SamaasaAnnotation],
        classOf[CommentAnnotation],
        classOf[TranslationAnnotation],
        classOf[Topic],
        classOf[TopicAnnotation],
        classOf[RatingAnnotation],
        classOf[Metre],
        classOf[MetreAnnotation]
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
  
  def fromFile[T](filePath: String)(implicit mf: Manifest[T]): T = {
    fromString[T](scala.io.Source.fromFile(filePath).mkString)
  }

  def fromUrl[T](url: String)(implicit mf: Manifest[T]): T = {
    fromString[T](scala.io.Source.fromURL(url).mkString)
  }
  
  def fromUrlOrFile[T](url: String)(implicit mf: Manifest[T]): T = {
    try {
      fromUrl[T](url)
    } catch {
      case _: Throwable => fromFile[T](url)
    }
  }

  def fromJsonMap[T](jsonMap: collection.Map[String, _])(implicit mf: Manifest[T]): T = {
    val jobj = Extraction.decompose(jsonMap)
    jobj.extract[T]
  }
}

object jsonHelper extends JsonHelper {

}
