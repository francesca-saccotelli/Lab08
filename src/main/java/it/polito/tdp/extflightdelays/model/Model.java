package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport, DefaultWeightedEdge>grafo;
	private Map<Integer, Airport>idMap;
	private ExtFlightDelaysDAO dao;
	
	public Model() {
		idMap= new HashMap<Integer,Airport>();
		dao=new ExtFlightDelaysDAO();
		this.dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int distanza) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());
		for(Rotta r:dao.getRotte(idMap, distanza)) {
			DefaultWeightedEdge e=grafo.getEdge(r.getA1(), r.getA2());
			if(e==null) {
				Graphs.addEdge(this.grafo,r.getA1(), r.getA2(), r.getPeso());
			}else {
				double pesoVecchio = this.grafo.getEdgeWeight(e);
				double pesoNuovo = (pesoVecchio + r.getPeso())/2;
				this.grafo.setEdgeWeight(e, pesoNuovo);
			}
		}
	}
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	public List<Rotta>elencoArchi(){
		List<Rotta>archi=new ArrayList<>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			archi.add(new Rotta(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}
}
