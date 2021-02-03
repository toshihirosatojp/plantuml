/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 * 
 */
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.AbstractExtremityFactory;
import net.sourceforge.plantuml.svek.Side;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ExtremityFactoryArrowAndCircle extends AbstractExtremityFactory implements ExtremityFactory {
	private final HColor backgroundColor;

	public ExtremityFactoryArrowAndCircle(HColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public UDrawable createUDrawable(Point2D p0, Point2D p1, Point2D p2, Side side) {
		final double ortho = atan2(p0, p2);
		final Point2D center = new Point2D.Double((p0.getX() + p2.getX()) / 2, (p0.getY() + p2.getY()) / 2);
		return new ExtremityArrowAndCircle(p1, ortho, center, backgroundColor);
	}
}