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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;

public class ComplementSeveralDays implements Something {

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("COMPLEMENT" + suffix, "(\\d+)[%s]+(day|week)s?" + //
						"(?:[%s]+and[%s]+(\\d+)[%s]+(day|week)s?)?" //
				)); //
	}

	public Failable<Load> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final String nb1 = arg.get("COMPLEMENT" + suffix, 0);
		final int factor1 = arg.get("COMPLEMENT" + suffix, 1).startsWith("w") ? system.daysInWeek() : 1;
		final int days1 = Integer.parseInt(nb1) * factor1;

		final String nb2 = arg.get("COMPLEMENT" + suffix, 2);
		int days2 = 0;
		if (nb2 != null) {
			final int factor2 = arg.get("COMPLEMENT" + suffix, 3).startsWith("w") ? system.daysInWeek() : 1;
			days2 = Integer.parseInt(nb2) * factor2;
		}

		return Failable.ok(Load.inWinks(days1 + days2));
	}

}
