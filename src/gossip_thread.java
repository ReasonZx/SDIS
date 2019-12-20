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

public class gossip_thread extends Thread{
	
	private ArrayList<String> str_array;
	private Node gossip_node;
	private Node test_node;
	private Iterator<Node> node_it;

	
	public gossip_thread(Node node,ArrayList<String> Q){
		gossip_node=node;
		str_array = Q;
	}
	
	public void run(){
		if(gossip_node.getIndex() == 0) {																//If origin node
		    gossip_node.addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 10px;");				//Paint as blue
			str_array.set(0,"work");																		//Fill the buffer of the origin Node with information to disseminate
		    node_it = gossip_node.getNeighborNodeIterator();												//Get the iterator for Neighbor nodes
			while(node_it.hasNext()==true) {																//Iterating the neighbor nodes
				test_node = node_it.next();
				str_array.set(test_node.getIndex(),"work");													//Putting information on the buffers of the neighbors 
				test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");				//Painting red node that has been disseminated with information
			}
		}
		
		else {																							//All other nodes
			while(true) {
				if(str_array.get(gossip_node.getIndex()) == "work") {										//If it already has information to disseminate
					node_it = gossip_node.getNeighborNodeIterator();											//Get the iterator for Neighbor nodes
					while(node_it.hasNext()==true) {										
						test_node = node_it.next();
						str_array.set(test_node.getIndex(),"work");												//Putting information on the buffers of the neighbors 
						if(test_node.getIndex() != 0) {															//Painting red node that has been disseminated with information
							test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 10px;");		//If the node is the origin node dont paint it (leave it blue)
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
