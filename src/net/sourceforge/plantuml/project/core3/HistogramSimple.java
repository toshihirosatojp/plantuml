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
package net.sourceforge.plantuml.project.core3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class HistogramSimple implements Histogram {

	private final Map<Long, Long> events = new TreeMap<Long, Long>();

	public long getNext(long moment) {
		for (long e : events.keySet()) {
			if (e > moment) {
				return e;
			}
		}
		return TimeLine.MAX_TIME;
	}

	public long getPrevious(long moment) {
		long last = -TimeLine.MAX_TIME;
		for (long e : events.keySet()) {
			if (e >= moment) {
				return last;
			}
			last = e;
		}
		return last;
	}

	public void put(long event, long value) {
		this.events.put(event, value);
	}

	@Override
	public String toString() {
		return events.toString();
	}

	public long getValueAt(long moment) {
		long last = 0;
		for (Entry<Long, Long> ent : events.entrySet()) {
			if (ent.getKey() > moment) {
				return last;
			}
			last = ent.getValue();
		}
		return last;
	}

}
