package it.polito.tdp.imdb.model;

import it.polito.tdp.imdb.db.ImdbDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	
	private int pause=0;
	private int numGiorni;
	private Actor array[];
	private List<Actor> attori;
	private Actor precedente;
	private String gender;
	private boolean pausa=false;
	
	
	public Model() {
		dao=new ImdbDAO();
		idMap=new HashMap<>();
	}
	
	public void creaGrafo (String genere) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.getVertici(genere, idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for (Adiacenza a:dao.getArchi(genere, idMap)) {
			if (grafo.vertexSet().contains(a.getA1()) && grafo.vertexSet().contains(a.getA2())) {
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	
	public int getNumVertici () {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi () {
		return grafo.edgeSet().size();
	}
	
	public List<Actor> getAttori () {
		List<Actor> attori=new ArrayList<>();
		for (Actor a:grafo.vertexSet()) {
			attori.add(a);
		}
		Collections.sort(attori);
		return attori;
	}
	
	public List<Actor> raggiungibili (Actor a) {
		ConnectivityInspector <Actor, DefaultWeightedEdge> ci=new ConnectivityInspector<>(grafo);
		List<Actor> actors=new ArrayList<>(ci.connectedSetOf(a));
		Collections.sort(actors);
		return actors;
	}
	
	public List<String> getGeneri () {
		return dao.getGeneri();
	}
	
	public void initialize (int n) {
		this.numGiorni=n;
		pause=0;
		array=new Actor[numGiorni];
		this.attori=this.getAttori();
		int p= (int) Math.random()*grafo.vertexSet().size();
		array[0]=attori.get(p);
		gender=array[0].gender;
		precedente=attori.get(p);
		attori.remove(p);
	}
	
	public void run () {
		for (int i=1; i<numGiorni; i++) {
			double probPausa=2;
			if (pausa) {
				probPausa=Math.random();
				if (probPausa<0.9) {
					this.pause++;
					Actor a=new Actor(000, "Nessun", "intervistato", "J");
					array[i]=a;
					gender=a.getGender();
					pausa=false;
				}
			} else if (!pausa || probPausa>=0.9) {
			double p=Math.random();
			if (p>0.6) {
				this.casuale(array, attori, i);
			} else {
				List<Actor> vicini=Graphs.neighborListOf(grafo, precedente);
				if (vicini.size()==0)
					this.casuale(array, attori, i);
				else {  
					   int max=0;
					   Actor intervistato=null;
					   for (Actor a:vicini) {
						 DefaultWeightedEdge e=grafo.getEdge(precedente, a);
					     if (grafo.getEdgeWeight(e)>max && attori.contains(a)) { 
					         max=(int) grafo.getEdgeWeight(e);
					         intervistato=a;
				         }
			         }
					  if (intervistato==null)
						  this.casuale(array, attori, i);
					  else {
					 array[i]=intervistato;
					 if (array[i].gender.equals(gender)) 
							pausa=true;
						else {
							gender=array[i].gender;
							pausa=false;
						}
					 precedente=intervistato;
					 attori.remove(intervistato);
					  }
			  }	
	}
			}
		}
	}
	
	public void casuale (Actor array[], List<Actor> attori, int i) {
		int pp= (int) Math.random()*attori.size();
		array[i]=attori.get(pp);
		if (array[i].gender.equals(gender)) 
			pausa=true;
		else {
			gender=array[i].gender;
			pausa=false;
		}
		precedente=attori.get(pp);
		attori.remove(pp);
	}
	
	public int getPause() {
		return pause;
	}
	
	public Actor[] getIntervistati () {
		return array;
	}

}
