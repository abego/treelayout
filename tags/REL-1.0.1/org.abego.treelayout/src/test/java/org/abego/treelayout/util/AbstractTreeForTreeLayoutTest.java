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
package org.abego.treelayout.util;

import java.util.Iterator;
import java.util.List;

import org.abego.treelayout.StringTreeNode;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class AbstractTreeForTreeLayoutTest {
	StringTreeNode root = new StringTreeNode("root");
	StringTreeNode n1 = new StringTreeNode("n1");
	StringTreeNode n2 = new StringTreeNode("n2");
	AbstractTreeForTreeLayout<StringTreeNode> tree = new AbstractTreeForTreeLayout<StringTreeNode>(
			root) {

		@Override
		public StringTreeNode getParent(StringTreeNode node) {
			return node.getParent();
		}

		@Override
		public List<StringTreeNode> getChildrenList(StringTreeNode node) {
			return node.getChildren();
		}
	};

	{
		root.addChildren(n1, n2);
	}

	@Test
	public void testGetRoot() throws Exception {
		assertEquals(root, tree.getRoot());
	}

	@Test
	public void testIsLeaf() throws Exception {
		assertEquals(false, tree.isLeaf(root));
		assertEquals(true, tree.isLeaf(n1));
		assertEquals(true, tree.isLeaf(n2));
	}

	@Test
	public void testGetChildrenList() throws Exception {
		List<StringTreeNode> children = tree.getChildrenList(root);

		assertEquals(2, children.size());
		assertEquals(n1, children.get(0));
		assertEquals(n2, children.get(1));
	}

	@Test
	public void testGetChildrenList_leaf() throws Exception {
		List<StringTreeNode> children = tree.getChildrenList(n1);

		assertEquals(0, children.size());
	}

	@Test
	public void testIsChildOfParent() throws Exception {
		assertEquals(false, tree.isChildOfParent(root, n1));
		assertEquals(true, tree.isChildOfParent(n1, root));
		assertEquals(true, tree.isChildOfParent(n2, root));
		assertEquals(false, tree.isChildOfParent(n1, n2));
	}

	@Test
	public void testGetChildren() throws Exception {
		Iterable<StringTreeNode> children = tree.getChildren(root);
		Iterator<StringTreeNode> iter = children.iterator();

		assertEquals(n1, iter.next());
		assertEquals(n2, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testGetChildren_leaf() throws Exception {
		// getChildren is not required to handle leaf nodes, so it may throw an
		// exception here. If it does not throw an exception it must return an
		// empty iterator.
		try {
			Iterable<StringTreeNode> iter = tree.getChildren(n1);
			assertFalse(iter.iterator().hasNext());

		} catch (Exception ex) {
			// OK to fail (see above)
		}
	}

	@Test
	public void testGetChildrenReverse() throws Exception {
		Iterable<StringTreeNode> children = tree.getChildrenReverse(root);
		Iterator<StringTreeNode> iter = children.iterator();

		assertEquals(n2, iter.next());
		assertEquals(n1, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	public void testGetChildrenReverse_leaf() throws Exception {
		// getChildrenReverse is not required to handle leaf nodes, so it may
		// throw an exception here. If it does not throw an exception it must
		// return an empty iterator.
		try {
			Iterable<StringTreeNode> iter = tree.getChildrenReverse(n1);
			assertFalse(iter.iterator().hasNext());

		} catch (Exception ex) {
			// OK to fail (see above)
		}
	}

	@Test
	public void testGetFirstChild() throws Exception {
		assertEquals(n1, tree.getFirstChild(root));
	}

	@Test
	public void testGetFirstChild_leaf() throws Exception {
		try {
			tree.getFirstChild(n1);
			fail("exception expected");
		} catch (Exception ex) {
			// any exception will do
		}
	}

	@Test
	public void testGetLastChild() throws Exception {
		assertEquals(n2, tree.getLastChild(root));
	}

	@Test
	public void testGetLastChild_leaf() throws Exception {
		try {
			tree.getLastChild(n1);
			fail("exception expected");
		} catch (Exception ex) {
			// any exception will do
		}
	}

}
