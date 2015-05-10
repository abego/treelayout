# Migrated to SourceForge #

[![](http://treelayout.sourceforge.net/image/moved.png)](http://treelayout.sourceforge.net)

**abego TreeLayout - Efficient and Customizable Tree Layout Algorithm in Java**

# Overview #

The TreeLayout creates tree layouts for arbitrary trees. It is not restricted to a specific output or format, but can be used for any kind of two dimensional diagram. Examples are Swing based components, SVG files, and many more. This is possible because TreeLayout separates the layout of a tree from the actual rendering.

To use the TreeLayout you mainly need to supply an instance of the TreeLayout class with the nodes of the tree (including "children" links), together with the "size" of each node. In addition you can configure the layout by specifying parameters like "gap between levels" etc..

Based on this information TreeLayout creates a compact, nice looking layout. The layout has the following properties `[2]`:
  1. The layout displays the hierarchical structure of the tree, i.e. the y-coordinate of a node is given by its level.
  1. The edges do not cross each other and nodes on the same level have a minimal horizontal distance.
  1. The drawing of a subtree does not depend on its position in the tree, i.e. isomorphic subtrees are drawn identically up to translation.
  1. The order of the children of a node is displayed in the drawing.
  1. The algorithm works symmetrically, i.e. the drawing of the reflection of a tree is the reflected drawing of the original tree.

Here an example tree layout:

![http://treelayout.googlecode.com/files/TreeGraphView-Figure7.png](http://treelayout.googlecode.com/files/TreeGraphView-Figure7.png)

# Content #


# Download and Installation #

  * **Sources**: http://code.google.com/p/treelayout/source/browse/
  * **JAR files**:
    * http://sourceforge.net/projects/treelayout/files/org.abego.treelayout.core.jar/download: the TreeLayout algorithm core.
    * http://sourceforge.net/projects/treelayout/files/org.abego.treelayout.netbeans.jar/download: use the TreeLayout algorithm for the NetBeans visual API (GraphLayout, ...). _(When using this JAR make sure the NetBeans libraries 'org.netbeans.api.visual', 'org.openide.util', and 'org.openide.util.lookup' are in the classpath.)_
    * for more files (e.g. older or specific versions) see the [download page](https://sourceforge.net/projects/treelayout/files/).
  * **Twitter**: [@abego](http://twitter.com/#!/abego) _(for announcement of new releases)_
TreeLayout is also available from [Maven Central](http://repo1.maven.org/maven2/org/abego/treelayout/). You may want to add this to your POM:
```
    <dependencies>
        ...
        <dependency>
            <groupId>org.abego.treelayout</groupId>
            <artifactId>org.abego.treelayout.core</artifactId>
            <version>1.0.2</version>
        </dependency>
        ...
    </dependencies>
```

# Development #

You may check out the TreeLayout source code from the [TreeLayout SVN repository](http://code.google.com/p/treelayout/source/checkout).

The projects contain configurations for the Eclipse and NetBeans IDEs as well as ANT scripts to build them. Maven is also supported.

# NetBeans and abego TreeLayout #

The NetBeans Visual API already includes an algorithm to layout trees (and graphs in general). However the default implementation of NetBeans does not always generate layouts as compact as the abego TreeLayout implementation:
| **Layout using abego TreeLayout** | **Layout using NetBeans' default  Tree GraphLayout** |
|:----------------------------------|:-----------------------------------------------------|
| ![http://treelayout.googlecode.com/files/compact-layout.png](http://treelayout.googlecode.com/files/compact-layout.png) | ![http://treelayout.googlecode.com/files/netbeans-layout.png](http://treelayout.googlecode.com/files/netbeans-layout.png) |

Both screenshots are taken from the demo application in the "org.abego.treelayout.netbeans.demo" project, also provided in the sources. The demo project includes the "org.abego.treelayout.netbeans" library to easily access the abego TreeLayout algorithm from standard NetBeans visual code.

As the following comparision shows using the abego TreeLayout in NetBeans visual code is as simple as using the standard GraphLayout:

**Use abego TreeLayout to create a SceneLayout**:
```
AbegoTreeLayoutForNetbeans<N, E> graphLayout = 
        new AbegoTreeLayoutForNetbeans<N, E>(root, 100, 100, 50, 50, true);
SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene,graphLayout);
...
```

**Use NetBeans' default Tree GraphLayout to create a SceneLayout**:
```
GraphLayout<N, E> graphLayout = 
        GraphLayoutFactory.createTreeGraphLayout(100, 100, 50, 50, true);
GraphLayoutSupport.setTreeGraphLayoutRootNode(graphLayout, root);
SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene, graphLayout);
...
```

# Documentation #

For detailed documentation see the javaDoc in the source code and this [pdf document](http://treelayout.googlecode.com/files/abegoTreeLayout.pdf).

In addition you may also find the example code helpful. It is included in the "demo" packages.

## Algorithm Performance ##

Based on Walker's algorithm `[1]` with enhancements suggested by Buchheim,  Jünger, and Leipert `[2]` the software builds tree layouts in linear time. I.e. even trees with many nodes are built fast. Other than with the Reingold–Tilford algorithm `[3]` one is not limited to binary trees.

The following figure shows the results running the TreeLayout algorithm on a MacBook Pro 2.4 GHz Intel Core 2 Duo (2 GB Memory (-Xmx2000m)). The variously sized trees were created randomly.

![http://treelayout.googlecode.com/files/performance.png](http://treelayout.googlecode.com/files/performance.png)

The picture illustrates the linear time behavior of the algorithm and shows the applicability also for large number of nodes. In this setting it takes approx. 5 μs to place one node.

## Known Usages ##
  * [Actuate](http://www.actuate.com) is using abego TreeLayout in some of their products to help provide personalized analytics and insights.
  * Terence Parr uses abego TreeLayout in [ANTLR 4](http://www.antlr.org/) to visualize parse trees.
  * The [Attack-Defense Tree Tool (ADTool)](http://satoss.uni.lu/members/piotr/adtool/) displays attack-defense trees using abego TreeLayout. ADTool allows users to model and analyze attack-defense scenarios represented with attack-defense trees and attack-defense terms.
  * In his article "[Zeichnen von Bäumen beliebigen Grades mithilfe der abego TreeLayout-Bibliothek und dem MVC-Design Pattern.](http://www.it-cow.de/post/Zeichnen-von-Baumen-beliebigen-Grades-mithilfe-der-abego-TreeLayout-Bibliothek-und-dem-MVC-Design-Pattern.aspx)" Thomas Kramer describes how he uses abego TreeLayout together with the Model-View-Controller pattern to create his application (sources included).
  * Martin Skurla integrated abego TreeLayout in his [AST visualizer](https://bitbucket.org/crazyjavahacking) and wrote a nice article about it ([The story of javac AST Visualization on NetBeans Platform](http://crazyjavahacking.org/the-story-of-javac-ast-visualization-on-netbeans-platform/)).

_(If you are using abego TreeLayout in your application please let me know. If you like I will also add you to this list.)_

## History ##

For a history see the [Changes](http://code.google.com/p/treelayout/source/browse/trunk/org.abego.treelayout/CHANGES.txt) file.

New release of TreeLayout will be announced on Twitter [@abego](http://twitter.com/#!/abego).

## License ##

abego TreeLayout is distributed under a BSD license of [abego Software](http://www.abego-software.de). ([License text](http://treelayout.googlecode.com/files/LICENSE.TXT))

## References ##

`[1]` Walker JQ II. A node-positioning algorithm for general trees. <i>Software—Practice and Experience</i> 1990; <b>20</b>(7):685–705.

`[2]` Buchheim C, Jünger M, Leipert S. Drawing rooted trees in linear time. <i>Software—Practice and Experience</i> 2006; <b>36</b>(6):651–665

`[3]` Reingold EM, Tilford JS. Tidier drawings of trees. <i>IEEE Transactions on Software Engineering</i> 1981; <b>7</b>(2):223–228.