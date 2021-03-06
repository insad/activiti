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

package org.activiti.engine.impl.persistence.runtime;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessInstance;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.PersistentObject;
import org.activiti.engine.impl.persistence.repository.ProcessDefinitionEntity;
import org.activiti.pvm.impl.process.ActivityImpl;
import org.activiti.pvm.impl.process.ProcessDefinitionImpl;
import org.activiti.pvm.impl.runtime.ActivityInstanceImpl;
import org.activiti.pvm.impl.runtime.ProcessInstanceImpl;


/**
 * @author Tom Baeyens
 */
public class ProcessInstanceEntity extends ProcessInstanceImpl implements ProcessInstance, PersistentObject {
  
  protected String id;
  protected int revision;
  protected String processDefinitionId;
  protected VariableInstanceMap variableInstanceMap;
  protected String superActivityInstanceId;

  public ProcessInstanceEntity() {
  }
  
  protected ProcessInstanceEntity(ProcessDefinitionImpl processDefinition) {
    super(processDefinition);
  }
  
  public static ProcessInstanceEntity createAndInsert(ProcessDefinitionImpl processDefinition) {
    ProcessInstanceEntity processInstance = new ProcessInstanceEntity(processDefinition);
    processInstance.setProcessDefinitionId(processDefinition.getId());
    
    CommandContext
      .getCurrent()
      .getDbSqlSession()
      .insert(processInstance);
    
    processInstance.variableInstanceMap = new ProcessInstanceVariableMap(processInstance);
    
    return processInstance;
  }
  
  @Override
  protected ActivityInstanceImpl createActivityInstance(ActivityImpl activity) {
    ActivityInstanceEntity activityInstance = ActivityInstanceEntity.createAndInsert(activity, this);
    activityInstances.add(activityInstance);
    return activityInstance;
  }

  @Override
  public void removeActivityInstance(ActivityInstanceImpl activityInstance) {
    super.removeActivityInstance(activityInstance);
    ((ActivityInstanceEntity)activityInstance).delete();
  }
  
  /** normal end */
  @Override
  public void end() {
    end(null);
  }

  /** abnormal end of a process instance */
  public void end(String nonCompletionReason) {
    delete();
  }

  public void delete() {
    CommandContext
      .getCurrent()
      .getDbSqlSession()
      .delete(ProcessInstanceEntity.class, id);
  }
  
  public VariableInstanceMap getVariableInstanceMap() {
    if (variableInstanceMap==null) {
      variableInstanceMap = new ProcessInstanceVariableMap(this);
    }
    return variableInstanceMap;
  }
  

  public Object getPersistentState() {
    Map<String, Object> persistentState = new HashMap<String, Object>();
    persistentState.put("superActivityInstanceId", superActivityInstanceId);
    return persistentState;
  }
  
  public int getRevisionNext() {
    return revision+1;
  }
  
  public ProcessDefinitionEntity getProcessDefinition() {
    if (processDefinitionId!=null && processDefinition==null) {
      processDefinition = CommandContext
        .getCurrent()
        .getRepositorySession()
        .findProcessDefinitionById(processDefinitionId);
    }
    return (ProcessDefinitionEntity) processDefinition;
  }
  
  public String toString() {
    return "ProcessInstanceEntity["+id+"]";
  }
  
  // getters and setters //////////////////////////////////////////////////////

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public String getProcessDefinitionId() {
    return processDefinitionId;
  }

  public void setProcessDefinitionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }

  public String getSuperActivityInstanceId() {
    return superActivityInstanceId;
  }

  public void setSuperActivityInstanceId(String superActivityInstanceId) {
    this.superActivityInstanceId = superActivityInstanceId;
  }
}
