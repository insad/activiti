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

package org.activiti.service.engine.test;

import org.activiti.service.api.Activiti;
import org.activiti.service.impl.rest.impl.RestRequestContext;
import org.activiti.service.rest.RestServlet;


/**
 * @author Tom Baeyens
 */
public class TestRestServlet extends RestServlet {

  private static final long serialVersionUID = 1L;

  public TestRestServlet(Activiti activiti) {
    this.activiti = activiti;
  }
  
  protected void initializeActiviti() {
  }

  protected void logException(Throwable e, RestRequestContext restRequestContext) {
    RestTestCase.setServletException(e);
    super.logException(e, restRequestContext);
  }
}
