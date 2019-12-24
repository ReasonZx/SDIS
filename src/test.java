import org.graphstream.algorithm.generator.BananaTreeGenerator;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.ChainGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class test {
	public static void main(String args[]) throws InterruptedException {
		
		ArrayList<String> str_array = new ArrayList<String>();						//Array of buffers (1 for each node)
		Graph graph = new SingleGraph("test");
		int count = 0;
		
	    Generator gen = new RandomEuclideanGenerator();										//Changeable Generator
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<1000; i++) {
	            gen.nextEvents();													//Creating nodes
	    }
	    gen.end();
	    graph.display(true);
	    
	    	    
	    for(int i = 0 ; i < graph.getNodeCount() ; i++) {							//Creating the Array of Buffers (each node has one buffer of it's own)
	    	str_array.add(i, "");													
	    }
	    
	    List<Thread> list_of_nodes = new ArrayList<Thread>();						//Array of threads (1 for each node) 
	    
	    int nodeCount = graph.getNodeCount();
	    for(int i=0 ; i < nodeCount ; i++) {	
	    	list_of_nodes.add(i,new gossip_thread(graph.getNode(i),str_array,0.3));		//Assigning a thread for each node 
	    	list_of_nodes.get(i).start();											//Run void run() of the thread		
	    }
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {					
	    	list_of_nodes.get(i).join();											//Wait for all threads to finish
	    }	    
	    System.out.println("DONE!");
	    

	    for(int i=0 ; i < nodeCount ; i++) {								// Test if every node has the message
	    	if(str_array.get(i) == "work") {
			    count++;
	    	}
	    }
  	  System.out.println(count +  " vs " + nodeCount );
	    
	    
	    
	    
	    																			//JUST FOR TESTING NEIGHBORS// CODE NOT IMPORTANT //
	    /*
	    Iterator<Node> node_it;
	    Node this_node;
	    
	    for (int i = 0 ; i < graph.getNodeCount() ; i++) {
	    	System.out.print(i + " - ");
		    graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
	    	node_it = graph.getNode(i).getNeighborNodeIterator();
	    	while(node_it.hasNext()) {
		    	this_node = node_it.next();
		    	System.out.print(this_node.getIndex() + " ");
				this_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
	    	}
	    	try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(0,0,0); size: 15px;");
	    	node_it = graph.getNode(i).getNeighborNodeIterator();
		    while(node_it.hasNext()) {
		    	this_node = node_it.next();
		    	System.out.print(this_node.getIndex() + " ");
				this_node.addAttribute("ui.style", "fill-color: rgb(0, 0, 0); size: 15px;");
	    	}
	    	System.out.println();
	    }
	    */

	}
}