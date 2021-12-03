import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        DirectedWeightedGraphAlgorithms algos = new DirectedGraphAlgorithms();
        algos.load("data/G1.json");
        int bla = 0;
        DirectedWeightedGraph asd =algos.getGraph();
        Iterator<EdgeData> iteraa = asd.edgeIter();
        while (iteraa.hasNext()){
            EdgeData edy = iteraa.next();
            if (bla ==3){
                asd.connect(0,4,1.6);
            }
            System.out.println(edy.getWeight());
            bla+=1;
        }

        algos.shortestPath(0,6);
    }
}
