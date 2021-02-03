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
package net.sourceforge.plantuml;

public class ErrorUml {

	private final String error;
	private final ErrorUmlType type;
	private final LineLocation lineLocation;
	private final int score;

	public ErrorUml(ErrorUmlType type, String error, int score, LineLocation lineLocation) {
		if (error == null || type == null) {
			throw new IllegalArgumentException();
		}
		this.score = score;
		this.error = error;
		this.type = type;
		this.lineLocation = lineLocation;
	}

	public int score() {
		return score;
	}

	@Override
	public boolean equals(Object obj) {
		final ErrorUml this2 = (ErrorUml) obj;
		return this.type == this2.type && this.getPosition() == this2.getPosition() && this.error.equals(this2.error);
	}

	@Override
	public int hashCode() {
		return error.hashCode() + type.hashCode() + getPosition();
	}

	@Override
	public String toString() {
		return type.toString() + " " + getPosition() + " " + error;
	}

	public final String getError() {
		return error;
	}

	public final ErrorUmlType getType() {
		return type;
	}

	public final int getPosition() {
		return lineLocation.getPosition();
	}

	public final LineLocation getLineLocation() {
		return lineLocation;
	}

}
