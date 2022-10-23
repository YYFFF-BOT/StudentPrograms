"""
Binary Tree Node
----------

This class represents an individual Node in a Binary Tree.

Each Node consists of the following properties:
    - left_child: Pointer to the left child of the Node
    - right_child: Pointer to the right child of the Node
    - parent: Pointer to the parent of the Node
    - weight: The weight of the Node
    - imbalance: The imbalance of the Node (absolute difference between the weight of the left and right subtree)

The class also supports the following functions:
    - add_left_child(Node): Adds the given Node as the left child
    - add_right_child(Node): Adds the given Node as the right child
    - is_external(): Returns True if the Node is a leaf node
    - update_weight(int): Updates the weight of the Node
    - get_left_child(): Returns the left child of the Node
    - get_right_child(): Returns the right child of the Node
    - get_imbalance(): Returns the imbalance of the Node

Your task is to complete the following functions which are marked by the TODO comment.
Note that your Node should automatically update the imbalance as nodes are added and weights are updated!
You are free to add properties and functions to the class as long as the given signatures remain identical
"""


class Node:
    # These are the defined properties as described above
    left_child: 'Node'
    right_child: 'Node'
    parent: 'Node'
    root: 'Node'
    weight: int
    imbalance: int

    def __init__(self, weight: int) -> None:
        """
        The constructor for the Node class.
        :param weight: The weight of the node.
        """
        self.left_child = None
        self.right_child = None
        self.parent = None
        self.root = None
        self.weight = weight
        self.imbalance = 0

        # TODO Initialize the properties of the node

    def add_left_child(self, node: 'Node') -> None:
        """
        Adds the given node as the left child of the current node.
        Should do nothing if the the current node already has a left child.
        The given node is guaranteed to be new and not a child of any other node.
        :param node: The node to add as the left child.
        """
        if node is not None and self.left_child is None:
            self.left_child = node
            node.parent = self
            self.update_imbalence_all()

        else:
            return
        # TODO Add the given node as the left child of the current node

    def add_right_child(self, node: 'Node') -> None:
        """
        Adds the given node as the right child of the current node.
        Should do nothing if the the current node already has a right child.
        The given node is guaranteed to be new and not a child of any other node.
        :param node: The node to add as the right child.
        """
        if node is not None and self.right_child is None:
            self.right_child = node
            self.get_imbalance()
            node.parent = self
            self.update_imbalence_all()
        else:
            return
        # TODO Add the given node as the right child of the current node

    def is_external(self) -> bool:
        """
        Returns True if the node is a leaf node.
        :return: True if the node is a leaf node.
        """
        return self.left_child is None and self.right_child is None

    def update_weight(self, weight: int) -> None:
        """
        Updates the weight of the current node.
        :param weight: The new weight of the node.
        """
        self.weight = weight
        self.update_imbalence_all()
        # TODO Update the weight of the node

    def get_left_child(self) -> 'Node':
        """
        Returns the left child of the current node.
        :return: The left child of the current node.
        """

        return self.left_child

    def get_right_child(self) -> 'Node':
        """
        Returns the right child of the current node.
        :return: The right child of the current node.
        """

        return self.right_child

    def get_imbalance(self) -> int:
        """
        Returns the imbalance of the current node.
        :return: The imbalance of the current node.
        """
        sum_subtree_left = 0
        sum_subtree_right = 0
        if self.left_child is not None:
            children_left = [self.left_child]
            self.left_child.pre_order_traverse(children_left)
            for i in children_left:
                sum_subtree_left += i.weight

        if self.right_child is not None:
            children_right = [self.right_child]
            self.right_child.pre_order_traverse(children_right)
            for i in children_right:
                sum_subtree_right += i.weight

        self.imbalance = abs(sum_subtree_left - sum_subtree_right)
        return self.imbalance

    def pre_order_traverse(self, ls: list):
        if self.left_child is not None:
            ls.append(self.left_child)
            self.left_child.pre_order_traverse(ls)
        if self.right_child is not None:
            ls.append(self.right_child)
            self.right_child.pre_order_traverse(ls)

    def get_root(self, ls:list):
        if self.parent is None:
            ls.append(self)
            return
        if self.parent.parent is not None:
            ls.append(self.parent)
            self.parent.get_root(ls)
        elif self.parent.parent is None:
            ls.append(self.parent)
            return

    def update_imbalence_all(self):
        children = []
        self.get_root(children)

        root = children[-1]
        children = [root]
        root.pre_order_traverse(children)
        for i in children:
            i.get_imbalance()
"""
     A10 
  B100   C7
     F3     D5
            E4
          
"""
"""A = Node(10)
B = Node(10)
C = Node(10)
D = Node(10)
E = Node(10)
F= Node(10)
A.add_left_child(B)
A.add_right_child(C)
C.add_right_child(D)
D.add_right_child(E)
B.add_right_child(F)

ls = [A]
A.preorder_traverse(ls)
A.update_imbalence_all()
for i in ls:
    print(i.weight,end=" ")
print()
for i in ls:
    print(i.imbalance,end=" ")"""
