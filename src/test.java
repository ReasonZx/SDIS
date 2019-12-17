import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import static org.graphstream.algorithm.Toolkit.*;

import java.util.ArrayList;
import java.util.List;

public class test {
	public static void main(String args[]) {
		
		
		Graph graph = new SingleGraph("test");
		
	    Generator gen = new DorogovtsevMendesGenerator();
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<100; i++) {
	            gen.nextEvents();
	    }
	    gen.end();
	    graph.display(true);
	    
	    
	   
	    List<Thread> list_of_nodes = new ArrayList<Thread>();
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	list_of_nodes.add(i,new gossip_thread(graph.getNode(i)));
	    	list_of_nodes.get(i).start();
	    }
	    
	    System.out.println();
	}
}
