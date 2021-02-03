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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.svek.GuideLine;

public enum LinkArrow {

	NONE_OR_SEVERAL, DIRECT_NORMAL, BACKWARD;

	public LinkArrow reverse() {
		if (this == DIRECT_NORMAL) {
			return BACKWARD;
		}
		if (this == BACKWARD) {
			return DIRECT_NORMAL;
		}
		return NONE_OR_SEVERAL;
	}

	public GuideLine mute(final GuideLine guide) {
		switch (this) {
		case DIRECT_NORMAL:
			return guide;
		case BACKWARD:
			return new GuideLine() {
				public Direction getArrowDirection() {
					return guide.getArrowDirection().getInv();
				}

				public double getArrowDirection2() {
					return Math.PI + guide.getArrowDirection2();
				}
			};

		}
		throw new UnsupportedOperationException();
	}

}
