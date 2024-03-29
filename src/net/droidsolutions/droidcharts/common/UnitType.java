/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 * -------------
 * UnitType.java
 * -------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: UnitType.java,v 1.6 2004/04/26 19:15:46 taqua Exp $
 *
 * Changes:
 * --------
 * 11-Feb-2004 : Version 1 (DG);
 * 
 */

package net.droidsolutions.droidcharts.common;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Used to indicate absolute or relative units.
 * 
 */
public final class UnitType implements Serializable {

	/** Absolute. */
	public static final UnitType ABSOLUTE = new UnitType("UnitType.ABSOLUTE");

	/** Relative. */
	public static final UnitType RELATIVE = new UnitType("UnitType.RELATIVE");

	/** The name. */
	private String name;

	/**
	 * Private constructor.
	 * 
	 * @param name
	 *            the name.
	 */
	private UnitType(final String name) {
		this.name = name;
	}

	/**
	 * Returns a string representing the object.
	 * 
	 * @return the string.
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Returns <code>true</code> if this object is equal to the specified
	 * object, and <code>false</code> otherwise.
	 * 
	 * @param o
	 *            the other object.
	 * 
	 * @return a boolean.
	 */
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UnitType)) {
			return false;
		}

		final UnitType unitType = (UnitType) o;

		if (!this.name.equals(unitType.name)) {
			return false;
		}

		return true;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return the hashcode
	 */
	public int hashCode() {
		return this.name.hashCode();
	}

	/**
	 * Ensures that serialization returns the unique instances.
	 * 
	 * @return the object.
	 * 
	 * @throws ObjectStreamException
	 *             if there is a problem.
	 */
	private Object readResolve() throws ObjectStreamException {
		if (this.equals(UnitType.ABSOLUTE)) {
			return UnitType.ABSOLUTE;
		} else if (this.equals(UnitType.RELATIVE)) {
			return UnitType.RELATIVE;
		}
		return null;
	}

}
