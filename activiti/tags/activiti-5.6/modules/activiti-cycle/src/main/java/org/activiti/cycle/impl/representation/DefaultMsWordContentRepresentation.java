package org.activiti.cycle.impl.representation;

import org.activiti.cycle.ContentRepresentation;
import org.activiti.cycle.MimeType;
import org.activiti.cycle.RenderInfo;
import org.activiti.cycle.annotations.CycleComponent;
import org.activiti.cycle.context.CycleContextType;
import org.activiti.cycle.impl.mimetype.MsWordMimeType;

/**
 * Default {@link ContentRepresentation} for {@link MsWordMimeType}
 * 
 * @author daniel.meyer@camunda.com
 */
@CycleComponent(context = CycleContextType.APPLICATION)
public class DefaultMsWordContentRepresentation extends AbstractBasicArtifactTypeContentRepresentation {

  private static final long serialVersionUID = 1L;

  public RenderInfo getRenderInfo() {
    return RenderInfo.BINARY;
  }

  protected Class< ? extends MimeType> getMimeType() {
    return MsWordMimeType.class;
  }

}
