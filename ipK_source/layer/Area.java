/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;

/** Area contains all the scaling and translation variables and routines for
 * mapping double coorinates within bounds (x, y, width, height) to pixel
 * integer coordinates.  A single Area instance is associated with a
 * BaseLayer and passed to every Layer of a particular DataDisplay in the
 * draw(Graphics g, Area a) method.
 */
public final class Area {

	double x, y, width, height;
	Dimension screen;
	double minX, minY, maxWidth, maxHeight, minWidth, minHeight;
	static final double MAX_ZOOM = 1000.0;

	/** Creates an Area object that has its origin at (0.0, 0.0).
	 */
	public Area(double width, double height) {
		this(0.0, 0.0, width, height);
	}

	/** Creates an Area object that has its origin at (x, y) and has the given
	 * maximum width and height values.  This means that objects translated
	 * with horizontal coordinates less than x or more than x + width would be
	 * outside the drawing area of the DataDisplay.
	 */
	public Area(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = (width == 0.0) ? 1.0 : Math.abs(width);
		this.height = (height == 0.0) ? 1.0 : Math.abs(height);
		
		minX = this.x;
		minY = this.y;
		maxWidth = this.width;
		maxHeight = this.height;
		minWidth = maxWidth / MAX_ZOOM;
		minHeight = maxHeight / MAX_ZOOM;
	}

	public String toString() {
		return "Area (" + x + ", " + y + ") [" + width + " * " + height
				+ "] mapped to screen (0, 0) [" + screen.width + " * " 
				+ screen.height + "]";
	}
	
	String getInfo() {
		return "Origin: (" + (float) x + ", " + (float) y + ") Area: [" 
				+ (float) width + " x " + (float) height + "]";
	}

	/** Returns the size of the screen drawing area.
	 *<p>
	 * @return A Dimension object containing the size of the drawing area in
	 * pixels.
	 */
	public Dimension getScreen() {
		return screen;
	}

	/** Translates a point in the double domain to a point in the screen domain
	 * (pixels) which is sutable for use by the drawing methods of Graphics.
	 * Allways use this method for translating coordinates pairs, DON'T use two
	 * calls to the translate(double, boolean) method non-zero origins and the 
	 * y-axis will not be represented properly (y will not be flipped to 
	 * preserve the origin at the lower left corner).
	 *<p>
	 * @param x The horizontal component of the coordinate to be translated.
	 * Must be between x and x + width as passed to the constructor.
	 * @param y The vertical component of the coordinate to be translated.
	 * Must be between y and y + height as passed to the constructor.
	 * @return A Point holding a pixel based coordinate based on the current
	 * zoom.
	 */
	public Point translate(double x, double y) {
		return new Point(sTranslate(x, true), sTranslate(y, false));
	}

	/** Translates a width or height in the double domain to a width or height
	 * in the screen domain (pixels). Allways use this method for translating
	 * width or heights, do NOT use two calls to this method to translate
	 * coodrinate pairs because non-zero origins and the y-axis will not be 
	 * represented properly (y will not be flipped to preserve the origin at 
	 * the lower left corner).
	 *<p>
	 * @param value The width or height to be translated.
	 * Must be between 0 and width or height as passed to the constructor.
	 * @param horizonal Indicated whether the translation will be for a width 
	 * or a height. Note that since aspect ratio is currently maintained, this 
	 * parameter should seem to have no effect, but use it anyway for future 
	 * compatibility.
	 * @return The width or height in pixels based on the current zoom.
	 */
	public int translate(double value, boolean horizontal) {
		return (horizontal) ? translate(0, screen.width, value, 0, width) :
				translate(0, screen.height, value, 0, height);
	}

	/** Checks to see if a Point in the screen domain is within a double range
	 * of a point in the double domain.  Appripriate for use in the
	 * handleClick() method.
	 *<p>
	 * @param p The location of the click.
	 * @param range The radius which the click must be inside of.
	 * @param x The horizontal component of the center of the click target.
	 * @param y The vertical component of the center of the click target.
	 * @returns true if the click was within the range of (x, y) after 
	 * translation.
	 */
	public boolean withinRange(Point p, double range, double x, double y) {
		double xDelta = x - sTranslate(p.x, true);
		double yDelta = y - sTranslate(p.y, false);
		return (xDelta * xDelta + yDelta * yDelta <= range * range);
	}
	
	void setScreen(Dimension screen) {
		this.screen = screen;
		assertArea();
	}

