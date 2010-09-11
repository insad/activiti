package org.activiti.graphiti.bpmn.designer.features;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public class CreateEndEventFeature extends AbstractCreateBPMNFeature {
	
	private static final String FEATURE_ID_KEY = "endevent";

	public CreateEndEventFeature(IFeatureProvider fp) {
		// set name and description of the creation feature
		super(fp, "EndEvent", "Add end event");
	}

	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	public Object[] create(ICreateContext context) {
		EndEvent endEvent = Bpmn2Factory.eINSTANCE.createEndEvent();
		
		endEvent.setId(getNextId());
		endEvent.setName("End");
		
		getDiagram().eResource().getContents().add(endEvent);

		// do the add
		addGraphicalRepresentation(context, endEvent);
		
		// return newly created business object(s)
		return new Object[] { endEvent };
	}

	@Override
	protected String getFeatureIdKey() {
		return FEATURE_ID_KEY;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getFeatureClass() {
		return Bpmn2Factory.eINSTANCE.createEndEvent().getClass();
	}

}
