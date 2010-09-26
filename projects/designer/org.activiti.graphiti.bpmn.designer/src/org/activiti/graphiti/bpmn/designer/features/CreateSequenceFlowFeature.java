package org.activiti.graphiti.bpmn.designer.features;

import org.activiti.graphiti.bpmn.designer.ActivitiImageProvider;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class CreateSequenceFlowFeature extends AbstractCreateBPMNConnectionFeature {

	private static final String FEATURE_ID_KEY = "flow";

	public CreateSequenceFlowFeature(IFeatureProvider fp) {
		// provide name and description for the UI, e.g. the palette
		super(fp, "SequenceFlow", "Create SequenceFlow"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public boolean canCreate(ICreateConnectionContext context) {
		FlowNode source = getFlowNode(context.getSourceAnchor());
		FlowNode target = getFlowNode(context.getTargetAnchor());
		if (source != null && target != null && source != target) {
			if (source instanceof StartEvent && target instanceof StartEvent) {
				return false;
			} else if (source instanceof EndEvent) {
				// prevent adding outgoing connections from EndEvents
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean canStartConnection(ICreateConnectionContext context) {
		// return true if source anchor isn't undefined
		if (getFlowNode(context.getSourceAnchor()) != null) {
			return true;
		}
		return false;
	}

	public Connection create(ICreateConnectionContext context) {
		Connection newConnection = null;

		FlowNode source = getFlowNode(context.getSourceAnchor());
		FlowNode target = getFlowNode(context.getTargetAnchor());

		if (source != null && target != null) {
			// create new business object
			SequenceFlow sequenceFlow = createSequenceFlow(source, target);

			// add connection for business object
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
					context.getTargetAnchor());
			addContext.setNewObject(sequenceFlow);
			newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		}
		return newConnection;
	}

	/**
	 * Returns the FlowNode belonging to the anchor, or null if not available.
	 */
	private FlowNode getFlowNode(Anchor anchor) {
		if (anchor != null) {
			Object obj = getBusinessObjectForPictogramElement(anchor.getParent());
			if (obj instanceof FlowNode) {
				return (FlowNode) obj;
			}
		}
		return null;
	}

	/**
	 * Creates a SequenceFlow between two BaseElements.
	 */
	private SequenceFlow createSequenceFlow(FlowNode source, FlowNode target) {
		SequenceFlow sequenceFlow = Bpmn2Factory.eINSTANCE.createSequenceFlow();

		sequenceFlow.setId(getNextId());
		sequenceFlow.setSourceRef(source);
		sequenceFlow.setTargetRef(target);
		sequenceFlow.setName(String.format("to %s", target.getName()));

		getDiagram().eResource().getContents().add(sequenceFlow);

		source.getOutgoing().add(sequenceFlow);
		target.getIncoming().add(sequenceFlow);
		return sequenceFlow;
	}

	@Override
	public String getCreateImageId() {
		return ActivitiImageProvider.IMG_EREFERENCE;
	}

	@Override
	protected String getFeatureIdKey() {
		return FEATURE_ID_KEY;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getFeatureClass() {
		return Bpmn2Factory.eINSTANCE.createSequenceFlow().getClass();
	}

}
