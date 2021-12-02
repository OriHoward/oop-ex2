import api.NodeData;

import java.util.Iterator;

public class DecoratedNodeIterator implements Iterator<NodeData> {
    DirectedGraph graph;
    int currIndex;
    int MCount;
    Iterator<NodeData> nodeIter;

    public DecoratedNodeIterator(Iterator<NodeData> edgeIter, DirectedGraph graph) {
        currIndex = 0;
        this.graph = graph;
        this.MCount = graph.getMC();
        this.nodeIter = edgeIter;
    }

    @Override
    public boolean hasNext() {
        this.graph.hasChanged(this.MCount);
        return nodeIter.hasNext();
    }

    @Override
    public NodeData next() {
        this.graph.hasChanged(this.MCount);
        return nodeIter.next();
    }
}
