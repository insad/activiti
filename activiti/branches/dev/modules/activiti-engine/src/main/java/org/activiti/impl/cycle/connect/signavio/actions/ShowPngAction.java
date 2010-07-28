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
package org.activiti.impl.cycle.connect.signavio.actions;

import java.util.Map;

import org.activiti.impl.cycle.connect.api.ItemInfo;
import org.activiti.impl.cycle.connect.api.actions.FileAction;
import org.activiti.impl.cycle.connect.api.actions.FileActionGuiRepresentation;
import org.activiti.impl.cycle.connect.signavio.SignavioConnector;


/**
 * 
 * @author bernd.ruecker@camunda.com
 * @author christian.lipphardt@camunda.com
 */
public class ShowPngAction extends FileAction {

	@Override
	public void execute() {
	}

	@Override
	public void execute(ItemInfo itemInfo) {
	}
	
	@Override
	public void execute(ItemInfo itemInfo, Map<String, Object> param) {
	}

	@Override
	public FileActionGuiRepresentation getGuiRepresentation() {
		return FileActionGuiRepresentation.DEFAULT_PANE;
	}
	
	@Override
	public String getGuiRepresentationHtmlSnippet() {
		// TODO: Think about all the casting stuff, maybe make that nicer?
		SignavioConnector connector = (SignavioConnector) getFile().getConnector();
		String imageUrl = connector.getModelAsPngUrl(getFile());
		// TODO: Think about how to get this from the Signavio Connector by the API in the best way 
		return "<img src='" + imageUrl + "' />";
	}

	@Override
	public String getName() {
		return "show image";
	}

	@Override
	public String getGuiRepresentationAsString() {
		return FileActionGuiRepresentation.DEFAULT_PANE.toString();
	}
}
