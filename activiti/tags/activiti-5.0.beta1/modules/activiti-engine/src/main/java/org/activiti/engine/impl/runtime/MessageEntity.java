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
package org.activiti.engine.impl.runtime;

import java.util.Map;

import org.activiti.engine.impl.interceptor.CommandContext;


/**
 * @author Tom Baeyens
 */
public class MessageEntity extends JobEntity {

  private static final long serialVersionUID = 1L;

  private String repeat = null;
  
  @Override
  public void execute(CommandContext commandContext) {
    super.execute(commandContext);
    commandContext
      .getDbSqlSession()
      .delete(JobEntity.class, id);
  }

  @SuppressWarnings("unchecked")
  public Object getPersistentState() {
    Map<String, Object> persistentState = (Map<String, Object>) super.getPersistentState();
    persistentState.put("duedate", getDuedate());
    return persistentState;
  }
  
  public String getRepeat() {
    return repeat;
  }
  public void setRepeat(String repeat) {
    this.repeat = repeat;
  }
}
