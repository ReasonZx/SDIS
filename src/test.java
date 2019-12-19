import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.LobsterGenerator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class test {
	public static void main(String args[]) throws InterruptedException {
		
		ArrayList<String> str_array = new ArrayList<String>();
		Graph graph = new SingleGraph("test");
		
	    Generator gen = new DorogovtsevMendesGenerator();
	    
	    gen.addSink(graph);
	    gen.begin();
	    for(int i=0; i<100; i++) {
	            gen.nextEvents();
	    }
	    gen.end();
	    graph.display(true);
	    
	    List<Thread> list_of_nodes = new ArrayList<Thread>();
	    
	    System.out.println(graph.getNodeCount());
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	str_array.add(i, "");
	    	list_of_nodes.add(i,new gossip_thread(graph.getNode(i), str_array));
	    }
	    
	    for(int i=0 ; i < graph.getNodeCount() ; i++) {
	    	list_of_nodes.get(i).start();
	    }
	    
	    
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