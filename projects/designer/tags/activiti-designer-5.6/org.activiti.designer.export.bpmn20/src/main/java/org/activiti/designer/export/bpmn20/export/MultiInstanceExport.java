/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.designer.export.bpmn20.export;

import javax.xml.stream.XMLStreamWriter;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.emf.ecore.EObject;


/**
 * @author Tijs Rademakers
 */
public class MultiInstanceExport implements ActivitiNamespaceConstants {

  public static void createMultiInstance(EObject object, String subProcessId, XMLStreamWriter xtw) throws Exception {
    Activity activity = (Activity) object;
    if(activity.getLoopCharacteristics() == null) return;
    
    MultiInstanceLoopCharacteristics multiInstanceDef = (MultiInstanceLoopCharacteristics) activity.getLoopCharacteristics();
    
    // start multiInstance element
    xtw.writeStartElement("multiInstanceLoopCharacteristics");
    xtw.writeAttribute("isSequential", "" + multiInstanceDef.isIsSequential());
    if(multiInstanceDef.getInputDataItem() != null && multiInstanceDef.getInputDataItem().contains("${")) {
      xtw.writeAttribute(ACTIVITI_EXTENSIONS_PREFIX, ACTIVITI_EXTENSIONS_NAMESPACE, "collection", multiInstanceDef.getInputDataItem());
      if(multiInstanceDef.getElementVariable() != null && multiInstanceDef.getElementVariable().length() > 0) {
        xtw.writeAttribute(ACTIVITI_EXTENSIONS_PREFIX, ACTIVITI_EXTENSIONS_NAMESPACE, "elementVariable", multiInstanceDef.getInputDataItem());
      }
    }
    if (multiInstanceDef.getLoopCardinality() != null && multiInstanceDef.getLoopCardinality().length() > 0) {
      xtw.writeStartElement("loopCardinality");
      xtw.writeCharacters(multiInstanceDef.getLoopCardinality());
      xtw.writeEndElement();
    }
    
    if (multiInstanceDef.getInputDataItem() != null && multiInstanceDef.getInputDataItem().length() > 0 &&
            multiInstanceDef.getInputDataItem().contains("${") == false) {
      
      xtw.writeStartElement("loopDataInputRef");
      xtw.writeCharacters(multiInstanceDef.getInputDataItem());
      xtw.writeEndElement();
      
      if(multiInstanceDef.getElementVariable() != null && multiInstanceDef.getElementVariable().length() > 0) {
        xtw.writeStartElement("inputDataItem");
        xtw.writeAttribute("name", multiInstanceDef.getElementVariable());
        xtw.writeEndElement();
      }
    }
    
    if(multiInstanceDef.getCompletionCondition() != null && multiInstanceDef.getCompletionCondition().length() > 0) {
      xtw.writeStartElement("completionCondition");
      xtw.writeCharacters(multiInstanceDef.getCompletionCondition());
      xtw.writeEndElement();
    }

    // end multiInstance element
    xtw.writeEndElement();
  }
}
