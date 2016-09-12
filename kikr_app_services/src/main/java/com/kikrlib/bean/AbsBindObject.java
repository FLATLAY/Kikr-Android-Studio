package com.kikrlib.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.kikrlib.utils.Syso;


public abstract class AbsBindObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1169190773487123604L;

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} catch (IllegalAccessException ex) {
				Syso.error(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}
