/**
 * 
 */
package org.activiti.designer.integration.servicetask;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Abstract base class for implementing CustomServiceTasks. Defaults provided by
 * this base class:
 * 
 * <li>Provides an id which is determined by invoking {@link #getClass()
 * .getCanonicalName}.</li> <li>Don't contribute to a palette drawer (i.e., the
 * "" drawer). Override {@link #contributeToPaletteDrawer()} if you
 * <strong>do</strong> wish to contribute to a drawer.</li>
 * 
 * @author Tiese Barrell
 * @version 1
 * @since 0.5.1
 * 
 */
public abstract class AbstractCustomServiceTask implements CustomServiceTask {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.designer.integration.servicetask.CustomServiceTask#
	 * contributeToPaletteDrawer()
	 */
	@Override
	public String contributeToPaletteDrawer() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.designer.integration.servicetask.CustomServiceTask#
	 * getId()
	 */
	@Override
	public final String getId() {
		return getClass().getCanonicalName();
	}

	public abstract String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		final Class clazz = this.getClass();
		final Field[] fields = clazz.getDeclaredFields();

		builder.append("Custom Service Task ").append(this.getClass().getSimpleName()).append("\n\tID:\t")
				.append(this.getId()).append("\n\tProvider class:\t").append(this.getClass().getCanonicalName())
				.append("\n\tPalette drawer:\t").append(contributeToPaletteDrawer()).append("\n\tProperties:\n");

		for (final Field field : fields) {
			final Annotation[] annotations = field.getAnnotations();
			for (final Annotation annotation : annotations) {
				if (annotation instanceof Property) {
					builder.append("\t\t").append(field.getName()).append(" (")
							.append(((Property) annotation).type().name()).append(")\n");
				}
			}
		}

		boolean hierarchyOpen = true;
		Class currentClass = clazz;
		while (hierarchyOpen) {
			currentClass = currentClass.getSuperclass();
			if (CustomServiceTask.class.isAssignableFrom(currentClass)) {
				for (Field currentSuperclassField : currentClass.getDeclaredFields()) {
					final Annotation[] currentSuperclassFieldAnnotations = currentSuperclassField.getAnnotations();
					for (final Annotation currentSuperclassFieldAnnotation : currentSuperclassFieldAnnotations) {
						if (currentSuperclassFieldAnnotation instanceof Property) {
							builder.append("\t\t").append(currentSuperclassField.getName()).append(" (")
									.append(((Property) currentSuperclassFieldAnnotation).type().name())
									.append(") (inherited from ").append(currentClass.getSimpleName()).append(")\n");
						}
					}
				}
			} else {
				hierarchyOpen = false;
			}
		}

		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.designer.integration.servicetask.CustomServiceTask#
	 * getSmallIconPath()
	 */
	@Override
	public String getSmallIconPath() {
		return null;
	}

}
