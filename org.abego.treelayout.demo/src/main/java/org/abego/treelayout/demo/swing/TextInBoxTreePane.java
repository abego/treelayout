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
package org.abego.treelayout.demo.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;

/**
 * A JComponent displaying a tree of TextInBoxes, given by a {@link TreeLayout}.
 * 
 * @author Udo Borkowski (ub@abego.org)
 */
public class TextInBoxTreePane extends JComponent {
	private final TreeLayout<TextInBox> treeLayout;
	private boolean boxVisible = true;

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
	 * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
	 * that tree.
	 * 
	 * @param treeLayout the {@link TreeLayout} to be displayed
	 */
	public TextInBoxTreePane(TreeLayout<TextInBox> treeLayout) {
		this.treeLayout = treeLayout;

		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
	}

	public boolean isBoxVisible() {
		return boxVisible;
	}

	public void setBoxVisible(boolean boxVisible) {
		this.boxVisible = boxVisible;
	}

	// -------------------------------------------------------------------
	// painting

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.orange;
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color TEXT_COLOR = Color.black;

	private void paintEdges(Graphics g, TextInBox parent) {
		if (!getTree().isLeaf(parent)) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (TextInBox child : getChildren(parent)) {
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
						(int) b2.getCenterY());

				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, TextInBox textInBox) {
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		if(isBoxVisible()) {
			// draw the box in the background
			g.setColor(BOX_COLOR);
			g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
					(int) box.height - 1, ARC_SIZE, ARC_SIZE);
			g.setColor(BORDER_COLOR);
			g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
					(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		} else {
			g.setColor(getBackground());
			g.fillRect((int) box.x, (int) box.y, (int) box.width - 1,(int) box.height - 1);
		}

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String[] lines = textInBox.text.split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int centerX = (int) box.getCenterX();
		int y = (int) box.y + m.getAscent() + m.getLeading() + 1;
		for (String line : lines) {
			Rectangle2D r = m.getStringBounds(line, g);
			int x = centerX - (int) (r.getWidth() / 2);
			g.drawString(line, x, y);
			y += m.getHeight();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		paintEdges(g, getTree().getRoot());

		// paint the boxes
		for (TextInBox textInBox : treeLayout.getNodeBounds().keySet()) {
			paintBox(g, textInBox);
		}
	}
}