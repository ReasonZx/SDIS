import org.graphstream.graph.Node;

public class gossip_thread extends Thread{
	
	private Node gossip_node;
	
	gossip_thread(Node x){
		gossip_node=x;
	}
	
	public void run(){
		System.out.println(gossip_node);
	}
	
	
}
