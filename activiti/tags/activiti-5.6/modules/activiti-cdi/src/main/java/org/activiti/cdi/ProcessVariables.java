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
package org.activiti.cdi;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.cdi.annotation.ProcessVariable;

/**
 * Allows to access the process variables of the managed process instance. A
 * process instnace cyn be managed, using the {@link BusinessProcess}-bean.
 * 
 * @author Daniel Meyer
 */
@Named
public class ProcessVariables {

  private Logger logger = Logger.getLogger(ProcessVariables.class.getName());

  @Inject BusinessProcess businessProcess;

  protected String getVariableName(InjectionPoint ip) {
    String variableName = ip.getAnnotated().getAnnotation(ProcessVariable.class).value();
    if (variableName.length() == 0) {
      variableName = ip.getMember().getName();
    }
    return variableName;
  }

  @Produces
  @ProcessVariable("")
  protected Object getProcessVariable(InjectionPoint ip) {
    String processVariableName = getVariableName(ip);

    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Getting process variable '" + processVariableName + "' from ProcessInstance[" + businessProcess.getProcessInstanceId() + "].");
    }

    return businessProcess.getProcessVariable(processVariableName);
  }

  /**
   * Intended to be used in El:
   * <code>#{processVariables.get('variableName')}</code>
   * 
   */
  public Object get(String processVariable) {
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Getting process variable '" + processVariable + "' from ProcessInstance[" + businessProcess.getProcessInstanceId() + "].");
    }
    return businessProcess.getProcessVariable(processVariable);
  }

}
