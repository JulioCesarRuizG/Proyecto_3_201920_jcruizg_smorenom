package model.data_structures;

import java.util.Iterator;
import java.util.Scanner;


public class GrafoNoDirigido<K, V> {

	private Interseccion<K,V>[] vertices;
	private int cantidadVertices;

	public GrafoNoDirigido(Interseccion[] inter)
	{
		vertices = inter;
	}
	
	public GrafoNoDirigido(int n)
	{
		vertices = new Interseccion[n];
		cantidadVertices = 0;
	}

	public int V()
	{
		return cantidadVertices;
	}

	public int E()
	{
		int cantidad = 0;
		for(Interseccion inter : vertices)
		{
			if(inter != null)
			{
				cantidad = cantidad + inter.darCantidadArcos();
			}
		}
		return cantidad;
	}
	
	public void AddEdge(K idVertexIni, K idVertexFin, double cost)
	{
		for(Interseccion inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					int id = (int) idVertexFin;
					Arco arc = new Arco(id, cost);
					inter.agregarArco(arc);
				}
			}
		}
	}
	
	public Interseccion<K,V> getInfoVertex(K idVertex)
	{
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				int j= (int) inter.darId();
				int k= (int) idVertex;
				if(j == k)
				{
					return inter;
				}
			}
		}
		return null;
	}
	
	public V getInfoVertex2(K idVertex)
	{
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				int j= (int) inter.darId();
				int k= (int) idVertex;
				if(j == k)
				{
					return inter.darInfo();
				}
			}
		}
		return null;
	}
	
	public void setInfoVertex(K idVertex, V infoVertex)
	{
		Interseccion<K,V> inter = getInfoVertex(idVertex);
		inter.cambiarInformacion(infoVertex);
	}
	
	public double getCostArc(K idVertexIni, K idVertexFin)
	{
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id = (K)arc.darDestino();
							K id2 = (K) idVertexFin;
							if(id == id2)
							{
								return arc.darCosto();
							}
						}
						
					}
				}
			}
		}
		return -1;
	}
	
	public void setCostArc(K idVertexIni, K idVertexFin, double cost)
	{
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id =  arc.darDestino();
							K id2 =  idVertexFin;
							if(id == id2)
							{
								arc.cambiarCosto(cost);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	public void setArc(K idVertexIni, K idVertexFin)
	{
		Arco<K> i = new Arco<K>(idVertexFin, 0);
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				int j= (int) inter.darId();
				int k= (int) idVertexIni;
				if(j == k)
				{
					inter.agregarArco(i);
					return;
				}
			}
		}
	}
	
	public void setArcAndCost(K idVertexIni, K idVertexFin, double cost)
	{
		Arco<K> i = new Arco<K>(idVertexFin, cost);
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				int j= (int) inter.darId();
				int k= (int) idVertexIni;
				if(j == k)
				{
					inter.agregarArco(i);
					return;
				}
			}
		}
	}
	
	public void addVertex(K idVertex, V infoVertex)
	{
		Interseccion<K,V> inter = new Interseccion<K,V>(idVertex, infoVertex, null);
		vertices[cantidadVertices] = inter;
		cantidadVertices++;
	}
	
	public Iterator<K> adj(K idVertex)
	{
		Queue<K> res= new Queue<K>(null);
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertex)
				{ 
					for(Arco<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id =  arc.darDestino();
							res.enQueue(id);
						}
					}
					return res.iterator();
				}
			}
			
		}
		return res.iterator();
	}
	
	public void uncheck()
	{
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				inter.desmarcar();
			}
		}
	}
	
	public void dfs(K s)
	{
		
		//falta implementar
		
	}
	
	public int cc()
	{
		uncheck();
		int cantidad = 0;
		for(Interseccion<K,V> inter : vertices)
		{
			if(inter != null)
			{
				for(Arco arco : inter.darArcos())
				{
					if(arco != null)
					{
						K buscar =  (K) arco.darDestino();
						Interseccion interseccion = getInfoVertex(buscar);
						if(!interseccion.estaMarcado())
						{
							cantidad++;
							interseccion.marcar();
							interseccion.conectadoA(inter);
						}
					}
				}
				inter.marcar();
			}
			
		}
		return cantidad;
	}
	
	public Iterable<K> getCC(K idVertex)
	{
		//falta implementar
	}
	
	public Interseccion[] darVertices()
	{
		return vertices;
	}
	
}
