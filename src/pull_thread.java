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

public class pull_thread extends Thread{
	
	private ArrayList<String> str_array;
	private ArrayList<ReentrantLock> locks;
	private Node pull_node;
	private Node test_node;
	private Iterator<Node> node_it;
	private List<Node> neighbors;
	private boolean stop;
	private Random rand2 = new Random();
	private long startTime;								//Adicionar como parametro
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

	
	public pull_thread(Node node,ArrayList<String> Q, int time_trans, int time_out,  ArrayList<ReentrantLock> lockers, Datashare traff){
		pull_node=node;
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
		if(pull_node.getIndex() == 0) {	//If origin node
			pull_node.addAttribute("ui.style", "fill-color: rgb(0,0,255); size: 10px;");				//Paint as blue
			str_array.set(0,"work");
    		traffic.setValue(traffic.getValue() + 1);
		}
		
		else {																									//All other nodes
			startTime = System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime < timeOut) {
				String workStr = str_array.get(pull_node.getIndex());
				if(!workStr.contains("work")) {	
					while(true) {
						node_it = pull_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
					    neighbors = new ArrayList<Node>();
					    while (node_it.hasNext()) {
					        neighbors.add(node_it.next());
					    }																				
					    test_node = getRandomElement(neighbors);
					    if(str_array.get(test_node.getIndex()).contains("work")) {
				    		locks.get(test_node.getIndex()).lock();
				    		sleep(tTrans);
				    		locks.get(0).lock();
				    		traffic.setValue(traffic.getValue() + 1);
				    		locks.get(0).unlock();
							str_array.set(pull_node.getIndex(),"work:");
				    		locks.get(test_node.getIndex()).unlock();
							break;
					    }
					    else {
				    		sleep(5);
					    }	
				    }																						//Information has been disseminated -- Break;
				}
				else {
					Thread paint = new PaintingThread(pull_node);
			    	paint.start();						
					break;
				}
			}
		}
	}	
}