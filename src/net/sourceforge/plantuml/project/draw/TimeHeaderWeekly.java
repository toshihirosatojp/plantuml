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
package net.sourceforge.plantuml.project.draw;

import java.util.Map;

import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeHeaderWeekly extends TimeHeader {

	private final LoadPlanable defaultPlan;
	private final Map<Day, HColor> colorDays;
	private final Map<DayOfWeek, HColor> colorDaysOfWeek;

	protected double getTimeHeaderHeight() {
		return 16 + 13;
	}

	public double getTimeFooterHeight() {
		return 16;
	}

	public TimeHeaderWeekly(Day calendar, Day min, Day max, LoadPlanable defaultPlan, Map<Day, HColor> colorDays,
			Map<DayOfWeek, HColor> colorDaysOfWeek, Map<Day, String> nameDays) {
		super(min, max, new TimeScaleCompressed(calendar, PrintScale.WEEKLY.getCompress()));
		this.defaultPlan = defaultPlan;
		this.colorDays = colorDays;
		this.colorDaysOfWeek = colorDaysOfWeek;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawCalendar(ug, totalHeightWithoutFooter);
		drawHline(ug, 0);
		drawHline(ug, Y_POS_ROW16());
		drawHline(ug, getFullHeaderHeight());
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		drawHline(ug, 0);
		printMonths(ug);
		drawHline(ug, getTimeFooterHeight());
	}

	private void drawCalendar(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTexts(ug, totalHeightWithoutFooter);
		printDaysOfMonth(ug);
		printSmallVbars(ug, totalHeightWithoutFooter);
		printMonths(ug);
	}

	private void drawTexts(final UGraphic ug, double totalHeightWithoutFooter) {
		final double height = totalHeightWithoutFooter - getFullHeaderHeight();
		Day firstDay = null;
		HColor lastColor = null;

		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			HColor back = colorDays.get(wink);
			// Day of week should be stronger than period of time (back color).
			final HColor backDoW = colorDaysOfWeek.get(wink.getDayOfWeek());
			if (backDoW != null) {
				back = backDoW;
			}
			if (back == null && defaultPlan.getLoadAt(wink) == 0) {
				back = veryLightGray;
			}
			if (back == null) {
				if (lastColor != null)
					drawBack(ug.apply(lastColor.bg()), firstDay, wink, height);
				firstDay = null;
				lastColor = null;
			} else {
				assert back != null;
				if (lastColor != null && lastColor.equals(back) == false) {
					drawBack(ug.apply(lastColor.bg()), firstDay, wink, height);
				}
				if (firstDay == null)
					firstDay = wink;
				lastColor = back;
			}
		}
	}

	private void drawBack(UGraphic ug, Day start, Day end, double height) {
		final double x1 = getTimeScale().getStartingPosition(start);
		final double x2 = getTimeScale().getStartingPosition(end);
		drawRectangle(ug, height, x1, x2);
	}

	private void printMonths(final UGraphic ug) {
		MonthYear last = null;
		double lastChangeMonth = -1;
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				drawVbar(ug, x1, 0, Y_POS_ROW16());
				if (last != null) {
					printMonth(ug, last, lastChangeMonth, x1);
				}
				lastChangeMonth = x1;
				last = wink.monthYear();
			}
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), 0, Y_POS_ROW16());
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChangeMonth) {
			printMonth(ug, last, lastChangeMonth, x1);
		}
	}

	private void printSmallVbars(final UGraphic ug, double totalHeightWithoutFooter) {
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			if (wink.getDayOfWeek() == DayOfWeek.MONDAY) {
				drawVbar(ug, getTimeScale().getStartingPosition(wink), Y_POS_ROW16(), totalHeightWithoutFooter);
			}
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), Y_POS_ROW16(), totalHeightWithoutFooter);
	}

	private void printDaysOfMonth(final UGraphic ug) {
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			if (wink.getDayOfWeek() == DayOfWeek.MONDAY) {
				printLeft(ug.apply(UTranslate.dy(Y_POS_ROW16())),
						getTextBlock("" + wink.getDayOfMonth(), 10, false, HColorUtils.BLACK),
						getTimeScale().getStartingPosition(wink) + 5);
			}
		}
	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock(monthYear.shortName(), 12, true, HColorUtils.BLACK);
		final TextBlock big = getTextBlock(monthYear.shortNameYYYY(), 12, true, HColorUtils.BLACK);
		printCentered(ug, start, end, small, big);
	}

	private void drawVbar(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(HColorUtils.LIGHT_GRAY).apply(new UTranslate(x, y1)).draw(vbar);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight();
	}

}
