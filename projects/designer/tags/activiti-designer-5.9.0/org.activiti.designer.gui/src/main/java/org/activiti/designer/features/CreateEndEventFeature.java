package org.activiti.designer.features;

import org.activiti.designer.ActivitiImageProvider;
import org.activiti.designer.bpmn2.model.EndEvent;
import org.activiti.designer.bpmn2.model.FlowElement;
import org.activiti.designer.bpmn2.model.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public class CreateEndEventFeature extends AbstractCreateFastBPMNFeature {
	
	public static final String FEATURE_ID_KEY = "endevent";

	public CreateEndEventFeature(IFeatureProvider fp) {
		// set name and description of the creation feature
		super(fp, "EndEvent", "Add end event");
	}

	public boolean canCreate(ICreateContext context) {
	  Object parentObject = getBusinessObjectForPictogramElement(context.getTargetContainer());
    return (context.getTargetContainer() instanceof Diagram || parentObject instanceof SubProcess);
	}

	public Object[] create(ICreateContext context) {
		EndEvent endEvent = new EndEvent();
		addObjectToContainer(context, endEvent, "End");
		
		// return newly created business object(s)
		return new Object[] { endEvent };
	}
	
	@Override
	public String getCreateImageId() {
		return ActivitiImageProvider.IMG_ENDEVENT_NONE;
	}

	@Override
	protected String getFeatureIdKey() {
		return FEATURE_ID_KEY;
	}

	@Override
	protected Class<? extends FlowElement> getFeatureClass() {
		return new EndEvent().getClass();
	}

}
