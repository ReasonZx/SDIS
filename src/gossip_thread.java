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
	private Node test_node;
	private Iterator<Node> node_it;
	private Graph graph;
	private int index;
	private ReentrantLock lock;

	
	public gossip_thread(Graph x,int i,ArrayList<String> Q,ReentrantLock l){
		graph=x;
		str_array = Q;
		index = i;
		lock = l;
	}
	
	public void run(){
		if(index == 0) {
		    graph.getNode(0).addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
			str_array.add(0,"work");																	// Node disseminating information
		    node_it = graph.getNode(0).getNeighborNodeIterator();
			while(node_it.hasNext()==true) {															//Painting( blue node 0. red neighbors)
				test_node = node_it.next();
				str_array.set(test_node.getIndex(),"work");
				lock.lock();
				test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 15px;");
				lock.unlock();
			}
		}
		
		else {																							// All other nodes
			while(true) {
				if(str_array.get(index) == "work") {
					node_it = graph.getNode(index).getNeighborNodeIterator();
					while(node_it.hasNext()==true) {										
						test_node = node_it.next();
						str_array.set(test_node.getIndex(),"work");
						if(test_node.getIndex() != 0) {
							lock.lock();
							test_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 15px;");
							lock.unlock();
						}
					}
				 break;
				}
				else {
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}				
				}
			}
		}
	}	
}
