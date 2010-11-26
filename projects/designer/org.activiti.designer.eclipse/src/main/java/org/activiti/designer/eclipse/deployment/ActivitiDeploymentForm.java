package org.activiti.designer.eclipse.deployment;

import java.util.ArrayList;

import org.activiti.designer.eclipse.Logger;
import org.activiti.designer.eclipse.common.ActivitiPlugin;
import org.activiti.designer.eclipse.editor.ActivitiEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ActivitiDeploymentForm {

	public static final int NONE = 0;
	public static final int EXPRESSION = 1;
	public static final int HANDLER = 2;
	public static final String defaultLocation = "C:\\Documents and Settings\\"+System.getProperty("user.name")+"\\Desktop\\Output\\activitiDeployment\\";
	
	private FormToolkit toolkit;
	private Composite composite;
	private IFolder processFolder;
	private ActivitiEditor editor;
	private DeploymentInfo deploymentInfo;
	
	private Form form;
	private Text nameText;
	private Text portText;
	private Text deployerText;
	private Text locationText;
	private Text usernameText;
	private Text passwordText;
	private Text pathText;
	
	private Button deployButton;
	private Button saveButton;
	private Button locationButton;
	private Button saveLocallyButton;
	private Button useCredentialsButton;
	private Button selectPathButton;
	
	private IncludeInDeploymentTreeViewer includeFilesTreeViewer;
	private IncludeInDeploymentTreeViewer includeClassesTreeViewer;
	
	public ActivitiDeploymentForm(FormToolkit toolkit, Composite composite, IFolder processFolder, ActivitiEditor editor) {
		System.out.println("login-----"+defaultLocation);
		this.toolkit = toolkit;
		this.composite = composite;
		this.processFolder = processFolder;
		this.editor = editor;
	}	
		
	public void create() {
		createMainForm();
		createIncludeFilesSection();
		createIncludeClassesSection();
		createLocalSaveSection();
		//createServerInfoSection();
		toolkit.createForm(form.getBody()); 
		//createDeployButton();
	}

	private Composite createServerInfoFormClient() {
		Section serverInfoDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		serverInfoDetails.marginWidth = 5;
		serverInfoDetails.setText("Deployment Server Settings");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.BEGINNING;
		serverInfoDetails.setLayoutData(gridData);
		
		Composite infoFormClient =  toolkit.createComposite(serverInfoDetails);
		serverInfoDetails.setClient(infoFormClient);
		serverInfoDetails.setDescription("Specify the settings of the server you wish to deploy to.");
		toolkit.paintBordersFor(infoFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 3;
		infoFormClient.setLayout(layout);
		return infoFormClient;
	}
	
	private void createServerInfoSection() {		
		Composite serverInfoFormClient = createServerInfoFormClient();		
		createServerNameField(serverInfoFormClient);
		createServerPortField(serverInfoFormClient);
		createUserNameField(serverInfoFormClient);
		createPasswordField(serverInfoFormClient);
	}

	private void createServerNameField(Composite infoFormClient) {
		Label nameLabel = toolkit.createLabel(infoFormClient, "Server Name:");
		nameLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		nameText = toolkit.createText(infoFormClient, "");
		String nameString = getDeploymentInfo().getServerName();
		nameText.setText((nameString == null || nameString.length() == 0) ? "localhost" : nameString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		nameText.setLayoutData(gridData);
		nameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				getDeploymentInfo().setServerName(nameText.getText());
				editor.setDirty(true);
			}
		});
	}
	
	private void createServerPortField(Composite infoFormClient) {
		Label portLabel = toolkit.createLabel(infoFormClient, "Server Port:");
		portLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		portText = toolkit.createText(infoFormClient, "");
		String portString = getDeploymentInfo().getServerPort();
		portText.setText((portString == null || portString.length() == 0) ? "9092" : portString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		portText.setLayoutData(gridData);
		portText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				getDeploymentInfo().setServerPort(portText.getText());
				editor.setDirty(true);
			}
		});
	}
	
	private void createUserNameField(Composite composite) {
		Label usernameLabel = toolkit.createLabel(composite, "Username:");
		usernameLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		usernameText = toolkit.createText(composite, "", SWT.BORDER);
		String usernameString = ActivitiPlugin.getDefault().getPreferenceStore().getString("user name");
		usernameText.setText((usernameString == null || usernameString.length() == 0) ? "sa" : usernameString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		usernameText.setLayoutData(gridData);
	}
	
	private void createPasswordField(Composite composite) {
		Label passwordLabel = toolkit.createLabel(composite, "Password:");
		passwordLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		passwordText = toolkit.createText(composite, "", SWT.PASSWORD | SWT.BORDER);
		String passwordString = ActivitiPlugin.getDefault().getPreferenceStore().getString("password");
		passwordText.setText(passwordString == null ? "" : passwordString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		passwordText.setLayoutData(gridData);
	}

	private void createMainForm() {
		form = toolkit.createForm(composite);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(layoutData);		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createDeployButton() {
		deployButton = toolkit.createButton(form.getBody(), "Deploy Process Archive", SWT.PUSH);
		deployButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		deployButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (cancelOrSaveAndContinue()) {
					createProcessDeployer().deploy();
				}
			}
		});
	}
	
	private IPreferenceStore getPreferenceStore() {
		return ActivitiPlugin.getDefault().getPreferenceStore();
	}
	
	public DeploymentInfo getDeploymentInfo() {
		if (deploymentInfo == null) {
			deploymentInfo = new DeploymentInfo();
			String serverName = getPreferenceStore().getString("server name");
			deploymentInfo.setServerName(serverName == null ? "localhost" : serverName);
			String serverPort = getPreferenceStore().getString("server port");
			deploymentInfo.setServerPort(serverPort == null ? "8080" : serverPort);
			final IJavaProject project = JavaCore.create(processFolder.getProject());
			deploymentInfo.setFilesAndFolders(getElementsToCheckFor(processFolder).toArray());
		}
		return deploymentInfo;
	}
	
	public void setDeploymentInfo(DeploymentInfo deploymentInfo) {
		this.deploymentInfo = deploymentInfo;
		refresh();
	}
	
	private ProcessDeployer createProcessDeployer() {
		ProcessDeployer result = new ProcessDeployer();		
		String location = null;
	
		location = locationText.getText();
		if(location.trim().length()<=0){
			location = defaultLocation;
		}
		result.setTargetLocation(location);
		result.setShell(form.getShell());
		result.setProcessFolder(processFolder);
		result.setFilesAndFolders(getIncludedFiles());
		result.setClassesAndResources(getClassesAndResources());
		return result;
	}
	
	private ArrayList getIncludedFiles() {
		ArrayList result = new ArrayList();
		Object[] objects = includeFilesTreeViewer.getCheckedElements();
		for (int i = 0; i < objects.length; i++) {
			result.add(objects[i]);
		}
		return result;
	}
	
	private ArrayList getClassesAndResources() {
		ArrayList result = new ArrayList();
		Object[] objects = includeClassesTreeViewer.getCheckedElements();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof ICompilationUnit) {
				String string = getResourceName(((ICompilationUnit)objects[i]).getResource());
				result.add(string.substring(0, string.lastIndexOf(".java")) + ".class");
			} else if (objects[i] instanceof IFile) {
				result.add(getResourceName((IFile)objects[i]));
			}
		}
		return result;
	}
	
	private String getResourceName(IResource resource) {
		IPackageFragmentRoot root = getPackageFragmentRoot(resource);
		if (root == null) {
			return null;
		} else {
			int index = root.getResource().getProjectRelativePath().toString().length() + 1;
			return resource.getProjectRelativePath().toString().substring(index);
		}
	}
	
	private IPackageFragmentRoot getPackageFragmentRoot(IResource resource) {
		IPackageFragmentRoot root = null;
		IResource r = resource;
		while (r != null) {
			IJavaElement javaElement = JavaCore.create(r);
			if (javaElement != null && javaElement instanceof IPackageFragmentRoot) {
				root = (IPackageFragmentRoot)javaElement;
				break;
			}
			r = r.getParent();
		}
		return root;
	}
	
	private void createLocalSaveSection() {		
		Composite localSaveFormClient = createLocalSaveFormClient();	
		createSaveLocallyCheckBox(localSaveFormClient);
		createSaveLocationField(localSaveFormClient);
		createSaveButton(localSaveFormClient);
	}
	
	private Composite createIncludeFilesSection() {
		Section includeFilesDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		includeFilesDetails.marginWidth = 5;
		includeFilesDetails.setText("Files and Folders");
		includeFilesDetails.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite includeFilesFormClient = toolkit.createComposite(includeFilesDetails);
		includeFilesDetails.setClient(includeFilesFormClient);
		includeFilesDetails.setDescription("Select the files and folders to include in the process archive.");
		toolkit.paintBordersFor(includeFilesFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 1;
		includeFilesFormClient.setLayout(layout);

		Tree tree = toolkit.createTree(includeFilesFormClient, SWT.CHECK);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		includeFilesTreeViewer = new IncludeInDeploymentTreeViewer(tree);
		includeFilesTreeViewer.setContentProvider(new IncludeFilesTreeContentProvider());
		includeFilesTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		includeFilesTreeViewer.setInput(processFolder);
		includeFilesTreeViewer.addFilter(new IncludeFilesViewerFilter());
		tree.getDisplay().asyncExec(new Runnable() {
			public void run() {
				includeFilesTreeViewer.setCheckedElements(getDeploymentInfo().getFilesAndFolders());
			}			
		});
		includeFilesTreeViewer.addCheckStateListener(new ICheckStateListener() {			
			public void checkStateChanged(CheckStateChangedEvent event) {
				getDeploymentInfo().setFilesAndFolders(includeFilesTreeViewer.getCheckedElements());
				editor.setDirty(true);
			}
		});
		
		final Button includeFilesDefaultButton = toolkit.createButton(includeFilesFormClient, "Reset Defaults", SWT.PUSH);
		includeFilesDefaultButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		includeFilesDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				includeFilesDefaultButton.getDisplay().asyncExec(new Runnable() {
					public void run() {
						includeFilesTreeViewer.setCheckedElements(getDeploymentInfo().getFilesAndFolders());
					}			
				});
			}			
		});
		

		return includeFilesFormClient;
	}
	
	private ArrayList getElementsToCheckFor(IFolder folder) {
		ArrayList list = new ArrayList();
		try {
			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				list.add(members[i]);
				if (members[i] instanceof IFolder) {
					list.addAll(getElementsToCheckFor((IFolder)members[i]));
				}
			}
		} catch(CoreException e) {
			Logger.logError(e);
		}
		return list;
	}
	
	private Composite createIncludeClassesSection() {
		Section includeClassesDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		includeClassesDetails.marginWidth = 5;
		includeClassesDetails.setText("Java Classes and Resources");
		includeClassesDetails.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite includeClassesFormClient =  toolkit.createComposite(includeClassesDetails);
		includeClassesDetails.setClient(includeClassesFormClient);
		includeClassesDetails.setDescription("Select the Java classes and resources to include in the process archive.");
		toolkit.paintBordersFor(includeClassesFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 1;
		includeClassesFormClient.setLayout(layout);

		Tree tree = toolkit.createTree(includeClassesFormClient, SWT.CHECK);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		includeClassesTreeViewer = new IncludeInDeploymentTreeViewer(tree);
		includeClassesTreeViewer.setContentProvider(new IncludeClassesTreeContentProvider());
		includeClassesTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		includeClassesTreeViewer.addFilter(new IncludeClassesViewerFilter());
		final IJavaProject project = JavaCore.create(processFolder.getProject());
		if (project != null) {
			includeClassesTreeViewer.setInput(project);
		}
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				includeClassesTreeViewer.setCheckedElements(getDeploymentInfo().getClassesAndResources());
			}			
		});
		includeClassesTreeViewer.addCheckStateListener(new ICheckStateListener() {			
			public void checkStateChanged(CheckStateChangedEvent event) {
				getDeploymentInfo().setClassesAndResources(includeClassesTreeViewer.getCheckedElements());
				editor.setDirty(true);
			}
		});

		final Button includeClassesDefaultButton = toolkit.createButton(includeClassesFormClient, "Reset Defaults", SWT.PUSH);
		includeClassesDefaultButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		includeClassesDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				composite.getDisplay().asyncExec(new Runnable() {
					public void run() {
						includeClassesTreeViewer.setCheckedElements(getDeploymentInfo().getClassesAndResources());
					}			
				});
			}			
		});
		
		return includeClassesFormClient;
	}
	
	private Composite createLocalSaveFormClient() {
		Section httpInfoDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		httpInfoDetails.marginWidth = 5;
		httpInfoDetails.setText("Local Save Settings");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.BEGINNING;
		httpInfoDetails.setLayoutData(gridData);
		
		Composite detailClient =  toolkit.createComposite(httpInfoDetails);
		httpInfoDetails.setClient(detailClient);
		httpInfoDetails.setDescription("Choose if and where you wish to save the process archive locally.");
		toolkit.paintBordersFor(detailClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 3;
		detailClient.setLayout(layout);
		return detailClient;
	}

	private void createSaveLocallyCheckBox(Composite localSaveFormclient) {
		saveLocallyButton = toolkit.createButton(localSaveFormclient, "Save Process Archive Locally", SWT.CHECK);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		saveLocallyButton.setLayoutData(gridData);
		saveLocallyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button)e.widget).getSelection();
				locationText.setEditable(selection);
				locationButton.setEnabled(selection);
				updateSaveAndDeployButtons(selection);
			}
		});
	}
	
	private void updateSaveAndDeployButtons(boolean selection) {
		if (!selection) {
			saveButton.setEnabled(false);
		} else {
			if (notEmpty(locationText)) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}
	}
	
	private void updateDeployButton() {
		if (notEmpty(pathText)) {
			deployButton.setEnabled(true);
		} else {
			deployButton.setEnabled(false);
		}
	}
	
	private boolean notEmpty(Text text) {
		String string = text.getText();
		return string != null && !"".equals(string);
	}
	
	private void createSaveLocationField(Composite localSaveFormclient) {
		Label locationLabel = toolkit.createLabel(localSaveFormclient, "Location:");
		locationLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		locationText = toolkit.createText(localSaveFormclient, "");
		locationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		locationText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateSaveAndDeployButtons(true);
			}
		});
		locationText.setEditable(false);
		locationButton = toolkit.createButton(localSaveFormclient, "Search...", SWT.PUSH);
		locationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				searchLocation();
			}
		});
		locationButton.setEnabled(false);
	}
	
	private void searchLocation() {
		DirectoryDialog dialog = new DirectoryDialog(form.getShell());
		String result = dialog.open();
		if (result != null) {
			locationText.setText(result);
			updateSaveAndDeployButtons(true);
		}		
	}
	
	private void createSaveButton(Composite localSaveFormClient) {
		saveButton = toolkit.createButton(localSaveFormClient, "Save Without Deploying...", SWT.PUSH);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.BEGINNING;
		saveButton.setLayoutData(gridData);
		saveButton.setEnabled(false);
		saveButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if (cancelOrSaveAndContinue()) {
					createProcessDeployer().saveWithoutDeploying();
				}
			}			
		});
	}
	
	private boolean cancelOrSaveAndContinue() {
		IEditorPart editor = getEditorPart();
		boolean result = true;
		if (editor.isDirty()) {
			int saveProceedCancel = openSaveProceedCancelDialog();
			if (saveProceedCancel == 2) {
				result = false;
			} else if (saveProceedCancel == 0) {
				editor.doSave(null);
			}
		}
		return result;
	}

	private int openSaveProceedCancelDialog() {
        MessageDialog dialog = new MessageDialog(
        		getWorkBenchWindow().getShell(), 
        	"Save Resource", 
        	null, 
        	"'" + processFolder.getName() + "' has been modified. Save changes before deploying?", 
        	MessageDialog.QUESTION, 
        	new String[] { 
        		IDialogConstants.YES_LABEL, 
        		IDialogConstants.NO_LABEL,
        		IDialogConstants.CANCEL_LABEL}, 
        	0);
        return dialog.open();
		
	}
	
	private IWorkbenchWindow getWorkBenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	private IEditorPart getEditorPart() {
		return getWorkBenchWindow().getActivePage().getActiveEditor();
	}
	
	public void refresh() {
		if (composite.isDisposed()) return;
		nameText.setText(getDeploymentInfo().getServerName());
		portText.setText(getDeploymentInfo().getServerPort());
		deployerText.setText(getDeploymentInfo().getServerDeployer());
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!includeClassesTreeViewer.getTree().isDisposed()) {
					includeClassesTreeViewer.refresh();
					includeClassesTreeViewer.setCheckedElements(getDeploymentInfo().getClassesAndResources());
				}
			}			
		});
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!includeFilesTreeViewer.getTree().isDisposed()) {
					includeFilesTreeViewer.refresh();
					includeFilesTreeViewer.setCheckedElements(getDeploymentInfo().getFilesAndFolders());
				}
			}			
		});
	}
}
