/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.history;

import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.runtime.ExecutionEntity;
import org.activiti.engine.impl.util.ClockUtil;

/**
 * @author Tom Baeyens
 * @author Christian Stettler
 */
public class HistoricProcessInstanceEntity extends HistoricScopeInstanceEntity implements HistoricProcessInstance {

  protected String endActivityId;
  protected String businessKey;
  protected String startFormUserId;
  protected String startFormActivityId;

  public HistoricProcessInstanceEntity() {
  }

  public HistoricProcessInstanceEntity(ExecutionEntity processInstance) {
    id = processInstance.getId();
    processInstanceId = processInstance.getId();
    businessKey = processInstance.getBusinessKey();
    processDefinitionId = processInstance.getProcessDefinitionId();
    startTime = ClockUtil.getCurrentTime();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getPersistentState() {
    Map<String, Object> persistentState = (Map<String, Object>) super.getPersistentState();
    persistentState.put("endStateName", endActivityId);
    return persistentState;
  }

  // getters and setters //////////////////////////////////////////////////////
  
  
  public String getEndActivityId() {
    return endActivityId;
  }
  public String getBusinessKey() {
    return businessKey;
  }
  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }
  public void setEndActivityId(String endActivityId) {
    this.endActivityId = endActivityId;
  }
  public String getStartFormUserId() {
    return startFormUserId;
  }
  public void setStartFormUserId(String startFormUserId) {
    this.startFormUserId = startFormUserId;
  }
  public String getStartFormActivityId() {
    return startFormActivityId;
  }
  public void setStartFormActivityId(String startFormActivityId) {
    this.startFormActivityId = startFormActivityId;
  }
}
