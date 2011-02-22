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

package org.activiti.spring;

import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.test.ProcessEngineInitializer;
import org.activiti.engine.impl.util.ReflectUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author Tom Baeyens
 */
public class SpringProcessEngineInitializer implements ProcessEngineInitializer {

  private static Logger log = Logger.getLogger(SpringProcessEngineInitializer.class.getName());
  
  public ProcessEngine getProcessEngine() {
    log.fine("==== BUILDING SPRING APPLICATION CONTEXT AND PROCESS ENGINE =========================================");
    
    String contextResourceName = null;
    if (ReflectUtil.getResource("activiti-engine-testsuite-context.xml")!=null) {
      contextResourceName = "activiti-engine-testsuite-context.xml";
    } else if (ReflectUtil.getResource("activiti-context.xml")!=null) {
      contextResourceName = "activiti-context.xml";
    } else {
      throw new ActivitiException("no spring context configuration file found (tried 'activiti-context.xml' and 'activiti-engine-testsuite-context.xml')");
    }
    
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(contextResourceName);
    Map<String, ProcessEngine> beansOfType = applicationContext.getBeansOfType(ProcessEngine.class);
    if ( (beansOfType==null)
         || (beansOfType.isEmpty())
       ) {
      throw new ActivitiException("no "+ProcessEngine.class.getName()+" defined in the application context activiti-context.xml");
    }
    
    ProcessEngine processEngine = beansOfType.values().iterator().next();

    log.fine("==== SPRING PROCESS ENGINE CREATED ==================================================================");
    return processEngine;
  }
}
