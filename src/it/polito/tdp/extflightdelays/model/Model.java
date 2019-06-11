package it.polito.tdp.extflightdelays.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private Graph<Airport,DefaultEdge> grafo;
	
	public void creaGrafo(int minimo) {
		ExtFlightDelaysDAO dao=new ExtFlightDelaysDAO();
		List<Airport> aeroporti=dao.loadAllAirports();
	    grafo=new SimpleWeightedGraph<>(DefaultEdge.class);
		for(Airport a:aeroporti) {
			if(dao.contaCompagnie(a)>=minimo) {
				grafo.addVertex(a);
			}
		}
		for(Airport a1:grafo.vertexSet()) {
			for(Airport a2:grafo.vertexSet()) {
				if(a1.getId()!=a2.getId()) {
					grafo.addEdge(a1, a2);
	                grafo.setEdgeWeight(grafo.getEdge(a1, a2), dao.contaVoli(a1, a2));
				}
			}
		}
	}
	
	public List<Airport> tutti(){
		List<Airport> scelti=new LinkedList<Airport>();
		ExtFlightDelaysDAO dao=new ExtFlightDelaysDAO();
		for(Airport a:grafo.vertexSet())
			scelti.add(a);
		return scelti ;
	}
	
	public Map<Double,Airport> listaAdiacenti(Airport daPassare) {
		Map<Double,Airport> adiacenti=new TreeMap<Double,Airport>();
		for(Airport a:grafo.vertexSet()) {
			if(grafo.containsEdge(a, daPassare)) {
				adiacenti.put(grafo.getEdgeWeight(grafo.getEdge(a, daPassare)),a);
			}
		}
		return adiacenti;
		
	}
	
	public List<Airline> compagnie(){
		ExtFlightDelaysDAO dao=new ExtFlightDelaysDAO();
		return dao.loadAllAirlines();
	}

}
