package algos;

import java.util.Formatter;

public class GenerateNodes {

    DirectedGraphAlgorithms algos = new DirectedGraphAlgorithms();
    DirectedGraph graph = new DirectedGraph();
    NodeLocation pos;
    GraphNode node;
    final int bigSize = 20000000;

    public GenerateNodes() {
        algos.init(graph);
    }

    public void generateTen() {
        for (int i = 0; i <10; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i = 0; i < 9; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >18) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*10,(int)(Math.random())*10,(Math.random())*10);
        }
        algos.save("10Nodes");
    }

    public void generateHundred() {
        int currSize = graph.getNodeMap().size();
        for (int i = currSize; i <100; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i =currSize -1; i < 99; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >190) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*100,(int)(Math.random())*100,(Math.random())*10);
        }
        algos.save("100Nodes");
    }

    public void generateThousand() {
        int currSize = graph.getNodeMap().size();
        for (int i = currSize; i <1000; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i =currSize-1; i < 999; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >1990) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*1000,(int)(Math.random())*1000,(Math.random())*10);
        }
        algos.save("1000Nodes");
    }
    public void generateTenThousand() {
        int currSize = graph.getNodeMap().size();
        for (int i = currSize; i <10000; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i =currSize-1; i < 9999; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >19900) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*10000,(int)(Math.random())*10000,(Math.random())*10);
        }
        algos.save("10000Nodes");

    }
    public void generateHundredThousand() {
        int currSize = graph.getNodeMap().size();
        for (int i = currSize; i <100000; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i =currSize-1; i < 99999; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >199900) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*100000,(int)(Math.random())*100000,(Math.random())*10);
        }
        algos.save("100000Nodes");

    }
    public void generateOneMillion() {
        int currSize = graph.getNodeMap().size();
        for (int i = currSize; i <1000000; i++) {
            pos = new NodeLocation(Math.random()*10,Math.random()*10,0);
            node = new GraphNode(pos,i);
            algos.getGraph().addNode(node);
        }
        for (int i =currSize-1; i < 999999; i++) {
            algos.getGraph().connect(i,i+1,Math.random()*10);
            algos.getGraph().connect(i +1,i,Math.random()*10);
        }
        for (int i = 0; i <bigSize; i++) {
            if (algos.getGraph().edgeSize() >1999900) {
                break;
            }
            algos.getGraph().connect((int)(Math.random())*1000000,(int)(Math.random())*1000000,(Math.random())*10);
        }
        algos.save("1000000Nodes");
    }

    public static void main(String[] args) {
        GenerateNodes generate = new GenerateNodes();

        generate.generateTen();
        String f10 = String.format("generated %s nodes \ngenerated %s edges\n",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f10);

        generate.generateHundred();
        String f100 = String.format("generated %s nodes \ngenerated %s edges\n",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f100);

        generate.generateThousand();
        String f1000 = String.format("generated %s nodes \ngenerated %s edges\n",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f1000);

        generate.generateTenThousand();
        String f10000 = String.format("generated %s nodes \ngenerated %s edges\n",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f10000);

        generate.generateHundredThousand();
        String f100000 = String.format("generated %s nodes \ngenerated %s edges\n",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f100000);

        generate.generateOneMillion();
        String f1000000 = String.format("generated %s nodes \ngenerated %s edges",generate.graph.nodeSize(),generate.graph.edgeSize());
        System.out.println(f1000000);

    }
}
