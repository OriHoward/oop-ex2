import api.EdgeData;

import java.util.Iterator;
import java.util.List;

public class EdgeIterator implements Iterator<EdgeData> {

    DirectedGraph graph;
    int currIndex;
    int MCount;
    List<EdgeData> edges;

    public EdgeIterator(DirectedGraph graph) {

        currIndex = 0;
        this.graph = graph;
        this.MCount = graph.getMC();
        this.edges = graph.getParsedEdges();
    }

    @Override
    public boolean hasNext() {
        this.graph.hasChanged(this.MCount);
        return edges.size() > currIndex && edges.get(currIndex) != null;
    }

    @Override
    public EdgeData next() {
        this.graph.hasChanged(this.MCount);
        return edges.get(currIndex++);
    }

}
