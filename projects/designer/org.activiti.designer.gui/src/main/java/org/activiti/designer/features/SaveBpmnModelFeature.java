package org.activiti.designer.features;

import org.activiti.designer.xml.BpmnXMLExport;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

public class SaveBpmnModelFeature extends AbstractCustomFeature {

	public SaveBpmnModelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Save to bpmn 2.0"; //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return "Generate the bpmn 2.0 xml file"; //$NON-NLS-1$
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}

	public void execute(ICustomContext context) {
		try {
			URI uri = getDiagram().eResource().getURI();
			URI bpmnUri = uri.trimFragment();
			bpmnUri = bpmnUri.trimFileExtension();
			bpmnUri = bpmnUri.appendFileExtension("bpmn20.xml");
			
			IProject project = null;
			String parentDiagramName = null;
			if (bpmnUri.isPlatformResource()) {
				String platformString = bpmnUri.toPlatformString(true);
				IResource fileResource = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
				if(fileResource != null) {
					project = fileResource.getProject();
					parentDiagramName = uri.trimFragment().trimFileExtension().lastSegment();
				}
			}
			boolean validBpmn = BpmnXMLExport.validateBpmn(getDiagram().eResource().getContents(), project, parentDiagramName);
			if(validBpmn) {
				
				BpmnXMLExport.createBpmnFile(bpmnUri, getDiagram(), project, parentDiagramName);
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IPath location = Path.fromOSString(bpmnUri.toPlatformString(false));
				IFile file = workspace.getRoot().getFile(location);
				file.refreshLocal(IResource.DEPTH_INFINITE, null);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}