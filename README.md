# oop-ex2

We were given an assignment to craete a graph visualizer which allows the user to run algorithms on the graph that we had to implement

![plot](./misc/Screen%20Shot%202021-12-09%20at%2018.02.21.png)

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

The following algorithms were the core of the project:

* Dijkstra - We have implemented the Dijkstra algorithm with the minimum heap
* DFS - When we check whether the graph is connected we use two iterations of DFS, the first one on the original graph which inverses the graph and a 2nd one on the inverse graph

## Class overview

### DecoratedGraphIterator
This is a usage of the Decorator pattern

This is a wrapper class that decorates the Iterators of the graph so that it can behave according to the graph's state.
if the graph changes during the Iteration it will throw an exception.

in order to check if the graph has changed we are using the MCount

### DirectedGraph
This is an implementation of the `DirectedWeightedGraph`.
The details about the design of this class are in the [idea of Implementation section](##ideaOfImplementation)


### DirectedGraphAlgorithms
This class is an implementation of the `DirectedWeightedGraphAlgorithms` interface
contains implementation of Dijkstra,dfs,tsp as they were described above.

### GraphEdge
An object representing an edge in the graph

### GraphNode
An object representing an node in the graph

### NodeLocation
An object representing the location of the node, this is used in the GUI to draw the nodes

### NodeTagEnum
This Enum is used when traversing through the graph (DFS) to tag the nodes that are being traversed

### PosSerializer
A serializer class used to write the data to a json file

## Detailed execution details of the algorithms

### Scenario 1 (1K nodes):
* center: 3.2 sec
* isConnected: 230ms

### Scenario 2 (10K nodes):
* center: 11.3 Min
* isConnected: 1.3 sec

### Scenario 3 (100K nodes):
* center: 3.2 sec
* isConnected: 230ms

### Scenario 3 (1M nodes):

* center: 3.2 sec
* isConnected: 230ms
