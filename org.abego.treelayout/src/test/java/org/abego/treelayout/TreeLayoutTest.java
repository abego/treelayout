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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.abego.treelayout.Configuration.AlignmentInLevel;
import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import org.junit.Test;

/**
 * 
 * @author Udo Borkowski (ub@abego.org)
 * 
 * 
 */
public class TreeLayoutTest {
	private static StringTreeNode newTree(String text,
			StringTreeNode... children) {
		StringTreeNode root = new StringTreeNode(text);
		root.addChildren(children);
		return root;
	}

	private static void appendDescription(StringBuilder sb,
			StringTreeNode node,
			Map<StringTreeNode, Rectangle2D.Double> nodeBounds) {
		Rectangle2D rect = nodeBounds.get(node);
		sb.append(String.format("%s @ %d,%d (%dx%d)\n", node.getText(),
				(int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(),
				(int) rect.getHeight()));
		for (StringTreeNode child : node.getChildren()) {
			appendDescription(sb, child, nodeBounds);
		}
	}

	/**
	 * 
	 * @param layout
	 * @param withBounds
	 *            [default=false]
	 * @return
	 */
	public static String toString(TreeLayout<StringTreeNode> layout,
			boolean withBounds) {
		StringBuilder sb = new StringBuilder();
		if (withBounds) {
			Rectangle2D r = layout.getBounds();
			sb.append(String.format("bounds: %f,%f,%f,%f\n", r.getX(),
					r.getY(), r.getWidth(), r.getHeight()));
		}
		appendDescription(sb, layout.getTree().getRoot(),
				layout.getNodeBounds());
		return sb.toString();
	}

	public static String toString(TreeLayout<StringTreeNode> layout) {
		return toString(layout, false);
	}

	public static void dump(StringTreeNode node,
			Map<StringTreeNode, Rectangle2D.Double> nodeBounds, String prefix) {
		Rectangle2D rect = nodeBounds.get(node);
		System.out.println(String.format("%s%s @ %d,%d (%dx%d)", prefix,
				node.getText(), (int) rect.getX(), (int) rect.getY(),
				(int) rect.getWidth(), (int) rect.getHeight()));
		for (StringTreeNode child : node.getChildren()) {
			dump(child, nodeBounds, prefix + "    ");
		}
	}

	/**
	 * Check if toString(actuals) is the expected string.
	 * 
	 * @param expected
	 * @param actual
	 * @param showDialogOnFailure
	 *            [default:false]
	 */
	public static void assertEqualsToString(String expected,
			TreeLayout<StringTreeNode> actual) {
		String actualString = toString(actual);
		assertEquals(expected, actualString);
	}

	public static StringTreeNode createSampleTree_0() {
		final StringTreeNode root = newTree("root", //
				newTree("n1"), //
				newTree("n2"));
		return root;
	}

	public static StringTreeNode createSampleTree_1() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1"), //
						newTree("n1.2")), //
				newTree("n2", //
						newTree("n2.1")));
		return root;
	}

	public static StringTreeNode createSampleTree_2() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1")), //
				newTree("n2", //
						newTree("n2.1"), //
						newTree("n2.2")));
		return root;
	}

	public static StringTreeNode createSampleTree_3() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1"), //
						newTree("n1.2")), //
				newTree("n2", //
						newTree("n2.1"), //
						newTree("n2.2")));
		return root;
	}

	public static StringTreeNode createSampleTree_4() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1"), //
						newTree("n1.2"), //
						newTree("n1.3"), //
						newTree("n1.4"), //
						newTree("n1.5"), //
						newTree("n1.6")), //
				newTree("n2"), //
				newTree("n3", //
						newTree("n3.1")));
		return root;
	}

	public static StringTreeNode createSampleTree_5() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1"), //
						newTree("n1.2")), //
				newTree("n2", //
						newTree("n2.1")), //
				newTree("n3"));
		return root;
	}

	public static StringTreeNode createSampleTree_6() {
		final StringTreeNode root = newTree("root", //
				newTree("n1"), //
				newTree("n4", //
						newTree("n4.1")), //
				newTree("n5"), //
				newTree("n6", //
						newTree("n6.1", //
								newTree("n6.1.1"), //
								newTree("n6.1.2"), //
								newTree("n6.1.3"), //
								newTree("n6.1.4"), //
								newTree("n6.1.5"), //
								newTree("n6.1.6"), //
								newTree("n6.1.7"))));
		return root;
	}

	public static StringTreeNode createSampleTree_7() {
		final StringTreeNode root = newTree("root", //
				newTree("n1"), //
				newTree("n2", //
						newTree("n2.1"), //
						newTree("n2.2"), //
						newTree("n2.3"), //
						newTree("n2.4"), //
						newTree("n2.5")), //
				newTree("n3"), //
				newTree("n4", //
						newTree("n4.1", //
								newTree("n4.1.1"), //
								newTree("n3.1.2"), //
								newTree("n4.1.3"), //
								newTree("n4.1.4"))));
		return root;
	}

	public static StringTreeNode createSampleTree_8() {
		final StringTreeNode root = newTree("root", //
				newTree("n1"), //
				newTree("n2", //
						newTree("n2.1"), //
						newTree("n2.2"), //
						newTree("n2.3"), //
						newTree("n2.4"), //
						newTree("n2.5")), //
				newTree("n3"), //
				newTree("n4", //
						newTree("n4.1", //
								newTree("n4.1.1"), //
								newTree("n4.1.2"), //
								newTree("n4.1.3"), //
								newTree("n4.1.4"))), //
				newTree("n5", //
						newTree("n5.1", //
								newTree("n5.1.1"), //
								newTree("n5.1.2"), //
								newTree("n5.1.3"), //
								newTree("n5.1.4"))));
		return root;
	}

	/**
	 * A sample tree, as depicted in Figure 7 of the paper.
	 * 
	 * @return
	 */
	public static StringTreeNode createSampleTree_Figure7() {
		final StringTreeNode root = newTree("root", //
				newTree("n1", //
						newTree("n1.1"), //
						newTree("n1.2"), //
						newTree("n1.3"), //
						newTree("n1.4"), //
						newTree("n1.5"), //
						newTree("n1.6"), //
						newTree("n1.7", //
								newTree("n1.7.1"))), //
				newTree("n2"), //
				newTree("n3"), //
				newTree("n4", //
						newTree("n4.1")), //
				newTree("n5"), //
				newTree("n6", //
						newTree("n6.1", //
								newTree("n6.1.1"), //
								newTree("n6.1.2"), //
								newTree("n6.1.3"), //
								newTree("n6.1.4"), //
								newTree("n6.1.5"), //
								newTree("n6.1.6"), //
								newTree("n6.1.7"))));
		return root;
	}

	public static StringTreeNode createSampleTree_MultiLineNodes() {
		final StringTreeNode root = newTree("root", //
				newTree("n1\n(first node)"), //
				newTree("n2"), //
				newTree("n3\n(last node)"));
		return root;
	}

	public static StringTreeNode createSampleTree_DuplicateNode() {
		StringTreeNode child = newTree("n1");
		StringTreeNode root = newTree("root");
		root.addChild(child, false);
		root.addChild(child, false);
		return root;
	}

	private static TreeLayout<StringTreeNode> layout(StringTreeNode root,
			Configuration<StringTreeNode> config,
			NodeExtentProvider<StringTreeNode> nodeExtentProvider) {
		StringTreeAsTreeForTreeLayout treeForTreeLayout = new StringTreeAsTreeForTreeLayout(
				root);
		TreeLayout<StringTreeNode> layout = new TreeLayout<StringTreeNode>(
				treeForTreeLayout, nodeExtentProvider, config);
		return layout;
	}

	private static TreeLayout<StringTreeNode> layout(StringTreeNode root,
			Configuration<StringTreeNode> config) {
		return layout(root, config,
				new FixedNodeExtentProvider<StringTreeNode>(60, 20));
	}

	private static TreeLayout<StringTreeNode> layout(StringTreeNode root) {
		return layout(root, new DefaultConfiguration<StringTreeNode>(
				10, 10));
	}

	@Test
	public void test_0() throws Exception {
		assertEqualsToString(
				"root @ 35,0 (60x20)\nn1 @ 0,30 (60x20)\nn2 @ 70,30 (60x20)\n",
				layout(createSampleTree_0()));
	}

	@Test
	public void test_1() throws Exception {
		assertEqualsToString(
				"root @ 87,0 (60x20)\nn1 @ 35,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn2 @ 140,30 (60x20)\nn2.1 @ 140,60 (60x20)\n",
				layout(createSampleTree_1()));
	}

	@Test
	public void test_2() throws Exception {
		assertEqualsToString(
				"root @ 52,0 (60x20)\nn1 @ 0,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn2 @ 105,30 (60x20)\nn2.1 @ 70,60 (60x20)\nn2.2 @ 140,60 (60x20)\n",
				layout(createSampleTree_2()));
	}

	@Test
	public void test_3() throws Exception {
		assertEqualsToString(
				"root @ 105,0 (60x20)\nn1 @ 35,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn2 @ 175,30 (60x20)\nn2.1 @ 140,60 (60x20)\nn2.2 @ 210,60 (60x20)\n",
				layout(createSampleTree_3()));
	}

	@Test
	public void test_4() throws Exception {
		assertEqualsToString(
				"root @ 297,0 (60x20)\nn1 @ 175,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn1.3 @ 140,60 (60x20)\nn1.4 @ 210,60 (60x20)\nn1.5 @ 280,60 (60x20)\nn1.6 @ 350,60 (60x20)\nn2 @ 297,30 (60x20)\nn3 @ 420,30 (60x20)\nn3.1 @ 420,60 (60x20)\n",
				layout(createSampleTree_4()));
	}

	@Test
	public void test_5() throws Exception {
		assertEqualsToString(
				"root @ 122,0 (60x20)\nn1 @ 35,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn2 @ 140,30 (60x20)\nn2.1 @ 140,60 (60x20)\nn3 @ 210,30 (60x20)\n",
				layout(createSampleTree_5()));
	}

	@Test
	public void test_6() throws Exception {
		assertEqualsToString(
				"root @ 105,0 (60x20)\nn1 @ 0,30 (60x20)\nn4 @ 70,30 (60x20)\nn4.1 @ 70,60 (60x20)\nn5 @ 140,30 (60x20)\nn6 @ 210,30 (60x20)\nn6.1 @ 210,60 (60x20)\nn6.1.1 @ 0,90 (60x20)\nn6.1.2 @ 70,90 (60x20)\nn6.1.3 @ 140,90 (60x20)\nn6.1.4 @ 210,90 (60x20)\nn6.1.5 @ 280,90 (60x20)\nn6.1.6 @ 350,90 (60x20)\nn6.1.7 @ 420,90 (60x20)\n",
				layout(createSampleTree_6()));
	}

	@Test
	public void test_7() throws Exception {
		assertEqualsToString(
				"root @ 210,0 (60x20)\nn1 @ 70,30 (60x20)\nn2 @ 140,30 (60x20)\nn2.1 @ 0,60 (60x20)\nn2.2 @ 70,60 (60x20)\nn2.3 @ 140,60 (60x20)\nn2.4 @ 210,60 (60x20)\nn2.5 @ 280,60 (60x20)\nn3 @ 245,30 (60x20)\nn4 @ 350,30 (60x20)\nn4.1 @ 350,60 (60x20)\nn4.1.1 @ 245,90 (60x20)\nn3.1.2 @ 315,90 (60x20)\nn4.1.3 @ 385,90 (60x20)\nn4.1.4 @ 455,90 (60x20)\n",
				layout(createSampleTree_7()));
	}

	@Test
	public void test_8() throws Exception {
		final Map<String, Dimension> sizes = new HashMap<String, Dimension>();
		sizes.put("root", new Dimension(40, 30));
		sizes.put("n1", new Dimension(30, 30));
		sizes.put("n2", new Dimension(30, 30));
		sizes.put("n2.1", new Dimension(40, 30));
		sizes.put("n2.2", new Dimension(40, 30));
		sizes.put("n2.3", new Dimension(40, 30));
		sizes.put("n2.4", new Dimension(40, 30));
		sizes.put("n2.5", new Dimension(40, 30));
		sizes.put("n3", new Dimension(30, 30));
		sizes.put("n4", new Dimension(30, 30));
		sizes.put("n4.1", new Dimension(40, 30));
		sizes.put("n4.1.1", new Dimension(55, 30));
		sizes.put("n4.1.2", new Dimension(55, 30));
		sizes.put("n4.1.3", new Dimension(55, 30));
		sizes.put("n4.1.4", new Dimension(55, 30));
		sizes.put("n5", new Dimension(30, 30));
		sizes.put("n5.1", new Dimension(40, 30));
		sizes.put("n5.1.1", new Dimension(55, 30));
		sizes.put("n5.1.2", new Dimension(55, 30));
		sizes.put("n5.1.3", new Dimension(55, 30));
		sizes.put("n5.1.4", new Dimension(55, 30));

		NodeExtentProvider<StringTreeNode> provider = new NodeExtentProvider<StringTreeNode>() {
			@Override
			public double getWidth(StringTreeNode treeNode) {
				return getSize(sizes, treeNode).getWidth();
			}

			@Override
			public double getHeight(StringTreeNode treeNode) {
				return getSize(sizes, treeNode).getWidth();
			}

			private Dimension getSize(final Map<String, Dimension> sizes,
					StringTreeNode treeNode) {
				String text = treeNode.getText();
				Dimension size = sizes.get(text);
				if (size == null) {
					System.out.println(text);
				}
				return size;
			}
		};

		TreeLayout<StringTreeNode> layout = layout(createSampleTree_8(),
				new DefaultConfiguration<StringTreeNode>(50, 20),
				provider);

		assertEqualsToString(
				"root @ 335,0 (40x40)\nn1 @ 75,90 (30x30)\nn2 @ 125,90 (30x30)\nn2.1 @ 0,170 (40x40)\nn2.2 @ 60,170 (40x40)\nn2.3 @ 120,170 (40x40)\nn2.4 @ 180,170 (40x40)\nn2.5 @ 240,170 (40x40)\nn3 @ 215,90 (30x30)\nn4 @ 305,90 (30x30)\nn4.1 @ 300,170 (40x40)\nn4.1.1 @ 180,260 (55x55)\nn4.1.2 @ 255,260 (55x55)\nn4.1.3 @ 330,260 (55x55)\nn4.1.4 @ 405,260 (55x55)\nn5 @ 605,90 (30x30)\nn5.1 @ 600,170 (40x40)\nn5.1.1 @ 480,260 (55x55)\nn5.1.2 @ 555,260 (55x55)\nn5.1.3 @ 630,260 (55x55)\nn5.1.4 @ 705,260 (55x55)\n",
				layout);
	}

	@Test
	public void test_Figure7() throws Exception {
		assertEqualsToString(
				"root @ 455,0 (60x20)\nn1 @ 210,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn1.3 @ 140,60 (60x20)\nn1.4 @ 210,60 (60x20)\nn1.5 @ 280,60 (60x20)\nn1.6 @ 350,60 (60x20)\nn1.7 @ 420,60 (60x20)\nn1.7.1 @ 420,90 (60x20)\nn2 @ 317,30 (60x20)\nn3 @ 424,30 (60x20)\nn4 @ 532,30 (60x20)\nn4.1 @ 532,60 (60x20)\nn5 @ 616,30 (60x20)\nn6 @ 700,30 (60x20)\nn6.1 @ 700,60 (60x20)\nn6.1.1 @ 490,90 (60x20)\nn6.1.2 @ 560,90 (60x20)\nn6.1.3 @ 630,90 (60x20)\nn6.1.4 @ 700,90 (60x20)\nn6.1.5 @ 770,90 (60x20)\nn6.1.6 @ 840,90 (60x20)\nn6.1.7 @ 910,90 (60x20)\n",
				layout(createSampleTree_Figure7()));
	}

	@Test
	public void test_RootLocation_Top() throws Exception {
		assertEqualsToString(
				"root @ 87,0 (60x20)\nn1 @ 35,30 (60x20)\nn1.1 @ 0,60 (60x20)\nn1.2 @ 70,60 (60x20)\nn2 @ 140,30 (60x20)\nn2.1 @ 140,60 (60x20)\n",
				layout(createSampleTree_1(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Top)));

	}

	@Test
	public void test_RootLocation_Bottom() throws Exception {
		assertEqualsToString(
				"root @ 87,60 (60x20)\nn1 @ 35,30 (60x20)\nn1.1 @ 0,0 (60x20)\nn1.2 @ 70,0 (60x20)\nn2 @ 140,30 (60x20)\nn2.1 @ 140,0 (60x20)\n",
				layout(createSampleTree_1(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Bottom)));
	}

	@Test
	public void test_RootLocation_Left() throws Exception {
		assertEqualsToString(
				"root @ 0,37 (60x20)\nn1 @ 70,15 (60x20)\nn1.1 @ 140,0 (60x20)\nn1.2 @ 140,30 (60x20)\nn2 @ 70,60 (60x20)\nn2.1 @ 140,60 (60x20)\n",
				layout(createSampleTree_1(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Left)));
	}

	@Test
	public void test_RootLocation_Right() throws Exception {
		assertEqualsToString(
				"root @ 140,37 (60x20)\nn1 @ 70,15 (60x20)\nn1.1 @ 0,0 (60x20)\nn1.2 @ 0,30 (60x20)\nn2 @ 70,60 (60x20)\nn2.1 @ 0,60 (60x20)\n",
				layout(createSampleTree_1(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Right)));
	}

	@Test
	public void test_AlignmentInLevel_Center() throws Exception {
		assertEqualsToString(
				"root @ 70,0 (60x20)\nn1\n(first node) @ 0,30 (60x40)\nn2 @ 70,40 (60x20)\nn3\n(last node) @ 140,30 (60x40)\n",
				layout(createSampleTree_MultiLineNodes(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Top, AlignmentInLevel.Center),
						new MultiLineNodeExtentProvider(60, 20, 0)));
	}

	@Test
	public void test_AlignmentInLevel_TowardsRoot() throws Exception {
		assertEqualsToString(
				"root @ 70,0 (60x20)\nn1\n(first node) @ 0,30 (60x40)\nn2 @ 70,30 (60x20)\nn3\n(last node) @ 140,30 (60x40)\n",
				layout(createSampleTree_MultiLineNodes(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Top, AlignmentInLevel.TowardsRoot),
						new MultiLineNodeExtentProvider(60, 20, 0)));
	}

	@Test
	public void test_AlignmentInLevel_AwayFromRoot() throws Exception {
		assertEqualsToString(
				"root @ 70,0 (60x20)\nn1\n(first node) @ 0,30 (60x40)\nn2 @ 70,50 (60x20)\nn3\n(last node) @ 140,30 (60x40)\n",
				layout(createSampleTree_MultiLineNodes(),
						new DefaultConfiguration<StringTreeNode>(10,
								10, Location.Top, AlignmentInLevel.AwayFromRoot),
						new MultiLineNodeExtentProvider(60, 20, 0)));
	}

	@Test
	public void testConstructor() {
		StringTreeNode root = new StringTreeNode("root");
		StringTreeAsTreeForTreeLayout tree = new StringTreeAsTreeForTreeLayout(
				root);
		FixedNodeExtentProvider<StringTreeNode> nodeExtentProvider = new FixedNodeExtentProvider<StringTreeNode>();
		DefaultConfiguration<StringTreeNode> config = new DefaultConfiguration<StringTreeNode>(
				10, 20);

		TreeLayout<StringTreeNode> layout = new TreeLayout<StringTreeNode>(
				tree, nodeExtentProvider, config);

		assertEquals(tree, layout.getTree());
		assertEquals(nodeExtentProvider, layout.getNodeExtentProvider());
		assertEquals(config, layout.getConfiguration());
	}

	@Test
	public void testGetNodeBoundsIsCached() {
		StringTreeNode root = new StringTreeNode("root");
		StringTreeAsTreeForTreeLayout tree = new StringTreeAsTreeForTreeLayout(
				root);
		FixedNodeExtentProvider<StringTreeNode> nodeExtentProvider = new FixedNodeExtentProvider<StringTreeNode>();
		DefaultConfiguration<StringTreeNode> config = new DefaultConfiguration<StringTreeNode>(
				10, 20);

		TreeLayout<StringTreeNode> layout = new TreeLayout<StringTreeNode>(
				tree, nodeExtentProvider, config);

		Map<StringTreeNode, Double> nodeBounds = layout.getNodeBounds();
		assertEquals(nodeBounds, layout.getNodeBounds());
	}

	@Test
	public void testGetSizeOfLevel() {
		StringTreeNode root = createSampleTree_MultiLineNodes();
		StringTreeAsTreeForTreeLayout tree = new StringTreeAsTreeForTreeLayout(
				root);
		NodeExtentProvider<StringTreeNode> nodeExtentProvider = new MultiLineNodeExtentProvider(
				60, 20, 0);
		DefaultConfiguration<StringTreeNode> config = new DefaultConfiguration<StringTreeNode>(
				10, 10);

		TreeLayout<StringTreeNode> layout = new TreeLayout<StringTreeNode>(
				tree, nodeExtentProvider, config);

		assertEquals(2, layout.getLevelCount());
		assertEquals(20.0, layout.getSizeOfLevel(0), 0.0);
		assertEquals(40.0, layout.getSizeOfLevel(1), 0.0);

		// check for out of bounds
		try {
			layout.getSizeOfLevel(-1);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("level must be >= 0", ex.getMessage());
		}

		try {
			layout.getSizeOfLevel(2);
			fail("exception expected");
		} catch (Exception ex) {
			assertEquals("level must be < levelCount", ex.getMessage());
		}
	}

	@Test
	public void testGetBounds() {
		StringTreeNode root = createSampleTree_MultiLineNodes();
		StringTreeAsTreeForTreeLayout tree = new StringTreeAsTreeForTreeLayout(
				root);
		NodeExtentProvider<StringTreeNode> nodeExtentProvider = new MultiLineNodeExtentProvider(
				60, 20, 0);
		DefaultConfiguration<StringTreeNode> config = new DefaultConfiguration<StringTreeNode>(
				10, 10);

		TreeLayout<StringTreeNode> layout = new TreeLayout<StringTreeNode>(
				tree, nodeExtentProvider, config);

		assertEquals(0, layout.getBounds().getX(), 0.0);
		assertEquals(0, layout.getBounds().getY(), 0.0);
		assertEquals(200, layout.getBounds().getWidth(), 0.0);
		assertEquals(70, layout.getBounds().getHeight(), 0.0);
	}
	
	@Test
	public void testCheckTree_fail() {
		StringTreeNode root = createSampleTree_DuplicateNode();
		TreeLayout<StringTreeNode> layout = layout(root);
		try {
			layout.checkTree();
			fail("Exception expected");
		} catch (Exception ex) {
			assertEquals("Node used more than once in tree: n1", ex.getMessage());
		}
	}
	
	@Test
	public void testCheckTree_success() {
		StringTreeNode root = createSampleTree_1();
		TreeLayout<StringTreeNode> layout = layout(root);
		layout.checkTree();
	}
	
	@Test
	public void testDumpTree() {
		StringTreeNode root = createSampleTree_1();
		TreeLayout<StringTreeNode> layout = layout(root);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(out);
		layout.dumpTree(printStream, new TreeLayout.DumpConfiguration("- ",
				true, false));
		String s = out.toString();
		assertEquals(//
				"\"root\" (size: 60.0x20.0)\n" + //
						"- \"n1\" (size: 60.0x20.0)\n" + //
						"- - \"n1.1\" (size: 60.0x20.0)\n" + //
						"- - \"n1.2\" (size: 60.0x20.0)\n" + //
						"- \"n2\" (size: 60.0x20.0)\n" + //
						"- - \"n2.1\" (size: 60.0x20.0)\n", s);
	}
	
	@Test
	public void testBug_CannotHandleNodesWithRedefinedEquals() {
		StringTreeNode root = new StringTreeNode("root",true);
		// add two different nodes that "equal" (because the text is used for
		// "equals" checks) but not identical.
		root.addChild(new StringTreeNode("n1",true));
		root.addChild(new StringTreeNode("n1",true));
		
		// In version 1.0 this failed because "equality" was used when checking
		// nodes in the tree, not identity.
		TreeLayout<StringTreeNode> layout = layout(root, new DefaultConfiguration<StringTreeNode>(
				10, 10));
		assertEqualsToString(
				"root @ 35,0 (60x20)\nn1 @ 0,30 (60x20)\nn1 @ 70,30 (60x20)\n",
				layout);
	}
	
	
}
