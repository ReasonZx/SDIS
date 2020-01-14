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
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random; 

public class pushpull_thread extends Thread{
	
	private ArrayList<String> str_array;
	private ArrayList<ReentrantLock> locks;
	private Node pushpull_node;
	private Node test_node;
	private Iterator<Node> node_it;
	private List<Node> neighbors;
	private long startTime;								
	private int tTrans;
	private int timeOut;
	private Datashare traffic;

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public pushpull_thread(Node node,ArrayList<String> Q, int time_trans, int time_out,  ArrayList<ReentrantLock> lockers, Datashare traff){
		pushpull_node=node;
		str_array = Q;
		tTrans = time_trans;
		timeOut = time_out;
		locks = lockers;
		traffic = traff;
	}
	public Node getRandomElement(List<Node> list) 
    { 
        Random rand = new Random(); 
        return list.get(rand.nextInt(list.size())); 
    }
	
	public void run() {
		if(pushpull_node.getIndex() == 0) {																	
			pushpull_node.addAttribute("ui.style", "fill-color: rgb(0,0,255); size: 8px;");
    		traffic.setValue(traffic.getValue() + 1);
			str_array.set(0,"work spreader");		
			node_it = pushpull_node.getNeighborNodeIterator();												
		    
		    neighbors = new ArrayList<Node>();
		    while (node_it.hasNext()) {
		        neighbors.add(node_it.next());
		    }
		    while(true) {														
				test_node = getRandomElement(neighbors);
		    	if(str_array.get(test_node.getIndex()).contains("spreader")) {
		    		locks.get(test_node.getIndex()).lock();
					sleep(tTrans);
		    		locks.get(0).lock();
		    		traffic.setValue(traffic.getValue() + 1);
		    		locks.get(0).unlock();
		    		str_array.set(test_node.getIndex(),"work spreader");													
		    		locks.get(test_node.getIndex()).unlock();
		    	}
		    	else {
		    		locks.get(test_node.getIndex()).lock();
					sleep(tTrans);
		    		locks.get(0).lock();
		    		traffic.setValue(traffic.getValue() + 1);
		    		locks.get(0).unlock();
					str_array.set(test_node.getIndex(),"work");
		    		locks.get(test_node.getIndex()).unlock(); 		
		    	}
		    	int count = 0;
				for (Node neighbor : neighbors) 
		    		if(str_array.get(neighbor.getIndex()).contains("work")) 
		    			count++;
				if(count == neighbors.size()) break;
		    }
		}

		else {																									//All other nodes
			startTime = System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime < timeOut) {
				String workStr = str_array.get(pushpull_node.getIndex());
				if(!workStr.contains("work")) {	
					while(true) {
						node_it = pushpull_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
					    neighbors = new ArrayList<Node>();
					    while (node_it.hasNext()) {
					        neighbors.add(node_it.next());
					    }																				
					    test_node = getRandomElement(neighbors);
					    if(str_array.get(test_node.getIndex()).contains("spreader")){
				    		locks.get(test_node.getIndex()).lock();
					    	sleep(tTrans);
				    		locks.get(0).lock();
				    		traffic.setValue(traffic.getValue() + 1);
				    		locks.get(0).unlock();
							str_array.set(pushpull_node.getIndex(),"work spreader");
				    		locks.get(test_node.getIndex()).unlock();
							break;
				    	}
				    	else {
				    		sleep(5);
				    	}
					}																						//Information has been disseminated -- Break;
				}
				else
					if(workStr.contains("spreader")) {
						node_it = pushpull_node.getNeighborNodeIterator();												
						
					    neighbors = new ArrayList<Node>();
					    while (node_it.hasNext()) {
					        neighbors.add(node_it.next());
					    }
					    while(true) {
							test_node = getRandomElement(neighbors);
					    	if(!str_array.get(test_node.getIndex()).contains("spreader")) {
						    	locks.get(test_node.getIndex()).lock();
					    		sleep(tTrans);
					    		locks.get(0).lock();
					    		traffic.setValue(traffic.getValue() + 1);
					    		locks.get(0).unlock();
								str_array.set(test_node.getIndex(),"work");			
					    		locks.get(test_node.getIndex()).unlock();
						    }
							int count = 0;
							for (Node neighbor : neighbors) 
					    		if(str_array.get(neighbor.getIndex()).contains("work")) 
					    			count++;
							if(count == neighbors.size()) break;
					    }
					}
				if(str_array.get(pushpull_node.getIndex()).contains("work")) {
					Thread paint = new PaintingThread(pushpull_node);
			    	paint.start();
			    	break;
				}
			}
		}
	}	
}