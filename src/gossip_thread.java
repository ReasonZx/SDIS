import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Node;

public class gossip_thread extends Thread{
	
	private Node gossip_node;
	private ArrayList<String> str_array;
	private Node test_node;
	private Iterator<Node> node_it;
	
	public gossip_thread(Node x,ArrayList<String> Q){
		gossip_node=x;
		str_array = Q;
	}
	
	public void run(){
		
		if(gossip_node.getIndex() == 0) {
		    gossip_node.addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
			node_it = gossip_node.getNeighborNodeIterator();
			while(node_it.hasNext()==true) {						//Painting( blue node 0. red neighbors)
				test_node = node_it.next();
				str_array.add(test_node.getIndex(),"work");
				//test_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
				//System.out.println(test_node.getIndex());
			}
		}
		else {
			while(true) {
				if(str_array.get(gossip_node.getIndex()) == "work") {
				    gossip_node.addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 15px;");
					node_it = gossip_node.getNeighborNodeIterator();
					while(node_it.hasNext()==true) {						//Painting( blue node 0. red neighbors)
						test_node = node_it.next();
						str_array.add(test_node.getIndex(),"work");
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
				}
			}
		}	
	}	
}
