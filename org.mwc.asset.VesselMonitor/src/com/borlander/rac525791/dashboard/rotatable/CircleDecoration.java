/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package com.borlander.rac525791.dashboard.rotatable;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;

public class CircleDecoration extends GradientDecoration {
	private final Point TEMP = new Point();
	private final Point myCenter;
	private final int myRadius;

	public CircleDecoration(Point center, int radius, Color far, Color near){
		Point farPoint = center.getCopy().getTranslated(0, radius);
		Point nearPoint = center.getCopy().getTranslated(0, -radius);
		setGradient(farPoint, far, nearPoint, near);
		myRadius = radius;
		myCenter = center.getCopy();
		
		PointList fakePointsForBoundsOnly = new PointList();
		fakePointsForBoundsOnly.addPoint(center.x + radius + 1, center.y);
		fakePointsForBoundsOnly.addPoint(center.x, center.y + radius + 1);
		fakePointsForBoundsOnly.addPoint(center.x - radius - 1, center.y);
		fakePointsForBoundsOnly.addPoint(center.x, center.y - radius - 1);
		setTemplate(fakePointsForBoundsOnly);
	}
	
	@Override
	protected void outlineShape(Graphics g) {
		Point center = findCenter();
		g.pushState();
		g.setForegroundPattern(getPattern(getBounds()));
		g.drawOval(center.x - myRadius, center.y - myRadius, 2 * myRadius, 2 * myRadius);
		g.popState();
	}

	@Override
	protected void fillShape(Graphics g) {
		fillShadow(g);

		Point center = findCenter();
		Pattern pattern = getPattern(getBounds());
		g.pushState();
		g.setBackgroundPattern(pattern);
		g.fillOval(center.x - myRadius - 1, center.y - myRadius - 1, 2 * myRadius + 2, 2 * myRadius + 2);
		g.popState();
	}
	
	private Point findCenter(){
		return transformPoint(TEMP.setLocation(myCenter));
	}
	
}