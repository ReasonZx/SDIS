import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import static org.graphstream.algorithm.Toolkit.*;

public class test {
	public static void main(String args[]) {
		Graph graph = new SingleGraph("test");
	    Generator gen = new RandomEuclideanGenerator();
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<50; i++) {
	            gen.nextEvents();
	    }
	    gen.end();
	    graph.display(false);
	    double i=diameter(graph);
	    System.out.print("comprimento = "); System.out.println(i);
	    
	}
}
