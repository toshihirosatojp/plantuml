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
package net.sourceforge.plantuml.asciiart;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class ComponentTextActor extends AbstractComponentText {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;
	private final AsciiShape shape;

	public ComponentTextActor(ComponentType type, Display stringsToDisplay, FileFormat fileFormat, AsciiShape shape) {
		this.type = type;
		this.stringsToDisplay = stringsToDisplay;
		this.fileFormat = fileFormat;
		this.shape = shape;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight();
		charArea.fillRect(' ', 0, 0, width, height);

		final int xman = width / 2 - 1;
		if (type == ComponentType.ACTOR_HEAD) {
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStringsLRUnicode(stringsToDisplay.as(), 1, getHeight());
				charArea.drawShape(AsciiShape.STICKMAN_UNICODE, xman, 0);
			} else {
				charArea.drawStringsLRSimple(stringsToDisplay.as(), 1, getHeight());
				charArea.drawShape(AsciiShape.STICKMAN, xman, 0);
			}
		} else if (type == ComponentType.ACTOR_TAIL) {
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStringsLRUnicode(stringsToDisplay.as(), 1, 0);
				charArea.drawShape(AsciiShape.STICKMAN_UNICODE, xman, 1);
			} else {
				charArea.drawStringsLRSimple(stringsToDisplay.as(), 1, 0);
				charArea.drawShape(AsciiShape.STICKMAN, xman, 1);
			}
		} else {
			assert false;
		}
	}

	private int getHeight() {
		if (fileFormat == FileFormat.UTXT) {
			return AsciiShape.STICKMAN_UNICODE.getHeight();
		}
		return AsciiShape.STICKMAN.getHeight();
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + getHeight();
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWcWidth(stringsToDisplay) + 2;
	}

}
