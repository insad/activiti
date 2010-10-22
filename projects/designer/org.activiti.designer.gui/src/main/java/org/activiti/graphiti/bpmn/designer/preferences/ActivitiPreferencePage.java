package org.activiti.graphiti.bpmn.designer.preferences;

import org.activiti.graphiti.bpmn.designer.Activator;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ActivitiPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ActivitiPreferencePage() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(ActivitiDesignerPreferences.ECLIPSE_ACTIVITI_JAR_LOCATION.getPreferenceId(),
				"&Directory with Activiti jars:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(prefStore);
		setDescription("Set the location of your Activiti install base.");
		String test = prefStore.getString(ActivitiDesignerPreferences.ECLIPSE_ACTIVITI_JAR_LOCATION.getPreferenceId());
		System.out.println("Val = " + test);
	}
}
