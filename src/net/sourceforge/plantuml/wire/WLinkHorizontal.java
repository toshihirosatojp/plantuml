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
package net.sourceforge.plantuml.wire;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class WLinkHorizontal {

	private final UTranslate start;
	private final double destination;
	private final WLinkType type;
	private final WArrowDirection direction;
	private final HColor color;
	private final Display label;
	private final ISkinParam skinParam;

	public WLinkHorizontal(ISkinParam skinParam, UTranslate start, double destination, WLinkType type,
			WArrowDirection direction, HColor color, Display label) {
		this.start = start;
		this.destination = destination;
		this.skinParam = skinParam;
		this.direction = direction;
		this.type = type;
		this.label = label;
		this.color = color == null ? HColorUtils.BLACK : color;
	}

	private TextBlock getTextBlock() {
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(UFont.sansSerif(10))
				.changeColor(color);
		return label.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
	}

	public void drawMe(UGraphic ug) {
		ug = ug.apply(color);
		final TextBlock textBlock = getTextBlock();
		final Dimension2D dimText = textBlock.calculateDimension(ug.getStringBounder());

		UGraphic ugText = ug.apply(start);
		final double len = destination - start.getDx();

		if (type == WLinkType.NORMAL) {
			ug = ug.apply(color.bg());
			drawNormalArrow(ug);

			ugText = ugText.apply(UTranslate.dy(-dimText.getHeight() / 2));

		} else if (type == WLinkType.BUS) {
			ug = ug.apply(HColorUtils.WHITE.bg());
			drawBusArrow(ug);
			ugText = ugText.apply(UTranslate.dy((20 - dimText.getHeight()) / 2 - 5));
		}

		if (dimText.getHeight() > 0) {
			switch (direction) {
			case NORMAL:
				ugText = ugText.apply(UTranslate.dx(4));
				break;
			case REVERSE:
				ugText = ugText.apply(UTranslate.dx(len - dimText.getWidth() - 4));
				break;
			default:
				ugText = ugText.apply(UTranslate.dx((len - dimText.getWidth()) / 2));
				break;
			}
			if (type == WLinkType.NORMAL) {
				ugText.apply(HColorUtils.WHITE).apply(HColorUtils.WHITE.bg()).draw(new URectangle(dimText));
			}
			textBlock.drawU(ugText);
		}

	}

	private void drawBusArrow(UGraphic ug) {
		final double dx = destination - start.getDx() - 2;
		final UPath path = new UPath();
		if (direction == WArrowDirection.NONE) {
			path.moveTo(0, 0);
			path.lineTo(dx, 0);
			path.lineTo(dx, 10);
			path.lineTo(0, 10);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(1))).draw(path);
		}
		if (direction == WArrowDirection.NORMAL) {
			path.moveTo(0, 0);
			path.lineTo(dx - 15, 0);
			path.lineTo(dx - 15, -5);
			path.lineTo(dx, 5);
			path.lineTo(dx - 15, 15);
			path.lineTo(dx - 15, 10);
			path.lineTo(0, 10);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(1))).draw(path);
		}
		if (direction == WArrowDirection.BOTH) {
			path.moveTo(0, 5);
			path.lineTo(15, -5);
			path.lineTo(15, 0);
			path.lineTo(dx - 15, 0);
			path.lineTo(dx - 15, -5);
			path.lineTo(dx, 5);
			path.lineTo(dx - 15, 15);
			path.lineTo(dx - 15, 10);
			path.lineTo(15, 10);
			path.lineTo(15, 15);
			path.lineTo(0, 5);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(1))).draw(path);
		}
		if (direction == WArrowDirection.REVERSE) {
			path.moveTo(0, 5);
			path.lineTo(15, -5);
			path.lineTo(15, 0);
			path.lineTo(dx, 0);
			path.lineTo(dx, 10);
			path.lineTo(15, 10);
			path.lineTo(15, 15);
			path.lineTo(0, 5);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(1))).draw(path);
		}
	}

	private void drawNormalArrow(UGraphic ug) {
		final double dx = destination - start.getDx() - 2;
		if (direction == WArrowDirection.BOTH || direction == WArrowDirection.NORMAL) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(-5, -5);
			path.lineTo(-5, 5);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(dx))).draw(path);
		}
		if (direction == WArrowDirection.BOTH || direction == WArrowDirection.REVERSE) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(5, -5);
			path.lineTo(5, 5);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(start.compose(UTranslate.dx(1))).draw(path);
		}
		ug.apply(start.compose(UTranslate.dx(1))).draw(ULine.hline(dx));
	}

}
