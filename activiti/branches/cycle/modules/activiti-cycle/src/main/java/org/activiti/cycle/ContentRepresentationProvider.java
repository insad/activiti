package org.activiti.cycle;

import java.util.logging.Logger;

import org.activiti.cycle.impl.conf.RepositoryRegistry;

/**
 * A {@link ContentRepresentationProvider} is responsible to create
 * {@link ContentRepresentationDefinition} objects for certain
 * {@link RepositoryArtifact} s. It is registered via the
 * {@link RepositoryRegistry} and new providers can be added on the fly.
 * 
 * @author bernd.ruecker
 */
public abstract class ContentRepresentationProvider {

  protected Logger log = Logger.getLogger(this.getClass().getName());

  private String contentRepresentationName;
  private String contentRepresentationType;

  public ContentRepresentationProvider(String contentRepresentationName, String contentRepresentationType) {
    this.contentRepresentationName = contentRepresentationName;
    this.contentRepresentationType = contentRepresentationType;
  }

  /**
   * creates the {@link ContentRepresentationDefinition} object for the given
   * artifact
   */
  public ContentRepresentationDefinition createContentRepresentationDefinition(RepositoryArtifact artifact) {
    ContentRepresentationDefinition contentRepresentation = new ContentRepresentationDefinition();
    // contentRepresentation.setArtifact(artifact);
    contentRepresentation.setName(contentRepresentationName);
    contentRepresentation.setType(contentRepresentationType);
    return contentRepresentation;
  }

  public abstract void addValueToContent(Content content, RepositoryArtifact artifact);

  public Content createContent(RepositoryArtifact artifact) {
    Content c = new Content();

    addValueToContent(c, artifact);
    if (c.isNull()) {
      throw new RepositoryException("No content created for artifact " + artifact.getId() + " ' from provider '" + getContentRepresentationName()
              + "' (was null). Please check provider or artifact.");
    }

    return c;
  }

  /**
   * key for name in properties for GUI
   * 
   * TODO: I18n
   */
  public String getContentRepresentationName() {
    return contentRepresentationName;
  }

  public String getContentRepresentationType() {
    return contentRepresentationType;
  }
}
