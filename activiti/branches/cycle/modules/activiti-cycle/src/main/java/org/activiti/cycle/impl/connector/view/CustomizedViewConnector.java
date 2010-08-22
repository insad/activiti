package org.activiti.cycle.impl.connector.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.activiti.cycle.Content;
import org.activiti.cycle.ContentRepresentationDefinition;
import org.activiti.cycle.RepositoryArtifact;
import org.activiti.cycle.RepositoryConnector;
import org.activiti.cycle.RepositoryFolder;
import org.activiti.cycle.RepositoryNode;
import org.activiti.cycle.impl.connector.AbstractRepositoryConnector;

/**
 * Connector to represent customized view for a user of cycle to hide all the
 * internal configuration and {@link RepositoryConnector} stuff from the client
 * (e.g. the webapp)
 * 
 * @author ruecker
 */
public class CustomizedViewConnector extends AbstractRepositoryConnector<CustomizedViewConfiguration> {

  // private final String repositoryName;
  //
  private Map<String, RepositoryConnector> repositoryConnectors;

  // private final RepositoryConnector connector;
      
  public CustomizedViewConnector(CustomizedViewConfiguration customizedViewConfiguration) {
    super(customizedViewConfiguration);
  }

  /**
   * Get a map with all {@link RepositoryConnector}s created lazily and the name
   * of the connector as key for the map.
   */
  private Map<String, RepositoryConnector> getRepositoryConnectors() {
    if (repositoryConnectors == null) {
      repositoryConnectors = getConfiguration().getConfigurationContainer().getConnectorMap();
    }
    return repositoryConnectors;
  }

  /**
   * login into all repositories configured (if no username and password was
   * provided by the configuration already).
   * 
   * TODO: Make more sophisticated. Questions: What to do if one repo cannot
   * login?
   */
  public boolean login(String username, String password) {
    for (RepositoryConnector connector : getRepositoryConnectors().values()) {
      connector.login(username, password);
    }
    return true;
  }

  public void commitPendingChanges(String comment) {
    // TODO: Check implementation
    for (RepositoryConnector connector : getRepositoryConnectors().values()) {
      connector.commitPendingChanges(comment);
    }
  }

  private String getClientUrl(RepositoryNode repositoryNode) {
    return getConfiguration().getBaseUrlWithoutSlashAtTheEnd() + repositoryNode.getId(); 
  }

  private String getIdWithRepoName(RepositoryNode repositoryNode) {
    String repositoryName = repositoryNode.getConnector().getConfiguration().getName();
    return getRepositoryPrefix(repositoryName) + repositoryNode.getId();
  }

  private String getRepositoryPrefix(String repositoryName) {
    return "/" + repositoryName;
  }

  private String getClientUrl(RepositoryArtifact artifact, ContentRepresentationDefinition contentRepresentationDefinition) {
    return artifact.getClientUrl() + "/content/" + contentRepresentationDefinition.getName();
  }


  /**
   * add repository name in config to URL
   */
  private RepositoryNode adjust(RepositoryNode repositoryNode) {
    repositoryNode.setId(getIdWithRepoName(repositoryNode));
    repositoryNode.setClientUrl(getClientUrl(repositoryNode));

    if (repositoryNode instanceof RepositoryArtifact) { 
      RepositoryArtifact artifact = (RepositoryArtifact) repositoryNode;
      Collection<ContentRepresentationDefinition> contentRepresentationDefinitions = artifact.getContentRepresentationDefinitions();
      for (ContentRepresentationDefinition contentRepresentationDefinition : contentRepresentationDefinitions) {
        contentRepresentationDefinition.setClientUrl(
                getClientUrl(artifact, contentRepresentationDefinition));
      }
    }
    return repositoryNode;
  }

  protected RepositoryConnector getConnectorFromUrl(String url) {
    int index = url.indexOf("/");
    if (index == 0) {
      return getConnectorFromUrl(url.substring(1));
    } else {
      String repositoryName = url.substring(0, index);
      return getRepositoryConnectors().get(repositoryName);
    }
  }

  protected String getRepositoryPartOfUrl(String url) {
    int index = url.indexOf("/");
    if (index == 0) {
      return getRepositoryPartOfUrl(url.substring(1));
    } else {
      return url.substring(index);
    }
  }

  public List<RepositoryNode> getChildNodes(String parentUrl) {
    // special handling for root
    if ("/".equals(parentUrl)) {
      return getRepoRootFolders();
    } 
    
    // First identify correct repo and truncate path to local part of
    // connector
    RepositoryConnector connector = getConnectorFromUrl(parentUrl);
    parentUrl = getRepositoryPartOfUrl(parentUrl);

      // now make the query
    List<RepositoryNode> childNodes = connector.getChildNodes(parentUrl);
   
    // and adjust the result to include repo name
    for (RepositoryNode repositoryNode : childNodes) {
       adjust(repositoryNode);
    }
    return childNodes;
  }

  public List<RepositoryNode> getRepoRootFolders() {
    ArrayList<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
    for (String repoName : getRepositoryConnectors().keySet()) {
      RepositoryFolder folder = new RepositoryFolder(this);
      folder.setId(repoName);
      folder.setClientUrl(getClientUrl(folder));
      nodes.add(folder);
    }
    return nodes;
  }

  public RepositoryArtifact getArtifactDetails(String id) {
    RepositoryArtifact repositoryArtifact = getConnectorFromUrl(id).getArtifactDetails(
            getRepositoryPartOfUrl(id));
    adjust(repositoryArtifact);
    return repositoryArtifact;
  }
  
  public void createNewArtifact(String containingFolderId, RepositoryArtifact artifact, Content artifactContent) {
    // TODO: Do we have to change artifact id?
    getConnectorFromUrl(containingFolderId).createNewArtifact(getRepositoryPartOfUrl(containingFolderId), artifact, artifactContent);
  }

  public void modifyArtifact(RepositoryArtifact artifact, ContentRepresentationDefinition artifactContent) {
    RepositoryConnector connector = getConnectorFromUrl(artifact.getId());
    artifact.setId(getRepositoryPartOfUrl(artifact.getId()));    
    connector.modifyArtifact(artifact, artifactContent);
  }

  public void createNewSubFolder(String parentFolderUrl, RepositoryFolder subFolder) {
    // TODO: Do we have to change subFolder id?
    getConnectorFromUrl(parentFolderUrl).createNewSubFolder(getRepositoryPartOfUrl(parentFolderUrl), subFolder);
  }

  public void deleteArtifact(String artifactUrl) {
    getConnectorFromUrl(artifactUrl).deleteArtifact(getRepositoryPartOfUrl(artifactUrl));
  }

  public void deleteSubFolder(String subFolderUrl) {
    getConnectorFromUrl(subFolderUrl).deleteSubFolder(getRepositoryPartOfUrl(subFolderUrl));
  }

  public Content getContent(String nodeId, String representationName) {
    return getConnectorFromUrl(nodeId).getContent(getRepositoryPartOfUrl(nodeId), representationName);
  }

}
