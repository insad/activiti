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

import org.activiti.impl.Cmd;
import org.activiti.impl.persistence.IbatisPersistenceSession;
import org.activiti.impl.tx.TransactionContext;
import org.activiti.mgmt.TablePage;


/**
 * @author Tom Baeyens
 */
public class GetTablePageCmd implements Cmd<TablePage> {

  String tableName;
  int offset;
  int maxResults;
  
  public GetTablePageCmd(String tableName, int offset, int maxResults) {
    this.tableName = tableName;
    this.offset = offset;
    this.maxResults = maxResults;
  }

  public TablePage execute(TransactionContext transactionContext) {
    IbatisPersistenceSession ibatisPersistenceSession = transactionContext.getTransactionalObject(IbatisPersistenceSession.class);
    return ibatisPersistenceSession.getTablePage(tableName, offset, maxResults);
  }
}
