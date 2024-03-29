/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
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
 * ---------------
 * JFreeChart.java
 * ---------------
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Andrzej Porebski;
 *                   David Li;
 *                   Wolfgang Irler;
 *                   Christian W. Zuckschwerdt;
 *                   Klaus Rheinwald;
 *                   Nicolas Brodu;
 *                   Peter Kolb (patch 2603321);
 *
 * NOTE: The above list of contributors lists only the people that have
 * contributed to this source file (JFreeChart.java) - for a list of ALL
 * contributors to the project, please see the README.txt file.
 *
 * Changes (from 20-Jun-2001)
 * --------------------------
 * 20-Jun-2001 : Modifications submitted by Andrzej Porebski for legend
 *               placement;
 * 21-Jun-2001 : Removed JFreeChart parameter from Plot constructors (DG);
 * 22-Jun-2001 : Multiple titles added (original code by David Berry, with
 *               reworkings by DG);
 * 18-Sep-2001 : Updated header (DG);
 * 15-Oct-2001 : Moved data source classes into new package
 *               com.jrefinery.data.* (DG);
 * 18-Oct-2001 : New factory method for creating VerticalXYBarChart (DG);
 * 19-Oct-2001 : Moved series paint and stroke methods to the Plot class (DG);
 *               Moved static chart creation methods to new ChartFactory
 *               class (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 *               Fixed bug where chart isn't registered with the dataset (DG);
 * 07-Nov-2001 : Fixed bug where null title in constructor causes
 *               exception (DG);
 *               Tidied up event notification code (DG);
 * 17-Nov-2001 : Added getLegendItemCount() method (DG);
 * 21-Nov-2001 : Set clipping in draw method to ensure that nothing gets drawn
 *               outside the chart area (DG);
 * 11-Dec-2001 : Added the createBufferedImage() method, taken from the
 *               JFreeChartServletDemo class (DG);
 * 13-Dec-2001 : Added tooltips (DG);
 * 16-Jan-2002 : Added handleClick() method (DG);
 * 22-Jan-2002 : Fixed bug correlating legend labels with pie data (DG);
 * 05-Feb-2002 : Removed redundant tooltips code (DG);
 * 19-Feb-2002 : Added accessor methods for the backgroundImage and
 *               backgroundImageAlpha attributes (DG);
 * 21-Feb-2002 : Added static fields for INFO, COPYRIGHT, LICENCE, CONTRIBUTORS
 *               and LIBRARIES.  These can be used to display information about
 *               JFreeChart (DG);
 * 06-Mar-2002 : Moved constants to JFreeChartConstants interface (DG);
 * 18-Apr-2002 : PieDataset is no longer sorted (oldman);
 * 23-Apr-2002 : Moved dataset to the Plot class (DG);
 * 13-Jun-2002 : Added an extra draw() method (DG);
 * 25-Jun-2002 : Implemented the Drawable interface and removed redundant
 *               imports (DG);
 * 26-Jun-2002 : Added another createBufferedImage() method (DG);
 * 18-Sep-2002 : Fixed issues reported by Checkstyle (DG);
 * 23-Sep-2002 : Added new contributor (DG);
 * 28-Oct-2002 : Created main title and subtitle list to replace existing title
 *               list (DG);
 * 08-Jan-2003 : Added contributor (DG);
 * 17-Jan-2003 : Added new constructor (DG);
 * 22-Jan-2003 : Added ChartColor class by Cameron Riley, and background image
 *               alignment code by Christian W. Zuckschwerdt (DG);
 * 11-Feb-2003 : Added flag to allow suppression of chart change events, based
 *               on a suggestion by Klaus Rheinwald (DG);
 * 04-Mar-2003 : Added small fix for suppressed chart change events (see bug id
 *               690865) (DG);
 * 10-Mar-2003 : Added Benoit Xhenseval to contributors (DG);
 * 26-Mar-2003 : Implemented Serializable (DG);
 * 15-Jul-2003 : Added an optional border for the chart (DG);
 * 11-Sep-2003 : Took care of listeners while cloning (NB);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 22-Sep-2003 : Added nullpointer checks.
 * 25-Sep-2003 : Added nullpointer checks too (NB).
 * 03-Dec-2003 : Legends are now registered by this class instead of using the
 *               old constructor way (TM);
 * 03-Dec-2003 : Added anchorPoint to draw() method (DG);
 * 08-Jan-2004 : Reworked title code, introducing line wrapping (DG);
 * 09-Feb-2004 : Created additional createBufferedImage() method (DG);
 * 05-Apr-2004 : Added new createBufferedImage() method (DG);
 * 27-May-2004 : Moved constants from JFreeChartConstants.java back to this
 *               class (DG);
 * 25-Nov-2004 : Updates for changes to Title class (DG);
 * 06-Jan-2005 : Change lookup for default background color (DG);
 * 31-Jan-2005 : Added Don Elliott to contributors (DG);
 * 02-Feb-2005 : Added clearSubtitles() method (DG);
 * 03-Feb-2005 : Added Mofeed Shahin to contributors (DG);
 * 08-Feb-2005 : Updated for RectangleConstraint changes (DG);
 * 28-Mar-2005 : Renamed Legend --> OldLegend (DG);
 * 12-Apr-2005 : Added methods to access legend(s) in subtitle list (DG);
 * 13-Apr-2005 : Added removeLegend() and removeSubtitle() methods (DG);
 * 20-Apr-2005 : Modified to collect chart entities from titles and
 *               subtitles (DG);
 * 26-Apr-2005 : Removed LOGGER (DG);
 * 06-Jun-2005 : Added addLegend() method and padding attribute, fixed equals()
 *               method (DG);
 * 24-Nov-2005 : Removed OldLegend and related code - don't want to support
 *               this in 1.0.0 final (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 27-Jan-2006 : Updated version number (DG);
 * 07-Dec-2006 : Added some missing credits (DG);
 * 17-Jan-2007 : Added Darren Jung to contributor list (DG);
 * 05-Mar-2007 : Added Sergei Ivanov to the contributor list (DG);
 * 16-Mar-2007 : Modified initial legend border (DG);
 * 22-Mar-2007 : New methods for text anti-aliasing (DG);
 * 16-May-2007 : Fixed argument check in getSubtitle(), copy list in
 *               get/setSubtitles(), and added new addSubtitle(int, Title)
 *               method (DG);
 * 05-Jun-2007 : Add change listener to default legend (DG);
 * 04-Dec-2007 : In createBufferedImage() methods, make the default image type
 *               BufferedImage.TYPE_INT_ARGB (thanks to Klaus Rheinwald) (DG);
 * 05-Dec-2007 : Fixed bug 1749124 (not registering as listener with
 *               TextTitle) (DG);
 * 23-Apr-2008 : Added new contributor (Diego Pierangeli) (DG);
 * 16-May-2008 : Added new contributor (Michael Siemer) (DG);
 * 19-Sep-2008 : Check for title visibility (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 * 19-Mar-2009 : Added entity support - see patch 2603321 by Peter Kolb (DG);
 *
 */

package net.droidsolutions.droidcharts.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.droidsolutions.droidcharts.awt.Font;
import net.droidsolutions.droidcharts.awt.Point2D;
import net.droidsolutions.droidcharts.awt.Rectangle;
import net.droidsolutions.droidcharts.awt.Rectangle2D;
import net.droidsolutions.droidcharts.common.Align;
import net.droidsolutions.droidcharts.common.Drawable;
import net.droidsolutions.droidcharts.common.HorizontalAlignment;
import net.droidsolutions.droidcharts.common.RectangleEdge;
import net.droidsolutions.droidcharts.common.RectangleInsets;
import net.droidsolutions.droidcharts.common.Size2D;
import net.droidsolutions.droidcharts.common.VerticalAlignment;
import net.droidsolutions.droidcharts.core.block.BlockParams;
import net.droidsolutions.droidcharts.core.block.EntityBlockResult;
import net.droidsolutions.droidcharts.core.block.LengthConstraintType;
import net.droidsolutions.droidcharts.core.block.LineBorder;
import net.droidsolutions.droidcharts.core.block.RectangleConstraint;
import net.droidsolutions.droidcharts.core.data.Range;
import net.droidsolutions.droidcharts.core.entity.EntityCollection;
import net.droidsolutions.droidcharts.core.event.ChartChangeEvent;
import net.droidsolutions.droidcharts.core.event.PlotChangeEvent;
import net.droidsolutions.droidcharts.core.event.PlotChangeListener;
import net.droidsolutions.droidcharts.core.event.TitleChangeEvent;
import net.droidsolutions.droidcharts.core.event.TitleChangeListener;
import net.droidsolutions.droidcharts.core.plot.CategoryPlot;
import net.droidsolutions.droidcharts.core.plot.Plot;
import net.droidsolutions.droidcharts.core.plot.PlotRenderingInfo;
import net.droidsolutions.droidcharts.core.plot.XYPlot;
import net.droidsolutions.droidcharts.core.title.LegendTitle;
import net.droidsolutions.droidcharts.core.title.TextTitle;
import net.droidsolutions.droidcharts.core.title.Title;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * A chart class implemented using the Java 2D APIs. The current version
 * supports bar charts, line charts, pie charts and xy plots (including time
 * series data).
 * <P>
 * JFreeChart coordinates several objects to achieve its aim of being able to
 * draw a chart on a Java 2D graphics device: a list of {@link Title} objects
 * (which often includes the chart's legend), a {@link Plot} and a
 * {@link org.jfree.data.general.Dataset} (the plot in turn manages a domain
 * axis and a range axis).
 * <P>
 * You should use a {@link ChartPanel} to display a chart in a GUI.
 * <P>
 * The {@link ChartFactory} class contains static methods for creating
 * 'ready-made' charts.
 * 
 * @see ChartPanel
 * @see ChartFactory
 * @see Title
 * @see Plot
 */
public class JFreeChart implements Drawable, TitleChangeListener,
		PlotChangeListener, Serializable, Cloneable {

	/** For serialization. */
	private static final long serialVersionUID = -3470703747817429120L;

	/** The default font for titles. */
	public static final Font DEFAULT_TITLE_FONT = new Font("SansSerif",
			Typeface.BOLD, 18);

	/** The default background color. */
	public static final Paint DEFAULT_BACKGROUND_PAINT = new Paint(
			Paint.ANTI_ALIAS_FLAG);
	static {
		DEFAULT_BACKGROUND_PAINT.setColor(Color.LTGRAY);
		DEFAULT_BACKGROUND_PAINT.setStyle(Paint.Style.FILL);
	}

	/** The default background image alignment. */
	public static final int DEFAULT_BACKGROUND_IMAGE_ALIGNMENT = Align.FIT;

	/** The default background image alpha. */
	public static final float DEFAULT_BACKGROUND_IMAGE_ALPHA = 0.5f;

	/** A flag that controls whether or not the chart border is drawn. */
	private boolean borderVisible;

	/** The stroke used to draw the chart border (if visible). */
	private transient float borderStroke;

	/** The paint used to draw the chart border (if visible). */
	private transient Paint borderPaint;

	/** The padding between the chart border and the chart drawing area. */
	private RectangleInsets padding;

	/** The chart title (optional). */
	private TextTitle title;

	/**
	 * The chart subtitles (zero, one or many). This field should never be
	 * <code>null</code>.
	 */
	private List subtitles;

	/** Draws the visual representation of the data. */
	private Plot plot;

	/** Paint used to draw the background of the chart. */
	private transient Paint backgroundPaint;

	/**
	 * A flag that can be used to enable/disable notification of chart change
	 * events.
	 */
	private boolean notify;

	/**
	 * Creates a new chart with the given title and plot. The
	 * <code>createLegend</code> argument specifies whether or not a legend
	 * should be added to the chart. <br>
	 * <br>
	 * Note that the {@link ChartFactory} class contains a range of static
	 * methods that will return ready-made charts, and often this is a more
	 * convenient way to create charts than using this constructor.
	 * 
	 * @param title
	 *            the chart title (<code>null</code> permitted).
	 * @param titleFont
	 *            the font for displaying the chart title (<code>null</code>
	 *            permitted).
	 * @param plot
	 *            controller of the visual representation of the data (
	 *            <code>null</code> not permitted).
	 * @param createLegend
	 *            a flag indicating whether or not a legend should be created
	 *            for the chart.
	 */
	public JFreeChart(String title, Font titleFont, Plot plot,
			boolean createLegend) {

		if (plot == null) {
			throw new NullPointerException("Null 'plot' argument.");
		}

		this.borderVisible = true;
		this.borderStroke = 2;
		this.borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.borderPaint.setColor(Color.WHITE);
		this.borderPaint.setStyle(Paint.Style.STROKE);
		this.borderPaint.setStrokeWidth(borderStroke);

		this.plot = plot;

		this.subtitles = new ArrayList();

		// create a legend, if requested...
		if (createLegend) {
			LegendTitle legend = new LegendTitle(this.plot);
			legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
			legend.setFrame(new LineBorder());
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.WHITE);
			legend.setBackgroundPaint(paint);
			legend.setPosition(RectangleEdge.BOTTOM);
			this.subtitles.add(legend);
		}

		// add the chart title, if one has been specified...
		if (title != null) {
			if (titleFont == null) {
				titleFont = DEFAULT_TITLE_FONT;
			}
			this.title = new TextTitle(title, titleFont);
		}

		this.backgroundPaint = DEFAULT_BACKGROUND_PAINT;

	}

	/**
	 * Returns a flag that controls whether or not a border is drawn around the
	 * outside of the chart.
	 * 
	 * @return A boolean.
	 * 
	 * @see #setBorderVisible(boolean)
	 */
	public boolean isBorderVisible() {
		return this.borderVisible;
	}

	/**
	 * Sets a flag that controls whether or not a border is drawn around the
	 * outside of the chart.
	 * 
	 * @param visible
	 *            the flag.
	 * 
	 * @see #isBorderVisible()
	 */
	public void setBorderVisible(boolean visible) {
		this.borderVisible = visible;
	}

	/**
	 * Returns the stroke used to draw the chart border (if visible).
	 * 
	 * @return The border stroke.
	 * 
	 * @see #setBorderStroke(Stroke)
	 */
	public float getBorderStroke() {
		return this.borderStroke;
	}

	/**
	 * Sets the stroke used to draw the chart border (if visible).
	 * 
	 * @param stroke
	 *            the stroke.
	 * 
	 * @see #getBorderStroke()
	 */
	public void setBorderStroke(float stroke) {
		this.borderStroke = stroke;
	}

	/**
	 * Returns the paint used to draw the chart border (if visible).
	 * 
	 * @return The border paint.
	 * 
	 * @see #setBorderPaint(Paint)
	 */
	public Paint getBorderPaint() {
		return this.borderPaint;
	}

	/**
	 * Sets the paint used to draw the chart border (if visible).
	 * 
	 * @param paint
	 *            the paint.
	 * 
	 * @see #getBorderPaint()
	 */
	public void setBorderPaint(Paint paint) {
		this.borderPaint = paint;
	}

	/**
	 * Returns the padding between the chart border and the chart drawing area.
	 * 
	 * @return The padding (never <code>null</code>).
	 * 
	 * @see #setPadding(RectangleInsets)
	 */
	public RectangleInsets getPadding() {
		return this.padding;
	}

	/**
	 * Sets the padding between the chart border and the chart drawing area, and
	 * sends a {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @param padding
	 *            the padding (<code>null</code> not permitted).
	 * 
	 * @see #getPadding()
	 */
	public void setPadding(RectangleInsets padding) {
		if (padding == null) {
			throw new IllegalArgumentException("Null 'padding' argument.");
		}
		this.padding = padding;
	}

	/**
	 * Returns the main chart title. Very often a chart will have just one
	 * title, so we make this case simple by providing accessor methods for the
	 * main title. However, multiple titles are supported - see the
	 * {@link #addSubtitle(Title)} method.
	 * 
	 * @return The chart title (possibly <code>null</code>).
	 * 
	 * @see #setTitle(TextTitle)
	 */
	public TextTitle getTitle() {
		return this.title;
	}

	/**
	 * Sets the main title for the chart and sends a {@link ChartChangeEvent} to
	 * all registered listeners. If you do not want a title for the chart, set
	 * it to <code>null</code>. If you want more than one title on a chart, use
	 * the {@link #addSubtitle(Title)} method.
	 * 
	 * @param title
	 *            the title (<code>null</code> permitted).
	 * 
	 * @see #getTitle()
	 */
	public void setTitle(TextTitle title) {

		this.title = title;

	}

	/**
	 * Sets the chart title and sends a {@link ChartChangeEvent} to all
	 * registered listeners. This is a convenience method that ends up calling
	 * the {@link #setTitle(TextTitle)} method. If there is an existing title,
	 * its text is updated, otherwise a new title using the default font is
	 * added to the chart. If <code>text</code> is <code>null</code> the chart
	 * title is set to <code>null</code>.
	 * 
	 * @param text
	 *            the title text (<code>null</code> permitted).
	 * 
	 * @see #getTitle()
	 */
	public void setTitle(String text) {
		if (text != null) {
			if (this.title == null) {
				setTitle(new TextTitle(text, JFreeChart.DEFAULT_TITLE_FONT));
			} else {
				this.title.setText(text);
			}
		} else {
			setTitle((TextTitle) null);
		}
	}

	/**
	 * Adds a legend to the plot and sends a {@link ChartChangeEvent} to all
	 * registered listeners.
	 * 
	 * @param legend
	 *            the legend (<code>null</code> not permitted).
	 * 
	 * @see #removeLegend()
	 */
	public void addLegend(LegendTitle legend) {
		addSubtitle(legend);
	}

	public void addSubtitle(Title subtitle) {
		if (subtitle == null) {
			throw new IllegalArgumentException("Null 'subtitle' argument.");
		}
		this.subtitles.add(subtitle);
	}

	/**
	 * Returns the legend for the chart, if there is one. Note that a chart can
	 * have more than one legend - this method returns the first.
	 * 
	 * @return The legend (possibly <code>null</code>).
	 * 
	 * @see #getLegend(int)
	 */
	public LegendTitle getLegend() {
		return getLegend(0);
	}

	/**
	 * Returns the nth legend for a chart, or <code>null</code>.
	 * 
	 * @param index
	 *            the legend index (zero-based).
	 * 
	 * @return The legend (possibly <code>null</code>).
	 * 
	 * @see #addLegend(LegendTitle)
	 */
	public LegendTitle getLegend(int index) {
		int seen = 0;
		Iterator iterator = this.subtitles.iterator();
		while (iterator.hasNext()) {
			Title subtitle = (Title) iterator.next();
			if (subtitle instanceof LegendTitle) {
				if (seen == index) {
					return (LegendTitle) subtitle;
				} else {
					seen++;
				}
			}
		}
		return null;
	}

	/**
	 * Removes the first legend in the chart and sends a
	 * {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @see #getLegend()
	 */
	public void removeLegend() {
		removeSubtitle(getLegend());
	}

	public void removeSubtitle(Title title) {
		this.subtitles.remove(title);
	}

	/**
	 * Returns the list of subtitles for the chart.
	 * 
	 * @return The subtitle list (possibly empty, but never <code>null</code>).
	 * 
	 * @see #setSubtitles(List)
	 */
	public List getSubtitles() {
		return new ArrayList(this.subtitles);
	}

	/**
	 * Sets the title list for the chart (completely replaces any existing
	 * titles) and sends a {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @param subtitles
	 *            the new list of subtitles (<code>null</code> not permitted).
	 * 
	 * @see #getSubtitles()
	 */
	public void setSubtitles(List subtitles) {
		if (subtitles == null) {
			throw new NullPointerException("Null 'subtitles' argument.");
		}
		// setNotify(false);
		clearSubtitles();
		Iterator iterator = subtitles.iterator();
		while (iterator.hasNext()) {
			Title t = (Title) iterator.next();
			if (t != null) {
				addSubtitle(t);
			}
		}
	}

	public void clearSubtitles() {
		Iterator iterator = this.subtitles.iterator();
		while (iterator.hasNext()) {
			Title t = (Title) iterator.next();

		}
		this.subtitles.clear();
	}

	/**
	 * Returns the number of titles for the chart.
	 * 
	 * @return The number of titles for the chart.
	 * 
	 * @see #getSubtitles()
	 */
	public int getSubtitleCount() {
		return this.subtitles.size();
	}

	/**
	 * Returns a chart subtitle.
	 * 
	 * @param index
	 *            the index of the chart subtitle (zero based).
	 * 
	 * @return A chart subtitle.
	 * 
	 * @see #addSubtitle(Title)
	 */
	public Title getSubtitle(int index) {
		if ((index < 0) || (index >= getSubtitleCount())) {
			throw new IllegalArgumentException("Index out of range.");
		}
		return (Title) this.subtitles.get(index);
	}

	/**
	 * Returns the plot for the chart. The plot is a class responsible for
	 * coordinating the visual representation of the data, including the axes
	 * (if any).
	 * 
	 * @return The plot.
	 */
	public Plot getPlot() {
		return this.plot;
	}

	/**
	 * Returns the plot cast as a {@link CategoryPlot}.
	 * <p>
	 * NOTE: if the plot is not an instance of {@link CategoryPlot}, then a
	 * <code>ClassCastException</code> is thrown.
	 * 
	 * @return The plot.
	 * 
	 * @see #getPlot()
	 */
	public CategoryPlot getCategoryPlot() {
		return (CategoryPlot) this.plot;
	}

	/**
	 * Returns the plot cast as an {@link XYPlot}.
	 * <p>
	 * NOTE: if the plot is not an instance of {@link XYPlot}, then a
	 * <code>ClassCastException</code> is thrown.
	 * 
	 * @return The plot.
	 * 
	 * @see #getPlot()
	 */
	public XYPlot getXYPlot() {
		return (XYPlot) this.plot;
	}

	/**
	 * Returns a flag that indicates whether or not anti-aliasing is used when
	 * the chart is drawn.
	 * 
	 * @return The flag.
	 * 
	 * @see #setAntiAlias(boolean)
	 */
	// public boolean getAntiAlias() {
	// Object val = this.renderingHints.get(RenderingHints.KEY_ANTIALIASING);
	// return RenderingHints.VALUE_ANTIALIAS_ON.equals(val);
	// }
	/**
	 * Sets a flag that indicates whether or not anti-aliasing is used when the
	 * chart is drawn.
	 * <P>
	 * Anti-aliasing usually improves the appearance of charts, but is slower.
	 * 
	 * @param flag
	 *            the new value of the flag.
	 * 
	 * @see #getAntiAlias()
	 */
	// public void setAntiAlias(boolean flag) {
	// Object val = this.renderingHints.get(RenderingHints.KEY_ANTIALIASING);
	// if (val == null) {
	// val = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
	// }
	// if (!flag && RenderingHints.VALUE_ANTIALIAS_OFF.equals(val)
	/*
	 * || flag && RenderingHints.VALUE_ANTIALIAS_ON.equals(val)) { // no change,
	 * do nothing return; } if (flag) {
	 * this.renderingHints.put(RenderingHints.KEY_ANTIALIASING,
	 * RenderingHints.VALUE_ANTIALIAS_ON); } else {
	 * this.renderingHints.put(RenderingHints.KEY_ANTIALIASING,
	 * RenderingHints.VALUE_ANTIALIAS_OFF); } fireChartChanged();
	 * 
	 * }
	 */

	/**
	 * Returns the current value stored in the rendering hints table for
	 * {@link RenderingHints#KEY_TEXT_ANTIALIASING}.
	 * 
	 * @return The hint value (possibly <code>null</code>).
	 * 
	 * @since 1.0.5
	 * 
	 * @see #setTextAntiAlias(Object)
	 */
	/*
	 * public Object getTextAntiAlias() { return
	 * this.renderingHints.get(RenderingHints.KEY_TEXT_ANTIALIASING); }
	 *//**
	 * Sets the value in the rendering hints table for
	 * {@link RenderingHints#KEY_TEXT_ANTIALIASING} to either
	 * {@link RenderingHints#VALUE_TEXT_ANTIALIAS_ON} or
	 * {@link RenderingHints#VALUE_TEXT_ANTIALIAS_OFF}, then sends a
	 * {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @param flag
	 *            the new value of the flag.
	 * 
	 * @since 1.0.5
	 * 
	 * @see #getTextAntiAlias()
	 * @see #setTextAntiAlias(Object)
	 */
	/*
	 * public void setTextAntiAlias(boolean flag) { if (flag) {
	 * setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_ON); } else {
	 * setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF); } }
	 */

	/**
	 * Sets the value in the rendering hints table for
	 * {@link RenderingHints#KEY_TEXT_ANTIALIASING} and sends a
	 * {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @param val
	 *            the new value (<code>null</code> permitted).
	 * 
	 * @since 1.0.5
	 * 
	 * @see #getTextAntiAlias()
	 * @see #setTextAntiAlias(boolean)
	 */
	// public void setTextAntiAlias(Object val) {
	// this.renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, val);
	// notifyListeners(new ChartChangeEvent(this));
	// }
	/**
	 * Returns the paint used for the chart background.
	 * 
	 * @return The paint (possibly <code>null</code>).
	 * 
	 * @see #setBackgroundPaint(Paint)
	 */
	public Paint getBackgroundPaint() {
		return this.backgroundPaint;
	}

	/**
	 * Sets the paint used to fill the chart background and sends a
	 * {@link ChartChangeEvent} to all registered listeners.
	 * 
	 * @param paint
	 *            the paint (<code>null</code> permitted).
	 * 
	 * @see #getBackgroundPaint()
	 */
	public void setBackgroundPaint(Paint paint) {

		if (this.backgroundPaint != null) {
			if (!this.backgroundPaint.equals(paint)) {
				this.backgroundPaint = paint;
			}
		} else {
			if (paint != null) {
				this.backgroundPaint = paint;
			}
		}

	}

	/**
	 * Draws the chart on a Java 2D graphics device (such as the screen or a
	 * printer).
	 * <P>
	 * This method is the focus of the entire JFreeChart library.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param area
	 *            the area within which the chart should be drawn.
	 */
	public void draw(Canvas g2, Rectangle2D area) {
		draw(g2, area, null, null);
	}

	/**
	 * Draws the chart on a Java 2D graphics device (such as the screen or a
	 * printer). This method is the focus of the entire JFreeChart library.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param area
	 *            the area within which the chart should be drawn.
	 * @param info
	 *            records info about the drawing (null means collect no info).
	 */
	public void draw(Canvas g2, Rectangle2D area, ChartRenderingInfo info) {
		draw(g2, area, null, info);
	}

	/**
	 * Draws the chart on a Java 2D graphics device (such as the screen or a
	 * printer).
	 * <P>
	 * This method is the focus of the entire JFreeChart library.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param chartArea
	 *            the area within which the chart should be drawn.
	 * @param anchor
	 *            the anchor point (in Java2D space) for the chart (
	 *            <code>null</code> permitted).
	 * @param info
	 *            records info about the drawing (null means collect no info).
	 */
	public void draw(Canvas g2, Rectangle2D chartArea, Point2D anchor,
			ChartRenderingInfo info) {
		EntityCollection entities = null;
		Rect savedClip = g2.getClipBounds();
		// ensure no drawing occurs outside chart area...
		g2.clipRect((float) chartArea.getMinX(), (float) chartArea.getMinY(),
				(float) chartArea.getMaxX(), (float) chartArea.getMaxY());

		// draw the chart background...
		if (this.backgroundPaint != null) {
			g2.drawRect((float) chartArea.getMinX(), (float) chartArea
					.getMinY(), (float) chartArea.getMaxX(), (float) chartArea
					.getMaxY(), backgroundPaint);
		}

		if (isBorderVisible()) {
			Paint paint = getBorderPaint();
			float stroke = getBorderStroke();
			if (paint != null) {
				Rectangle2D borderArea = new Rectangle2D.Double(chartArea
						.getX(), chartArea.getY(), chartArea.getWidth() - 1.0,
						chartArea.getHeight() - 1.0);
				g2.drawRect((float) borderArea.getMinX(), (float) borderArea
						.getMinY(), (float) borderArea.getMaxX(),
						(float) borderArea.getMaxY(), borderPaint);
			}
		}

		// Rectangle2D plotArea = nonTitleArea;
		Rectangle2D plotArea = chartArea;

		// draw the plot (axes and data visualisation)
		PlotRenderingInfo plotInfo = null;
		if (info != null) {
			plotInfo = info.getPlotInfo();
		}
		this.plot.draw(g2, plotArea, anchor, null, plotInfo);

		// draw the title and subtitles...
		Rectangle2D nonTitleArea = new Rectangle2D.Double();
		nonTitleArea.setRect(chartArea);
		if (this.padding != null)
			this.padding.trim(nonTitleArea);

		if (this.title != null) {
			EntityCollection e = drawTitle(this.title, g2, nonTitleArea,
					(entities != null));
			if (e != null) {
				entities.addAll(e);
			}
		}

		Iterator iterator = this.subtitles.iterator();
		while (iterator.hasNext()) {
			Title currentTitle = (Title) iterator.next();
			if (currentTitle.isVisible()) {
				EntityCollection e = drawTitle(currentTitle, g2, nonTitleArea,
						(entities != null));
				if (e != null) {
					entities.addAll(e);
				}
			}
		}

		g2.clipRect(savedClip);

	}

	/**
	 * Creates a rectangle that is aligned to the frame.
	 * 
	 * @param dimensions
	 *            the dimensions for the rectangle.
	 * @param frame
	 *            the frame to align to.
	 * @param hAlign
	 *            the horizontal alignment.
	 * @param vAlign
	 *            the vertical alignment.
	 * 
	 * @return A rectangle.
	 */
	private Rectangle2D createAlignedRectangle2D(Size2D dimensions,
			Rectangle2D frame, HorizontalAlignment hAlign,
			VerticalAlignment vAlign) {
		double x = Double.NaN;
		double y = Double.NaN;
		if (hAlign == HorizontalAlignment.LEFT) {
			x = frame.getX();
		} else if (hAlign == HorizontalAlignment.CENTER) {
			x = frame.getCenterX() - (dimensions.width / 2.0);
		} else if (hAlign == HorizontalAlignment.RIGHT) {
			x = frame.getMaxX() - dimensions.width;
		}
		if (vAlign == VerticalAlignment.TOP) {
			y = frame.getY();
		} else if (vAlign == VerticalAlignment.CENTER) {
			y = frame.getCenterY() - (dimensions.height / 2.0);
		} else if (vAlign == VerticalAlignment.BOTTOM) {
			y = frame.getMaxY() - dimensions.height;
		}

		return new Rectangle2D.Double(x, y, dimensions.width, dimensions.height);
	}

	/**
	 * Draws a title. The title should be drawn at the top, bottom, left or
	 * right of the specified area, and the area should be updated to reflect
	 * the amount of space used by the title.
	 * 
	 * @param t
	 *            the title (<code>null</code> not permitted).
	 * @param g2
	 *            the graphics device (<code>null</code> not permitted).
	 * @param area
	 *            the chart area, excluding any existing titles (
	 *            <code>null</code> not permitted).
	 * @param entities
	 *            a flag that controls whether or not an entity collection is
	 *            returned for the title.
	 * 
	 * @return An entity collection for the title (possibly <code>null</code>).
	 */
	protected EntityCollection drawTitle(Title t, Canvas g2, Rectangle2D area,
			boolean entities) {

		if (t == null) {
			throw new IllegalArgumentException("Null 't' argument.");
		}
		if (area == null) {
			throw new IllegalArgumentException("Null 'area' argument.");
		}
		Rectangle2D titleArea = new Rectangle2D.Double();
		RectangleEdge position = t.getPosition();
		double ww = area.getWidth();
		if (ww <= 0.0) {
			return null;
		}
		double hh = area.getHeight();
		if (hh <= 0.0) {
			return null;
		}
		RectangleConstraint constraint = new RectangleConstraint(ww, new Range(
				0.0, ww), LengthConstraintType.RANGE, hh, new Range(0.0, hh),
				LengthConstraintType.RANGE);
		Object retValue = null;
		BlockParams p = new BlockParams();
		p.setGenerateEntities(entities);
		if (position == RectangleEdge.TOP) {
			Size2D size = t.arrange(g2, constraint);
			titleArea = createAlignedRectangle2D(size, area, t
					.getHorizontalAlignment(), VerticalAlignment.TOP);
			retValue = t.draw(g2, titleArea, p);
			area.setRect(area.getX(), Math.min(area.getY() + size.height, area
					.getMaxY()), area.getWidth(), Math.max(area.getHeight()
					- size.height, 0));
		} else if (position == RectangleEdge.BOTTOM) {
			Size2D size = t.arrange(g2, constraint);
			titleArea = createAlignedRectangle2D(size, area, t
					.getHorizontalAlignment(), VerticalAlignment.BOTTOM);
			retValue = t.draw(g2, titleArea, p);
			area.setRect(area.getX(), area.getY(), area.getWidth(), area
					.getHeight()
					- size.height);
		} else if (position == RectangleEdge.RIGHT) {
			Size2D size = t.arrange(g2, constraint);
			titleArea = createAlignedRectangle2D(size, area,
					HorizontalAlignment.RIGHT, t.getVerticalAlignment());
			retValue = t.draw(g2, titleArea, p);
			area.setRect(area.getX(), area.getY(),
					area.getWidth() - size.width, area.getHeight());
		}

		else if (position == RectangleEdge.LEFT) {
			Size2D size = t.arrange(g2, constraint);
			titleArea = createAlignedRectangle2D(size, area,
					HorizontalAlignment.LEFT, t.getVerticalAlignment());
			retValue = t.draw(g2, titleArea, p);
			area.setRect(area.getX() + size.width, area.getY(), area.getWidth()
					- size.width, area.getHeight());
		} else {
			throw new RuntimeException("Unrecognised title position.");
		}
		EntityCollection result = null;
		if (retValue instanceof EntityBlockResult) {
			EntityBlockResult ebr = (EntityBlockResult) retValue;
			result = ebr.getEntityCollection();
		}
		return result;
	}

	@Override
	public void draw(Canvas g2, Rectangle area) {
		draw(g2, area);

	}

	@Override
	public void titleChanged(TitleChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotChanged(PlotChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
