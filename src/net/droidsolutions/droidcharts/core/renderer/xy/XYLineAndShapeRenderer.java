/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
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
 * ---------------------------
 * XYLineAndShapeRenderer.java
 * ---------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes:
 * --------
 * 27-Jan-2004 : Version 1 (DG);
 * 10-Feb-2004 : Minor change to drawItem() method to make cut-and-paste
 *               overriding easier (DG);
 * 25-Feb-2004 : Replaced CrosshairInfo with CrosshairState (DG);
 * 25-Aug-2004 : Added support for chart entities (required for tooltips) (DG);
 * 24-Sep-2004 : Added flag to allow whole series to be drawn as a path
 *               (necessary when using a dashed stroke with many data
 *               items) (DG);
 * 04-Oct-2004 : Renamed BooleanUtils --> BooleanUtilities (DG);
 * 11-Nov-2004 : Now uses ShapeUtilities to translate shapes (DG);
 * 27-Jan-2005 : The getLegendItem() method now omits hidden series (DG);
 * 28-Jan-2005 : Added new constructor (DG);
 * 09-Mar-2005 : Added fillPaint settings (DG);
 * 20-Apr-2005 : Use generators for legend tooltips and URLs (DG);
 * 22-Jul-2005 : Renamed defaultLinesVisible --> baseLinesVisible,
 *               defaultShapesVisible --> baseShapesVisible and
 *               defaultShapesFilled --> baseShapesFilled (DG);
 * 29-Jul-2005 : Added code to draw item labels (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 20-Jul-2006 : Set dataset and series indices in LegendItem (DG);
 * 06-Feb-2007 : Fixed bug 1086307, crosshairs with multiple axes (DG);
 * 21-Feb-2007 : Fixed bugs in clone() and equals() (DG);
 * 20-Apr-2007 : Updated getLegendItem() for renderer change (DG);
 * 18-May-2007 : Set dataset and seriesKey for LegendItem (DG);
 * 08-Jun-2007 : Fix for bug 1731912 where entities are created even for data
 *               items that are not displayed (DG);
 * 26-Oct-2007 : Deprecated override attributes (DG);
 * 02-Jun-2008 : Fixed tooltips at lower edges of data area (DG);
 * 17-Jun-2008 : Apply legend shape, font and paint attributes (DG);
 * 19-Sep-2008 : Fixed bug with drawSeriesLineAsPath - patch by Greg Darke (DG);
 *
 */

package net.droidsolutions.droidcharts.core.renderer.xy;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import net.droidsolutions.droidcharts.awt.Font;
import net.droidsolutions.droidcharts.awt.GeneralPath;
import net.droidsolutions.droidcharts.awt.Line2D;
import net.droidsolutions.droidcharts.awt.PathIterator;
import net.droidsolutions.droidcharts.awt.Rectangle2D;
import net.droidsolutions.droidcharts.awt.Shape;
import net.droidsolutions.droidcharts.common.BooleanList;
import net.droidsolutions.droidcharts.common.BooleanUtilities;
import net.droidsolutions.droidcharts.common.RectangleEdge;
import net.droidsolutions.droidcharts.common.ShapeUtilities;
import net.droidsolutions.droidcharts.core.LegendItem;
import net.droidsolutions.droidcharts.core.axis.ValueAxis;
import net.droidsolutions.droidcharts.core.data.XYDataset;
import net.droidsolutions.droidcharts.core.entity.EntityCollection;
import net.droidsolutions.droidcharts.core.event.RendererChangeListener;
import net.droidsolutions.droidcharts.core.label.ItemLabelPosition;
import net.droidsolutions.droidcharts.core.label.XYItemLabelGenerator;
import net.droidsolutions.droidcharts.core.plot.CrosshairState;
import net.droidsolutions.droidcharts.core.plot.PlotOrientation;
import net.droidsolutions.droidcharts.core.plot.PlotRenderingInfo;
import net.droidsolutions.droidcharts.core.plot.XYPlot;

/**
 * A renderer that connects data points with lines and/or draws shapes at each
 * data point. This renderer is designed for use with the {@link XYPlot} class.
 * The example shown here is generated by the
 * <code>XYLineAndShapeRendererDemo2.java</code> program included in the
 * JFreeChart demo collection: <br>
 * <br>
 * <img src="../../../../../images/XYLineAndShapeRendererSample.png"
 * alt="XYLineAndShapeRendererSample.png" />
 * 
 */
public class XYLineAndShapeRenderer extends AbstractXYItemRenderer implements
		XYItemRenderer, Cloneable, Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -7435246895986425885L;

	/**
	 * A flag that controls whether or not lines are visible for ALL series.
	 * 
	 * @deprecated As of 1.0.7.
	 */
	private Boolean linesVisible;

	/**
	 * A table of flags that control (per series) whether or not lines are
	 * visible.
	 */
	private BooleanList seriesLinesVisible;

	/** The default value returned by the getLinesVisible() method. */
	private boolean baseLinesVisible;

	/** The shape that is used to represent a line in the legend. */
	private transient Shape legendLine;

	/**
	 * A flag that controls whether or not shapes are visible for ALL series.
	 * 
	 * @deprecated As of 1.0.7.
	 */
	private Boolean shapesVisible;

	/**
	 * A table of flags that control (per series) whether or not shapes are
	 * visible.
	 */
	private BooleanList seriesShapesVisible;

	/** The default value returned by the getShapeVisible() method. */
	private boolean baseShapesVisible;

	/**
	 * A flag that controls whether or not shapes are filled for ALL series.
	 * 
	 * @deprecated As of 1.0.7.
	 */
	private Boolean shapesFilled;

	/**
	 * A table of flags that control (per series) whether or not shapes are
	 * filled.
	 */
	private BooleanList seriesShapesFilled;

	/** The default value returned by the getShapeFilled() method. */
	private boolean baseShapesFilled;

	/** A flag that controls whether outlines are drawn for shapes. */
	private boolean drawOutlines;

	/**
	 * A flag that controls whether the fill paint is used for filling shapes.
	 */
	private boolean useFillPaint;

	/**
	 * A flag that controls whether the outline paint is used for drawing shape
	 * outlines.
	 */
	private boolean useOutlinePaint;

	/**
	 * A flag that controls whether or not each series is drawn as a single
	 * path.
	 */
	private boolean drawSeriesLineAsPath;

	/**
	 * Creates a new renderer with both lines and shapes visible.
	 */
	public XYLineAndShapeRenderer() {
		this(true, true);
	}

	/**
	 * Creates a new renderer.
	 * 
	 * @param lines
	 *            lines visible?
	 * @param shapes
	 *            shapes visible?
	 */
	public XYLineAndShapeRenderer(boolean lines, boolean shapes) {
		this.linesVisible = null;
		this.seriesLinesVisible = new BooleanList();
		this.baseLinesVisible = lines;
		this.legendLine = new Line2D.Double(-7.0, 0.0, 7.0, 0.0);

		this.shapesVisible = null;
		this.seriesShapesVisible = new BooleanList();
		this.baseShapesVisible = shapes;

		this.shapesFilled = null;
		this.useFillPaint = false; // use item paint for fills by default
		this.seriesShapesFilled = new BooleanList();
		this.baseShapesFilled = true;

		this.drawOutlines = true;
		this.useOutlinePaint = false; // use item paint for outlines by
		// default, not outline paint

		this.drawSeriesLineAsPath = false;
	}

	/**
	 * Returns a flag that controls whether or not each series is drawn as a
	 * single path.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setDrawSeriesLineAsPath(boolean)
	 */
	public boolean getDrawSeriesLineAsPath() {
		return this.drawSeriesLineAsPath;
	}

	/**
	 * Sets the flag that controls whether or not each series is drawn as a
	 * single path and sends a {@link RendererChangeEvent} to all registered
	 * listeners.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getDrawSeriesLineAsPath()
	 */
	public void setDrawSeriesLineAsPath(boolean flag) {
		if (this.drawSeriesLineAsPath != flag) {
			this.drawSeriesLineAsPath = flag;
			// fireChangeEvent();
		}
	}

	/**
	 * Returns the number of passes through the data that the renderer requires
	 * in order to draw the chart. Most charts will require a single pass, but
	 * some require two passes.
	 * 
	 * @return The pass count.
	 */
	public int getPassCount() {
		return 2;
	}

	// LINES VISIBLE

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * visible.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * 
	 * @return A boolean.
	 */
	public boolean getItemLineVisible(int series, int item) {
		Boolean flag = this.linesVisible;
		if (flag == null) {
			flag = getSeriesLinesVisible(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseLinesVisible;
		}
	}

	/**
	 * Returns a flag that controls whether or not lines are drawn for ALL
	 * series. If this flag is <code>null</code>, then the "per series" settings
	 * will apply.
	 * 
	 * @return A flag (possibly <code>null</code>).
	 * 
	 * @see #setLinesVisible(Boolean)
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public Boolean getLinesVisible() {
		return this.linesVisible;
	}

	/**
	 * Sets a flag that controls whether or not lines are drawn between the
	 * items in ALL series, and sends a {@link RendererChangeEvent} to all
	 * registered listeners. You need to set this to <code>null</code> if you
	 * want the "per series" settings to apply.
	 * 
	 * @param visible
	 *            the flag (<code>null</code> permitted).
	 * 
	 * @see #getLinesVisible()
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setLinesVisible(Boolean visible) {
		this.linesVisible = visible;
		// fireChangeEvent();
	}

	/**
	 * Sets a flag that controls whether or not lines are drawn between the
	 * items in ALL series, and sends a {@link RendererChangeEvent} to all
	 * registered listeners.
	 * 
	 * @param visible
	 *            the flag.
	 * 
	 * @see #getLinesVisible()
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setLinesVisible(boolean visible) {
		// we use BooleanUtilities here to preserve JRE 1.3.1 compatibility
		setLinesVisible(BooleanUtilities.valueOf(visible));
	}

	/**
	 * Returns the flag used to control whether or not the lines for a series
	 * are visible.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * 
	 * @return The flag (possibly <code>null</code>).
	 * 
	 * @see #setSeriesLinesVisible(int, Boolean)
	 */
	public Boolean getSeriesLinesVisible(int series) {
		return this.seriesLinesVisible.getBoolean(series);
	}

	/**
	 * Sets the 'lines visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag (<code>null</code> permitted).
	 * 
	 * @see #getSeriesLinesVisible(int)
	 */
	public void setSeriesLinesVisible(int series, Boolean flag) {
		this.seriesLinesVisible.setBoolean(series, flag);
		// fireChangeEvent();
	}

	/**
	 * Sets the 'lines visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param visible
	 *            the flag.
	 * 
	 * @see #getSeriesLinesVisible(int)
	 */
	public void setSeriesLinesVisible(int series, boolean visible) {
		setSeriesLinesVisible(series, BooleanUtilities.valueOf(visible));
	}

	/**
	 * Returns the base 'lines visible' attribute.
	 * 
	 * @return The base flag.
	 * 
	 * @see #setBaseLinesVisible(boolean)
	 */
	public boolean getBaseLinesVisible() {
		return this.baseLinesVisible;
	}

	/**
	 * Sets the base 'lines visible' flag and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getBaseLinesVisible()
	 */
	public void setBaseLinesVisible(boolean flag) {
		this.baseLinesVisible = flag;
		// fireChangeEvent();
	}

	/**
	 * Returns the shape used to represent a line in the legend.
	 * 
	 * @return The legend line (never <code>null</code>).
	 * 
	 * @see #setLegendLine(Shape)
	 */
	public Shape getLegendLine() {
		return this.legendLine;
	}

	/**
	 * Sets the shape used as a line in each legend item and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param line
	 *            the line (<code>null</code> not permitted).
	 * 
	 * @see #getLegendLine()
	 */
	public void setLegendLine(Shape line) {
		if (line == null) {
			throw new IllegalArgumentException("Null 'line' argument.");
		}
		this.legendLine = line;
		// fireChangeEvent();
	}

	// SHAPES VISIBLE

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * visible.
	 * <p>
	 * The default implementation passes control to the
	 * <code>getSeriesShapesVisible</code> method. You can override this method
	 * if you require different behaviour.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * 
	 * @return A boolean.
	 */
	public boolean getItemShapeVisible(int series, int item) {
		Boolean flag = this.shapesVisible;
		if (flag == null) {
			flag = getSeriesShapesVisible(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseShapesVisible;
		}
	}

	/**
	 * Returns the flag that controls whether the shapes are visible for the
	 * items in ALL series.
	 * 
	 * @return The flag (possibly <code>null</code>).
	 * 
	 * @see #setShapesVisible(Boolean)
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public Boolean getShapesVisible() {
		return this.shapesVisible;
	}

	/**
	 * Sets the 'shapes visible' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param visible
	 *            the flag (<code>null</code> permitted).
	 * 
	 * @see #getShapesVisible()
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setShapesVisible(Boolean visible) {
		this.shapesVisible = visible;
		// fireChangeEvent();
	}

	/**
	 * Sets the 'shapes visible' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param visible
	 *            the flag.
	 * 
	 * @see #getShapesVisible()
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setShapesVisible(boolean visible) {
		setShapesVisible(BooleanUtilities.valueOf(visible));
	}

	/**
	 * Returns the flag used to control whether or not the shapes for a series
	 * are visible.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * 
	 * @return A boolean.
	 * 
	 * @see #setSeriesShapesVisible(int, Boolean)
	 */
	public Boolean getSeriesShapesVisible(int series) {
		return this.seriesShapesVisible.getBoolean(series);
	}

	/**
	 * Sets the 'shapes visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param visible
	 *            the flag.
	 * 
	 * @see #getSeriesShapesVisible(int)
	 */
	public void setSeriesShapesVisible(int series, boolean visible) {
		setSeriesShapesVisible(series, BooleanUtilities.valueOf(visible));
	}

	/**
	 * Sets the 'shapes visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getSeriesShapesVisible(int)
	 */
	public void setSeriesShapesVisible(int series, Boolean flag) {
		this.seriesShapesVisible.setBoolean(series, flag);
		// fireChangeEvent();
	}

	/**
	 * Returns the base 'shape visible' attribute.
	 * 
	 * @return The base flag.
	 * 
	 * @see #setBaseShapesVisible(boolean)
	 */
	public boolean getBaseShapesVisible() {
		return this.baseShapesVisible;
	}

	/**
	 * Sets the base 'shapes visible' flag and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getBaseShapesVisible()
	 */
	public void setBaseShapesVisible(boolean flag) {
		this.baseShapesVisible = flag;
		// fireChangeEvent();
	}

	// SHAPES FILLED

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * filled.
	 * <p>
	 * The default implementation passes control to the
	 * <code>getSeriesShapesFilled</code> method. You can override this method
	 * if you require different behaviour.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * 
	 * @return A boolean.
	 */
	public boolean getItemShapeFilled(int series, int item) {
		Boolean flag = this.shapesFilled;
		if (flag == null) {
			flag = getSeriesShapesFilled(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseShapesFilled;
		}
	}

	/**
	 * Sets the 'shapes filled' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param filled
	 *            the flag.
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setShapesFilled(boolean filled) {
		setShapesFilled(BooleanUtilities.valueOf(filled));
	}

	/**
	 * Sets the 'shapes filled' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param filled
	 *            the flag (<code>null</code> permitted).
	 * 
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	public void setShapesFilled(Boolean filled) {
		this.shapesFilled = filled;
		// fireChangeEvent();
	}

	/**
	 * Returns the flag used to control whether or not the shapes for a series
	 * are filled.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * 
	 * @return A boolean.
	 * 
	 * @see #setSeriesShapesFilled(int, Boolean)
	 */
	public Boolean getSeriesShapesFilled(int series) {
		return this.seriesShapesFilled.getBoolean(series);
	}

	/**
	 * Sets the 'shapes filled' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getSeriesShapesFilled(int)
	 */
	public void setSeriesShapesFilled(int series, boolean flag) {
		setSeriesShapesFilled(series, BooleanUtilities.valueOf(flag));
	}

	/**
	 * Sets the 'shapes filled' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getSeriesShapesFilled(int)
	 */
	public void setSeriesShapesFilled(int series, Boolean flag) {
		this.seriesShapesFilled.setBoolean(series, flag);
		// fireChangeEvent();
	}

	/**
	 * Returns the base 'shape filled' attribute.
	 * 
	 * @return The base flag.
	 * 
	 * @see #setBaseShapesFilled(boolean)
	 */
	public boolean getBaseShapesFilled() {
		return this.baseShapesFilled;
	}

	/**
	 * Sets the base 'shapes filled' flag and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getBaseShapesFilled()
	 */
	public void setBaseShapesFilled(boolean flag) {
		this.baseShapesFilled = flag;
		// fireChangeEvent();
	}

	/**
	 * Returns <code>true</code> if outlines should be drawn for shapes, and
	 * <code>false</code> otherwise.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setDrawOutlines(boolean)
	 */
	public boolean getDrawOutlines() {
		return this.drawOutlines;
	}

	/**
	 * Sets the flag that controls whether outlines are drawn for shapes, and
	 * sends a {@link RendererChangeEvent} to all registered listeners.
	 * <P>
	 * In some cases, shapes look better if they do NOT have an outline, but
	 * this flag allows you to set your own preference.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getDrawOutlines()
	 */
	public void setDrawOutlines(boolean flag) {
		this.drawOutlines = flag;
		// fireChangeEvent();
	}

	/**
	 * Returns <code>true</code> if the renderer should use the fill paint
	 * setting to fill shapes, and <code>false</code> if it should just use the
	 * regular paint.
	 * <p>
	 * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the effect
	 * of this flag.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setUseFillPaint(boolean)
	 * @see #getUseOutlinePaint()
	 */
	public boolean getUseFillPaint() {
		return this.useFillPaint;
	}

	/**
	 * Sets the flag that controls whether the fill paint is used to fill
	 * shapes, and sends a {@link RendererChangeEvent} to all registered
	 * listeners.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getUseFillPaint()
	 */
	public void setUseFillPaint(boolean flag) {
		this.useFillPaint = flag;
		// fireChangeEvent();
	}

	/**
	 * Returns <code>true</code> if the renderer should use the outline paint
	 * setting to draw shape outlines, and <code>false</code> if it should just
	 * use the regular paint.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setUseOutlinePaint(boolean)
	 * @see #getUseFillPaint()
	 */
	public boolean getUseOutlinePaint() {
		return this.useOutlinePaint;
	}

	/**
	 * Sets the flag that controls whether the outline paint is used to draw
	 * shape outlines, and sends a {@link RendererChangeEvent} to all registered
	 * listeners.
	 * <p>
	 * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the effect
	 * of this flag.
	 * 
	 * @param flag
	 *            the flag.
	 * 
	 * @see #getUseOutlinePaint()
	 */
	public void setUseOutlinePaint(boolean flag) {
		this.useOutlinePaint = flag;
		// fireChangeEvent();
	}

	/**
	 * Records the state for the renderer. This is used to preserve state
	 * information between calls to the drawItem() method for a single chart
	 * drawing.
	 */
	public static class State extends XYItemRendererState {

		/** The path for the current series. */
		public GeneralPath seriesPath;

		/**
		 * A flag that indicates if the last (x, y) point was 'good' (non-null).
		 */
		private boolean lastPointGood;

		/**
		 * Creates a new state instance.
		 * 
		 * @param info
		 *            the plot rendering info.
		 */
		public State(PlotRenderingInfo info) {
			super(info);
		}

		/**
		 * Returns a flag that indicates if the last point drawn (in the current
		 * series) was 'good' (non-null).
		 * 
		 * @return A boolean.
		 */
		public boolean isLastPointGood() {
			return this.lastPointGood;
		}

		/**
		 * Sets a flag that indicates if the last point drawn (in the current
		 * series) was 'good' (non-null).
		 * 
		 * @param good
		 *            the flag.
		 */
		public void setLastPointGood(boolean good) {
			this.lastPointGood = good;
		}

		/**
		 * This method is called by the {@link XYPlot} at the start of each
		 * series pass. We reset the state for the current series.
		 * 
		 * @param dataset
		 *            the dataset.
		 * @param series
		 *            the series index.
		 * @param firstItem
		 *            the first item index for this pass.
		 * @param lastItem
		 *            the last item index for this pass.
		 * @param pass
		 *            the current pass index.
		 * @param passCount
		 *            the number of passes.
		 */
		public void startSeriesPass(XYDataset dataset, int series,
				int firstItem, int lastItem, int pass, int passCount) {
			this.seriesPath.reset();
			this.lastPointGood = false;
			super.startSeriesPass(dataset, series, firstItem, lastItem, pass,
					passCount);
		}

	}

	/**
	 * Initialises the renderer.
	 * <P>
	 * This method will be called before the first item is rendered, giving the
	 * renderer an opportunity to initialise any state information it wants to
	 * maintain. The renderer can do nothing if it chooses.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param dataArea
	 *            the area inside the axes.
	 * @param plot
	 *            the plot.
	 * @param data
	 *            the data.
	 * @param info
	 *            an optional info collection object to return data back to the
	 *            caller.
	 * 
	 * @return The renderer state.
	 */
	public XYItemRendererState initialise(Canvas g2, Rectangle2D dataArea,
			XYPlot plot, XYDataset data, PlotRenderingInfo info) {

		State state = new State(info);
		state.seriesPath = new GeneralPath();
		return state;

	}

	/**
	 * Draws the visual representation of a single data item.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param info
	 *            collects information about the drawing.
	 * @param plot
	 *            the plot (can be used to obtain standard color information
	 *            etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param crosshairState
	 *            crosshair information for the plot (<code>null</code>
	 *            permitted).
	 * @param pass
	 *            the pass index.
	 */
	public void drawItem(Canvas g2, XYItemRendererState state,
			Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
			ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
			int series, int item, CrosshairState crosshairState, int pass) {

		// do nothing if item is not visible
		if (!getItemVisible(series, item)) {
			return;
		}

		// first pass draws the background (lines, for instance)
		if (isLinePass(pass)) {
			if (getItemLineVisible(series, item)) {
				if (this.drawSeriesLineAsPath) {
					drawPrimaryLineAsPath(state, g2, plot, dataset, pass,
							series, item, domainAxis, rangeAxis, dataArea);
				} else {
					drawPrimaryLine(state, g2, plot, dataset, pass, series,
							item, domainAxis, rangeAxis, dataArea);
				}
			}
		}
		// second pass adds shapes where the items are ..
		else if (isItemPass(pass)) {

			// setup for collecting optional entity info...
			EntityCollection entities = null;
			if (info != null) {
				entities = info.getOwner().getEntityCollection();
			}

			drawSecondaryPass(g2, plot, dataset, pass, series, item,
					domainAxis, dataArea, rangeAxis, crosshairState, entities);
		}
	}

	/**
	 * Returns <code>true</code> if the specified pass is the one for drawing
	 * lines.
	 * 
	 * @param pass
	 *            the pass.
	 * 
	 * @return A boolean.
	 */
	protected boolean isLinePass(int pass) {
		return pass == 0;
	}

	/**
	 * Returns <code>true</code> if the specified pass is the one for drawing
	 * items.
	 * 
	 * @param pass
	 *            the pass.
	 * 
	 * @return A boolean.
	 */
	protected boolean isItemPass(int pass) {
		return pass == 1;
	}

	/**
	 * Draws the item (first pass). This method draws the lines connecting the
	 * items.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param plot
	 *            the plot (can be used to obtain standard color information
	 *            etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 */
	protected void drawPrimaryLine(XYItemRendererState state, Canvas g2,
			XYPlot plot, XYDataset dataset, int pass, int series, int item,
			ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {
		if (item == 0) {
			return;
		}

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		if (Double.isNaN(y1) || Double.isNaN(x1)) {
			return;
		}

		double x0 = dataset.getXValue(series, item - 1);
		double y0 = dataset.getYValue(series, item - 1);
		if (Double.isNaN(y0) || Double.isNaN(x0)) {
			return;
		}

		RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
		RectangleEdge yAxisLocation = plot.getRangeAxisEdge();

		double transX0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
		double transY0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);

		double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
		double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

		// only draw if we have good values
		if (Double.isNaN(transX0) || Double.isNaN(transY0)
				|| Double.isNaN(transX1) || Double.isNaN(transY1)) {
			return;
		}

		PlotOrientation orientation = plot.getOrientation();
		if (orientation == PlotOrientation.HORIZONTAL) {
			state.workingLine.setLine(transY0, transX0, transY1, transX1);
		} else if (orientation == PlotOrientation.VERTICAL) {
			state.workingLine.setLine(transX0, transY0, transX1, transY1);
		}

		if (state.workingLine.intersects(dataArea)) {
			drawFirstPassShape(g2, pass, series, item, state.workingLine);
		}
	}

	/**
	 * Draws the first pass shape.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index.
	 * @param item
	 *            the item index.
	 * @param shape
	 *            the shape.
	 */
	protected void drawFirstPassShape(Canvas g2, int pass, int series,
			int item, Shape shape) {

		Paint p = getItemPaint(series, item);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(getItemStroke(series, item));
		Path path = convertAwtPathToAndroid(shape.getPathIterator(null));	
		g2.drawPath(path, p);
	}

	/**
	 * Draws the item (first pass). This method draws the lines connecting the
	 * items. Instead of drawing separate lines, a GeneralPath is constructed
	 * and drawn at the end of the series painting.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param plot
	 *            the plot (can be used to obtain standard color information
	 *            etc).
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 */
	protected void drawPrimaryLineAsPath(XYItemRendererState state, Canvas g2,
			XYPlot plot, XYDataset dataset, int pass, int series, int item,
			ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {

		RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
		RectangleEdge yAxisLocation = plot.getRangeAxisEdge();

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
		double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

		State s = (State) state;
		// update path to reflect latest point
		if (!Double.isNaN(transX1) && !Double.isNaN(transY1)) {
			float x = (float) transX1;
			float y = (float) transY1;
			PlotOrientation orientation = plot.getOrientation();
			if (orientation == PlotOrientation.HORIZONTAL) {
				x = (float) transY1;
				y = (float) transX1;
			}
			if (s.isLastPointGood()) {
				s.seriesPath.lineTo(x, y);
			} else {
				s.seriesPath.moveTo(x, y);
			}
			s.setLastPointGood(true);
		} else {
			s.setLastPointGood(false);
		}
		// if this is the last item, draw the path ...
		if (item == s.getLastItemIndex()) {
			// draw path
			drawFirstPassShape(g2, pass, series, item, s.seriesPath);
		}
	}

	/**
	 * Draws the item shapes and adds chart entities (second pass). This method
	 * draws the shapes which mark the item positions. If <code>entities</code>
	 * is not <code>null</code> it will be populated with entity information for
	 * points that fall within the data area.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param plot
	 *            the plot (can be used to obtain standard color information
	 *            etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param crosshairState
	 *            the crosshair state.
	 * @param entities
	 *            the entity collection.
	 */
	protected void drawSecondaryPass(Canvas g2, XYPlot plot, XYDataset dataset,
			int pass, int series, int item, ValueAxis domainAxis,
			Rectangle2D dataArea, ValueAxis rangeAxis,
			CrosshairState crosshairState, EntityCollection entities) {

		Shape entityArea = null;

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		if (Double.isNaN(y1) || Double.isNaN(x1)) {
			return;
		}

		PlotOrientation orientation = plot.getOrientation();
		RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
		RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
		double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
		double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

		if (getItemShapeVisible(series, item)) {
			Shape shape = getItemShape(series, item);
			if (orientation == PlotOrientation.HORIZONTAL) {
				shape = ShapeUtilities.createTranslatedShape(shape, transY1,
						transX1);
			} else if (orientation == PlotOrientation.VERTICAL) {
				shape = ShapeUtilities.createTranslatedShape(shape, transX1,
						transY1);
			}
			entityArea = shape;
			if (shape.intersects(dataArea)) {
				if (getItemShapeFilled(series, item)) {
					Paint p;
					if (this.useFillPaint) {
						p = getItemFillPaint(series, item);
					} else {
						p = getItemPaint(series, item);
					}
					p.setStyle(Paint.Style.FILL_AND_STROKE);
					p.setStrokeWidth(getItemStroke(series, item));
					Path path = convertAwtPathToAndroid(shape
							.getPathIterator(null));
					g2.drawPath(path, p);
				}
				if (this.drawOutlines) {
					Paint p;
					if (getUseOutlinePaint()) {
						p = getItemOutlinePaint(series, item);
					} else {
						p = getItemPaint(series, item);
					}
					p.setStyle(Paint.Style.STROKE);
					p.setStrokeWidth(getItemStroke(series, item));
					Path path = convertAwtPathToAndroid(shape
							.getPathIterator(null));
					g2.drawPath(path, p);
				}
			}
		}

		double xx = transX1;
		double yy = transY1;
		if (orientation == PlotOrientation.HORIZONTAL) {
			xx = transY1;
			yy = transX1;
		}

		// draw the item label if there is one...
		if (isItemLabelVisible(series, item)) {
			drawItemLabel(g2, orientation, dataset, series, item, xx, yy,
					(y1 < 0.0));
		}

		int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
		int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
		updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex,
				rangeAxisIndex, transX1, transY1, orientation);

		// add an entity for the item, but only if it falls within the data
		// area...
		if (entities != null && isPointInRect(dataArea, xx, yy)) {
			addEntity(entities, entityArea, dataset, series, item, xx, yy);
		}
	}

	/**
	 * Returns a legend item for the specified series.
	 * 
	 * @param datasetIndex
	 *            the dataset index (zero-based).
	 * @param series
	 *            the series index (zero-based).
	 * 
	 * @return A legend item for the series.
	 */
	public LegendItem getLegendItem(int datasetIndex, int series) {

		XYPlot plot = getPlot();
		if (plot == null) {
			return null;
		}

		LegendItem result = null;
		XYDataset dataset = plot.getDataset(datasetIndex);
		if (dataset != null) {
			if (getItemVisible(series, 0)) {
				String label = getLegendItemLabelGenerator().generateLabel(
						dataset, series);
				String description = label;

				boolean shapeIsVisible = getItemShapeVisible(series, 0);
				Shape shape = lookupLegendShape(series);
				boolean shapeIsFilled = getItemShapeFilled(series, 0);
				Paint fillPaint = (this.useFillPaint ? lookupSeriesFillPaint(series)
						: lookupSeriesPaint(series));
				boolean shapeOutlineVisible = this.drawOutlines;
				Paint outlinePaint = (this.useOutlinePaint ? lookupSeriesOutlinePaint(series)
						: lookupSeriesPaint(series));
				Float outlineStroke = lookupSeriesOutlineStroke(series);
				boolean lineVisible = getItemLineVisible(series, 0);
				Float lineStroke = lookupSeriesStroke(series);
				Paint linePaint = lookupSeriesPaint(series);
				result = new LegendItem(label, description, "", "",
						shapeIsVisible, shape, shapeIsFilled, fillPaint
								.getColor(), shapeOutlineVisible, outlinePaint
								.getColor(), outlineStroke, lineVisible,
						this.legendLine, lineStroke, linePaint.getColor());
				result.setLabelFont(lookupLegendTextFont(series));
				Paint labelPaint = lookupLegendTextPaint(series);
				if (labelPaint != null) {
					result.setLabelPaint(labelPaint);
				}
				result.setSeriesKey(dataset.getSeriesKey(series));
				result.setSeriesIndex(series);
				result.setDataset(dataset);
				result.setDatasetIndex(datasetIndex);
			}
		}

		return result;

	}

	@Override
	public void addChangeListener(RendererChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Font getItemLabelFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Paint getItemLabelPaint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemLabelPosition getNegativeItemLabelPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemLabelPosition getPositiveItemLabelPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getSeriesVisible() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getSeriesVisibleInLegend() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeChangeListener(RendererChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBaseOutlineStroke(Float stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBaseStroke(Float stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelFont(Font font) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelGenerator(XYItemLabelGenerator generator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelPaint(Paint paint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelsVisible(boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelsVisible(Boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItemLabelsVisible(Boolean visible, boolean notify) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNegativeItemLabelPosition(ItemLabelPosition position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNegativeItemLabelPosition(ItemLabelPosition position,
			boolean notify) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutlinePaint(Paint paint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutlineStroke(Float stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPaint(Paint paint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPositiveItemLabelPosition(ItemLabelPosition position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPositiveItemLabelPosition(ItemLabelPosition position,
			boolean notify) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesOutlineStroke(int series, Float stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesStroke(int series, Float stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesVisible(Boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesVisible(Boolean visible, boolean notify) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesVisibleInLegend(Boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeriesVisibleInLegend(Boolean visible, boolean notify) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShape(Shape shape) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStroke(Float stroke) {
		// TODO Auto-generated method stub

	}

	private Path convertAwtPathToAndroid(PathIterator pi) {
		Path path = new Path();
		float[] coords = new float[6];
		while (!pi.isDone()) {
			int windingRule = pi.getWindingRule();

			if (windingRule == PathIterator.WIND_EVEN_ODD) {
				path.setFillType(Path.FillType.EVEN_ODD);
			} else {
				path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
			}

			int pathType = pi.currentSegment(coords);

			switch (pathType) {
			case PathIterator.SEG_CLOSE:
				path.close();
				break;
			case PathIterator.SEG_CUBICTO:
				path.cubicTo(coords[0], coords[1], coords[2], coords[3],
						coords[4], coords[5]);
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_MOVETO:
				path.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			}

			pi.next();
		}
		return path;
	}

}
