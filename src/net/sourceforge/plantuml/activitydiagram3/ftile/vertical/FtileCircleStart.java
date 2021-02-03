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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class FtileCircleStart extends AbstractFtile {

	private static final int SIZE = 20;

	private final HColor backColor;
	private final Swimlane swimlane;
	private double shadowing;

	public FtileCircleStart(ISkinParam skinParam, HColor backColor, Swimlane swimlane, Style style) {
		super(skinParam);
		this.backColor = backColor;
		this.swimlane = swimlane;
		if (UseStyle.useBetaStyle()) {
			this.shadowing = style.value(PName.Shadowing).asDouble();
		} else {
			if (skinParam().shadowing(null)) {
				this.shadowing = 3;
			}
		}

	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.emptyList();
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public void drawU(UGraphic ug) {
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		circle.setDeltaShadow(shadowing);
		ug.apply(new HColorNone()).apply(backColor.bg()).draw(circle);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		return new FtileGeometry(SIZE, SIZE, SIZE / 2, 0, SIZE);
	}

}
