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
		
		
		int bitrate = 100;
		int packetSize = 10000;
		int tTrans = packetSize/bitrate;
		int timeOut = 10000;
		double probability = 0.001;
		
		ArrayList<String> str_array = new ArrayList<String>();						//Array of buffers (1 for each node)
		Graph graph = new SingleGraph("test");
		
	    Generator gen = new LobsterGenerator();										//Changeable Generator
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<1000; i++) {
	            gen.nextEvents();													//Creating nodes
	    }
	    gen.end();
	    graph.display(true);
	    int nodeCount = graph.getNodeCount();
	    	    
	    for(int i = 0 ; i < nodeCount ; i++) {							//Creating the Array of Buffers (each node has one buffer of it's own)
	    	str_array.add(i, "");													
	    }
	    
	    List<Thread> list_of_nodes = new ArrayList<Thread>();						//Array of threads (1 for each node) 
	    	    
	    for(int i=0 ; i < nodeCount ; i++) {	
	    	list_of_nodes.add(i,new gossip_thread(graph.getNode(i),str_array,probability,tTrans, timeOut));		//Assigning a thread for each node 
	    	list_of_nodes.get(i).start();												//Run void run() of the thread		
	    }
	    
	    int num_over=0;
	    boolean over_list[];
	    over_list = new boolean[nodeCount];
	    
	    while(num_over != nodeCount) {
		    for(int i=0 ; i < nodeCount ; i++) {
			    if(!list_of_nodes.get(i).isAlive() && over_list[i]!=true) {
			    	num_over++;
			    	over_list[i] = true;
			    	System.out.println(num_over);
			    };
		    }
	    }

	    
	    for(int i=0 ; i < nodeCount ; i++) {					
	    	list_of_nodes.get(i).join();											//Wait for all threads to finish
	    }	    
	    
	    int count = 0;
	    System.out.println("DONE!\n");
	    for(int i=0 ; i < nodeCount ; i++) {								// Test if every node has the message
	    	if(str_array.get(i).contains("work")) {
			    count++;
	    	}
	    	else {
				graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");				//Painting red node that has been disseminated with information
	    	}
	    }
	    
	    String format = "%-40s%d%n";
	    System.out.printf(format,"Total nodes",nodeCount);
	    System.out.printf(format,"Disseminated nodes",count);
	    System.out.printf(format,"Transmission time",tTrans);
	    System.out.printf(format,"Bitrate",bitrate);
	    System.out.printf(format,"Pakcet Size",packetSize);	    
	    
	    
	    
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