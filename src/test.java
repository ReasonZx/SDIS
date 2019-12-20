import org.graphstream.algorithm.generator.BananaTreeGenerator;
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


public class test {
	public static void main(String args[]) throws InterruptedException {
		
		ArrayList<String> str_array = new ArrayList<String>();						//Array of buffers (1 for each node)
		Graph graph = new SingleGraph("test");
		ReentrantLock lock = new ReentrantLock();
		
	    Generator gen = new RandomEuclideanGenerator();
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<100; i++) {
	            gen.nextEvents();													//Creating nodes
	    }
	    gen.end();
	    graph.display(true);
	    	    
	    List<Thread> list_of_nodes = new ArrayList<Thread>();						//Array of threads (1 for each node)
	    
	    System.out.println(graph.getNodeCount());
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	str_array.add(i, "");													//Initializing each node buffer as empty
	    	list_of_nodes.add(i,new gossip_thread(graph,i, str_array, lock));	//Assigning a thread for each node and passing it it's identifier and the string of arrays
	    }
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	list_of_nodes.get(i).start();
	    }
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	list_of_nodes.get(i).join();
	    }	    
	    graph.getNode(0).addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");

	    for(int i=1 ; i < graph.getNodeCount() ; i++) {
	    	if(str_array.get(i) == "work") {
	    		System.out.println(i);
			    graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(255,0,0); size: 15px;");
	    	}
	    }
	    
	   /* //JUST FOR TESTING NEIGHBORS
	    
	    Iterator<Node> node_it;
	    Node this_node;
	    
	    for (int i = 0 ; i < graph.getNodeCount() ; i++) {
	    	System.out.print(i + " - ");
		    graph.getNode(i).addAttribute("ui.style", "fill-color: rgb(0,100,255); size: 15px;");
	    	node_it = graph.getNode(i).getNeighborNodeIterator();
	    	while(node_it.hasNext()) {
		    	this_node = node_it.next();
		    	System.out.print(this_node.getIndex() + " ");
				this_node.addAttribute("ui.style", "fill-color: rgb(255, 0, 0); size: 15px;");
	    	}
	    	System.out.println();
	    	try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    */
	    
	    
	    /*
        SpringBox box = new SpringBox();
        Viewer v = graph.display(false);
        ViewerPipe pipe = v.newViewerPipe();
        pipe.addAttributeSink(graph);
        v.enableAutoLayout(box);
       
        //Thread.sleep(5000);
        pipe.pump();
       
        for (Node n : graph) {
                Object[] xy = n.getArray("xyz");
                double x = (Double) xy[0];
                double y = (Double) xy[1];
                org.graphstream.ui.geom.Point3 pixels = v.getDefaultView().getCamera().transformGuToPx(x, y, 0);
                System.out.printf("'%s': (%.3f;%.3f)\t--> (%.0f;%.0f)\n", n.getId(), x, y, pixels.x, pixels.y);
            }*/
    }
}