package RunAlgos;

import algos.DirectedGraphAlgorithms;
import api.DirectedWeightedGraphAlgorithms;

public class Main {
    public static void main(String[] args) {

        DirectedWeightedGraphAlgorithms algos = new DirectedGraphAlgorithms();
        algos.load("data/G1.json");

        algos.save("data/outGraph");

//        GraphGUI.main(args);
    }

}



