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

import java.util.ArrayList;
import java.util.List;
import static org.abego.treelayout.internal.util.Contract.*;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class StringTreeNode {
	private boolean useTextForEquals;
	
	public StringTreeNode(String text, boolean useTextForEquals) {
		this.text = text;
		this.useTextForEquals = useTextForEquals;
	}

	public StringTreeNode(String text) {
		this(text, false);
	}

	// ------------------------------------------------------------------------
	// children

	private final List<StringTreeNode> children = new ArrayList<StringTreeNode>();

	public List<StringTreeNode> getChildren() {
		return children;
	}

	/**
	 * @param child
	 * @param checkConsistency [default: true]
	 */
	public void addChild(StringTreeNode child, boolean checkConsistency) {
		child.setParent(this, checkConsistency);
		this.getChildren().add(child);
	}

	public void addChild(StringTreeNode child) {
		addChild(child, true);
	}
	
	public void addChildren(StringTreeNode... children) {
		for (StringTreeNode child : children) {
			addChild(child);
		}
	}

	// ------------------------------------------------------------------------
	// parent

	private StringTreeNode parent;

	private void setParent(StringTreeNode parent, boolean checkConsistency) {
		if (checkConsistency) {
			checkState(this.parent == null, "parent already defined");
		}

		this.parent = parent;
	}

	public StringTreeNode getParent() {
		return parent;
	}

	// ------------------------------------------------------------------------
	// text

	private final String text;

	public String getText() {
		return text;
	}

	// ------------------------------------------------------------------------
	// equals/hashCode
	
	@Override
	public boolean equals(Object obj) {
		if (useTextForEquals && obj instanceof StringTreeNode) {
			return text.equals(((StringTreeNode) obj).getText());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return useTextForEquals ? text.hashCode() : super.hashCode();
	}
	
	
	// ------------------------------------------------------------------------
	// toString

	@Override
	public String toString() {
		return String.valueOf(getText());
	}
}