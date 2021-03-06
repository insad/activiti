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

package org.activiti.service.impl.json.mappers;

import org.activiti.service.impl.json.ClassMapper;
import org.activiti.service.impl.json.FieldMapper;
import org.activiti.service.impl.persistence.Persistable;
import org.bson.types.ObjectId;

import com.mongodb.DBObject;


/**
 * @author Tom Baeyens
 */
public class OidFieldMapper extends FieldMapper {

  public OidFieldMapper(ClassMapper classMapper) {
    super(classMapper, null);
  }

  public void toJson(DBObject dbObject, Object bean) {
    String oid = ((Persistable)bean).getOid();
    if (oid!=null) {
      dbObject.put("_id", new ObjectId(oid));
    }
  }

  public void toBean(DBObject dbObject, Object bean, Object parentBean) {
    ObjectId objectId = (ObjectId) dbObject.get("_id");
    if (objectId != null) {
      ((Persistable)bean).setOid(objectId.toString());
    }
  }
}
