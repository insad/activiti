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
package org.activiti.cycle.impl.connector.signavio.provider;

import org.activiti.cycle.ContentRepresentationType;
import org.activiti.cycle.RepositoryArtifact;
import org.activiti.cycle.RepositoryException;
import org.json.JSONObject;
import org.restlet.data.Response;

public class Bpmn20Provider extends SignavioContentRepresentationProvider {

  public static final String NAME = "BPMN 2.0";

  public Bpmn20Provider() {
    super(NAME, ContentRepresentationType.XML);
  }

  public byte[] getContent(RepositoryArtifact artifact) {
    try {
      // use the bpmn2_0_serialization export servlet to provide bpmn20 xml
      Response jsonResponse = getJsonResponse(artifact, "/json");
      JSONObject jsonData = new JSONObject(jsonResponse.getEntity().getText());
      JSONObject bpmn20Xml = getConnector(artifact).transformJsonToBpmn20Xml(jsonData.toString());
      String result = bpmn20Xml.getString("xml");
      // works for signavio but not for activiti-modeler
      // Response jpdlResponse = getJsonResponse(artifact, "/bpmn2_0_xml");
      // DomRepresentation xmlData = jpdlResponse.getEntityAsDom();
      // String result = getXmlAsString(xmlData);

      log.finest("BPMN 2.0 String: " + result);

      return toBytes(result);
    } catch (Exception ex) {
      throw new RepositoryException("Error while accessing Signavio repository", ex);
    }
  }

}
