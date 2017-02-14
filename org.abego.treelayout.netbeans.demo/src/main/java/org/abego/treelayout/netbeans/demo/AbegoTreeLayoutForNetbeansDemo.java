/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.abego.treelayout.netbeans.demo;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import org.abego.treelayout.netbeans.AbegoTreeLayoutForNetbeans;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutSupport;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * 
 * @author parrt
 * @author Udo Borkowski (ub@abego.org)
 */
public class AbegoTreeLayoutForNetbeansDemo {

	public static class TreeScene extends GraphScene<String, String> {

		private LayerWidget mainLayer;
		private LayerWidget connectionLayer;

		public TreeScene(@SuppressWarnings("unused") String root) {
			mainLayer = new LayerWidget(this);
			connectionLayer = new LayerWidget(this);
			addChild(mainLayer);
			addChild(connectionLayer);
		}

		public void addEdge(String a, String b) {
			addEdge(a + "->" + b);
			setEdgeSource(a + "->" + b, a);
			setEdgeTarget(a + "->" + b, b);
		}

		@Override
		protected Widget attachNodeWidget(String n) {
			LabelWidget w = new LabelWidget(this);
			w.setLabel(" " + n + " ");
			mainLayer.addChild(w);
			return w;
		}

		@Override
		protected Widget attachEdgeWidget(String e) {
			ConnectionWidget connectionWidget = new ConnectionWidget(this);
			connectionWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
			connectionLayer.addChild(connectionWidget);
			return connectionWidget;
		}

		protected void attachEdgeSourceAnchor(String edge,
				String oldSourceNode, String sourceNode) {
			ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
			Widget sourceNodeWidget = findWidget(sourceNode);
			Anchor sourceAnchor = AnchorFactory
					.createRectangularAnchor(sourceNodeWidget);
			edgeWidget.setSourceAnchor(sourceAnchor);
		}

		protected void attachEdgeTargetAnchor(String edge,
				String oldTargetNode, String targetNode) {
			ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
			Widget targetNodeWidget = findWidget(targetNode);
			Anchor targetAnchor = AnchorFactory
					.createRectangularAnchor(targetNodeWidget);
			edgeWidget.setTargetAnchor(targetAnchor);
		}
	}

	public static void main(String[] args) throws Exception {
		TreeScene scene = createScene();
		layoutScene(scene, "r");
		showScene(scene, "Using abego TreeLayout");

		scene = createScene();
		layoutScene_NetbeansStyle(scene, "r");
		showScene(scene, "Using NetBeans GraphLayout");
	}

	private static void showScene(TreeScene scene, String title) {
		JScrollPane panel = new JScrollPane(scene.createView());
		JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setTitle(title);
		dialog.add(panel, BorderLayout.CENTER);
		dialog.setSize(800, 600);
		dialog.setVisible(true);
		dialog.dispose();
	}

	private static void layoutScene(GraphScene<String, String> scene,
			String root) {
		AbegoTreeLayoutForNetbeans<String, String> graphLayout = new AbegoTreeLayoutForNetbeans<String, String>(
				root, 100, 100, 50, 50, true);
		SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene,
				graphLayout);
		sceneLayout.invokeLayoutImmediately();
	}

	private static void layoutScene_NetbeansStyle(
			GraphScene<String, String> scene, String root) {
		GraphLayout<String, String> graphLayout = GraphLayoutFactory
				.createTreeGraphLayout(100, 100, 50, 50, true);
		GraphLayoutSupport.setTreeGraphLayoutRootNode(graphLayout, root);
		SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene,
				graphLayout);
		sceneLayout.invokeLayoutImmediately();
	}

	private static TreeScene createScene() {
		TreeScene scene = new TreeScene("r");

		scene.addNode("r");
		scene.addNode("a");
		scene.addNode("b");
		scene.addNode("c");
		scene.addNode("d");
		scene.addNode("e");
		scene.addNode("f");
		scene.addNode("g");
		scene.addNode("h");

		scene.addEdge("r", "a");
		scene.addEdge("r", "b");
		scene.addEdge("r", "c");

		scene.addEdge("c", "d");
		scene.addEdge("c", "e");
		scene.addEdge("c", "f");
		scene.addEdge("c", "g");
		scene.addEdge("c", "h");
		return scene;
	}
}
