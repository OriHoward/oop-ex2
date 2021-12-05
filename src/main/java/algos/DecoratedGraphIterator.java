package algos;

import java.util.Iterator;

public class DecoratedGraphIterator<T> implements Iterator<T> {

    DirectedGraph graph;
    int currIndex;
    int MCount;
    Iterator<T> graphIter;

    public DecoratedGraphIterator(Iterator<T> graphIter, DirectedGraph graph) {
        currIndex = 0;
        this.graph = graph;
        this.MCount = graph.getMC();
        this.graphIter = graphIter;
    }

    @Override
    public boolean hasNext() {
        this.graph.hasChanged(this.MCount);
        return graphIter.hasNext();
    }

    @Override
    public T next() {
        this.graph.hasChanged(this.MCount);
        return graphIter.next();
    }
}
