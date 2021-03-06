package org.activiti.cycle.impl.connector.signavio.action;

import java.util.Map;

import org.activiti.cycle.Content;
import org.activiti.cycle.RepositoryArtifact;
import org.activiti.cycle.RepositoryArtifactLink;
import org.activiti.cycle.RepositoryConnector;
import org.activiti.cycle.RepositoryFolder;
import org.activiti.cycle.impl.ParameterizedHtmlFormTemplateAction;
import org.activiti.cycle.impl.connector.fs.FileSystemPluginDefinition;
import org.activiti.cycle.impl.connector.signavio.SignavioConnector;
import org.activiti.cycle.impl.connector.signavio.SignavioPluginDefinition;
import org.activiti.cycle.impl.connector.signavio.util.SignavioTransformationHelper;
import org.activiti.cycle.impl.db.entity.RepositoryArtifactLinkEntity;
import org.activiti.cycle.impl.transform.signavio.RemedyTemporarySignavioIncompatibilityTransformation;

/**
 * This action creates a technical BPMN 2.0 XML for the process engines. It
 * copies the XML from Signavio to a given {@link RepositoryFolder}.
 * 
 * By doing that, registered plugins / transformations are executed. The link
 * between the two {@link RepositoryArtifact}s is remembered (TODO).
 * 
 * @author bernd.ruecker@camunda.com
 */
public class CreateTechnicalBpmnXmlAction extends ParameterizedHtmlFormTemplateAction {

  private static final long serialVersionUID = 1L;
  
  public static final String PARAM_TARGET_FOLDER = "targetFolderId";
  public static final String PARAM_TARGET_CONNECTOR = "targetConnectorId";
  public static final String PARAM_TARGET_NAME = "targetName";
  public static final String PARAM_COMMENT = "comment";
  public static final String CREATE_LINK_NAME = "createLink";

  public CreateTechnicalBpmnXmlAction() {
    // TODO: remove when real labels are introduced in the GUI
    super("Create technical model");
  }  

  public CreateTechnicalBpmnXmlAction(String name) {
    // TODO: remove when real labels are introduced in the GUI
    super(name);
  }

  public void execute(RepositoryConnector connector, RepositoryArtifact artifact, Map<String, Object> parameters) throws Exception {
    // TODO: Check with Nils that we get the object instead of the string in
    // here!
    String targetFolderId = (String) getParameter(parameters, PARAM_TARGET_FOLDER, true, null, String.class);
    String targetName = (String) getParameter(parameters, PARAM_TARGET_NAME, false, getProcessName(artifact), String.class);
    String comment = (String) getParameter(parameters, PARAM_COMMENT, false, null, String.class);
    RepositoryConnector targetConnector = (RepositoryConnector) getParameter(parameters, PARAM_TARGET_CONNECTOR, true, null, RepositoryConnector.class);
    boolean createLink = (Boolean) getParameter(parameters, CREATE_LINK_NAME, true, Boolean.TRUE, Boolean.class);

    RepositoryArtifact targetArtifact = createArtifact(connector, artifact, targetFolderId, targetName, targetConnector);

    // TODO: Think about that more, does it make sense like this?
    targetConnector.commitPendingChanges(comment);

    if (createLink) {
      RepositoryArtifactLink link = new RepositoryArtifactLinkEntity();
      link.setSourceArtifact(artifact);
      link.setTargetArtifact(targetArtifact);
      link.setComment(comment);
      link.setLinkType(getLinkType());
      connector.getConfiguration().getCycleService().addArtifactLink(link);
    }
  }

  public String getLinkType() {
    return RepositoryArtifactLinkEntity.TYPE_IMPLEMENTS;
  }

  public RepositoryArtifact createArtifact(RepositoryConnector connector, RepositoryArtifact artifact, String targetFolderId, String targetName,
          RepositoryConnector targetConnector) throws Exception {
    String bpmnXml = createBpmnXml(connector, artifact);
    RepositoryArtifact targetArtifact = createTargetArtifact(targetConnector, targetFolderId, targetName + ".bpmn20.xml", bpmnXml,
            FileSystemPluginDefinition.ARTIFACT_TYPE_BPMN_20_XML);
    return targetArtifact;
  }

  public String createBpmnXml(RepositoryConnector connector, RepositoryArtifact artifact) {
    String sourceJson = getBpmn20Json((SignavioConnector) connector, artifact);
    String transformedJson = SignavioTransformationHelper.applyJsonTransformations(sourceJson);
    String bpmnXml = transformToBpmn20((SignavioConnector) connector, transformedJson);
    return bpmnXml;
  }

  protected String getBpmn20Json(RepositoryConnector connector, RepositoryArtifact artifact) {
    return connector.getContent(artifact.getNodeId(), SignavioPluginDefinition.CONTENT_REPRESENTATION_ID_JSON).asString();
  }

  protected String transformToBpmn20(SignavioConnector connector, String transformedJson) {
    String bpmnXml = connector.transformJsonToBpmn20Xml(transformedJson);
    bpmnXml = new RemedyTemporarySignavioIncompatibilityTransformation().transformBpmn20Xml(bpmnXml);
    return bpmnXml;
  }

  public RepositoryArtifact createTargetArtifact(RepositoryConnector targetConnector, String targetFolderId, String artifactId, String bpmnXml,
          String artifactTypeId) {
    Content content = new Content();
    content.setValue(bpmnXml);
    return targetConnector.createArtifact(targetFolderId, artifactId, artifactTypeId, content);
  }
  
  public String getProcessName(RepositoryArtifact artifact) {
    return artifact.getMetadata().getName();
  }

  @Override
  public String getFormResourceName() {
    return getDefaultFormName();
  }
  
}
