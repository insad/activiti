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

package org.activiti.service.api;

import org.activiti.service.impl.mail.MailService;


/**
 * @author Tom Baeyens
 */
public class ActivitiConfiguration {

  protected String activitiHost;
  protected int activitiPort;
  protected String databaseName;
  protected String userName;
  protected String password;
  protected boolean onCloud;
  protected MailService mailService = new MailService();
  
  public ActivitiConfiguration() {
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }
  
  public String getActivitiHost() {
    return activitiHost;
  }

  public void setActivitiHost(String activitiHost) {
    this.activitiHost = activitiHost;
  }
  
  public int getActivitiPort() {
    return activitiPort;
  }
  
  public void setActivitiPort(int activitiPort) {
    this.activitiPort = activitiPort;
  }
  
  public String getUserName() {
  	return userName;
  }

	public void setUserName(String userName) {
  	this.userName = userName;
  }

	public String getPassword() {
  	return password;
  }

	public void setPassword(String password) {
  	this.password = password;
  }

	public MailService getMailService() {
    return mailService;
	}
  
  public boolean isOnCloud() {
  	return onCloud;
  }

	public void setOnCloud(boolean onCloud) {
  	this.onCloud = onCloud;
  }

	public void setMailService(MailService mailService) {
    this.mailService = mailService;
  }
  
}
