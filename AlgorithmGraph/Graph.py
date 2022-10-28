from Vertex import Vertex
"""
Graph Class
----------

This class represents the Graph modelling our courier network. 

Each Graph consists of the following properties:
    - vertices: A list of vertices comprising the graph

The class also supports the following functions:
    - add_vertex(vertex): Adds the vertex to the graph
    - remove_vertex(vertex): Removes the vertex from the graph
    - add_edge(vertex_A, vertex_B): Adds an edge between the two vertices
    - remove_edge(vertex_A, vertex_B): Removes an edge between the two vertices
    - send_message(s, t): Returns a valid path from s to t containing at most one untrusted vertex
    - check_security(s, t): Returns the set of edges that, if any are removed, would result in any s-t path having to use an untrusted edge
    - compute_minimum_untrusted_vertices(s, t): Returns the minimum number of untrusted vertices that are visited in order to send a message from s to t
    - send_message_with_oversight(s, t): Returns a valid path from s to t where every untrusted vertex is preceded by at least two trusted vertices

"""


class Graph():
    # These are the defined properties as described above
    vertices: 'list[Vertex]'

    def __init__(self) -> None:
        """
        The constructor for the Graph class.
        """
        self.vertices = []

    def add_vertex(self, vertex: Vertex) -> None:
        """
        Adds the given vertex to the graph.
        If the vertex is already in the graph or is invalid, do nothing.
        :param vertex: The vertex to add to the graph.
        """
        if(vertex in self.vertices or not vertex):
            return

        self.vertices.append(vertex)

    def remove_vertex(self, vertex: Vertex) -> None:
        """
        Removes the given vertex from the graph.
        If the vertex is not in the graph or is invalid, do nothing.
        :param vertex: The vertex to remove from the graph.
        """
        if(vertex not in self.vertices or not vertex):
            return

        edges = vertex.get_edges()[:]
        for e in edges:
            self.remove_edge(vertex, e)

        self.vertices.remove(vertex)

    def add_edge(self, vertex_A: Vertex, vertex_B: Vertex) -> None:
        """
        Adds an edge between the two vertices.
        If adding the edge would result in the graph no longer being simple or the vertices are invalid, do nothing.
        :param vertex_A: The first vertex.
        :param vertex_B: The second vertex.
        """
        # Checks are done in the add_edge function of the Vertex class
        if(vertex_A):
            vertex_A.add_edge(vertex_B)

    def remove_edge(self, vertex_A: Vertex, vertex_B: Vertex) -> None:
        """
        Removes an edge between the two vertices.
        If an existing edge does not exist or the vertices are invalid, do nothing.
        :param vertex_A: The first vertex.
        :param vertex_B: The second vertex.
        """
        if(vertex_A):
            vertex_A.remove_edge(vertex_B)

    def send_message(self, s: Vertex, t: Vertex) -> 'list[Vertex]':
        """
        Returns a valid path from s to t containing at most one untrusted vertex.
        Any such path between s and t satisfying the above condition is acceptable.
        Both s and t can be assumed to be unique and trusted vertices.
        If no such path exists, return None.
        :param s: The starting vertex.
        :param t: The ending vertex.
        :return: A valid path from s to t containing at most one untrusted vertex.
        """

        """
        An efficient way to solve this is to mark all nodes reachable without going through
        an untrusted vertex for both s & t. Then, there are 3 cases:
        1. s is directly reachable from t. If this is the case, we directly return the path.
        2. s is not directly reachable from t but there is a path going through an untrusted vertex.
           In this case, we search through the untrusted vertices and see if we can find one reachable
           from both s & t. We should then concatenate the paths ending at the first untrusted vertex 
           reachable from s & t
        3. s is not directly reachable from t and there is no path going through 1 untrusted vertex.
           In this case, we return None.
        """

        def dfs(at: Vertex, seen: 'dict[Vertex, Vertex]') -> None:
            """
            Recursively perform a dfs, marking the preceding Vertex in the seen list
            :param at: The current vertex
            :param seen: The list of vertices seen so far
            """
            if(not at.get_is_trusted()):
                return

            for e in at.get_edges():
                if(e not in seen):
                    seen[e] = at
                    dfs(e, seen)

        def construct_path(seen: 'dict[Vertex, Vertex]', end: Vertex) -> 'list[Vertex]':
            """
            Geneate a path from the end backwards based on the seen list
            :param seen: The list of vertices seen so far
            :return: The path
            """
            path = []
            curr = end
            while(curr != None):
                path.append(curr)
                curr = seen[curr]

            return path

        # Mark all nodes reachable from s
        # K,V pairs where K is a vertex and V is the the vertex preceding it in the dfs (for s)
        seenS: 'dict[Vertex, Vertex]' = {}
        seenS[s] = None
        dfs(s, seenS)

        # If we've made it to t, then we are done
        if(t in seenS):
            return construct_path(seenS, t)[::-1]

        # K,V pairs where K is a vertex and V is the the vertex preceding it in the dfs (for t)
        seenT: 'dict[Vertex, Vertex]' = {}
        seenT[t] = None
        dfs(t, seenT)

        for v in self.vertices:
            if(not v.get_is_trusted() and v in seenS and v in seenT):
                # This is a vertex which is not trusted but is visitable from both s & t.
                # We can now find the path from s to v and from v to t and combine them
                pathS = construct_path(seenS, v)
                pathT = construct_path(seenT, v)
                return pathS[::-1] + pathT[1:]

        # Otherwise no such path exists :(
        return None

    def check_security(self, s: Vertex, t: Vertex) -> 'list[(Vertex, Vertex)]':
        """
        Returns the list of edges as tuples of vertices (v1, v2) such that the removal
        of the edge (v1, v2) means a path between s and t is not possible or must use
        two or more untrusted vertices in a row. v1 and v2 must also satisfy the criteria
        that exactly one of v1 or v2 is trusted and the other untrusted.
        Both s and t can be assumed to be unique and trusted vertices.
        :param s: The starting vertex
        :param t: The ending vertex
        :return: A list of edges which, if removed, means a path from s to t uses an untrusted edge or is no longer possible.
        Note these edges can be returned in any order and are unordered.
        """

        """
        Let us assume that a path between the vertices s and t exists which doesn't use 2+ 
        untrusted vertices in a row. Then, any such edge (v1, v2) where exactly one of v1 or v2 is trusted
        can only lie in the s-t path defined above. So we only need to simulate the removal of trusted-untrusted
        edges that comprise this path.

        If no such path exists, then all edges between a trusted and untrusted vertex are in fact part
        of the resultant edgelist.
        """

        def dfs(at: Vertex, seen: 'dict[Vertex, Vertex]', v1: Vertex, v2: Vertex) -> None:
            """
            Recursively perform a dfs, making sure to not take the edge going from v1 to v2
            :param at: The current vertex
            :param seen: The set of vertices seen so far
            """
            for e in at.get_edges():
                if(e not in seen and
                   (at.get_is_trusted() or e.get_is_trusted()) and
                        not (at == v1 and e == v2) and
                        not (at == v2 and e == v1)):
                    # If we haven't seen the new vertex, the edge between them is not untrusted and they're not the "removed" edge, then dfs
                    seen[e] = at
                    dfs(e, seen, v1, v2)

        def construct_path(seen: 'dict[Vertex, Vertex]', end: Vertex) -> 'list[Vertex]':
            """
            Generate a path from the end backwards based on the seen list
            :param seen: The list of vertices seen so far
            :return: The path
            """
            path = []
            curr = end
            while(curr != None):
                path.append(curr)
                curr = seen[curr]

            return path

        edgelist: 'list[(Vertex, Vertex)]' = []  # Resultant list of edges
        seen: 'dict[Vertex, Vertex]' = {}  # Set of vertices seen so far
        seen[s] = None
        dfs(s, seen, None, None)

        if(t in seen):
            # An s-t path is possible so let's simulate removing trusted-untrusted vertices to see if we can find a path still
            path = construct_path(seen, t)[::-1]
            for i in range(1, len(path)):
                if((path[i].get_is_trusted() and not path[i-1].get_is_trusted()) or
                        (not path[i].get_is_trusted() and path[i-1].get_is_trusted())):
                    # We found a trusted-untrusted edge, so is a path still possible without it?
                    seen = {}
                    dfs(s, seen, path[i], path[i-1])
                    if(t not in seen):
                        edgelist.append((path[i-1], path[i]))
        else:
            # In this case, an s-t path does not exit and so all trusted and untrusted vertices should be reported
            for v in self.vertices:
                for e in v.get_edges():
                    if(((v.get_is_trusted() and not e.get_is_trusted()) or
                        (not v.get_is_trusted() and e.get_is_trusted())) and
                        (v, e) not in edgelist and
                            (e, v) not in edgelist):
                        edgelist.append((v, e))

        return edgelist