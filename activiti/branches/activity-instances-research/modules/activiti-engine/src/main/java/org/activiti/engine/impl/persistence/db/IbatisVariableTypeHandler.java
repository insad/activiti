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

package org.activiti.engine.impl.persistence.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.variable.Type;
import org.activiti.engine.impl.variable.VariableTypes;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


/**
 * @author Dave Syer
 */
public class IbatisVariableTypeHandler implements TypeHandler {

  protected VariableTypes variableTypes;

  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    String typeName = rs.getString(columnName);
    return getVariableTypes().getVariableType(typeName);
  }

  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    String typeName = cs.getString(columnIndex);
    return getVariableTypes().getVariableType(typeName);
  }

  public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    String typeName = ((Type) parameter).getTypeName();
    ps.setString(i, typeName);
  }

  protected VariableTypes getVariableTypes() {
    if (variableTypes==null) {
      variableTypes = CommandContext
        .getCurrent()
        .getProcessEngineConfiguration()
        .getVariableTypes();
    }
    return variableTypes;
  }
}
