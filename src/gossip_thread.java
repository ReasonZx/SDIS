import java.util.Iterator;

import org.graphstream.graph.Node;

public class gossip_thread extends Thread{
	
	private Node gossip_node;
	private Node test_node;
	private Iterator<Node> node_it;
	
	public gossip_thread(Node x){
		gossip_node=x;
	}
	
	public void run(){
		if(gossip_node.getIndex() == 0) {
		    gossip_node.addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
			node_it = gossip_node.getNeighborNodeIterator();
			while(node_it.hasNext()==true) {						//Painting( blue node 0. red neighbors)
				test_node = node_it.next();
				test_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
				System.out.println(test_node.getIndex());
			}
			//gossip_node.
		}
	}
	
	
}
