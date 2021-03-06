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

package org.activiti.pvm.impl.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.pvm.impl.process.ActivityImpl;
import org.activiti.pvm.impl.process.ProcessDefinitionImpl;
import org.activiti.pvm.impl.process.ScopeImpl;
import org.activiti.pvm.runtime.PvmScopeInstance;



/**
 * @author Tom Baeyens
 */
public class ScopeInstanceImpl implements PvmScopeInstance {

  private static final long serialVersionUID = 1L;
  
  protected ProcessDefinitionImpl processDefinition;
  protected ScopeImpl scope;
  protected ScopeInstanceImpl parent;
  protected Set<ActivityInstanceImpl> activityInstances = new HashSet<ActivityInstanceImpl>();
  protected Map<String, Object> variables;
  protected Map<String, Object> systemVariables;
  protected boolean isEnded;
  
  protected ScopeInstanceImpl() {    
  }

  public ScopeInstanceImpl(ProcessDefinitionImpl processDefinition, ScopeImpl scope) {
    this.processDefinition = processDefinition;
    this.scope = scope;
  }

  protected ActivityInstanceImpl createActivityInstance(ActivityImpl activity) {
    ActivityInstanceImpl activityInstance = newActivityInstance(activity);
    activityInstances.add(activityInstance);
    return activityInstance;
  }

  protected ActivityInstanceImpl newActivityInstance(ActivityImpl activity) {
    return new ActivityInstanceImpl(activity, this);
  }

  public void removeActivityInstance(ActivityInstanceImpl activityInstance) {
    activityInstance.destroy();
    activityInstances.remove(activityInstance);
    activityInstance.setParent(null);
  }

  public List<ActivityInstanceImpl> findActivityInstances(String activityId) {
    List<ActivityInstanceImpl> foundActivityInstances = new ArrayList<ActivityInstanceImpl>();
    collectActivityInstances(foundActivityInstances, activityId);
    return foundActivityInstances;
  }
  
  public ActivityInstanceImpl findActivityInstance(String activityId) {
    List<ActivityInstanceImpl> activityInstances = findActivityInstances(activityId);
    if (activityInstances.isEmpty()) {
      return null;
    }
    return activityInstances.get(0);
  }

  protected void collectActivityInstances(List<ActivityInstanceImpl> activityInstanceCollection, String activityId) {
    if (activityId==null) {
      throw new RuntimeException("activitId is null");
    }
    for (ActivityInstanceImpl activityInstance: activityInstances) {
      if (activityId.equals(activityInstance.getActivity().getId())) {
        activityInstanceCollection.add(activityInstance);
      }
      activityInstance.collectActivityInstances(activityInstanceCollection, activityId);
    }
  }
  
  protected void collectActivityIds(List<String> activityIds) {
    for (ActivityInstanceImpl activityInstance: activityInstances) {
      activityIds.add(activityInstance.getActivity().getId());
    }
  }

  
  // user variables ///////////////////////////////////////////////////////////
  
  public boolean hasVariable(String variableName) {
    if (getVariables().containsKey(variableName)) {
      return true;
    }
    if (parent!=null) {
      return parent.hasVariable(variableName);
    }
    return false;
  }

  public void setVariable(String variableName, Object value) {
    if (getVariables().containsKey(variableName)) {
      setVariableLocally(variableName, value);
      return;
    }
    if (parent!=null) {
      parent.setVariable(variableName, value);
      return;
    }
    setVariableLocally(variableName, value);
  }

  public void setVariableLocally(String variableName, Object value) {
    if (variables==null) {
      variables = new HashMap<String, Object>();
    }
    variables.put(variableName, value);
  }

  public Object getVariable(String variableName) {
    if ( (variables!=null) 
         && (variables.containsKey(variableName))
       ) {
      return variables.get(variableName);
    }
    if (parent!=null) {
      return parent.getVariable(variableName);
    }
    return variables.get(variableName);
  }

  public Map<String, Object> getVariables() {
    Map<String, Object> variableCollector = new HashMap<String, Object>();
    collectVariables(variableCollector);
    return variableCollector;
  }
  
  protected void collectVariables(Map<String, Object> variableCollector) {
    if (variables!=null) {
      variableCollector.putAll(variables);
    }
    if (parent!=null) {
      parent.collectVariables(variableCollector);
    }
  }

  public void setVariables(Map<String, Object> variables) {
    if (variables!=null) {
      for (String variableName: variables.keySet()) {
        setVariable(variableName, variables.get(variableName));
      }
    }
  }
  
  // system variables /////////////////////////////////////////////////////////
  
  public void setSystemVariable(ActivityImpl activity, String variableName, Object value) {
    if (systemVariables==null) {
      systemVariables = new HashMap<String, Object>();
    }
    String systemVariableKey = getSystemVariableKey(activity, variableName);
    systemVariables.put(systemVariableKey, value);
  }
  
  public Object getSystemVariable(ActivityImpl activity, String variableName) {
    if (systemVariables==null) {
      return null;
    }
    String systemVariableKey = getSystemVariableKey(activity, variableName);
    return systemVariables.get(systemVariableKey);
  }

  protected String getSystemVariableKey(ActivityImpl activity, String variableName) {
    return "["+activity.getId()+"|"+variableName+"]";
  }

  // end //////////////////////////////////////////////////////////////////////
  
  public void end() {
    isEnded = true;
  }
  
  // getters and setters //////////////////////////////////////////////////////
  
  public ScopeImpl getScope() {
    return scope;
  }

  
  public void setScope(ScopeImpl scope) {
    this.scope = scope;
  }

  
  public Set<ActivityInstanceImpl> getActivityInstances() {
    return activityInstances;
  }

  
  public void setActivityInstances(Set<ActivityInstanceImpl> activityInstances) {
    this.activityInstances = activityInstances;
  }

  
  public ProcessDefinitionImpl getProcessDefinition() {
    return processDefinition;
  }

  public ScopeInstanceImpl getParent() {
    return parent;
  }

  
  public void setParent(ScopeInstanceImpl parent) {
    this.parent = parent;
  }

  public boolean isEnded() {
    return isEnded;
  }

  public void setEnded(boolean isEnded) {
    this.isEnded = isEnded;
  }

}
