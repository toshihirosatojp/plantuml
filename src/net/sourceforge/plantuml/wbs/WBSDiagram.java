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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.ImageParameter;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class WBSDiagram extends UmlDiagram {

	public DiagramDescription getDescription() {
		return new DiagramDescription("Work Breakdown Structure");
	}

	public WBSDiagram() {
		super(UmlDiagramType.WBS);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final Scale scale = getScale();

		final double dpiFactor = scale == null ? getScaleCoef(fileFormatOption) : scale.getScale(100, 100);
		final ISkinParam skinParam = getSkinParam();
		final double margin1;
		final double margin2;
		if (UseStyle.useBetaStyle()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		HColor backcolor = skinParam.getBackgroundColor(false);
		final ClockwiseTopRightBottomLeft margins = ClockwiseTopRightBottomLeft.margin1margin2(margin1, margin2);
		final String metadata = fileFormatOption.isWithMetadata() ? getMetadata() : null;
		final ImageParameter imageParameter = new ImageParameter(skinParam.getColorMapper(), skinParam.handwritten(),
				null, dpiFactor, metadata, "", margins, backcolor);

		final ImageBuilder imageBuilder = ImageBuilder.build(imageParameter);
		TextBlock result = getTextBlock();

		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder(getSkinParam()))
				.addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return getDrawingElement().calculateDimension(stringBounder);

			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawMe(UGraphic ug) {
		getDrawingElement().drawU(ug);

	}

	private TextBlock getDrawingElement() {
		return new Fork(getSkinParam(), root);
	}

	public final static Pattern2 patternStereotype = MyPattern
			.cmpile("^\\s*(.*?)(?:\\s*\\<\\<\\s*(.*)\\s*\\>\\>)\\s*$");

	public CommandExecutionResult addIdea(HColor backColor, int level, String label, Direction direction,
			IdeaShape shape) {
		try {
			final Matcher2 m = patternStereotype.matcher(label);
			String stereotype = null;
			if (m.matches()) {
				label = m.group(1);
				stereotype = m.group(2);
			}
			if (level == 0) {
				if (root != null) {
					return CommandExecutionResult.error("Error 44");
				}
				initRoot(backColor, label, stereotype);
				return CommandExecutionResult.ok();
			}
			return add(backColor, level, label, stereotype, direction, shape);
		} catch (NoStyleAvailableException e) {
			// e.printStackTrace();
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

	private WElement root;
	private WElement last;

	private void initRoot(HColor backColor, String label, String stereotype) {
		root = new WElement(backColor, Display.getWithNewlines(label), stereotype,
				getSkinParam().getCurrentStyleBuilder());
		last = root;
	}

	private WElement getParentOfLast(int nb) {
		WElement result = last;
		for (int i = 0; i < nb; i++) {
			result = result.getParent();
		}
		return result;
	}

	private CommandExecutionResult add(HColor backColor, int level, String label, String stereotype,
			Direction direction, IdeaShape shape) {
		try {
			if (level == last.getLevel() + 1) {
				final WElement newIdea = last.createElement(backColor, level, Display.getWithNewlines(label),
						stereotype, direction, shape, getSkinParam().getCurrentStyleBuilder());
				last = newIdea;
				return CommandExecutionResult.ok();
			}
			if (level <= last.getLevel()) {
				final int diff = last.getLevel() - level + 1;
				final WElement newIdea = getParentOfLast(diff).createElement(backColor, level,
						Display.getWithNewlines(label), stereotype, direction, shape,
						getSkinParam().getCurrentStyleBuilder());
				last = newIdea;
				return CommandExecutionResult.ok();
			}
			return CommandExecutionResult.error("error42L");
		} catch (NoStyleAvailableException e) {
			// e.printStackTrace();
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

}
