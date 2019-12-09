package model.logic;

import static org.junit.Assert.assertNotNull;

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
import model.data_structures.IndexCP;
import model.data_structures.Informacion;
import model.data_structures.Interseccion;
import model.data_structures.InterseccionConCostos;
import model.data_structures.MaxHeapCP;
import model.data_structures.Queue;
import model.data_structures.Viaje;

/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo {

	private GrafoNoDirigido<Integer,Informacion> grafo;
	private GrafoNoDirigido<Integer,Informacion> grafoEnRango;
	private GrafoNoDirigido<Integer,Informacion> grafoRelax;
	private Grafo3Costos<Integer, Informacion> grafoCon3Costos;

	private class idYCosto implements Comparable<idYCosto>{
		private Interseccion id;
		private double costo;
		public idYCosto(Interseccion pID, double pCosto) {
			id=pID;
			costo=pCosto;
		}
		@Override
		public int compareTo(idYCosto o) {
			if(costo>o.costo)
				return 1;
			else if(costo<o.costo)
				return -1;
			else

				return 0;
		}
	}
	private class idYCostoMin implements Comparable<idYCostoMin>{
		private Integer id;
		private double costo;
		public idYCostoMin(Integer pID, double pCosto) {
			id=pID;
			costo=pCosto;
		}
		@Override
		public int compareTo(idYCostoMin o) {
			if(costo>o.costo)
				return -1;
			else if(costo<o.costo)
				return 1;
			else

				return 0;
		}

	}



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
		for(Interseccion<Integer,Informacion> inter : grafo.darVertices())
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

		for(Interseccion<Integer, Informacion> inter : grafo.darVertices())
		{
			if(inter != null)
			{
				int inicio = (int) inter.darId();
				Informacion info = (Informacion) inter.darInfo();
				grafoCon3Costos.addVertex(inicio, info);
				for(Arco<Integer> arcos : inter.darArcos())
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

			for(InterseccionConCostos<Integer,Informacion> inter : grafoCon3Costos.darVertices())
			{
				if(inter != null)
				{
					int inicio = (int) inter.darId();
					for(Arco3Costos<Integer> arcos : inter.darArcos())
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

	public void persistirJson3Costos()
	{
		Gson gson = new Gson();
		String dato = gson.toJson(grafoCon3Costos);
		String ruta = "./data/dataJson3Costos.json";
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(ruta);
		}catch (Exception e) {
			e.printStackTrace();
		}
		writer.println(dato);
	}

	public void cargarJson3Costos()
	{
		String path = "./data/dataJson3Costos.json";
		JsonReader reader;
		Gson gson = new Gson();
		try {
			reader = new JsonReader(new FileReader(path));
			GrafoNoDirigido lista3 = gson.fromJson(reader, GrafoNoDirigido.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Integer darIdMasCercano(double lng, double lat)
	{
		Integer id = null;
		double distancia = Double.POSITIVE_INFINITY;
		for(Interseccion<Integer,Informacion> inter : grafo.darVertices())
		{
			if(inter != null)
			{
				Informacion info1 = (Informacion) inter.darInfo();
				double sin1 = Math.pow(Math.sin((lat-info1.darLatitud())/2), 2);
				double cos1 = Math.cos(info1.darLatitud());
				double cos2 = Math.cos(lat);
				double sin2 = Math.pow((Math.sin((lng-info1.darLongitud())/2)), 2);
				double interno = Math.asin(Math.sqrt(sin1+(cos1*cos2*sin2)));
				double cost = 2*6371*interno;
				if(cost < distancia)
				{
					distancia = cost;
					id = inter.darId();
				}
			}
		}
		return id;
	}

	/**
	 * Parte A
	 * @throws Exception 
	 */

	//A1

	public Queue<String> CaminoCostoMinimo(double latinicio, double lnginicio, double latdestino, double lngdestino) throws Exception
	{
		int inicio = darIdMasCercano(lnginicio, latinicio);
		int destino = darIdMasCercano(lngdestino, latdestino);
		Interseccion puntoA = grafo.getInfoVertex(inicio);
		Interseccion puntoB = grafo.getInfoVertex(destino);
		Informacion infoA = (Informacion) puntoA.darInfo();
		Informacion infoB = (Informacion) puntoB.darInfo();

		Queue<Interseccion> cola = new Queue<Interseccion>(null);

		Queue<String> colaStrings = Djikstra4(infoA.darLatitud(), infoA.darLongitud(), infoB.darLatitud(), infoB.darLongitud());
		while(!colaStrings.isEmpty())
		{
			String actual = colaStrings.deQueue();
			actual.replace(" ", "");
			String[] partes = actual.split(",");
			//			String[] latpartes = partes[1].split(":");
			//			double lat2 = Double.parseDouble(latpartes[1]);
			//			String[] lngpartes = partes[2].split(":");
			//			double lng2 = Double.parseDouble(lngpartes[1]);
			String[] idpartes = partes[0].split(":"); 
			int id = Integer.parseInt(idpartes[1]);
			Interseccion agregar = grafo.getInfoVertex(id);
			cola.enQueue(agregar);

		}

		boolean primercaso = false;

		String ruta = "./data/mapaCaminoCorto.html";
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
		Interseccion segundo = null;
		Interseccion primero = null;
		boolean cambiar = false;


		Queue<String> cola2 = new Queue<String>(null);
		double tiempopromedio = 0;
		double haversine = 0;

		cola2.enQueue("Esta es la cantidad de vértices a seguir: " + cola.size());
		while(!cola.isEmpty())
		{

			if(primercaso == false)
			{
				segundo = cola.deQueue();
				primero = cola.deQueue();
				Informacion infotemp = (Informacion) primero.darInfo();
				Informacion info2temp = (Informacion) segundo.darInfo();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				cola2.enQueue("Id: " + id1 + " Lat: " + infotemp.darLatitud() + " Lng: " + infotemp.darLongitud());
				cola2.enQueue("Id: " + id2 + " Lat: " + info2temp.darLatitud() + " Lng: " + info2temp.darLongitud());
				haversine += grafoCon3Costos.getCostArcHaversine(id1, id2);
				tiempopromedio += grafoCon3Costos.getCostArcCSV(id1, id2);
			}
			else
			{
				primero = cola.deQueue();
				Informacion infotemp = (Informacion) primero.darInfo();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				cola2.enQueue("Id: " + id1 + " Lat: " + infotemp.darLatitud() + " Lng: " + infotemp.darLongitud());
				haversine += grafoCon3Costos.getCostArcHaversine(id2, id1);
				tiempopromedio += grafoCon3Costos.getCostArcCSV(id2, id1);
			}

			Informacion info = (Informacion) primero.darInfo();
			Informacion info2 = (Informacion) segundo.darInfo();
			writer.println("line = [");
			writer.println("{");
			writer.println("lat: " + info.darLatitud() + ",");
			writer.println("lng: " + info.darLongitud());
			writer.println("},");
			writer.println("{");
			writer.println("lat: " + info2.darLatitud()+ ",");
			writer.println("lng: " + info2.darLongitud());
			writer.println("}");
			writer.println("];");
			writer.println("path = new google.maps.Polyline({");
			writer.println("path: line,");
			writer.println("strokeColor: '#FF0000',");
			writer.println("strokeWeight: 2");
			writer.println("});");
			writer.println("path.setMap(map);");
			if(primercaso == true)
			{
				segundo = primero;
			}
			primercaso = true;

		}
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
		cola2.enQueue("El tiempo promedio es: " + tiempopromedio);
		double haversineKM = haversine/1000;
		cola2.enQueue("La distancia es: " + haversineKM);

		return cola2;
	}

	//A2

	public Queue<String> NVerticesMenorVelocidad(int n) throws Exception
	{
		Queue<String> cola = new Queue<String>(null);
		int tamanio = n;
		MaxHeapCP<idYCosto> menores = new MaxHeapCP<>();

		for(Interseccion<Integer,Informacion> inter : grafo.darVertices())
		{
			double promedioactual;
			int cantidad = 0;
			int suma = 0;
			if(inter != null)
			{
				for(Arco<Integer> arcos : inter.darArcos()) 
				{
					if(arcos != null)
					{
						cantidad++;
						suma += arcos.darCosto();
					}
				}
				promedioactual = suma/cantidad;
				idYCosto obj1 = new idYCosto(inter, promedioactual);
				boolean caso = false;
				if(menores.darNumElementos() < tamanio)
				{
					menores.agregar(obj1);
				}
				else
				{
					idYCosto mayor = menores.darMax();
					if(mayor.compareTo(obj1) == 1 )
					{
						menores.sacarMax();
						menores.agregar(obj1);
					}
				}

			}
		}

		cola.enQueue("Estos son los " + tamanio + " vértices con menor tiempo de viaje");
		for(int i=0 ; i<menores.darNumElementos() ; i++)
		{
			idYCosto todos = menores.darMax();
			Informacion info = (Informacion) todos.id.darInfo();
			cola.enQueue("ID: " + todos.id.darId() + " Latitud: " + info.darLatitud() + " Longitud: " + info.darLongitud());
		}
		return cola;
	}

	/**
	 * Parte B
	 * @throws Exception 
	 */

	//1B
	public Queue<String> caminoCostoMinimoB(double latinicio, double lnginicio, double latdestino, double lngdestino) throws Exception
	{
		int inicio = darIdMasCercano(lnginicio, latinicio);
		int destino = darIdMasCercano(lngdestino, latdestino);
		Interseccion puntoA = grafo.getInfoVertex(inicio);
		Interseccion puntoB = grafo.getInfoVertex(destino);
		Informacion infoA = (Informacion) puntoA.darInfo();
		Informacion infoB = (Informacion) puntoB.darInfo();

		Queue<Interseccion> cola = new Queue<Interseccion>(null);

		Queue<String> colaStrings = Djikstra7(infoA.darLatitud(), infoA.darLongitud(), infoB.darLatitud(), infoB.darLongitud());
		while(!colaStrings.isEmpty())
		{
			String actual = colaStrings.deQueue();
			actual.replace(" ", "");
			String[] partes = actual.split(",");
			String[] idpartes = partes[0].split(":"); 
			int id = Integer.parseInt(idpartes[1]);
			Interseccion agregar = grafo.getInfoVertex(id);
			cola.enQueue(agregar);

		}

		boolean primercaso = false;

		String ruta = "./data/mapaCaminoCortoB.html";
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
		Interseccion segundo = null;
		Interseccion primero = null;
		boolean cambiar = false;


		Queue<String> cola2 = new Queue<String>(null);
		double tiempopromedio = 0;
		double haversine = 0;

		cola2.enQueue("Esta es la cantidad de vértices a seguir: " + cola.size());
		while(!cola.isEmpty())
		{

			if(primercaso == false)
			{
				segundo = cola.deQueue();
				primero = cola.deQueue();
				Informacion infotemp = (Informacion) primero.darInfo();
				Informacion info2temp = (Informacion) segundo.darInfo();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				cola2.enQueue("Id: " + id1 + " Lat: " + infotemp.darLatitud() + " Lng: " + infotemp.darLongitud());
				cola2.enQueue("Id: " + id2 + " Lat: " + info2temp.darLatitud() + " Lng: " + info2temp.darLongitud());
				haversine += grafoCon3Costos.getCostArcHaversine(id1, id2);
				tiempopromedio += grafoCon3Costos.getCostArcCSV(id1, id2);
			}
			else
			{
				primero = cola.deQueue();
				Informacion infotemp = (Informacion) primero.darInfo();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				cola2.enQueue("Id: " + id1 + " Lat: " + infotemp.darLatitud() + " Lng: " + infotemp.darLongitud());
				haversine += grafoCon3Costos.getCostArcHaversine(id2, id1);
				tiempopromedio += grafoCon3Costos.getCostArcCSV(id2, id1);
			}

			Informacion info = (Informacion) primero.darInfo();
			Informacion info2 = (Informacion) segundo.darInfo();
			writer.println("line = [");
			writer.println("{");
			writer.println("lat: " + info.darLatitud() + ",");
			writer.println("lng: " + info.darLongitud());
			writer.println("},");
			writer.println("{");
			writer.println("lat: " + info2.darLatitud()+ ",");
			writer.println("lng: " + info2.darLongitud());
			writer.println("}");
			writer.println("];");
			writer.println("path = new google.maps.Polyline({");
			writer.println("path: line,");
			writer.println("strokeColor: '#FF0000',");
			writer.println("strokeWeight: 2");
			writer.println("});");
			writer.println("path.setMap(map);");
			if(primercaso == true)
			{
				segundo = primero;
			}
			primercaso = true;

		}
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
		cola2.enQueue("El tiempo promedio es: " + tiempopromedio);
		double haversineKM = haversine/1000;
		cola2.enQueue("La distancia es: " + haversineKM);

		return cola2;
	}


	//2B

	public Queue<String> verticesAlcanzableTiempoT(double tiempo, double lat, double lng) throws Exception
	{
		Queue<String> colaStrings = new Queue<String>(null);
		Queue<Interseccion> cola = new Queue<Interseccion>(null);
		int id = darIdMasCercano(lng, lat);
		InterseccionConCostos inicio = grafoCon3Costos.getInfoVertex(id);
		Informacion info1 = (Informacion) inicio.darInfo();
		colaStrings.enQueue("Los vértices alcanzables son:");

		String ruta = "./data/Alcanzables.html";
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

		for(InterseccionConCostos inter : grafoCon3Costos.darVertices())
		{
			double costo = 0;
			if(inter != null)
			{
				int idvertex = (int) inter.darId();
				Informacion info2 = (Informacion) inter.darInfo();
				Queue<String> vertices = Djikstra4(info1.darLatitud(), info1.darLongitud(), info2.darLatitud(), info2.darLongitud());
				while(!vertices.isEmpty())
				{
					String actual = vertices.deQueue();
					actual.replace(" ", "");
					String[] partes = actual.split(",");
					String[] idpartes = partes[0].split(":"); 
					int id2 = Integer.parseInt(idpartes[1]);
					Interseccion agregar = grafo.getInfoVertex(id2);
					cola.enQueue(agregar);
				}
				Interseccion primero = null;
				Interseccion segundo = null;
				boolean primercaso = false;
				while(!cola.isEmpty())
				{
					if(primercaso == false)
					{
						segundo = cola.deQueue();
						primero = cola.deQueue();
						int id1 = (int) primero.darId();
						int id2 = (int) segundo.darId();
						costo += grafoCon3Costos.getCostArcVelXTim(id1, id2);
					}
					else
					{
						primero = cola.deQueue();
						int id1 = (int) primero.darId();
						int id2 = (int) segundo.darId();
						costo += grafoCon3Costos.getCostArcVelXTim(id1, id2);
					}
					if(primercaso == true)
					{
						segundo = primero;
					}
					primercaso = true;
				}
				if(costo<tiempo)
				{
					colaStrings.enQueue("Id: " + idvertex + " Lat: " + info2.darLatitud() + " Lng: " + info2.darLongitud());
					Informacion info = (Informacion) primero.darInfo();
					writer.println("line = [");
					writer.println("{");
					writer.println("lat: " + info1.darLatitud() + ",");
					writer.println("lng: " + info1.darLongitud());
					writer.println("},");
					writer.println("{");
					writer.println("lat: " + info2.darLatitud()+ ",");
					writer.println("lng: " + info2.darLongitud());
					writer.println("}");
					writer.println("];");
					writer.println("path = new google.maps.Polyline({");
					writer.println("path: line,");
					writer.println("strokeColor: '#FF0000',");
					writer.println("strokeWeight: 2");
					writer.println("});");
					writer.println("path.setMap(map);");
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
		return colaStrings;
	}
	
	/**
	 * Parte C
	 * @throws Exception 
	 */
	
	//4C
	
	public Queue<String> caminoMasLargo(double lng, double lat) throws Exception
	{
		long inicioT = System.currentTimeMillis();
		Queue<String> colaMasLarga = new Queue<String>(null);
		Queue<String> colaStrings = new Queue<String>(null);
		colaMasLarga.enQueue("");
		Queue<Interseccion> cola = new Queue<Interseccion>(null);
		int id = darIdMasCercano(lng, lat);
		InterseccionConCostos inicio = grafoCon3Costos.getInfoVertex(id);
		Informacion info1 = (Informacion) inicio.darInfo();
		
		String ruta = "./data/MasLargo.html";
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
		
		for(InterseccionConCostos inter : grafoCon3Costos.darVertices())
		{
			Informacion info2  = (Informacion) inter.darInfo();
			Queue<String> colaTemp = Djikstra4(info1.darLatitud(), info1.darLongitud(), info2.darLatitud(), info2.darLongitud());
			if(colaTemp.size() > colaMasLarga.size())
			{
				colaMasLarga = colaTemp;
			}
		}
		boolean primercaso = false;
		Interseccion primero = null;
		Interseccion segundo = null;
		
		while(!colaMasLarga.isEmpty())
		{
			if(primercaso == false)
			{
				segundo = cola.deQueue();
				primero = cola.deQueue();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				Informacion infoA = (Informacion) primero.darInfo();
				Informacion infoB = (Informacion) segundo.darInfo();
				colaStrings.enQueue("Id: " + id1 + " Lat: " + infoA.darLatitud() + " Lng: " + infoA.darLongitud());
				colaStrings.enQueue("Id: " + id2 + " Lat: " + infoB.darLatitud() + " Lng: " + infoB.darLongitud());
			}
			else
			{
				primero = cola.deQueue();
				int id1 = (int) primero.darId();
				int id2 = (int) segundo.darId();
				Informacion infoA = (Informacion) primero.darInfo();
				colaStrings.enQueue("Id: " + id1 + " Lat: " + infoA.darLatitud() + " Lng: " + infoA.darLongitud());
			}
			int idvertex = id;
			Informacion infoA = (Informacion) primero.darInfo();
			Informacion infoB = (Informacion) segundo.darInfo();
			
			Informacion info = (Informacion) primero.darInfo();
			writer.println("line = [");
			writer.println("{");
			writer.println("lat: " + infoA.darLatitud() + ",");
			writer.println("lng: " + infoA.darLongitud());
			writer.println("},");
			writer.println("{");
			writer.println("lat: " + infoB.darLatitud()+ ",");
			writer.println("lng: " + infoB.darLongitud());
			writer.println("}");
			writer.println("];");
			writer.println("path = new google.maps.Polyline({");
			writer.println("path: line,");
			writer.println("strokeColor: '#FF0000',");
			writer.println("strokeWeight: 2");
			writer.println("});");
			writer.println("path.setMap(map);");
			if(primercaso == true)
			{
				segundo = primero;
			}
			primercaso = true;
			
		}
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
		
		long endTime = System.currentTimeMillis();
		long tiempoCola = endTime - inicioT;
		System.out.println("El tiempo fue de: " + tiempoCola);
		
		return colaStrings;
	}

	public Queue<String> Djikstra4(double latOrigen,double longOrigen,double latDestino,double longDestino) throws Exception{
		Double[] distTo= new Double[grafoCon3Costos.V()];         
		Arco3Costos<Integer>[] edgeTo= new Arco3Costos[grafoCon3Costos.V()];    

		Integer verticeOrigen=darIdMasCercano(longOrigen,latOrigen);
		Integer verticeDestino=darIdMasCercano(longDestino,latDestino);

		for (int v = 0; v < grafoCon3Costos.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;

		distTo[verticeOrigen] = 0.0;
		IndexCP<idYCostoMin,Integer> pq= new IndexCP<idYCostoMin, Integer>();
		idYCostoMin origen = new idYCostoMin(verticeOrigen,distTo[verticeOrigen]);
		pq.agregar(origen,origen.id);
		while (!pq.esVacia()) {
			int v = pq.sacarMax().id;
			for (Arco3Costos<Integer> e :  grafoCon3Costos.adj(v)){
				int w = e.darDestino();
				if (distTo[w] > distTo[v] + e.darCostoCSV()) {
					distTo[w] = distTo[v] + e.darCostoCSV();
					edgeTo[w] = e;

					if (pq.contains(w)) pq.cambiarPrioridad(w, new idYCostoMin(w,distTo[w]));
					else                pq.agregar(new idYCostoMin(w,distTo[w]),w);
				}
			}

		}
		Queue<Integer> idsDeLosVertices= new Queue<Integer>(null);
		boolean fin= false;
		Integer idActual = verticeDestino;
		idsDeLosVertices.enQueue(idActual);
		double tiempoPromedio = 0;
		double distanciaHaversine=0;
		while(!fin){
			tiempoPromedio+=edgeTo[idActual].darCostoCSV();
			distanciaHaversine+=edgeTo[idActual].darCostoHaversine();
			idActual= edgeTo[idActual].darOrigen();
			idsDeLosVertices.enQueue(idActual);
			if(idActual.equals(verticeOrigen)){
				fin=true;
			}

		}
		int cantidadVertices= idsDeLosVertices.size();

		Queue<String> respuesta = new Queue<String>(null);
		respuesta.enQueue("Numero de vertices utilizados:"+cantidadVertices);
		while(idsDeLosVertices.size()!=0){
			int actual= idsDeLosVertices.deQueue();
			Informacion datosDelVertice= grafoCon3Costos.getInfoVertex2(actual);
			respuesta.enQueue("Vertice:"+actual+", Latitud:"+ datosDelVertice.darLatitud()+", Longitud:"+ datosDelVertice.darLongitud());


		}
		respuesta.enQueue("El costo mínimo es:" +tiempoPromedio);
		respuesta.enQueue("La distancia estimada es:"+ distanciaHaversine);
		return respuesta;
	}
	public Queue<String> Djikstra7(double latOrigen,double longOrigen,double latDestino,double longDestino) throws Exception{
		Double[] distTo= new Double[grafoCon3Costos.V()];         
		Arco3Costos<Integer>[] edgeTo= new Arco3Costos[grafoCon3Costos.V()];    

		Integer verticeOrigen=darIdMasCercano(longOrigen,latOrigen);
		Integer verticeDestino=darIdMasCercano(longDestino,latDestino);

		for (int v = 0; v < grafoCon3Costos.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;

		distTo[verticeOrigen] = 0.0;
		IndexCP<idYCostoMin,Integer> pq= new IndexCP<idYCostoMin, Integer>();
		idYCostoMin origen = new idYCostoMin(verticeOrigen,distTo[verticeOrigen]);
		pq.agregar(origen,origen.id);
		while (!pq.esVacia()) {
			int v = pq.sacarMax().id;
			for (Arco3Costos<Integer> e :  grafoCon3Costos.adj(v)){
				int w = e.darDestino();
				if (distTo[w] > distTo[v] + e.darCostoHaversine()) {
					distTo[w] = distTo[v] + e.darCostoHaversine();
					edgeTo[w] = e;

					if (pq.contains(w)) pq.cambiarPrioridad(w, new idYCostoMin(w,distTo[w]));
					else                pq.agregar(new idYCostoMin(w,distTo[w]),w);
				}
			}

		}
		Queue<Integer> idsDeLosVertices= new Queue<Integer>(null);
		boolean fin= false;
		Integer idActual = verticeDestino;
		idsDeLosVertices.enQueue(idActual);
		double tiempoPromedio = 0;
		double distanciaHaversine=0;
		while(!fin){
			tiempoPromedio+=edgeTo[idActual].darCostoCSV();
			distanciaHaversine+=edgeTo[idActual].darCostoHaversine();
			idActual= edgeTo[idActual].darOrigen();
			idsDeLosVertices.enQueue(idActual);
			if(idActual.equals(verticeOrigen)){
				fin=true;
			}

		}
		int cantidadVertices= idsDeLosVertices.size();

		Queue<String> respuesta = new Queue<String>(null);
		respuesta.enQueue("Numero de vertices utilizados:"+cantidadVertices);
		while(idsDeLosVertices.size()!=0){
			int actual= idsDeLosVertices.deQueue();
			Informacion datosDelVertice= grafoCon3Costos.getInfoVertex2(actual);
			respuesta.enQueue("Vertice:"+actual+", Latitud:"+ datosDelVertice.darLatitud()+", Longitud:"+ datosDelVertice.darLongitud());


		}
		respuesta.enQueue("El costo mínimo es:" +tiempoPromedio);
		respuesta.enQueue("La distancia estimada es:"+ distanciaHaversine);
		return respuesta;
	}
	public Queue<String> Prim(){
		InterseccionConCostos<Integer, Informacion> inicial= darVerticeDelMaxSubgrafo();
		Iterable<Integer> idsVertices= grafoCon3Costos.getCC(inicial.darId());
		Grafo3Costos<Integer,Informacion>  subGrafo= grafoCon3Costos.crearSubgrafo(inicial.darId());

		Double[] distTo= new Double[subGrafo.V()];         
		Arco3Costos<Integer>[] edgeTo= new Arco3Costos[subGrafo.V()];    
		boolean[]  marked = new boolean[subGrafo.V()];
		IndexCP<idYCostoMin,Integer> pq= new IndexCP<idYCostoMin, Integer>();
		for (int v = 0; v < subGrafo.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;

		for (int v = 0; v < subGrafo.V(); v++)     
			if (!marked[v])
			{
				distTo[v] = 0.0;
				pq.agregar( new idYCostoMin(v,distTo[v]),v);
				while (!pq.esVacia()) {
					int s = pq.darMax().id;
					marked[v] = true;
					for (Arco3Costos<Integer> e : subGrafo.adj(v)) {
						int w = e.darDestino();
						if (marked[w]) continue;        
						if (e.darCostoHaversine() < distTo[w]) {
							distTo[w] = e.darCostoHaversine();
							edgeTo[w] = e;
							if (pq.contains(w)) pq.cambiarPrioridad(w,new idYCostoMin(w,distTo[w]));
							else                pq.agregar(new idYCostoMin(w,distTo[w]), w);;
						}
					}
				}  
			}
	}
	public InterseccionConCostos<Integer, Informacion> darVerticeDelMaxSubgrafo(){
		InterseccionConCostos<Integer, Informacion> verticeMax= null;
		int maximo= 0;
		for(InterseccionConCostos<Integer,Informacion> inter : grafoCon3Costos.darVertices())
		{    int cantidad=0;
		if(!inter.estaMarcado()){
			cantidad=grafoCon3Costos.dfsConSize(inter.darId());
		}
		if(cantidad>maximo){
			maximo=cantidad;
			verticeMax=inter;
		}
		}
		if(verticeMax!=null){
			grafoCon3Costos.uncheck();
			grafoCon3Costos.dfs(verticeMax.darId());
		}

		return verticeMax;
	}
	public static void main(String[] args) throws IOException {
		MVCModelo modelo = new MVCModelo();
	}
}
