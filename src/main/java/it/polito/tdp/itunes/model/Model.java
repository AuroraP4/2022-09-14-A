package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultEdge> grafo;
	
	public void creaGrafo(Double duration) {
		grafo = new SimpleGraph<>(DefaultEdge.class);
		ItunesDAO dao = new ItunesDAO();
		//vertici
		Graphs.addAllVertices(grafo, dao.getAlbumsWithDuration(duration)); 
		//archi
		Map<Integer, Album> albumIdMap = new HashMap<>();
		for(Album a: grafo.vertexSet()) {
			albumIdMap.put(a.getAlbumId(), a);  }
		List<Pair<Integer, Integer>> archi = dao.getComparableAlbums();
		for(Pair<Integer, Integer> arco: archi) {
			if(albumIdMap.containsKey(arco.getFirst()) && 
			   albumIdMap.containsKey(arco.getSecond()) &&
			   !arco.getFirst().equals(arco.getSecond())) {
					grafo.addEdge(albumIdMap.get(arco.getFirst()), 
					albumIdMap.get(arco.getSecond()));   }
		}
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
	}
	
	public List<Album> getAlbums() {
		List<Album> list = new ArrayList<>(grafo.vertexSet());
		Collections.sort(list);
		return list;  }
	
	public Set<Album> getComponente(Album a1) {
		ConnectivityInspector<Album, DefaultEdge> ci = 
				new ConnectivityInspector<>(grafo);
		return ci.connectedSetOf(a1); }
	
}