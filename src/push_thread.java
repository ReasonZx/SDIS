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

public class push_thread extends Thread{
	
	private ArrayList<String> str_array;
	private ArrayList<ReentrantLock> locks;
	private Node push_node;
	private Node test_node;
	private Iterator<Node> node_it;
	private List<Node> neighbors;
	private boolean stop;
	private Random rand2 = new Random();
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

	
	public push_thread(Node node,ArrayList<String> Q, int time_trans, int time_out, ArrayList<ReentrantLock> lockers, Datashare traff){
		push_node=node;
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
		if(push_node.getIndex() == 0) {
			push_node.addAttribute("ui.style", "fill-color: rgb(0,0,255); size: 10px;");				//Paint as blue
			str_array.set(0,"work");
    		traffic.setValue(traffic.getValue() + 1);
			node_it = push_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
		    
		    neighbors = new ArrayList<Node>();
		    while (node_it.hasNext()) {
		        neighbors.add(node_it.next());
		    }
		    while(true) {														
				test_node = getRandomElement(neighbors);
		    	locks.get(test_node.getIndex()).lock();
		    	sleep(tTrans);
	    		locks.get(0).lock();
	    		traffic.setValue(traffic.getValue() + 1);
	    		locks.get(0).unlock();
				str_array.set(test_node.getIndex(),"work");
	    		locks.get(test_node.getIndex()).unlock();
		    	int count = 0;
				for (Node neighbor : neighbors)
		    		if(str_array.get(neighbor.getIndex()).contains("work")) 
		    			count++;
				if(count == neighbors.size()) break;
		    }
		}
		
		else {																							//All other nodes
			startTime = System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime < timeOut) {
				String workStr = str_array.get(push_node.getIndex());
				if(workStr.contains("work")) {																	//If it already has information to disseminate
					node_it = push_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
				 
				    neighbors = new ArrayList<Node>();
				    while (node_it.hasNext()) {
				        neighbors.add(node_it.next());
				    }
				    while(true) {
				    	int count = 0;
						for (Node neighbor : neighbors) 
				    		if(str_array.get(neighbor.getIndex()).contains("work")) 
				    			count++;
						if(count == neighbors.size()) break;
						test_node = getRandomElement(neighbors);
				    	if(!str_array.get(test_node.getIndex()).contains("work:")) {
				    		locks.get(test_node.getIndex()).lock();
				    		sleep(tTrans);
				    		locks.get(0).lock();
				    		traffic.setValue(traffic.getValue() + 1);
				    		locks.get(0).unlock();
							str_array.set(test_node.getIndex(),"work:");
				    		locks.get(test_node.getIndex()).unlock();
				    	}
				    }																						//Information has been disseminated -- Break;
				break;
				}
				else {																						//If it doesn't have information wait until some node puts information
					sleep(5);
				}
				if(str_array.get(push_node.getIndex()).contains("work")){
					Thread paint = new PaintingThread(push_node);
			    	paint.start();
				}
			}
		}
	}	
}