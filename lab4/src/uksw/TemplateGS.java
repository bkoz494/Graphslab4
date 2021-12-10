package uksw;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.Random;
import java.util.Iterator;

public class TemplateGS {
	
	SingleGraph myGraph;
	Random rand = new Random();
	/**
	 * Constructor of the class
	 * @param args
	 */
	public TemplateGS(String[] args) {
		myGraph = new SingleGraph("template graph");
		consensus(false);
	}
	
//	private boolean isConsensusDone(SingleGraph myGraph) {
//		Node v = Toolkit.randomNode(myGraph);
//		for(Node n: myGraph.getNodeSet()) {
//			
//		}
//	}
	
	private void consensus(boolean visualise) {
		myGraph = Tools.grid(15, true, false);
		int colorNumber = 0;
		
		for(Edge e: myGraph.getEdgeSet()) {
			e.addAttribute("ui.style", "fill-color:white;");
		}
		int r, g, b;
		//setting random colors
		for(Node n: myGraph.getNodeSet()) {
			r = rand.nextInt(256);
			g = rand.nextInt(256);
			b = rand.nextInt(256);
			n.addAttribute("ui.style", "fill-color:rgb("+r+","+g+","+b+"); size:30px; shape: box;");
			n.addAttribute("red", r);
			n.addAttribute("green", g);
			n.addAttribute("blue", b);
		}
		if(visualise) {
			myGraph.display(false);
		}
		int stepNumber = 0;
		while(true) {
			stepNumber++;
			//pick random node
			Node v = Toolkit.randomNode(myGraph);
			//get it's neighborhood
			Iterator<Node> neighbours = v.getNeighborNodeIterator();
			//get random node n from neighborhood
			int x = rand.nextInt(8)+1;
			Node n = Toolkit.randomNode(myGraph);
			for(int i=0; i<x; i++) {
				if(neighbours.hasNext()) {
//					System.out.println(n);
					n = neighbours.next();
				}
			}
			//set color of v to be equal to n's
			r = n.getAttribute("red");
			g = n.getAttribute("green");
			b = n.getAttribute("blue");
			v.addAttribute("ui.style", "fill-color:rgb("+r+","+g+","+b+");");
			v.addAttribute("red", r);
			v.addAttribute("green", g);
			v.addAttribute("blue", b);
			if(visualise) {
				Tools.pause(5);
			}
			//*If all cells have the same color end loop
			int newColorNumber = countColors(myGraph, stepNumber);
			if(newColorNumber != colorNumber) {
				colorNumber = newColorNumber;
				System.out.println("step number: "+stepNumber+" color number: "+colorNumber);
			}
			if(colorNumber==1) {
				System.out.println(stepNumber);
				return;
			}
		}
	}
	/**
	 * function counts number of colors
	 * @param myGraph
	 * @param iterationNumber
	 * @return true if all nodes have the same color
	 */
	private int countColors(SingleGraph myGraph, int iterationNumber) {
		int colorNumber = 0;
		//we take a node
		for(Node n: myGraph.getNodeSet()) {
			//if marked it's color has  been already counted
			if(n.hasAttribute("marked")) continue;
			colorNumber++;
			//we mark all nodes that have the same color
			for(Node v: myGraph.getNodeSet()) {
				if(v.hasAttribute("marked")) continue;
				
				if(compareColors(n, v)) {
					v.addAttribute("marked", true);
				}
			}
		}
		for(Node n: myGraph.getNodeSet()) {
			n.removeAttribute("marked");
		}
		return colorNumber;
	}
	
	boolean compareColors(Node n, Node v) {
		int nR, nG, nB, vR, vG, vB;
		nR = n.getAttribute("red");
		nG = n.getAttribute("green");
		nB = n.getAttribute("blue");
		vR = v.getAttribute("red");
		vG = v.getAttribute("green");
		vB = v.getAttribute("blue");
		
		if((nR==vR)&&(nG==vG)&&(nB==vB)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * the main just chooses the viewer and instantiates the class
	 * @param args
	 */
	public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new TemplateGS(args);
	}

}