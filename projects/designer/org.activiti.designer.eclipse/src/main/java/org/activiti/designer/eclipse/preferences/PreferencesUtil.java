/**
 * 
 */
package org.activiti.designer.eclipse.preferences;

import org.activiti.designer.eclipse.common.ActivitiPlugin;
import org.activiti.designer.eclipse.extension.export.ExportMarshaller;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Utilities for working with preferences.
 * 
 * @author Tiese Barrell
 * @version 2
 * @since 0.5.1
 * 
 */
public final class PreferencesUtil {

	/**
	 * 
	 */
	private PreferencesUtil() {
	}

	/**
	 * Gets a boolean preference's value from the preference store.
	 * 
	 * @param preference
	 *            the {@link Preferences} to get
	 * @return true if the preference is stored as true, otherwise false and
	 *         false if there is no preference applied
	 */
	public static final boolean getBooleanPreference(final Preferences preference) {
		final IPreferenceStore store = ActivitiPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(preference.getPreferenceId());
	}

	/**
	 * Gets a boolean preference's value from the preference store. This method
	 * is intended for dynamic preference ids only. If possible, you should use
	 * {@link #getBooleanPreference(Preferences)} instead.
	 * 
	 * @param preferenceId
	 *            the id of the preferences to get
	 * @return true if the preference is stored as true, otherwise false and
	 *         false if there is no preference applied
	 */
	public static final boolean getBooleanPreference(final String preferenceId) {
		final IPreferenceStore store = ActivitiPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(preferenceId);
	}

	/**
	 * Returns the preference id to be used for an {@link ExportMarshaller}
	 * extension.
	 * 
	 * @param marshaller
	 *            the {@link ExportMarshaller} to get the property for
	 * @return the id of the preference
	 */
	public static final String getPreferenceId(final ExportMarshaller marshaller) {
		return Preferences.SAVE_TO_FORMAT.getPreferenceId() + "." + marshaller.getMarshallerName();
	}

}
