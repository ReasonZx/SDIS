import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import static org.graphstream.algorithm.Toolkit.*;

import java.util.ArrayList;

public class test {
	public static void main(String args[]) {
		ArrayList<Double> pos = new ArrayList<Double>();
		Graph graph = new SingleGraph("test");
	    Generator gen = new RandomEuclideanGenerator();
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<50; i++) {
	            gen.nextEvents();
	    }
	    for(double j=0; j<50; j++) {
	    	pos.add(nodePosition(j));
	    }
	    gen.end();
	    graph.display(false);
	    double i=diameter(graph);
	    System.out.println(i);
	}
}
