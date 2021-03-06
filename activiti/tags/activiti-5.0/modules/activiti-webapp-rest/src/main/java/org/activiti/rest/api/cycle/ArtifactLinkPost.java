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
package org.activiti.rest.api.cycle;

import java.util.Map;

import org.activiti.cycle.impl.db.entity.RepositoryArtifactLinkEntity;
import org.activiti.rest.util.ActivitiRequest;
import org.activiti.rest.util.ActivitiRequestObject;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;

/**
 * 
 * @author Nils Preusker (nils.preusker@camunda.com)
 */
public class ArtifactLinkPost extends ActivitiCycleWebScript {

  @Override
  protected void execute(ActivitiRequest req, Status status, Cache cache, Map<String, Object> model) {

    ActivitiRequestObject obj = req.getBody();

    String sourceConnectorId = req.getMandatoryString(obj, "connectorId");
    String sourceArtifactId = req.getMandatoryString(obj, "artifactId");

    // String srcElementName = req.getMandatoryString("sourceElementName");
    // String srcElementId = req.getMandatoryString("sourceElementId");

    // String tgtElementName = req.getMandatoryString("targetElementName");
    // String tgtElementId = req.getMandatoryString("targetElementId");

    String targetConnectorId = req.getMandatoryString(obj, "targetConnectorId");
    String targetArtifactId = req.getMandatoryString(obj, "targetArtifactId");

    RepositoryArtifactLinkEntity link = new RepositoryArtifactLinkEntity();
    link.setSourceConnectorId(sourceConnectorId);
    link.setSourceArtifactId(sourceArtifactId);
    link.setTargetConnectorId(targetConnectorId);
    link.setTargetArtifactId(targetArtifactId);    

    try {
      cycleService.addArtifactLink(link);
      model.put("result", true);
    } catch (Exception e) {
      // TODO: see whether this makes sense, probably either exception or
      // negative result.
      model.put("result", false);
      throw new RuntimeException(e);
    }
  }
}
