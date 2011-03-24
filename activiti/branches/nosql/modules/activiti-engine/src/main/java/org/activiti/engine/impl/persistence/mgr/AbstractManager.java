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

package org.activiti.engine.impl.persistence.mgr;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Session;


/**
 * @author Tom Baeyens
 */
public abstract class AbstractManager implements Session {

  protected DbSqlSession persistenceSession = Context.getCommandContext().getDbSqlSession();
  
  public void close() {
  }

  public void flush() {
  }
}