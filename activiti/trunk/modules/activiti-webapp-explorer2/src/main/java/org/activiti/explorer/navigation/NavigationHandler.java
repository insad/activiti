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

package org.activiti.explorer.navigation;



/**
 * Handler class for responding on navigation requests.
 * 
 * @author Frederik Heremans
 */
public interface NavigationHandler {

  /**
   * Gets the string that triggers this handler to be used
   * when navigation should be performed. This is the first part of
   * the uri-fragment.
   */
  String getTrigger();
  
  /**
   * Handle the incoming navigation request. The url-fragment passed contains all
   * uri parts that triggered the navigation request (including the one returned by 
   * {@link NavigationHandler#getTrigger()}), as well as the parameters contain 
   * the values of the parameters passed in the query string (if any).
   * 
   * The handleNavigation is responsible for setting the current URI-fragment to
   * the correct value (the passed uriFragment or another on in case URL is invalid).
   */
  void handleNavigation(UriFragment uriFragment);
}
