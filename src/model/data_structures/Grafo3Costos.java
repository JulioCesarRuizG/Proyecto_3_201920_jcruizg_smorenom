package model.data_structures;

import java.util.Iterator;

public class Grafo3Costos<K, V> {

	private InterseccionConCostos<K,V>[] vertices;
	private int cantidadVertices;

	public Grafo3Costos(InterseccionConCostos<K, V>[] inter)
	{
		vertices = inter;
	}
	
	public Grafo3Costos(int n)
	{
		vertices = new InterseccionConCostos[n];
		cantidadVertices = 0;
	}

	public int V()
	{
		return cantidadVertices;
	}

	public int E()
	{
		int cantidad = 0;
		for(InterseccionConCostos inter : vertices)
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
		for(InterseccionConCostos inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					int id = (int) idVertexFin;
					Arco3Costos<K> arc = new Arco3Costos(id, cost);
					inter.agregarArco(arc);
				}
			}
		}
	}
	
	public InterseccionConCostos<K,V> getInfoVertex(K idVertex)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
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
		for(InterseccionConCostos<K,V> inter : vertices)
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
		InterseccionConCostos<K,V> inter = getInfoVertex(idVertex);
		inter.cambiarInformacion(infoVertex);
	}
	
	public double getCostArcHaversine(K idVertexIni, K idVertexFin)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id = (K)arc.darDestino();
							K id2 = (K) idVertexFin;
							if(id == id2)
							{
								return arc.darCostoHaversine();
							}
						}
						
					}
				}
			}
		}
		return -1;
	}
	
	public double getCostArcCSV(K idVertexIni, K idVertexFin)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id = (K)arc.darDestino();
							K id2 = (K) idVertexFin;
							if(id == id2)
							{
								return arc.darCostoCSV();
							}
						}
						
					}
				}
			}
		}
		return -1;
	}
	
	public double getCostArcVelXTim(K idVertexIni, K idVertexFin)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id = (K)arc.darDestino();
							K id2 = (K) idVertexFin;
							if(id == id2)
							{
								return arc.darCostoVelXTim();
							}
						}
						
					}
				}
			}
		}
		return -1;
	}
	
	public void setCostArcHaversine(K idVertexIni, K idVertexFin, double cost)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id =  arc.darDestino();
							K id2 =  idVertexFin;
							if(id == id2)
							{
								arc.cambiarCostoHaversine(cost);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	public void setCostArcCSV(K idVertexIni, K idVertexFin, double cost)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id =  arc.darDestino();
							K id2 =  idVertexFin;
							if(id == id2)
							{
								arc.cambiarCostoCSV(cost);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	public void setCostArcVelXTim(K idVertexIni, K idVertexFin, double cost)
	{
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertexIni)
				{
					for(Arco3Costos<K> arc : inter.darArcos())
					{
						if(arc != null)
						{
							K id =  arc.darDestino();
							K id2 =  idVertexFin;
							if(id == id2)
							{
								arc.cambiarCostoVelXTim(cost);
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
		Arco3Costos<K> i = new Arco3Costos<K>(idVertexFin, 0);
		for(InterseccionConCostos<K,V> inter : vertices)
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
		Arco3Costos<K> i = new Arco3Costos<K>(idVertexFin, cost);
		for(InterseccionConCostos<K,V> inter : vertices)
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
		InterseccionConCostos<K,V> inter = new InterseccionConCostos<K,V>(idVertex, infoVertex, null);
		vertices[cantidadVertices] = inter;
		cantidadVertices++;
	}
	
	public Iterator<K> adj(K idVertex)
	{
		Queue<K> res= new Queue<K>(null);
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(inter.darId() == idVertex)
				{ 
					for(Arco3Costos<K> arc : inter.darArcos())
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
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				inter.desmarcar();
			}
		}
	}
	
	public void dfs(K s)
	{
		InterseccionConCostos<K, V> buscado=getInfoVertex(s);
		buscado.marcar();
		if(buscado != null)
		{
			Iterator<K> adjacentes= adj(s);
			while(adjacentes.hasNext()){
				K actual = adjacentes.next();
				dfs(actual);
			}
		}
	}
	
	public int cc()
	{
		uncheck();
		int cantidad = 0;
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			if(inter != null)
			{
				if(!inter.estaMarcado()){
					dfs(inter.darId());
					cantidad++;
				}
			}
			
		}
		return cantidad;
	}
	
	public Iterable<K> getCC(K idVertex)
	{
		uncheck();
		dfs(idVertex);
		Queue<K> componente= new Queue<K>(null);
		for(InterseccionConCostos<K,V> inter : vertices)
		{
			
			if(inter.estaMarcado())
				componente.enQueue(inter.darId());
				
			
		}
		return componente;
	}
	
	public InterseccionConCostos[] darVertices()
	{
		return vertices;
	}

}
