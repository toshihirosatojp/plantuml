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
package net.sourceforge.plantuml.jsondiagram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.json.ParseException;

public class JsonDiagramFactory extends PSystemAbstractFactory {

	public JsonDiagramFactory() {
		super(DiagramType.JSON);
	}

	public Diagram createSystem(UmlSource source) {
		final List<String> highlighted = new ArrayList<String>();
		JsonValue json;
		try {
			final StringBuilder sb = new StringBuilder();
			final Iterator<StringLocated> it = source.iterator2();
			it.next();
			while (true) {
				final String line = it.next().getString();
				if (it.hasNext() == false) {
					break;
				}
				if (line.startsWith("#")) {
					if (line.startsWith("#highlight ")) {
						highlighted.add(line.substring("#highlight ".length()).trim());
						continue;
					}
				} else {
					sb.append(line);
					sb.append(BackSlash.CHAR_NEWLINE);
				}
			}
			json = Json.parse(sb.toString());
		} catch (ParseException e) {
			json = null;
		}
		final JsonDiagram result = new JsonDiagram(UmlDiagramType.JSON, json, highlighted);
		result.setSource(source);
		return result;
	}

}
