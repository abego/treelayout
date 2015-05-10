# abego TreeLayout
__Efficient and Customizable Tree Layout Algorithm in Java__

The TreeLayout creates tree layouts for arbitrary trees. It is not restricted to a specific output or format, but can be used for any kind of two dimensional diagram. Examples are Swing based components, SVG files, and many more. This is possible because TreeLayout separates the layout of a tree from the actual rendering.

To use the TreeLayout you mainly need to supply an instance of the TreeLayout class with the nodes of the tree (including "children" links), together with the "size" of each node. In addition you can configure the layout by specifying parameters like "gap between levels" etc..

Based on this information TreeLayout creates a compact, nice looking layout. The layout has the following properties:

1. The layout displays the hierarchical structure of the tree, i.e. the y-coordinate of a node is given by its level.
2. The edges do not cross each other and nodes on the same level have a minimal horizontal distance. 
3. The drawing of a subtree does not depend on its position in the tree, i.e. isomorphic subtrees are drawn identically up to translation. 
4. The order of the children of a node is displayed in the drawing.
5. The algorithm works symmetrically, i.e. the drawing of the reflection of a tree is the reflected drawing of the original tree.

Here an example tree layout:

![Example tree layout](http://treelayout.sourceforge.net/image/TreeGraphView-Figure7.png)

## Documentation and Downloads

For detailed [documentation](http://treelayout.sourceforge.net) and [downloads](https://sourceforge.net/projects/treelayout/files/) see the TreeLayout web site [http://treelayout.sourceforge.net](http://treelayout.sourceforge.net).

