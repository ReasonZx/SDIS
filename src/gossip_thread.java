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
	private Node gossip_node;
	private Node test_node;
	private Iterator<Node> node_it;
	private List<Node> neighbors;
	private boolean stop;
	private long startTime;								//Adicionar como parametro
	private double loss_of_interest;				// Adicionar como parametro

	
	public gossip_thread(Node node,ArrayList<String> Q, double prob){
		gossip_node=node;
		str_array = Q;
		loss_of_interest = prob;
		
	}
	public Node getRandomElement(List<Node> list) 
    { 
        Random rand = new Random(); 
        return list.get(rand.nextInt(list.size())); 
    }
	
	public void run(){
		if(gossip_node.getIndex() == 0) {	//If origin node
		    
			
			gossip_node.addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 10px;");				//Paint as blue
			str_array.set(0,"work:0");																		//Fill the buffer of the origin Node with information to disseminate
		    node_it = gossip_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
		    
		    neighbors = new ArrayList<Node>();
		    while (node_it.hasNext()) {
		        neighbors.add(node_it.next());
		    }
		    double number = 1;
		    while(true) {															//Iterating the neighbor nodes
				test_node = getRandomElement(neighbors);
				
		    	if(str_array.get(test_node.getIndex()).contains("work")) {	
			    	number = number - loss_of_interest;
			    	stop = number <= 0;
		    		if(stop==true)	break;  		
		    	}
		    	else {
		    		str_array.set(test_node.getIndex(),"work:0");													//Putting information on the buffers of the neighbors 
					test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");				//Painting red node that has been disseminated with information		    		
		    	}
		    	
		    }
		} 
		
		else {																									//All other nodes
			startTime = System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime < 10000) {
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
					double number = 1;
					while(true) {																//Iterating the neighbor nodes
						test_node = getRandomElement(neighbors);
					    if(str_array.get(test_node.getIndex()).contains("work")) {
					    	number = number - loss_of_interest;
					    	stop = number <= 0;
					    	if(stop==true)	break;
					    }
					    else {
					    	str_array.set(test_node.getIndex(),"work:" + gossip_node.getIndex());													//Putting information on the buffers of the neighbors 		
					    	if(test_node.getIndex()!= 0 && !test_node.hasAttribute("ui.style")) {
					    		test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");				//Painting red node that has been disseminated with information
					    		System.out.println(test_node.getIndex());
					    	}
					    }
					}
					break;																							//Information has been disseminated -- Break;
				}
				else {																						//If it doesn't have information wait until some node puts information
					try {																					//in its buffer
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}				
				}
			}
		}
	}	
}