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

package org.activiti.rest.builder.repository;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.rest.builder.BaseJSONObjectBuilder;
import org.activiti.rest.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Frederik Heremans
 */
public class DeploymentsObjectBuilder extends BaseJSONObjectBuilder {

  private DeploymentJSONConverter converter = new DeploymentJSONConverter();

  @Override
  @SuppressWarnings("unchecked")
  public JSONObject createJsonObject(Object modelObject) throws JSONException {
    JSONObject result = new JSONObject();
    Map<String, Object> model = getModelAsMap(modelObject);
    List<Deployment> deployments = (List<Deployment>) model.get("deployments");

    JSONUtil.putPagingInfo(result, model);
    JSONArray deploymentsArray = JSONUtil.putNewArray(result, "data");

    if (deployments != null) {
      for (Deployment deployment : deployments) {
        deploymentsArray.put(converter.getJSONObject(deployment));
      }
    }

    return result;
  }

}
