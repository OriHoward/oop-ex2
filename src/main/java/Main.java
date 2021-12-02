import api.DirectedWeightedGraphAlgorithms;

public class Main {
    public static void main(String[] args) {

        DirectedWeightedGraphAlgorithms algos = new DirectedGraphAlgorithms();
        algos.load("data/G1.json");
        algos.shortestPath(0,6);
    }
}
