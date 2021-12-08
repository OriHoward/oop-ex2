# oop-ex2


## Idea of implementation

we wanted to create a graph the meets all the requirements of the functions in the classes,
and we can run algorithms efficiently on it

The way we chose to represent the graph is:

1. nodeMap - this is a hashmap which contains all the nodes of the graph, the key is the id of the node and the value is the node itself
2. parsedEdges - this is a list of the edges that are in the graph
3. MCount - this is the variable used to track the changes made to the graph

Each node comprises the following:
1. pos - GeoLocation of the graph node
2. id - the id of the node
3. weight - the weight of the node
4. destMap - a hashmap that maps between the nodes that this node can reach and the edges that reach them 
5. sourceMap - a hashmap that maps between a node and the sources that can each is
6. tag - This contains a tag from the Enum mainly used for coloring in the algorithms

Each edge comprises the following:
1. source - the id of the source node
2. dest - the id of the dest node
3. weight - the weight of the edge
   
   


## Class overview

### DecoratedGraphIterator
This is a usage of the Decorator pattern

This is a wrapper class that decorates the Iterators of the graph so that it can behave according to the graph's state.
if the graph changes during the Iteration it will throw an exception.

in order to check if the graph has changed we are using the MCount

### DirectedGraph
This is an implementation of the `DirectedWeightedGraph`

### DirectedGraphAlgorithms

### GraphEdge

### GraphNode

### NodeLocation

### NodeTagEnum

### PosSerializer


