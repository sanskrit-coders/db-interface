package dbSchema.dictionary

import dbSchema.common.ScriptRendering
import org.slf4j.LoggerFactory


case class DictLocation(dictionaryId: String, entryNumber: Int, pageImage: String) {
  def getKey: String = {
    return s"${dictionaryId}__${entryNumber}"
  }
}

/*
{
"headwords" : ["svar","स्वर्"],
"dictionaryId": "amarakosha",
"locationLink": "http://some-link-to-a-scanned-page",
"entry": "स्वर्ग पुं। <br><br> स्वर्गः <br><br> समानार्थक:स्वर्,स्वर्ग,नाक,त्रिदिव,त्रिदशालय,सुरलोक,द्यो,दिव्,त्रिविष्टप,गो <br><br> 1।1।6।1।2 <br><br> स्वरव्ययं स्वर्गनाकत्रिदिवत्रिदशालयः। सुरलोको द्योदिवौ द्वे स्त्रियां क्लीबे त्रिविष्टपम्.।  <br><br> अवयव : देवसभा <br><br> सम्बन्धि2 : देवः <br><br> पदार्थ-विभागः : , द्रव्यम्, पृथ्वी, अचलनिर्जीवः, स्थानम्, अलौकिकस्थानम्"
}
*/
case class DictEntry(headwords: List[ScriptRendering],
                     entry: ScriptRendering, location: DictLocation) {
  val log = LoggerFactory.getLogger(getClass.getName)
  // A unique identifier for a text rendering.
  def getKey: String = {
    return location.getKey
  }
}

object dictEntryHelper {

}
