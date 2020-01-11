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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class test {
	
	private static long startTime;
	
	public static void main(String args[]) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter("results.txt", "UTF-8");
		int bitrate = 100;
		int packetSize = 10000;
		int tTrans = packetSize/bitrate;
		int timeOut = 10000;
		double probability = 0.5;
		
		ArrayList<String> str_array = new ArrayList<String>();						//Array of buffers (1 for each node)
		Graph graph = new SingleGraph("test");
		
	    Generator gen = new RandomEuclideanGenerator();										//Changeable Generator
	    
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
	    boolean do_once[];
	    do_once = new boolean[11];
	    
	    
		startTime = System.currentTimeMillis();
	    String format = "%-40s%d%s%n";
		
	    while(num_over != nodeCount && (System.currentTimeMillis() - startTime < timeOut-500)) {
		    for(int i=0 ; i < nodeCount ; i++) {
			    if(!list_of_nodes.get(i).isAlive() && !over_list[i]) {
			    	num_over++;
			    	over_list[i] = true;
			    	if((double) num_over / nodeCount > 0.95 && !do_once[10]) {
			    		System.out.printf(format,"0.95 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.printf(format,"0.95 converged in", System.currentTimeMillis() - startTime , " ms");
			    		do_once[10] = true;
			    	}
			    	else if((double) num_over / nodeCount > 0.92 && !do_once[9]) {
			    		System.out.printf(format,"0.92 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.printf(format,"0.92 converged in", System.currentTimeMillis() - startTime , " ms");
			    		do_once[9] = true;
			    	}
			    	else if((double) num_over / nodeCount > 0.9 && !do_once[8]) {
			    		System.out.printf(format,"0.9 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.printf(format,"0.9 converged in", System.currentTimeMillis() - startTime , " ms");
			    		do_once[8] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.8 && !do_once[7]) {
			    		System.out.printf(format,"0.8 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.8 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[7] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.7 && !do_once[6]) {
			    		System.out.printf(format,"0.7 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.7 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[6] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.6 && !do_once[5]) {
			    		System.out.printf(format,"0.6 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.6 converged in", System.currentTimeMillis() - startTime, " ms");

			    		do_once[5] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.5 && !do_once[4]) {
			    		System.out.printf(format,"0.5 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.5 converged in", System.currentTimeMillis() - startTime, " ms");

			    		do_once[4] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.4 && !do_once[3]) {
			    		System.out.printf(format,"0.4 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.4 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[3] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.3 && !do_once[2]) {
			    		System.out.printf(format,"0.3 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.3 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[2] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.2 && !do_once[1]) {
			    		System.out.printf(format,"0.2 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.2 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[1] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.1 && !do_once[0]) {
			    		System.out.printf(format,"0.1 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.printf(format,"0.1 converged in", System.currentTimeMillis() - startTime, " ms");
			    		do_once[0] = true;
			    	}
			    }
		    }
	    }
	    
	    
	    int count = 0;
	    for(int i=0 ; i < nodeCount ; i++) {								// Test if every node has the message
	    	if(str_array.get(i).contains("work")) {
			    count++;
	    	}
	    	else {
				graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");				//Painting red node that has been disseminated with information
	    	}
	    }
	    if(count == nodeCount) {
		    System.out.printf(format,"1 converged in", System.currentTimeMillis() - startTime, " ms");
    		writer.printf(format,"1 converged in", System.currentTimeMillis() - startTime, " ms");
	    }
	    else {
	    	System.out.println("100% convergence not reached");
	    }

	    System.out.println();
	    
	    format = "%-40s%d%n";
	    System.out.printf(format,"Total nodes",nodeCount);
	    System.out.printf(format,"Disseminated nodes",count);
	    System.out.printf(format,"Transmission time",tTrans);
	    System.out.printf(format,"Bitrate",bitrate);
	    System.out.printf(format,"Pakcet Size",packetSize);	
	    
	    writer.println();
	    writer.printf(format,"Total nodes",nodeCount);
	    writer.printf(format,"Disseminated nodes",count);
	    writer.printf(format,"Transmission time",tTrans);
	    writer.printf(format,"Bitrate",bitrate);
	    writer.printf(format,"Pakcet Size",packetSize);
	    
	    writer.close();
	    
	}
}