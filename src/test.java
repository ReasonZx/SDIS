import org.graphstream.algorithm.Algorithm;
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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class test {
	
	private static long startTime;
	
	public static void main(String args[]) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		String Algoritmo = new String();
		PrintWriter writer = null;
		int bitrate = 100;
		int packetSize = 20000;
		int tTrans = packetSize/bitrate;
		int timeOut = 30000;
		double probability = 0.1;
		Datashare traffic = new Datashare();
		
		System.out.println("Algoritmo? (gossip || push || pull || pushpull)");
		Scanner scan = new Scanner(System.in);
		Algoritmo = scan.next();

		
		if(!Algoritmo.equals("gossip") && !Algoritmo.equals("push") && !Algoritmo.contentEquals("pull") && !Algoritmo.equals("pushpull")) {
			System.out.println("Wrong use");
			System.exit(0);
		}
		
		
	    if(Algoritmo.equals("gossip")) {
	    	writer = new PrintWriter("results_gossip.txt", "UTF-8");
	    	System.out.println("Probability?");
			probability = Double.parseDouble(scan.next());
	    }
	    else if(Algoritmo.equals("push")) {
	    	writer = new PrintWriter("results_push.txt", "UTF-8");
	    }
	    else if(Algoritmo.equals("pull")) {
	    	writer = new PrintWriter("results_pull.txt", "UTF-8");
	    }
	    else if(Algoritmo.equals("pushpull")) {
	    	writer = new PrintWriter("results_pushpull.txt", "UTF-8");
	    }



		

		ArrayList<String> str_array = new ArrayList<String>();
		ArrayList<ReentrantLock> locks = new ArrayList<ReentrantLock>();
		Graph graph = new SingleGraph("test");
		
	    Generator gen = new LobsterGenerator();										
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<1000; i++) {
	            gen.nextEvents();													
	    }
	    gen.end();
	    graph.display(true);
	    int nodeCount = graph.getNodeCount();
	    	    
	    for(int i = 0 ; i < nodeCount ; i++) {							
	    	str_array.add(i, "");
	    	locks.add(i,new ReentrantLock());
	    }
	    
	    List<Thread> list_of_nodes = new ArrayList<Thread>();						
	    	
	    if(Algoritmo.equals("gossip")) {
		    for(int i=0 ; i < nodeCount ; i++) {	
		    	list_of_nodes.add(i,new gossip_thread(graph.getNode(i),str_array,probability,tTrans, timeOut, locks, traffic));	
		    	list_of_nodes.get(i).start();																			
		    }
	    }
	    
	    else if(Algoritmo.equals("push")) {
	    	for(int i=0 ; i < nodeCount ; i++) {	
	    		list_of_nodes.add(i,new push_thread(graph.getNode(i),str_array,tTrans, timeOut, locks, traffic));		
		    	list_of_nodes.get(i).start();
	    	}																			
	    }
	    
	    else if(Algoritmo.equals("pull")) {
		    for(int i=0 ; i < nodeCount ; i++) {	
		    	list_of_nodes.add(i,new pull_thread(graph.getNode(i),str_array,tTrans, timeOut, locks, traffic));		
		    	list_of_nodes.get(i).start();
		    }
	    }
	    
	    if(Algoritmo.equals("pushpull")) {
		    for(int i=0 ; i < nodeCount ; i++) {	
		    	list_of_nodes.add(i,new pushpull_thread(graph.getNode(i),str_array,tTrans, timeOut, locks, traffic));		 
		    	list_of_nodes.get(i).start();
		    }
	    }
	    
	    int num_over=0;
	    boolean over_list[];
	    over_list = new boolean[nodeCount];
	    boolean do_once[];
	    do_once = new boolean[15];
	    
	    
		startTime = System.currentTimeMillis();
	    String format = "%-40s%d%s%n";
		
	    while(num_over != nodeCount && (System.currentTimeMillis() - startTime < timeOut-500)) {
		    for(int i=0 ; i < nodeCount ; i++) {
			    if(str_array.get(i).contains("work") && !over_list[i]) {
			    	num_over++;
			    	over_list[i] = true;
			    	if((double) num_over / nodeCount > 0.95 && !do_once[10]) {
			    		System.out.printf(format,"0.95 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.println("0.95 " + (System.currentTimeMillis() - startTime));
			    		do_once[10] = true;
			    	}
			    	else if((double) num_over / nodeCount > 0.92 && !do_once[9]) {
			    		System.out.printf(format,"0.92 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.println("0.92 " + (System.currentTimeMillis() - startTime));
			    		do_once[9] = true;
			    	}
			    	else if((double) num_over / nodeCount > 0.9 && !do_once[8]) {
			    		System.out.printf(format,"0.9 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.println("0.9 "+ (System.currentTimeMillis() - startTime));
			    		do_once[8] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.8 && !do_once[7]) {
			    		System.out.printf(format,"0.8 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.8 "+ (System.currentTimeMillis() - startTime));
			    		do_once[7] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.7 && !do_once[6]) {
			    		System.out.printf(format,"0.7 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.7 "+ (System.currentTimeMillis() - startTime));
			    		do_once[6] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.6 && !do_once[5]) {
			    		System.out.printf(format,"0.6 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.6 "+ (System.currentTimeMillis() - startTime));

			    		do_once[5] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.5 && !do_once[4]) {
			    		System.out.printf(format,"0.5 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.5 "+ (System.currentTimeMillis() - startTime));

			    		do_once[4] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.4 && !do_once[3]) {
			    		System.out.printf(format,"0.4 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.4 "+ (System.currentTimeMillis() - startTime));
			    		do_once[3] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.3 && !do_once[2]) {
			    		System.out.printf(format,"0.3 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.3 "+ (System.currentTimeMillis() - startTime));
			    		do_once[2] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.2 && !do_once[1]) {
			    		System.out.printf(format,"0.2 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.2 "+ (System.currentTimeMillis() - startTime));
			    		do_once[1] = true;
			    	}
			    	else if((double)num_over / nodeCount > 0.1 && !do_once[0]) {
			    		System.out.printf(format,"0.1 converged in", System.currentTimeMillis() - startTime, " ms");
			    		writer.println("0.1 "+ (System.currentTimeMillis() - startTime));
			    		do_once[0] = true;
			    	}
			    	else if((double) num_over / nodeCount > 0.05 && !do_once[11]) {
			    		System.out.printf(format,"0.05 converged in", System.currentTimeMillis() - startTime , " ms");
			    		writer.println("0.05 "+ (System.currentTimeMillis() - startTime));
			    		do_once[11] = true;
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
    		writer.println("1 "+ (System.currentTimeMillis() - startTime));
	    }
	    else {
	    	System.out.println("100% convergence not reached");
	    }

	    System.out.println();
	    
	    
	    format = "%-40s%d%n";
	    String format2 = "%-40s%f%n";
	    System.out.printf(format,"Total nodes",nodeCount);
	    System.out.printf(format,"Disseminated nodes",count);
	    System.out.printf(format,"Transmission time",tTrans);
	    System.out.printf(format,"Number of messages passed",traffic.getValue());
	    if(Algoritmo.equals("gossip"))
		    System.out.printf(format2,"Probability",probability);
	    System.out.printf(format,"Bitrate",bitrate);
	    System.out.printf(format,"Packet Size",packetSize);	
	    
	    writer.println();
	    writer.println("Total_Nodes " + nodeCount);
	    writer.println("Disseminated_Nodes " + count);
	    writer.println("Transmission_time " + tTrans);
	    writer.println("N_messages_passed " + traffic.getValue());
	    if(Algoritmo.equals("gossip"))
	    	writer.println("Probability " + probability);
	    writer.println("Bitrate " + bitrate);
	    writer.println("Packet_Size " + packetSize);

	    
	    writer.close();
	    
	}
}