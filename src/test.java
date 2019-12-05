import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;






public class test {
	 
    public static void main(String[] args) throws Exception {
            BarabasiAlbertGenerator gen = new BarabasiAlbertGenerator();
            DefaultGraph g = new DefaultGraph("g");

            gen.addSink(g);

            gen.begin();
            for (int i = 0; i < 1000; i++)
            gen.nextEvents();
            gen.end();

            SpringBox box = new SpringBox();
            Viewer v = g.display(false);
            ViewerPipe pipe = v.newViewerPipe();
            pipe.addAttributeSink(g);
            v.enableAutoLayout(box);
           
            Thread.sleep(5000);
            pipe.pump();
           
            for (Node n : g) {
                    Object[] xy = n.getArray("xyz");
                    double x = (Double) xy[0];
                    double y = (Double) xy[1];
                    org.graphstream.ui.geom.Point3 pixels = v.getDefaultView().getCamera().transformGuToPx(x, y, 0);
                    System.out.printf("'%s': (%.3f;%.3f)\t--> (%.0f;%.0f)\n", n.getId(), x, y, pixels.x, pixels.y);
            }
    }
}