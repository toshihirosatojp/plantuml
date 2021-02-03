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
package net.sourceforge.plantuml.style;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.security.SFile;

public class CommandStyleImport extends SingleLineCommand2<TitledDiagram> {

	public CommandStyleImport() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStyleImport.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("\\<style"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\w+"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("="), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf("PATH", "([^%q%g]*)"), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf("\\>"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg) {
		final String path = arg.get("PATH", 0);
		try {
			final SFile f = FileSystem.getInstance().getFile(path);
			BlocLines lines = null;
			if (f.exists()) {
				lines = BlocLines.load(f, location);
			} else {
				final InputStream internalIs = StyleLoader.class.getResourceAsStream("/skin/" + path);
				if (internalIs != null) {
					lines = BlocLines.load(internalIs, location);
				}
			}
			if (lines == null) {
				return CommandExecutionResult.error("Cannot read: " + path);
			}
			final StyleBuilder styleBuilder = diagram.getSkinParam().getCurrentStyleBuilder();
			for (Style modifiedStyle : StyleLoader.getDeclaredStyles(lines, styleBuilder)) {
				diagram.getSkinParam().muteStyle(modifiedStyle);
			}
		} catch (IOException e) {
			return CommandExecutionResult.error("Cannot read: " + path);
		}
		return CommandExecutionResult.ok();
	}
}
