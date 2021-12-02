import api.EdgeData;

import java.util.Iterator;

public class DecoratedEdgeIterator implements Iterator<EdgeData> {

    DirectedGraph graph;
    int currIndex;
    int MCount;
    Iterator<EdgeData> edgeIter;

    public DecoratedEdgeIterator(Iterator<EdgeData> edgeIter, DirectedGraph graph) {
        currIndex = 0;
        this.graph = graph;
        this.MCount = graph.getMC();
        this.edgeIter = edgeIter;
    }

    @Override
    public boolean hasNext() {
        this.graph.hasChanged(this.MCount);
        return edgeIter.hasNext();
    }

    @Override
    public EdgeData next() {
        this.graph.hasChanged(this.MCount);
        return edgeIter.next();
    }
}
