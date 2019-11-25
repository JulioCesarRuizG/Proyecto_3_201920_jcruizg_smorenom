package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;

import model.data_structures.Arco;
import model.data_structures.Arco3Costos;
import model.data_structures.Grafo3Costos;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.Informacion;
import model.data_structures.Interseccion;
import model.data_structures.InterseccionConCostos;
import model.data_structures.Viaje;

/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo<K, V> {

	private GrafoNoDirigido<Integer,Informacion> grafo;
	private GrafoNoDirigido<Integer,Informacion> grafoEnRango;
	private GrafoNoDirigido<Integer,Informacion> grafoRelax;
	private Grafo3Costos<Integer, Informacion> grafoCon3Costos;


	public MVCModelo() throws IOException {

		String path = "./data/dataJson.json";
		JsonReader reader2;
		Gson gson = new Gson();
		try {
			reader2 = new JsonReader(new FileReader(path));
			GrafoNoDirigido grafo = gson.fromJson(reader2, GrafoNoDirigido.class);
			GrafoNoDirigido grafoRelax = gson.fromJson(reader2, GrafoNoDirigido.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("El total de vértices cargados fue: " + grafo.V());
		System.out.println("El total de arcos cargados fue: " + grafo.E());
		int cantidad = 0;
		for(Interseccion<K, V> inter : grafo.darVertices())
		{
			if(inter != null)
			{
				if(inter.estaMarcado())
				{
					cantidad++;
				}
				inter.desmarcar();
			}
		}
		System.out.println("La cantidad de componentes conexos son: " + cantidad);

		for(Interseccion<K, V> inter : grafo.darVertices())
		{
			if(inter != null)
			{
				int inicio = (int) inter.darId();
				Informacion info = (Informacion) inter.darInfo();
				grafoCon3Costos.addVertex(inicio, info);
				for(Arco<K> arcos : inter.darArcos())
				{
					if(arcos != null)
					{
						int fin = (int) arcos.darDestino();
						Informacion info2 = grafo.getInfoVertex2(fin);
						double costo = arcos.darCosto();
						grafoCon3Costos.addVertex(fin, info2);
						grafoCon3Costos.setCostArcHaversine(inicio, fin, costo);
					}
				}
			}

		}

		String rutaCSV="./data/bogota-cadastral-2018-1-WeeklyAggregate.csv";
		CSVReader reader = null;
		int cargados = 0;
		try {
			reader= new CSVReader(new FileReader(rutaCSV));
			for(String[] nextLine : reader) {
				if(nextLine[0].toString().contains("sourceid"))
				{

				}
				else
				{
					int inicioID = Integer.parseInt(nextLine[0]);
					InterseccionConCostos inter = grafoCon3Costos.getInfoVertex(inicioID);

					int destinoID=Integer.parseInt(nextLine[1]);
					InterseccionConCostos inter2 = grafoCon3Costos.getInfoVertex(destinoID);

					Informacion info1 = (Informacion) inter.darInfo();
					Informacion info2 = (Informacion) inter2.darInfo();

					double tiempoPromedioEnSegundos=Double.parseDouble(nextLine[3]);
					if(info1.darMovementeID() == info2.darMovementeID())
					{
						grafoCon3Costos.setCostArcCSV(inicioID, destinoID, tiempoPromedioEnSegundos);
					}
					
					double haversine = grafoCon3Costos.getCostArcHaversine(inicioID, destinoID);
					double cost = haversine/tiempoPromedioEnSegundos;
					grafoCon3Costos.setCostArcVelXTim(inicioID, destinoID, cost);
				}
			}

			for(InterseccionConCostos<K, V> inter : grafoCon3Costos.darVertices())
			{
				if(inter != null)
				{
					int inicio = (int) inter.darId();
					for(Arco3Costos<K> arcos : inter.darArcos())
					{
						if(arcos != null)
						{
							int destino = (int) arcos.darDestino();
							InterseccionConCostos inter2 = grafoCon3Costos.getInfoVertex(destino);
							if(grafoCon3Costos.getCostArcCSV(inicio, destino) == 0)
							{
								Informacion info1 = (Informacion) inter.darInfo();
								Informacion info2 = (Informacion) inter2.darInfo();
								if(info1.darMovementeID() == info2.darMovementeID())
								{
									grafoCon3Costos.setCostArcCSV(inicio, destino, 10);
								}
								else
								{
									grafoCon3Costos.setCostArcCSV(inicio, destino, 100);
								}
							}
						}
					}
				}

			}
			
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
					crearArchivo();
				}
			}


		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void crearArchivo() throws IOException
	{
		String ruta = "./data/mapa.html";
		int contador = 0;
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(ruta);
		}catch (Exception e) {
			e.printStackTrace();
		}
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">");
		writer.println("<meta charset=\"utf-8\">");
		writer.println("<title>Simple Polylines</title>");
		writer.println("<style>");
		writer.println("#map {");
		writer.println("height: 100%;");
		writer.println("}");
		writer.println("html,");
		writer.println("body {");
		writer.println("height: 100%;");
		writer.println("margin: 0;");
		writer.println("padding: 0;");
		writer.println("}");
		writer.println("</style>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<div id=\"map\"></div>");
		writer.println("<script>");
		writer.println("function initMap() {");
		writer.println("var map = new google.maps.Map(document.getElementById('map'), {");
		writer.println("zoom: 5,");
		writer.println("center: {");
		writer.println("lat: 40.162838,");
		writer.println("lng: -3.494526");
		writer.println("},");
		writer.println("mapTypeId: 'roadmap'");
		writer.println("});");
		writer.println("var line;");
		writer.println("var path;");
		for(Interseccion<Integer,Informacion> inter: grafoEnRango.darVertices())
		{
			if(inter != null)
			{
				for(Arco<Integer> arcos : inter.darArcos())
				{
					if(arcos != null)
					{
						Informacion llegada = grafoEnRango.getInfoVertex2(arcos.darDestino());
						if(llegada != null)
						{
							Informacion info = (Informacion) inter.darInfo();
							writer.println("line = [");
							writer.println("{");
							writer.println("lat: " + info.darLatitud() + ",");
							writer.println("lng: " + info.darLongitud());
							writer.println("},");
							writer.println("{");
							writer.println("lat: " + llegada.darLatitud()+ ",");
							writer.println("lng: " + llegada.darLongitud());
							writer.println("}");
							writer.println("];");
							writer.println("path = new google.maps.Polyline({");
							writer.println("path: line,");
							writer.println("strokeColor: '#FF0000',");
							writer.println("strokeWeight: 2");
							writer.println("});");
							writer.println("path.setMap(map);");
							contador++;
							System.out.println(contador);
						}
					}
				}
			}
		}
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();

	}
	
	public static void main(String[] args) throws IOException {
		MVCModelo modelo = new MVCModelo();
	}
}
