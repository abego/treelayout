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
package org.abego.treelayout;

import static org.abego.treelayout.internal.util.Contract.*;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class MultiLineNodeExtentProvider implements
		NodeExtentProvider<StringTreeNode> {

	private final double width;
	private final double lineHeight;
	private double extraHeight;

	/**
	 * @param width
	 *            [default=0]
	 * 
	 * @param extraHeight
	 *            [default=0]
	 */
	public MultiLineNodeExtentProvider(double width, double lineHeight,
			double extraHeight) {
		checkArg(width >= 0, "width must be >= 0");
		checkArg(lineHeight >= 0, "lineHeight must be >= 0");
		checkArg(extraHeight >= 0, "extraHeight must be >= 0");

		this.width = width;
		this.lineHeight = lineHeight;
		this.extraHeight = extraHeight;
	}

	@Override
	public double getWidth(StringTreeNode treeNode) {
		return width;
	}

	private static int getLineCount(String s) {
		int lineCount = 1;
		int fromIndex = 0;
		int i;
		while ((i = s.indexOf('\n', fromIndex)) >= 0) {
			lineCount++;
			fromIndex = i + 1;
		}
		return lineCount;
	}

	@Override
	public double getHeight(StringTreeNode treeNode) {
		String s = treeNode.getText();
		int lineCount = getLineCount(s);
		return lineHeight * lineCount + extraHeight;
	}
}
