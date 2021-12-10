package uksw;

import java.io.IOException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;

public class Tools {


	public static void screenshot(SingleGraph graph, String pathToImage) { 
		if(graph != null) if(graph.getNodeCount() > 0) {
			FileSinkImages fsi = new FileSinkImages( FileSinkImages.OutputType.PNG, FileSinkImages.Resolutions.SVGA);
			fsi.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
			try {
				fsi.writeAll(graph, pathToImage);
			} catch (IOException e) { e.printStackTrace(); } 
		}
	}
	
	
	public static SingleGraph read(String pathtodgs) {
		SingleGraph graph = new SingleGraph("");
		try {
			graph.read(pathtodgs);
		} catch(Exception e) { }
		return graph;
	}
	

	public static void pause(long delay) {
		try {
			Thread.sleep(delay);
		} catch(InterruptedException ie) {}
	}
	
	/**
	 * Grid generator with VonNeumann neighborhood
	 * --> every vertex non located on the border will have exactly
	 * 4 neighbors (north, south, east and west)
	 * The algorithm consists first in creating the vertices and 
	 * then to add edges between them.
	 * The position of the vertices will be fixed. 
	 * @param n
	 */
	public static SingleGraph grid(int n, boolean moore, boolean torus) {
		SingleGraph myGraph = new SingleGraph("grid von Neumann or Moore");
		// creation of nodes
		for(int line = 0;line<n;line++) {
			for(int col=0;col<n;col++) {
				Node v = myGraph.addNode(line+"-"+col);
				v.addAttribute("x",col);
				v.addAttribute("y",line);
			}
		}
		// creation of edges for von Neumann neighborhood
		int val = 1;
		if(torus) val = 0;
		for(int line=0;line<n;line++) {
			for(int col=0;col<n-val;col++) {
				int colplusone = (col+1)%n;
				Node v = myGraph.getNode(line+"-"+col);
				Node w = myGraph.getNode(line+"-"+colplusone);
				Edge e = myGraph.addEdge(v.getId()+"_"+w.getId(),v.getId(),w.getId());
			}
		}
		
		for(int col=0;col<n;col++) {
			for(int line=0;line<n-val;line++) {
				int lineplusone = (line+1)%n;
				Node v = myGraph.getNode(line+"-"+col);
				Node w = myGraph.getNode(lineplusone+"-"+col);
				Edge e = myGraph.addEdge(v.getId()+"_"+w.getId(),v.getId(),w.getId());
			}
		}
		if(moore) {
			// creation of additional edges corresponding to Moore neighborhood 
			// addition of north-east, north-west, south-east and south-west neighbors
			for(int line=0;line<n-val;line++) {
				for(int col=0;col<n-val;col++) {
					int colplusone = (col+1)%n;
					int lineplusone = (line+1)%n;
					Node v = myGraph.getNode(line+"-"+col);
					Node w = myGraph.getNode(lineplusone+"-"+colplusone);
					Edge e = myGraph.addEdge(v.getId()+"_"+w.getId(),v.getId(),w.getId());
				}
			}
			for(int line=val;line<n;line++) {
				for(int col=0;col<n-val;col++) {
					int colplusone = (col+1)%n;
					int lineminusone = (n+line-1)%n;
					Node v = myGraph.getNode(line+"-"+col);
					Node w = myGraph.getNode(lineminusone+"-"+colplusone);
					Edge e = myGraph.addEdge(v.getId()+"_"+w.getId(),v.getId(),w.getId());
				}
			}
		}
		return myGraph;
	}
}