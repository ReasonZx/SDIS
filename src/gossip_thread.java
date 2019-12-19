import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Node;

public class gossip_thread extends Thread{
	
	private Node gossip_node;
	private BlockingQueue<String> queue;
	private Node test_node;
	private Iterator<Node> node_it;
	private String msg;
	
	public gossip_thread(Node x,BlockingQueue<String> Q){
		gossip_node=x;
		queue = Q;
	}
	
	public void run(){		
		if(gossip_node.getIndex() == 0) {
		    gossip_node.addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
			node_it = gossip_node.getNeighborNodeIterator();
			while(node_it.hasNext()==true) {						//Painting( blue node 0. red neighbors)
				queue.add("work");
				test_node = node_it.next();
				//test_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
				//System.out.println(test_node.getIndex());
			}
			/*while(true) {
			       while ((msg = queue.poll()) != null) {
			         System.out.println(msg);
			       }
			       // do other stuff
			}*/
		}
		else {
			while(true) {
				if(msg == "work") {
				    gossip_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 15px;");
					node_it = gossip_node.getNeighborNodeIterator();
					while(node_it.hasNext()==true) {						//Painting( blue node 0. red neighbors)
						queue.add(msg);
						test_node = node_it.next();
						//test_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
						//System.out.println(test_node.getIndex());
					}
					break;
				}
				else {
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg = queue.poll();
				}
			}
		}		
	}	
}