	void reset() {
		x = minX;
		y = minY;
		width = maxWidth;
		height = maxHeight;
	}

	Dimension getPreferredSize(Component parent) {
		Dimension screenSize = parent.getToolkit().getScreenSize();
		screenSize.width /= 2;
		screenSize.height /= 2;
		
		if ((width < screenSize.width) && (height < screenSize.height)) {
			int multiplier = 2;
			while ((width * multiplier < screenSize.width) 
					&& (height * multiplier < screenSize.height))
				multiplier++;
			
			return new Dimension((int) (width * --multiplier), 
					(int) (height * multiplier));
		}
			
		int divisor = 1;
		while ((width / divisor > screenSize.width) 
				&& (height / divisor > screenSize.height))
			divisor++;

		return new Dimension((int) (width / divisor), 
				(int) (height / divisor));
	}

	void zoom(Point startDrag, Point endDrag, boolean zoomIn) {
		double startClickX = sTranslate(startDrag.x, true);
		double startClickY = sTranslate(startDrag.y, false);
		double endClickX = sTranslate(endDrag.x, true);
		double endClickY = sTranslate(endDrag.y, false);

		double drawnWidth = Math.abs(startClickX - endClickX);
		double drawnHeight = Math.abs(startClickY - endClickY);

		double zoomFactor;

		if (drawnWidth / drawnHeight < screen.width / screen.height)
			zoomFactor = height / drawnHeight;
		else
			zoomFactor = width / drawnWidth;

		if (!zoomIn)
			zoomFactor = 1 / zoomFactor;

		zoom(new Point((startDrag.x + endDrag.x) / 2, 
				(startDrag.y + endDrag.y) / 2), zoomFactor);
	}

	void zoom(Point center, double zoomFactor) {
		center(center);
		zoom(zoomFactor);
	}

	void center(Point center) {
		x += sTranslate(center.x, true) - (x + width / 2);
		y += sTranslate(center.y, false) - (y + height / 2);
	}

	void zoom(double zoomFactor) {
		x += (width - width / zoomFactor) / 2;
		y += (height - height / zoomFactor) / 2;
		width /= zoomFactor;
		height /= zoomFactor;
	}
	
	private void assertArea() {
		// Verify aspect ratio
		double xUnitsPerPixel = width / screen.width;
		double yUnitsPerPixel = height / screen.height;

		if (xUnitsPerPixel > yUnitsPerPixel)
			height *= xUnitsPerPixel / yUnitsPerPixel;
		else if (yUnitsPerPixel > xUnitsPerPixel)
			width *= yUnitsPerPixel / xUnitsPerPixel;
	
		// Verify width and height
		double factorX = 1.0;
		if (width > maxWidth)
			factorX = width / maxWidth;
		else if (width < minWidth)
			factorX = width / minWidth;
			
		double factorY = 1.0;		
		if (height > maxHeight)
			factorY = height / maxHeight;
		else if (height < minHeight)
			factorY = height / minHeight;
			
		if (factorX != 1.0 || factorY != 1.0)
			zoom(Math.min(factorX, factorY));
			
		/*{
			if (factorX + factorY < 2.0) // Zoom out because area is too small
				factorX = Math.min(factorX, factorY);
			else // Zoom in because area is too big
				factorX = Math.max(factorX, factorY);
			
			area.zoom(factorX);
		}*/
		
		// Verify x and y
		if (x + width > minX + maxWidth)
			x = maxWidth - width;
		if (x < minX)
			x = minX;

		if (y + height > minY + maxHeight)
			y = maxHeight - height;
		if (y < minY)
			y = minY;
	}

	private double sTranslate(int value, boolean horizontal) {
		return (horizontal) ? translate(x, x + width, value, 0, screen.width) 
				: translate(y, y + height, value, screen.height, 0);
	}

	private double translate(double newMin, double newMax, int value, 
			int oldMin, int oldMax) {
		if (oldMin == oldMax) {
			oldMin--;
			oldMax++;
		}
		return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) 
				+ newMin;
	}
	
	private int sTranslate(double value, boolean horizontal) {
		return (horizontal)	? translate(0, screen.width, value, x, x + width) 
				: translate(0, screen.height, value, y + height, y);
	}

	private int translate(int newMin, int newMax, double value, double oldMin, 
			double oldMax) {
		if (oldMin == oldMax) {
			oldMin -= 1.0;
			oldMax += 1.0;
		}
		return (int) ((value - oldMin) * (newMax - newMin) 
				/ (oldMax - oldMin) + newMin);
	}
}
