package algos;

public class generateGraph {

    DirectedGraphAlgorithms algos = new DirectedGraphAlgorithms();
    DirectedGraph graph = new DirectedGraph();
    NodeLocation pos;
    GraphNode node;
    final int bigSize = 20000000;

    public generateGraph() {
        algos.init(graph);
    }

    public void generate(int numOfNodes) {
        int currSize = graph.getNodeMap().size();
        if (currSize == 0) {
            generateTen();
        }
        else {
            for (int i = currSize; i <numOfNodes; i++) {
                addRandomNodes(i);
            }
            for (int i = currSize -1; i < numOfNodes-1; i++) {
                connectCycle(i);
            }
            for (int i = 0; i <bigSize; i++) {
                if (numOfNodes <1000000) {
                    if (algos.getGraph().edgeSize() >numOfNodes*20 -1) {
                        break;
                    }
                }
                else {
                    if (algos.getGraph().edgeSize() > numOfNodes*5 -1){
                        break;
                    }
                }
                algos.getGraph().connect((int)(Math.random()*(numOfNodes)),(int)(Math.random()*(numOfNodes)),(Math.random())*10);
            }
            if (numOfNodes == 100) algos.save("100Nodes");
            if (numOfNodes == 1000) algos.save("1000Nodes");
            if (numOfNodes == 10000) algos.save("10000Nodes");
            if (numOfNodes == 100000) algos.save("100000Nodes");
            if (numOfNodes == 1000000) algos.save("1000000Nodes");
        }

    }



    private void generateTen() {
        for (int i = 0; i <10; i++) {
            addRandomNodes(i);
        }
        for (int i = 0; i < 9; i++) {
            connectCycle(i);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >99) {
                break;
            }
            algos.getGraph().connect((int)(Math.random()*(10)),(int)(Math.random()*(10)),(Math.random())*10);
        }
        algos.save("10Nodes");
    }

    private void connectCycle(int i) {
        algos.getGraph().connect(i, i +1,Math.random()*10);
        algos.getGraph().connect(i +1, i,Math.random()*10);
    }

    private void addRandomNodes(int i) {
        pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
        node = new GraphNode(pos, i);
        algos.getGraph().addNode(node);
    }

    private void print(generateGraph g) {
        String format = String.format("generated %s nodes \ngenerated %s edges\n",g.graph.nodeSize(),g.graph.edgeSize());
        System.out.println(format);
    }

    public static void main(String[] args) {
        generateGraph g = new generateGraph();
        g.generate(10);
        g.print(g);
        g.generate(100);
        g.print(g);
        g.generate(1000);
        g.print(g);
        g.generate(10000);
        g.print(g);
        g.generate(100000);
        g.print(g);
        g.generate(1000000);
        g.print(g);


    }
}
