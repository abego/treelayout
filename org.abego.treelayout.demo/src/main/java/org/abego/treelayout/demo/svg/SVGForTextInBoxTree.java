/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.abego.treelayout.demo.svg;

import static org.abego.treelayout.demo.svg.SVGUtil.doc;
import static org.abego.treelayout.demo.svg.SVGUtil.line;
import static org.abego.treelayout.demo.svg.SVGUtil.rect;
import static org.abego.treelayout.demo.svg.SVGUtil.svg;
import static org.abego.treelayout.demo.svg.SVGUtil.text;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;

/**
 * Generates SVG for a given {@link TreeLayout} of {@link TextInBox} nodes.
 * <p>
 * 
 * @author Udo Borkowski (ub@abego.org)
 */
public class SVGForTextInBoxTree {
	private final TreeLayout<TextInBox> treeLayout;
	private String svgText;

	private TreeForTreeLayout<TextInBox> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<TextInBox> getChildren(TextInBox parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(TextInBox node) {
		return treeLayout.getNodeBounds().get(node);
	}

	/**
	 * @param treeLayout the {@link TreeLayout} to be rendered as SVG
	 */
	public SVGForTextInBoxTree(TreeLayout<TextInBox> treeLayout) {
		this.treeLayout = treeLayout;
	}

	// -------------------------------------------------------------------
	// generating

	private void generateEdges(StringBuilder result, TextInBox parent) {
		if (!getTree().isLeaf(parent)) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (TextInBox child : getChildren(parent)) {
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				result.append(line(x1, y1, b2.getCenterX(), b2.getCenterY(),
						"stroke:black; stroke-width:2px;"));

				generateEdges(result, child);
			}
		}
	}

	private void generateBox(StringBuilder result, TextInBox textInBox) {
		// draw the box in the background
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		result.append(rect(box.x + 1, box.y + 1, box.width - 2, box.height - 2,
				"fill:orange; stroke:rgb(0,0,0);", "rx=\"10\""));

		// draw the text on top of the box (possibly multiple lines)
		String[] lines = textInBox.text.split("\n");
		int fontSize = 12;
		int x = (int) box.x + fontSize / 2 + 2;
		int y = (int) box.y + fontSize + 1;
		String style = String.format("font-family:sans-serif;font-size:%dpx;",
				fontSize);
		for (int i = 0; i < lines.length; i++) {
			result.append(text(x, y, style, lines[i]));
			y += fontSize;
		}
	}

	private String generateDiagram() {
		StringBuilder result = new StringBuilder();

		// generate the edges and boxes (with text)
		generateEdges(result, getTree().getRoot());
		for (TextInBox textInBox : treeLayout.getNodeBounds().keySet()) {
			generateBox(result, textInBox);
		}

		// generate the svg containing the diagram items (edges and boxes)
		Dimension size = treeLayout.getBounds().getBounds().getSize();
		return svg(size.width, size.height, result.toString());
	}

	/**
	 * @return the rendered tree, described in SVG format.
	 */
	public String getSVG() {
		if (svgText == null) {
			svgText = doc(generateDiagram());
		}
		return svgText;
	}
}