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
package org.activiti.cdi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.spi.PassivationCapable;

/**
 * Declare a bean to be BusinessProcessScoped. Instances of
 * BusinessProcessScoped beans are stored as process variables in a
 * ProcessInstance.
 * <p />
 * Note: BusinessProcessScoped beans need to be {@link PassivationCapable}.
 * <p />
 * If no ProcessInstance is associated with the current conversation, instances
 * of {@link BusinessProcessScoped} beans are temporarily stored in the
 * conversation. If the conversation is later associated with a business process
 * instance, the bean instances are flushed to the ProcessInstance.
 * 
 * 
 * @author Daniel Meyer
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface BusinessProcessScoped {

}