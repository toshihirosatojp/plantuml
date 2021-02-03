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
 */
package net.sourceforge.plantuml.nwdiag;

public class NwArray {

	private final LinkedElement data[][];

	public NwArray(int lines, int cols) {
		this.data = new LinkedElement[lines][cols];
	}

	public int getNbLines() {
		return data.length;
	}

	public int getNbCols() {
		return data[0].length;
	}

	public LinkedElement get(int i, int j) {
		return data[i][j];
	}

	public LinkedElement[] getLine(int i) {
		return data[i];
	}

	public void set(int i, int j, LinkedElement value) {
		data[i][j] = value;
	}

	public void swapCols(int col1, int col2) {
		if (col1 == col2) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < getNbLines(); i++) {
			final LinkedElement tmp = data[i][col1];
			data[i][col1] = data[i][col2];
			data[i][col2] = tmp;
		}

	}

	public Footprint getFootprint(NwGroup group) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < getNbLines(); i++) {
			for (int j = 0; j < getNbCols(); j++) {
				if (data[i][j] != null && group.matches(data[i][j])) {
					min = Math.min(min, j);
					max = Math.max(max, j);
				}
			}
		}
		return new Footprint(min, max);
	}

}
