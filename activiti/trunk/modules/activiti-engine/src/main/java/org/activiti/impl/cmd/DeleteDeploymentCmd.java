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
package org.activiti.impl.cmd;

import java.util.List;

import org.activiti.engine.impl.persistence.RepositorySession;
import org.activiti.engine.impl.persistence.repository.ProcessDefinitionEntity;
import org.activiti.impl.db.execution.DbExecutionImpl;
import org.activiti.impl.interceptor.CommandContext;
import org.activiti.impl.persistence.PersistenceSession;

/**
 * @author Joram Barrez
 */
public class DeleteDeploymentCmd extends CmdVoid {

  protected String deploymentId;

  protected boolean cascade;

  public DeleteDeploymentCmd(String deploymentId, boolean cascade) {
    this.deploymentId = deploymentId;
    this.cascade = cascade;
  }

  public void executeVoid(CommandContext commandContext) {
    RepositorySession repositorySession = commandContext.getRepositorySession();
    PersistenceSession persistenceSession = commandContext.getPersistenceSession();
    
    if (cascade) {
      List<ProcessDefinitionEntity> processDefinitions = repositorySession.findUndeployedProcessDefinitionsByDeploymentId(deploymentId);
      for (ProcessDefinitionEntity processDefinition : processDefinitions) {
        List<DbExecutionImpl> executions = persistenceSession.findProcessInstancesByProcessDefintionId(processDefinition.getId());
        for (DbExecutionImpl execution : executions) {
          execution.end();
        }
      }
    }

    repositorySession.deleteDeployment(deploymentId);
  }

}
