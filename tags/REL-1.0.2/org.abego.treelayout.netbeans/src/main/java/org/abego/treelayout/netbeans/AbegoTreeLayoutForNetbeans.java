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
package org.abego.treelayout.netbeans;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abego.treelayout.Configuration;
import org.abego.treelayout.Configuration.AlignmentInLevel;
import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.UniversalGraph;
import org.netbeans.api.visual.widget.Widget;

/**
 * This GraphLayout implementation uses the <a
 * href="http://treelayout.abego.org/">abego TreeLayout library</a> to create a
 * more compact tree layout than provided by the standard {@link GraphLayout}
 * code.
 * <p>
 * E.g.
 * <table border="1">
 * <tr>
 * <th><b>AbegoTreeLayoutForNetbeans</b></th>
 * <th><b>Default NetBeans Layout</b></th>
 * </tr>
 * <tr>
 * <td><img src="doc-files/compact-layout.png"></td>
 * <td><img src="doc-files/netbeans-layout.png"></td>
 * </tr>
 * </table>
 * <p>
 * <h2>Usage</h2>
 * Assume you already have added the tree elements (nodes and edges) to a
 * GraphScene <tt>scene</tt> and the root node is stored in <tt>root</tt>. Then
 * you may use code similar to the following to layout the tree and display it
 * in a dialog:
 * 
 * <pre>
 * // layout the tree
 * AbegoTreeLayoutForNetbeans graphLayout = new AbegoTreeLayoutForNetbeans(root,
 * 		100, 100, 50, 50, true);
 * SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene,
 * 		graphLayout);
 * sceneLayout.invokeLayoutImmediately();
 * 
 * // display the tree in a dialog
 * JScrollPane panel = new JScrollPane(scene.createView());
 * JDialog dialog = new JDialog();
 * dialog.setModal(true);
 * dialog.setTitle(title);
 * dialog.add(panel, BorderLayout.CENTER);
 * dialog.setSize(800, 600);
 * dialog.setVisible(true);
 * dialog.dispose();
 * </pre>
 * 
 * <h2>Documentation</h2>
 * For details, especially on the Configuration parameter, see the abego
 * TreeLayout documentation.
 * <h2>Examples</h2> In the "demo" package you will find an example using the
 * AbegoTreeLayoutForNetbeans.
 * <p>
 * @author Udo Borkowski (ub@abego.org)
 * 
 * @param <N>
 *            the node type
 * @param <E>
 *            the edge type
 */
public class AbegoTreeLayoutForNetbeans<N, E> extends GraphLayout<N, E> {

	private TreeLayout<N> treeLayout;
	private Configuration<N> configuration;
	private int originX;
	private int originY;
	private N rootNode;

	private class MyNodeExtentProvider implements NodeExtentProvider<N> {

		private UniversalGraph<N, E> graph;

		private Rectangle getBounds(N node) {
			Widget widget = graph.getScene().findWidget(node);
			widget.getLayout().layout(widget);
			return widget.getPreferredBounds();
		}

		public MyNodeExtentProvider(UniversalGraph<N, E> graph) {
			this.graph = graph;
		}

		public double getHeight(N treeNode) {
			return getBounds(treeNode).getHeight();
		}

		public double getWidth(N treeNode) {
			return getBounds(treeNode).getWidth();
		}
	};

	private class MyTreeForTreeLayout extends AbstractTreeForTreeLayout<N> {
		private Map<N, List<N>> childrenNodes = new HashMap<N, List<N>>();
		private Map<N, N> parents = new HashMap<N, N>();

		private UniversalGraph<N, E> graph;

		private N calcParent(N node) {
			Collection<E> edges = graph.findNodeEdges(node, false, true);
			int n = edges.size();
			if (n > 1) {
				throw new RuntimeException("node has more than one parent");
			}
			N parent = n == 0 ? null : graph.getEdgeSource(edges.iterator()
					.next());
			return parent;
		}

		private List<N> calcChildrenList(N parentNode) {
			List<N> children;
			Collection<E> edges = graph.findNodeEdges(parentNode, true, false);
			children = new ArrayList<N>(edges.size());
			for (E edge : edges) {
				children.add(graph.getEdgeTarget(edge));
			}
			return children;
		}

		public N getParent(N node) {
			if (parents.containsKey(node)) {
				return parents.get(node);
			}

			N parent = calcParent(node);

			parents.put(node, parent);
			return parent;
		}

		public List<N> getChildrenList(N parentNode) {
			List<N> children = childrenNodes.get(parentNode);

			if (children == null) {
				children = calcChildrenList(parentNode);
				childrenNodes.put(parentNode, children);
			}
			return children;
		}

		public MyTreeForTreeLayout(N root, UniversalGraph<N, E> graph) {
			super(root);
			this.graph = graph;
		}
	}

	@Override
	protected void performGraphLayout(UniversalGraph<N, E> graph) {
		if (!graph.getNodes().contains(rootNode)) {
			throw new IllegalArgumentException(
					"graph does not contain rootNode");
		}

		TreeLayout<N> layout = new TreeLayout<N>(new MyTreeForTreeLayout(
				rootNode, graph), new MyNodeExtentProvider(graph),
				configuration);
		Map<N, Rectangle2D.Double> bounds = layout.getNodeBounds();
		for (Map.Entry<N, Rectangle2D.Double> entry : bounds.entrySet()) {
			Rectangle2D.Double rect = entry.getValue();
			Point pt = new Point((int) Math.round(rect.getX() + originX),
					(int) Math.round(rect.getY() + originY));
			setResolvedNodeLocation(graph, entry.getKey(), pt);
		}
	}

	@Override
	protected void performNodesLayout(UniversalGraph<N, E> universalGraph,
			Collection<N> nodes) {
		throw new UnsupportedOperationException(
				"Cannot layout a subsets of nodes of a tree");
	}

	public AbegoTreeLayoutForNetbeans(N rootNode, int originX, int originY,
			Configuration<N> configuration) {
		this.rootNode = rootNode;
		this.originX = originX;
		this.originY = originY;
		this.configuration = configuration;
	}

	public AbegoTreeLayoutForNetbeans(N rootNode, int originX, int originY,
			int gapBetweenLevels, int gapBetweenNodes, boolean vertical) {
		this(rootNode, originX, originY, new DefaultConfiguration<N>(
				gapBetweenLevels, gapBetweenNodes, vertical ? Location.Top
						: Location.Left, AlignmentInLevel.TowardsRoot));
	}

	public N getRootNode() {
		return rootNode;
	}

	public Configuration<N> getConfiguration() {
		return this.configuration;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}
}
