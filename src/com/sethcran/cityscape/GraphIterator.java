package com.sethcran.cityscape;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

public class GraphIterator extends BreadthFirstIterator<Claim, DefaultEdge> {

	public GraphIterator(Graph<Claim, DefaultEdge> graph, Claim start) {
		super(graph, start);
	}
	
	@Override
	protected void encounterVertexAgain(Claim claim, DefaultEdge edge) {
		claim.setVisited(true);
	}

}
