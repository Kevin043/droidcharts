/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 * ---------------
 * StrokeList.java
 * ---------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: StrokeList.java,v 1.5 2005/10/18 13:24:19 mungady Exp $
 *
 * Changes
 * -------
 * 19-Aug-2003 : Version 1 (DG);
 * 
 */

package net.droidsolutions.droidcharts.common;

/**
 * A table of {@link Stroke} objects.
 * 
 * @author David Gilbert
 */
public class StrokeList extends AbstractObjectList {

	/**
	 * Creates a new list.
	 */
	public StrokeList() {
		super();
	}

	/**
	 * Returns a {@link Stroke} object from the list.
	 * 
	 * @param index
	 *            the index (zero-based).
	 * 
	 * @return The object.
	 */
	public Float getStroke(final int index) {
		return (Float) get(index);
	}

	/**
	 * Sets the {@link Stroke} for an item in the list. The list is expanded if
	 * necessary.
	 * 
	 * @param index
	 *            the index (zero-based).
	 * @param stroke
	 *            the {@link Stroke}.
	 */
	public void setStroke(final int index, final float stroke) {
		set(index, stroke);
	}

	/**
	 * Returns an independent copy of the list.
	 * 
	 * @return A clone.
	 * 
	 * @throws CloneNotSupportedException
	 *             if an item in the list cannot be cloned.
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Tests the list for equality with another object (typically also a list).
	 * 
	 * @param o
	 *            the other object.
	 * 
	 * @return A boolean.
	 */
	public boolean equals(final Object o) {

		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (o instanceof StrokeList) {
			return super.equals(o);
		}

		return false;

	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return the hashcode
	 */
	public int hashCode() {
		return super.hashCode();
	}

}
