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
 * LabelBlock.java
 * ---------------
 * (C) Copyright 2004-2009, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Pierre-Marie Le Biot;
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 19-Apr-2005 : Added optional tooltip and URL text items,
 *               draw() method now returns entities if
 *               requested (DG);
 * 13-May-2005 : Added methods to set the font (DG);
 * 01-Sep-2005 : Added paint management (PMLB);
 *               Implemented equals() and clone() (PublicCloneable) (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 20-Jul-2006 : Fixed entity area in draw() method (DG);
 * 16-Mar-2007 : Fixed serialization when using GradientPaint (DG);
 * 10-Feb-2009 : Added alignment fields (DG);
 *
 */

package net.droidsolutions.droidcharts.core.block;

import net.droidsolutions.droidcharts.awt.Font;
import net.droidsolutions.droidcharts.awt.Point2D;
import net.droidsolutions.droidcharts.awt.Rectangle;
import net.droidsolutions.droidcharts.awt.Rectangle2D;
import net.droidsolutions.droidcharts.awt.Shape;
import net.droidsolutions.droidcharts.common.RectangleAnchor;
import net.droidsolutions.droidcharts.common.Size2D;
import net.droidsolutions.droidcharts.core.entity.ChartEntity;
import net.droidsolutions.droidcharts.core.entity.StandardEntityCollection;
import net.droidsolutions.droidcharts.core.text.TextBlock;
import net.droidsolutions.droidcharts.core.text.TextBlockAnchor;
import net.droidsolutions.droidcharts.core.text.TextUtilities;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * A block containing a label.
 */
public class LabelBlock extends AbstractBlock implements Block {

	/** For serialization. */
	static final long serialVersionUID = 249626098864178017L;

	/**
	 * The text for the label - retained in case the label needs regenerating
	 * (for example, to change the font).
	 */
	private String text;

	/** The label. */
	private TextBlock label;

	/** The font. */
	private Font font;

	/** The tool tip text (can be <code>null</code>). */
	private String toolTipText;

	/** The URL text (can be <code>null</code>). */
	private String urlText;

	/** The default color. */
	public static final Paint DEFAULT_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	static {
		DEFAULT_PAINT.setColor(Color.BLACK);
	}

	/** The paint. */
	private transient Paint paint;

	/**
	 * The content alignment point.
	 * 
	 * @since 1.0.13
	 */
	private TextBlockAnchor contentAlignmentPoint;

	/**
	 * The anchor point for the text.
	 * 
	 * @since 1.0.13
	 */
	private RectangleAnchor textAnchor;

	/**
	 * Creates a new label block.
	 * 
	 * @param label
	 *            the label (<code>null</code> not permitted).
	 */
	public LabelBlock(String label) {
		this(label, new Font("SansSerif", Typeface.NORMAL, 10), DEFAULT_PAINT);
	}

	/**
	 * Creates a new label block.
	 * 
	 * @param text
	 *            the text for the label (<code>null</code> not permitted).
	 * @param font
	 *            the font (<code>null</code> not permitted).
	 */
	public LabelBlock(String text, Font font) {
		this(text, font, DEFAULT_PAINT);
	}

	/**
	 * Creates a new label block.
	 * 
	 * @param text
	 *            the text for the label (<code>null</code> not permitted).
	 * @param font
	 *            the font (<code>null</code> not permitted).
	 * @param paint
	 *            the paint (<code>null</code> not permitted).
	 */
	public LabelBlock(String text, Font font, Paint paint) {
		this.text = text;
		this.paint = paint;
		this.label = TextUtilities.createTextBlock(text, font, this.paint);
		this.font = font;
		this.toolTipText = null;
		this.urlText = null;
		this.contentAlignmentPoint = TextBlockAnchor.CENTER;
		this.textAnchor = RectangleAnchor.CENTER;
	}

	/**
	 * Returns the font.
	 * 
	 * @return The font (never <code>null</code>).
	 * 
	 * @see #setFont(Font)
	 */
	public Font getFont() {
		return this.font;
	}

	/**
	 * Sets the font and regenerates the label.
	 * 
	 * @param font
	 *            the font (<code>null</code> not permitted).
	 * 
	 * @see #getFont()
	 */
	public void setFont(Font font) {
		if (font == null) {
			throw new IllegalArgumentException("Null 'font' argument.");
		}
		this.font = font;
		this.label = TextUtilities.createTextBlock(this.text, font, this.paint);
	}

	/**
	 * Returns the paint.
	 * 
	 * @return The paint (never <code>null</code>).
	 * 
	 * @see #setPaint(Paint)
	 */
	public Paint getPaint() {
		return this.paint;
	}

	/**
	 * Sets the paint and regenerates the label.
	 * 
	 * @param paint
	 *            the paint (<code>null</code> not permitted).
	 * 
	 * @see #getPaint()
	 */
	public void setPaint(Paint paint) {
		if (paint == null) {
			throw new IllegalArgumentException("Null 'paint' argument.");
		}
		this.paint = paint;
		this.label = TextUtilities.createTextBlock(this.text, this.font,
				this.paint);
	}

	/**
	 * Returns the tool tip text.
	 * 
	 * @return The tool tip text (possibly <code>null</code>).
	 * 
	 * @see #setToolTipText(String)
	 */
	public String getToolTipText() {
		return this.toolTipText;
	}

	/**
	 * Sets the tool tip text.
	 * 
	 * @param text
	 *            the text (<code>null</code> permitted).
	 * 
	 * @see #getToolTipText()
	 */
	public void setToolTipText(String text) {
		this.toolTipText = text;
	}

	/**
	 * Returns the URL text.
	 * 
	 * @return The URL text (possibly <code>null</code>).
	 * 
	 * @see #setURLText(String)
	 */
	public String getURLText() {
		return this.urlText;
	}

	/**
	 * Sets the URL text.
	 * 
	 * @param text
	 *            the text (<code>null</code> permitted).
	 * 
	 * @see #getURLText()
	 */
	public void setURLText(String text) {
		this.urlText = text;
	}

	/**
	 * Returns the content alignment point.
	 * 
	 * @return The content alignment point (never <code>null</code>).
	 * 
	 * @since 1.0.13
	 */
	public TextBlockAnchor getContentAlignmentPoint() {
		return this.contentAlignmentPoint;
	}

	/**
	 * Sets the content alignment point.
	 * 
	 * @param anchor
	 *            the anchor used to determine the alignment point (never
	 *            <code>null</code>).
	 * 
	 * @since 1.0.13
	 */
	public void setContentAlignmentPoint(TextBlockAnchor anchor) {
		if (anchor == null) {
			throw new IllegalArgumentException("Null 'anchor' argument.");
		}
		this.contentAlignmentPoint = anchor;
	}

	/**
	 * Returns the text anchor (never <code>null</code>).
	 * 
	 * @return The text anchor.
	 * 
	 * @since 1.0.13
	 */
	public RectangleAnchor getTextAnchor() {
		return this.textAnchor;
	}

	/**
	 * Sets the text anchor.
	 * 
	 * @param anchor
	 *            the anchor (<code>null</code> not permitted).
	 * 
	 * @since 1.0.13
	 */
	public void setTextAnchor(RectangleAnchor anchor) {
		this.textAnchor = anchor;
	}

	/**
	 * Arranges the contents of the block, within the given constraints, and
	 * returns the block size.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param constraint
	 *            the constraint (<code>null</code> not permitted).
	 * 
	 * @return The block size (in Java2D units, never <code>null</code>).
	 */
	public Size2D arrange(Canvas g2, RectangleConstraint constraint) {
		// g2.setFont(this.font);
		Size2D s = this.label.calculateDimensions(g2);
		return new Size2D(calculateTotalWidth(s.getWidth()),
				calculateTotalHeight(s.getHeight()));
	}

	/**
	 * Draws the block.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param area
	 *            the area.
	 */
	public void draw(Canvas g2, Rectangle2D area) {
		draw(g2, area, null);
	}

	/**
	 * Draws the block within the specified area.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param area
	 *            the area.
	 * @param params
	 *            ignored (<code>null</code> permitted).
	 * 
	 * @return Always <code>null</code>.
	 */
	public Object draw(Canvas g2, Rectangle2D area, Object params) {
		area = trimMargin(area);
		drawBorder(g2, area);
		area = trimBorder(area);
		area = trimPadding(area);

		// check if we need to collect chart entities from the container
		EntityBlockParams ebp = null;
		StandardEntityCollection sec = null;
		Shape entityArea = null;
		if (params instanceof EntityBlockParams) {
			ebp = (EntityBlockParams) params;
			if (ebp.getGenerateEntities()) {
				sec = new StandardEntityCollection();
				entityArea = (Shape) area.clone();
			}
		}
		
		paint.setTypeface(font.getTypeFace());
		paint.setTextSize(font.getSize());
		Point2D pt = RectangleAnchor.coordinates(area, this.textAnchor);
		this.label.draw(g2, (float) pt.getX(), (float) pt.getY(),
				this.contentAlignmentPoint, paint);
		BlockResult result = null;
		if (ebp != null && sec != null) {
			if (this.toolTipText != null || this.urlText != null) {
				ChartEntity entity = new ChartEntity(entityArea,
						this.toolTipText, this.urlText);
				sec.add(entity);
				result = new BlockResult();
				result.setEntityCollection(sec);
			}
		}
		return result;
	}

	@Override
	public void draw(Canvas g2, Rectangle area) {
		draw(g2, area);
	}

}
