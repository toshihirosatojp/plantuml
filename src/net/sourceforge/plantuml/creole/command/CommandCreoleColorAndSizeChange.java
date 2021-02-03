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
package net.sourceforge.plantuml.creole.command;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorRuntimeException;

public class CommandCreoleColorAndSizeChange implements Command {

	private final Pattern2 pattern;

	public static final String fontPattern = "\\<font(?:[%s]+size[%s]*=[%s]*[%g]?(\\d+)[%g]?|[%s]+color[%s]*=[%s]*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?)+[%s]*\\>";

	public static Command create() {
		return new CommandCreoleColorAndSizeChange("^(?i)(" + fontPattern + "(.*?)\\</font\\>)");
	}

	public static Command createEol() {
		return new CommandCreoleColorAndSizeChange("^(?i)(" + fontPattern + "(.*))$");
	}

	private CommandCreoleColorAndSizeChange(String p) {
		this.pattern = MyPattern.cmpile(p);

	}

	public int matchingSize(String line) {
		final Matcher2 m = pattern.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(1).length();
	}

	public String executeAndGetRemaining(String line, StripeSimple stripe) throws NoSuchColorRuntimeException {
		final Matcher2 m = pattern.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		// for (int i = 1; i <= m.groupCount(); i++) {
		// System.err.println("i=" + i + " " + m.group(i));
		// }

		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		FontConfiguration fc2 = fc1;
		if (m.group(2) != null) {
			fc2 = fc2.changeSize(Integer.parseInt(m.group(2)));
		}
		try {
			if (m.group(3) != null) {
				final String s = m.group(3);
				final HColor color = HColorSet.instance().getColor(s);
				fc2 = fc2.changeColor(color);
			}

			stripe.setActualFontConfiguration(fc2);
			stripe.analyzeAndAdd(m.group(4));
			stripe.setActualFontConfiguration(fc1);
			return line.substring(m.group(1).length());
		} catch (NoSuchColorException e) {
			throw new NoSuchColorRuntimeException();
		}
	}
}
