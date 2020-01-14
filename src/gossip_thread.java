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

public class gossip_thread extends Thread{
	
	private ArrayList<String> str_array;
	private ArrayList<ReentrantLock> locks;
	private Node gossip_node;
	private Node test_node;
	private Iterator<Node> node_it;
	private List<Node> neighbors;
	private boolean stop;
	private Random rand2 = new Random();
	private long startTime;								
	private double probability;							
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

	public gossip_thread(Node node,ArrayList<String> Q, double prob, int time_trans, int time_out, ArrayList<ReentrantLock> lockers , Datashare traff){
		gossip_node=node;
		str_array = Q;
		probability = prob;
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
		if(gossip_node.getIndex() == 0) {	//If origin node
			gossip_node.addAttribute("ui.style", "fill-color: rgb(0,0,255); size: 10px;");				//Paint as blue
			str_array.set(0,"work:0");
    		traffic.setValue(traffic.getValue() + 1);
		    node_it = gossip_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
		    
		    neighbors = new ArrayList<Node>();
		    while (node_it.hasNext()) {
		        neighbors.add(node_it.next());
		    }
		    while(true) {															
				test_node = getRandomElement(neighbors);
		    	if(str_array.get(test_node.getIndex()).contains("work")) {	
		    		stop = rand2.nextDouble() < probability;
		    		if(stop==true)	break;
		    	}
		    	else {
		    		locks.get(test_node.getIndex()).lock();
		    		sleep(tTrans);
		    		locks.get(0).lock();
		    		traffic.setValue(traffic.getValue() + 1);
		    		locks.get(0).unlock();
		    		str_array.set(test_node.getIndex(),"work:0");	
		    		locks.get(test_node.getIndex()).unlock();
		    	}
		    	
		    }
		} 
		
		else {																								//All other nodes
			startTime = System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime < timeOut) {
				String workStr = str_array.get(gossip_node.getIndex());
				if(workStr.contains("work")) {																	//If it already has information to disseminate
					node_it = gossip_node.getNeighborNodeIterator();											//Get the iterator for Neighbor nodes
				    neighbors = new ArrayList<Node>();
					while (node_it.hasNext()) {
						Node n = node_it.next();
						if(!workStr.split(":")[1].equals(String.valueOf(n.getIndex()))) {
							neighbors.add(n);
						}
					}
					if(neighbors.size() <= 0) break;
					while(true) {
						test_node = getRandomElement(neighbors);
					    if(str_array.get(test_node.getIndex()).contains("work")) {
				    		stop = rand2.nextDouble() < probability;
					    	if(stop==true)	break;
					    }
					    else {
					    	locks.get(test_node.getIndex()).lock();
				    		sleep(tTrans);
				    		locks.get(0).lock();
					    	traffic.setValue(traffic.getValue() + 1);
				    		locks.get(0).unlock();
					    	str_array.set(test_node.getIndex(),"work:" + gossip_node.getIndex());		
				    		locks.get(test_node.getIndex()).unlock();
					    }
					}
					break;																							
				}
				else {																						
		    		sleep(5);
				}
				if(str_array.get(gossip_node.getIndex()).contains("work")) {
			    	Thread paint = new PaintingThread(gossip_node);
			    	paint.start();
				}
			}
		}
	}	
}