package RunAlgos;

import GUI.GraphGUI;
import algos.DirectedGraph;
import algos.DirectedGraphAlgorithms;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;

/**
 * This class is the main class for RunAlgos.Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = null;
        ans = new DirectedGraph(json_file);
        return ans;
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = null;
        ans = new DirectedGraphAlgorithms();
        ans.load(json_file);
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        if (alg.getGraph() == null) {
            System.out.println("Bad Json was provided");
        } else {
            GraphGUI.main(new String[]{}, (DirectedGraphAlgorithms) alg);
        }

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("please provide a valid file path");
        } else {
            String givenPath = args[0];
            runGUI(givenPath);
        }
    }
}