from Node import Node
from Tree import Tree

import unittest


def assert_equal(got, expected, msg):
    """
    Simple assert helper
    """
    assert expected == got, \
        "[{}] Expected: {}, got: {}".format(msg, expected, got)


class SampleTreeTestCases(unittest.TestCase):
    """
    Testing functionality of the Tree class
    """

    def setUp(self):
        """
        Set up the tree to be used throughout the test
        This is the tree given in the sample
        A(5)
        /   \
        C(2) D(8)
        /
        B(10)
        """

        self.A = Node(5)
        self.tree = Tree(self.A)

        self.B = Node(10)
        self.C = Node(2)
        self.D = Node(8)

        self.tree.put(self.A, self.C, True)
        self.tree.put(self.A, self.D, False)
        self.tree.put(self.C, self.B, True)

    def test_construction(self):
        """
        Test that the sample tree has been correctly constructed
        """
        assert_equal(self.A.is_external(), False, "A is not external")
        assert_equal(self.B.is_external(), True, "B is external")
        assert_equal(self.C.is_external(), False, "C is not external")
        assert_equal(self.D.is_external(), True, "D is external")

        assert_equal(self.A.get_imbalance(), 4, "A has an imbalance of 4")
        assert_equal(self.C.get_imbalance(), 10, "C has an imbalance of 10")
        assert_equal(self.D.get_imbalance(), 0, "D has no imbalance")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")

    def test_put(self):
        """
        Test that the sample tree puts nodes correctly
        """
        E = Node(7)
        self.tree.put(self.C, E, False)

        """
            A(5)
            /   \
            C(2) D(8)
            /  \
          B(10) E(7)
        """
        assert_equal(self.A.is_external(), False, "A is not external")
        assert_equal(self.B.is_external(), True, "B is external")
        assert_equal(self.C.is_external(), False, "C is not external")
        assert_equal(self.D.is_external(), True, "D is external")
        assert_equal(E.is_external(), True, "E is external")

        assert_equal(self.A.get_imbalance(), 11, "A has an imbalance of 11")
        assert_equal(self.C.get_imbalance(), 3, "C has an imbalance of 3")
        assert_equal(self.D.get_imbalance(), 0, "D has no imbalance")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")
        assert_equal(E.get_imbalance(), 0, "E has no imbalance")

    def test_find_max_imbalance(self):
        """
        Test that the sample tree finds the correct node with the maximum imbalance
        """
        assert_equal(self.tree.find_max_imbalance(), 10,
                     "C has the maximum imbalance with value 10")
