package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import model.data_structures.Arco;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.Informacion;
import model.data_structures.Interseccion;

/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo<K> {

	private GrafoNoDirigido<Integer,Informacion> grafo;
	private GrafoNoDirigido<Integer,Informacion> grafoEnRango;
	private GrafoNoDirigido<Integer,Informacion> grafoRelax;
	
	
	public MVCModelo() throws IOException {

		String path = "./data/dataJson.json";
		JsonReader reader;
		Gson gson = new Gson();
		try {
			reader = new JsonReader(new FileReader(path));
			GrafoNoDirigido grafo = gson.fromJson(reader, GrafoNoDirigido.class);
			GrafoNoDirigido grafoRelax = gson.fromJson(reader, GrafoNoDirigido.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("El total de vértices cargados fue: " + grafo.V());
		System.out.println("El total de arcos cargados fue: " + grafo.E());
		int cantidad = 0;
		for(Interseccion inter : grafo.darVertices())
		{
			if(inter != null)
			{
				if(inter.estaMarcado())
				{
					cantidad++;
				}
			}
		}
		System.out.println("La cantidad de componentes conexos son: " + cantidad);
		
		for(Interseccion inter : grafo.darVertices())
		{
			if(inter != null)
			{
				for(Arco arcos : inter.darArcos())
				{
					if(arcos != null)
					{
						int llegada = (int) arcos.darDestino();
						int inicio = (int) inter.darId();
						double costo = arcos.darCosto();
						grafoRelax.AddEdge(llegada, inicio, costo);
					}
				}
			}
		}
		
		for(Interseccion inter : grafo.darVertices())
		{
			if(inter != null)
			{
				Informacion info = (Informacion) inter.darInfo();
				double lng = info.darLongitud();
				double lat = info.darLatitud();
				if(-74.094723 <= lng && lng <=-74.062707 && 4.597714 <= lat && lat <= 4.621360)
				{
					int llave = (int) inter.darId();
					grafoEnRango.addVertex(llave, info);
					for(Arco arcos : inter.darArcos())
					{
						if(arcos != null)
						{
							int llegada = (int) arcos.darDestino();
							Informacion destino = grafo.getInfoVertex2(llegada);
							double lng2 = destino.darLongitud();
							double lat2 = destino.darLatitud();
							if(-74.094723 <= lng2 && lng2 <=-74.062707 && 4.597714 <= lat2 && lat2 <= 4.621360)
							{
								grafoEnRango.AddEdge(llave, llegada, arcos.darCosto());
							}
						}
					}
				}
			}
		}
	}
}
