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

package org.activiti.engine.impl;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.Deployment;
import org.activiti.engine.DeploymentBuilder;
import org.activiti.engine.ProcessDefinition;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.repository.DeploymentBuilderImpl;
import org.activiti.impl.cmd.DeleteDeploymentCmd;
import org.activiti.impl.cmd.DeployCmd;
import org.activiti.impl.cmd.FindDeploymentResourcesCmd;
import org.activiti.impl.cmd.FindDeploymentsByNameCmd;
import org.activiti.impl.cmd.FindDeploymentsCmd;
import org.activiti.impl.cmd.FindProcessDefinitionCmd;
import org.activiti.impl.cmd.FindProcessDefinitionsCmd;
import org.activiti.impl.cmd.GetDeploymentResourceCmd;
import org.activiti.impl.cmd.GetFormCmd;
import org.activiti.impl.interceptor.CommandExecutor;


/**
 * @author Tom Baeyens
 */
public class RepositoryServiceImpl extends ServiceImpl implements RepositoryService {

  public RepositoryServiceImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public DeploymentBuilder createDeployment() {
    return new DeploymentBuilderImpl(this);
  }

  public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) {
    return commandExecutor.execute(new DeployCmd<Deployment>(deploymentBuilder));
  }

  public void deleteDeployment(String deploymentId) {
    commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, false));
  }

  public void deleteDeploymentCascade(String deploymentId) {
    commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, true));
  }

  @SuppressWarnings("unchecked")
  public List<ProcessDefinition> findProcessDefinitions() {
    return commandExecutor.execute(new FindProcessDefinitionsCmd());
  }

  public ProcessDefinition findProcessDefinitionById(String processDefinitionId) {
    return commandExecutor.execute(new FindProcessDefinitionCmd(processDefinitionId));
  }

  @SuppressWarnings("unchecked")
  public List<Deployment> findDeployments() {
    return commandExecutor.execute(new FindDeploymentsCmd());
  }

  @SuppressWarnings("unchecked")
  public List<String> findDeploymentResourceNames(String deploymentId) {
    return commandExecutor.execute(new FindDeploymentResourcesCmd(deploymentId));
  }

  public InputStream getResourceAsStream(String deploymentId, String resourceName) {
    return commandExecutor.execute(new GetDeploymentResourceCmd(deploymentId, resourceName));
  }

  public Object getStartFormById(String processDefinitionId) {
    return commandExecutor.execute(new GetFormCmd(processDefinitionId, null, null));
  }

  public Object getStartFormByKey(String processDefinitionKey) {
    return commandExecutor.execute(new GetFormCmd(null, processDefinitionKey, null));
  }
}

