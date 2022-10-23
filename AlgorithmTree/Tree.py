from Node import Node

"""
Tree
----------

This class represents the Binary Tree used to model our baby mobile. 

Each Tree consists of the following properties:
    - root: The root of the Tree

The class also supports the following functions:
    - put(node, child, left_child): Adds child to the given node as the left or right child depending on the value of left_child
    - move_subtree(node_a, node_b, left_child): Move node_a to the left or right child of node_b depending on the value of left_child
    - find_max_imbalance(): Finds the node with the maximum imbalance in the tree

Your task is to complete the following functions which are marked by the TODO comment.
Note that your modifications to the structure of the tree should be correctly updated in the underlying Node class!
You are free to add properties and functions to the class as long as the given signatures remain identical.
"""


class Tree:
    # These are the defined properties as described above
    root: Node

    def __init__(self, root: Node = None) -> None:
        """
        The constructor for the Tree class.
        :param root: The root node of the Tree.
        """
        self.root = root

    def put(self, node: Node, child: Node, left_child: bool) -> None:
        """
        Adds the given child to the given node as the left or right child depending on the value of left_child.
        If a node already has a child at the indicated position, this function should do nothing.
        You are guranteed that the given node is not already part of the tree
        :param node: The node to add the child to.
        :param child: The child to add to the node.
        :param left_child: True if the child should be added to the left child, False otherwise.
        """
        if child is None or node is None:
            return

        if left_child:
            if node.left_child is None:
                node.left_child = child
                child.parent = node
        else:
            if node.right_child is None:
                node.right_child = child
                child.parent = node

        self.update_imbalance()
        # TODO Add the child to the node as the left or right child depending on the value of left_child

    def move_subtree(self, node_a: Node, node_b: Node, left_child: bool) -> None:
        """
        Moves the subtree rooted at node_a to the left or right child of node_b depending on the value of left_child.
        If node_b already has a child at the indicated position, this function should do nothing
        You can safely assume that node_b is not descendent of node_a.
        :param node_a: The root of the subtree to move.
        :param node_b: The node to add the subtree to.
        :param left_child: True if the subtree should be added to the left child, False otherwise.
        """
        if node_a is None or node_b is None:
            return
        node_new: Node
        if left_child:
            if node_b.left_child is not None:
                return
            else:
                node_new = node_a
                self.delete_node(node_a)
                node_b.left_child = node_new
                node_new.parent = node_b
        else:
            if node_b.right_child is not None:
                return
            else:
                node_new = node_a
                self.delete_node(node_a)
                node_b.right_child = node_new
                node_new.parent = node_b

        # TODO Move the subtree rooted at node_a to the left or right child of node_b

    def find_max_imbalance(self) -> int:
        """
        Finds the node with the maximum imbalance in the tree.
        :return: The node with the maximum imbalance.
        """
        if tree.root.is_external():
            return 0

        self.update_imbalance()
        ls = [self.root]
        self.root.pre_order_traverse(ls)
        children_imbalance = []
        for i in ls:
            children_imbalance.append(i.get_imbalance())

        return max(children_imbalance)
        # TODO Find the node with the maximum imbalance

    def delete_node(self, node: Node):
        if node.parent.left_child == node:
            node.parent.left_child = None
        elif node.parent.right_child == node:
            node.parent.right_child = None
        self.update_imbalance()

    def update_imbalance(self):
        ls = [self.root]
        self.root.pre_order_traverse(ls)
        for i in ls:
            i.get_imbalance()


"""
The following is the test of this program.
                   a10
            b8             c7
                e5     f4        g3
                     d6   
                  h2   i1      

"""
a = Node(10)
tree = Tree(a)
b = Node(8)
c = Node(7)
d = Node(6)
e = Node(5)
f = Node(4)
g = Node(3)
h = Node(2)
i = Node(1)
tree.put(a, c, False)
tree.put(a, b, True)
tree.put(b, d, True)
tree.put(b, e, False)
tree.put(d, h, True)
tree.put(d, i, False)
tree.put(c, f, True)
tree.put(c, g, False)
tree.move_subtree(d, f, True)
print(f.left_child.weight)
print(b.left_child)
ls = [tree.root]
tree.root.pre_order_traverse(ls)
print("The weight of children")
for i in ls:
    print(i.weight, end=" ")
print("\nThe imbalance of children")
for j in ls:
    print(j.imbalance, end=" ")
print("\nThe max imbalance is", tree.find_max_imbalance())