from Node import Node

import unittest


def assert_equal(got, expected, msg):
    """
    Simple assert helper
    """
    assert expected == got, \
        "[{}] Expected: {}, got: {}".format(msg, expected, got)


class SampleNodeTestCases(unittest.TestCase):
    """
    Testing functionality of the Node class
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
        self.B = Node(10)
        self.C = Node(2)
        self.D = Node(8)
        self.C.add_left_child(self.B)
        self.A.add_left_child(self.C)
        self.A.add_right_child(self.D)

    def test_is_external(self):
        """
        Test that the sample tree has been correctly classified
        """
        assert_equal(self.A.is_external(), False, "A is not external")
        assert_equal(self.B.is_external(), True, "B is external")
        assert_equal(self.C.is_external(), False, "C is not external")
        assert_equal(self.D.is_external(), True, "D is external")

    def test_get_left_child(self):
        """
        Test that the sample tree returns the correct left child
        """
        assert_equal(self.A.get_left_child(), self.C, "A's left child is C")
        assert_equal(self.C.get_left_child(), self.B, "C's left child is B")
        assert_equal(self.D.get_left_child(), None, "D has no left child")
        assert_equal(self.B.get_left_child(), None, "B has no left child")

    def test_get_right_child(self):
        """
        Test that the sample tree returns the correct right child
        """
        assert_equal(self.A.get_right_child(), self.D, "A's right child is D")
        assert_equal(self.C.get_right_child(), None, "C has no right child")
        assert_equal(self.D.get_right_child(), None, "D has no right child")
        assert_equal(self.B.get_right_child(), None, "B has no right child")

    def test_get_imbalance(self):
        """
        Test that the sample tree returns the correct imbalance
        """
        assert_equal(self.A.get_imbalance(), 4, "A has an imbalance of 4")
        assert_equal(self.C.get_imbalance(), 10, "C has an imbalance of 10")
        assert_equal(self.D.get_imbalance(), 0, "D has no imbalance")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")

    def test_update_weight(self):
        """
        Test that the sample tree updates the weight correctly
        """
        self.A.update_weight(10)
        assert_equal(self.A.get_imbalance(), 4, "A has an imbalance of 4")
        assert_equal(self.C.get_imbalance(), 10, "C has an imbalance of 10")
        assert_equal(self.D.get_imbalance(), 0, "D has no imbalance")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")

        self.B.update_weight(3)
        assert_equal(self.A.get_imbalance(), 3, "A has an imbalance of 3")
        assert_equal(self.C.get_imbalance(), 3, "C has an imbalance of 3")
        assert_equal(self.D.get_imbalance(), 0, "D has no imbalance")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")

        """
        Final Tree:
        A(10)
        /   \
        C(2) D(8)
        /
        B(3)
        """

    def test_propagate_imbalance(self):
        """
        Test that the sample tree propagates the imbalance correctly when adding children
        """

        self.D.add_right_child(Node(7))
        """
        Tree:
        A(5)
        /   \
        C(2) D(8)
        /      \
        B(10)   E(7)
        """

        assert_equal(self.A.get_imbalance(), 3, "A has an imbalance of 3")
        assert_equal(self.C.get_imbalance(), 10, "C has an imbalance of 10")
        assert_equal(self.D.get_imbalance(), 7, "D has an imbalance of 7")
        assert_equal(self.B.get_imbalance(), 0, "B has no imbalance")
        assert_equal(self.D.get_right_child().get_imbalance(),
                     0, "E has no imbalance")
