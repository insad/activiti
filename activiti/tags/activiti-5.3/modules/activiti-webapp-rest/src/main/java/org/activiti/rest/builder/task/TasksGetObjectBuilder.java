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

package org.activiti.rest.builder.task;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.activiti.rest.builder.BaseJSONObjectBuilder;
import org.activiti.rest.util.JSONUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author Frederik Heremans
 */
public class TasksGetObjectBuilder extends BaseJSONObjectBuilder {

  private TaskJSONConverter converter = new TaskJSONConverter();
  
  @Override
  @SuppressWarnings("unchecked")
  public JSONObject createJsonObject(Object modelObject) throws JSONException {
    JSONObject result = new JSONObject();
    Map<String, Object> model = getModelAsMap(modelObject);
    
    JSONUtil.putPagingInfo(result, model);
    
    List<Task> tasks = (List<Task>) model.get("tasks");
    JSONArray taskArray = JSONUtil.putNewArray(result, "data");
    
    if(tasks != null) {
      for(Task task : tasks) {
        taskArray.put(converter.getJSONObject(task));
      }
    }
    return result;
  }
}
