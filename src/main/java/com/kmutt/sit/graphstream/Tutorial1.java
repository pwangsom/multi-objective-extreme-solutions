package com.kmutt.sit.graphstream;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Tutorial1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new SingleGraph("Tutorial 1");
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		graph.display();
	}

}
