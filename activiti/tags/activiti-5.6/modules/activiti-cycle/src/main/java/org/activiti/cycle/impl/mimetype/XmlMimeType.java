package org.activiti.cycle.impl.mimetype;

import org.activiti.cycle.annotations.CycleComponent;
import org.activiti.cycle.context.CycleContextType;

/**
 * XML Mimetype
 * 
 * @author daniel.meyer@camunda.com
 */
@CycleComponent(context = CycleContextType.APPLICATION)
public class XmlMimeType extends AbstractMimeType {

  public String getName() {
    return "Xml";
  }

  public String getContentType() {
    return "application/xml";
  }

  public String[] getCommonFileExtensions() {
    return new String[] { "xml" };
  }

}
