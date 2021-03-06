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

package org.activiti.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.identity.UserQuery;


/**
 * Contains the possible properties that can be used by the {@link UserQuery}.
 * 
 * @author Joram Barrez
 */
public class UserQueryProperty {
  
  private static final Map<String, UserQueryProperty> properties = new HashMap<String, UserQueryProperty>();

  public static final UserQueryProperty ID = new UserQueryProperty("U.ID_");
  public static final UserQueryProperty FIRST_NAME = new UserQueryProperty("U.FIRST_");
  public static final UserQueryProperty LAST_NAME = new UserQueryProperty("U.LAST_");
  public static final UserQueryProperty EMAIL = new UserQueryProperty("U.EMAIL_");
  
  private String name;

  public UserQueryProperty(String name) {
    this.name = name;
    properties.put(name, this);
  }

  public String getName() {
    return name;
  }
  
  public static UserQueryProperty findByName(String propertyName) {
    return properties.get(propertyName);
  }

}
