package test.data_structures;

import static org.junit.Assert.*;

import java.util.Iterator;

import model.data_structures.GrafoNoDirigido;
import model.data_structures.Informacion;
import model.data_structures.Interseccion;

import org.junit.Before;
import org.junit.Test;

public class GrafoNoDirigidoTest {

	private GrafoNoDirigido<Integer, Informacion> grafo;
	private static int CAPACIDAD=6;
	private int llave1 = 1;
	private int llave2 = 2;
	private int llave3 = 3;
	private int llave4 = 4;
	private int llave5 = 5;
	private int llave6 = 6;
	private Informacion value1 = new Informacion(1, 1, 1);
	private Informacion value2 = new Informacion(2, 2, 2);
	private Informacion value3 = new Informacion(3, 3, 3);
	private Informacion value4 = new Informacion(4, 4, 4);
	private Informacion value5 = new Informacion(5, 5, 5);
	private Informacion value6 = new Informacion(6, 6, 6);
	
	@Before
	public void setUp1() {
		grafo = new GrafoNoDirigido<Integer, Informacion>(CAPACIDAD);
		grafo.addVertex(llave1, value1);
		grafo.addVertex(llave2, value2);
		grafo.addVertex(llave3, value3);
		grafo.addVertex(llave4, value4);
		grafo.addVertex(llave5, value5);
	}
	
	@Test
	public void TestV()
	{
		setUp1();
		try {
			assertEquals("No se ha dado el valor adecuado", 5, grafo.V());
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo V");
		}
	}
	
	@Test
	public void TestE()
	{
		setUp1();
		grafo.setArc(llave1, llave2);
		grafo.setArc(llave3, llave4);
		try {
			assertEquals("No se ha dado el valor adecuado", 2, grafo.E());
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo E");
		}
	}
	
	@Test
	public void TestAddEdge()
	{
		setUp1();
		grafo.AddEdge(llave1, llave2, 3);
		int val = (int) grafo.getCostArc(1, 2);
		try {
			assertEquals("No es el valor esperado", 3, val);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Add Edge");
		}
	}
	
	@Test
	public void TestGetInfoVertex()
	{
		setUp1();
		Interseccion<Integer, Informacion> inter= grafo.getInfoVertex(llave1);
		int val1 = (int) inter.darInfo().darLatitud();
		int val2 = (int) inter.darInfo().darLongitud();
		int val3 = (int) inter.darInfo().darMovementeID();
		try {
			assertEquals("No es el valor esperado: lat", 1, val1);
			assertEquals("No es el valor esperado: lng", 1, val2);
			assertEquals("No es el valor esperado: moveID", 1, val3);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Get Info Vertex");
		}
	}
	
	@Test
	public void TestGetInfoVertex2()
	{
		setUp1();
		Informacion info= grafo.getInfoVertex2(llave1);
		int val1 = (int) info.darLatitud();
		int val2 = (int) info.darLongitud();
		int val3 = (int) info.darMovementeID();
		try {
			assertEquals("No es el valor esperado: lat", 1, val1);
			assertEquals("No es el valor esperado: lng", 1, val2);
			assertEquals("No es el valor esperado: moveID", 1, val3);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Get Info Vertex 2");
		}
	}
	
	@Test
	public void TestSetInforVertex()
	{
		setUp1();
		grafo.setInfoVertex(1, value2);
		Informacion info= grafo.getInfoVertex2(llave1);
		int val1 = (int) info.darLatitud();
		int val2 = (int) info.darLongitud();
		int val3 = (int) info.darMovementeID();
		try {
			assertEquals("No es el valor esperado: lat", 2, val1);
			assertEquals("No es el valor esperado: lng", 2, val2);
			assertEquals("No es el valor esperado: moveID", 2, val3);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Set Info Vertex");
		}
	}
	
	@Test
	public void TestGetCostArc()
	{
		setUp1();
		grafo.AddEdge(1, 2, 3);
		int costo = (int) grafo.getCostArc(llave1, llave2);
		try {
			assertEquals("No es el valor esperado: costo", 3, costo);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Get Cost Arc");
		}
	}
	
	@Test
	public void TestSetArc()
	{
		setUp1();
		grafo.setArc(1, 2);
		Interseccion<Integer, Informacion> inter = grafo.getInfoVertex(llave1);
		int val1 = inter.darArcos()[1].darDestino();
		try {
			assertEquals("No es el valor esperado", 2, val1);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Set Arc");
		}
	}
	
	@Test
	public void TestSetArcAndCost()
	{
		setUp1();
		grafo.setArcAndCost(1, 2, 3);
		Interseccion<Integer, Informacion> inter = grafo.getInfoVertex(llave1);
		int val1 = inter.darArcos()[1].darDestino();
		int val2 = (int) inter.darArcos()[1].darCosto();
		try {
			assertEquals("No es el valor esperado: Destino", 2, val1);
			assertEquals("No es el valor esperado: Costo", 3, val2);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Set Arc And Cost");
		}
	}
	
	@Test
	public void TestAddVertex()
	{
		setUp1();
		grafo.addVertex(llave6, value6);
		Interseccion<Integer, Informacion> inter = grafo.getInfoVertex(llave6);
		int val1 = (int) inter.darInfo().darLatitud();
		int val2 = (int) inter.darInfo().darLongitud();
		int val3 = (int) inter.darInfo().darMovementeID();
		try {
			assertEquals("No es el valor esperado: lat", 6, val1);
			assertEquals("No es el valor esperado: lng", 6, val2);
			assertEquals("No es el valor esperado: moveID", 6, val3);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Add Vertex");
		}
	}
	
	@Test
	public void TestAdj()
	{
		setUp1();
		grafo.setArc(llave1, llave2);
		grafo.setArc(llave3, llave4);
		Interseccion<Integer, Informacion> inter1 = grafo.getInfoVertex(llave2);
		Interseccion<Integer, Informacion> inter2 = grafo.getInfoVertex(llave4);
		Iterator<Integer> it1 = grafo.adj(llave1);
		Iterator<Integer> it2 = grafo.adj(llave3);
		try
		{
			int val1 = it1.next();
			int val2 = inter1.darId();
			int val3 = it2.next();
			int val4 = inter2.darId();
			assertEquals("No es el valor esperado", val2, val1);
			assertEquals("No es el valor esperado", val4, val3);
		}
		catch (Exception e) {
			fail("Error al usar el m�todo Adj");
		}
	}
	@Test 
	public void TestDfs(){
		
		setUp1();
		grafo.dfs(llave1);
		grafo.dfs(llave3);
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave1).estaMarcado());
		   
			assertEquals("No es el valor esperado", false,grafo.getInfoVertex(llave2).estaMarcado());
			assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave3).estaMarcado());
			assertEquals("No es el valor esperado", false,grafo.getInfoVertex(llave4).estaMarcado());
	}
	@Test
	public void Testcc()
	{
		setUp1();
		int x =grafo.cc();
		assertEquals("No es el valor esperado", 5, x);
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave1).estaMarcado());
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave2).estaMarcado());
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave3).estaMarcado());
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave4).estaMarcado());
		assertEquals("No es el valor esperado", true,grafo.getInfoVertex(llave5).estaMarcado());
	}
	@Test
	public void TestGetCC()
	{
		setUp1();
		Iterable<Integer> componente= grafo.getCC(llave1);
		int x = componente.iterator().next();
		assertEquals("No es el valor esperado", llave1,x );
	}
	
	@Test
	public void TestDarVertices()
	{
		setUp1();
		int i=1;
		try {
			for(Interseccion<Integer, Informacion> interseccion : grafo.darVertices())
			{
				if(interseccion != null)
				{
					Informacion info = interseccion.darInfo();
					int val1 = (int) info.darLatitud();
					int val2 = (int) info.darLongitud();
					int val3 = (int) info.darMovementeID();
					assertEquals("No es el valor esperado: lat", i, val1);
					assertEquals("No es el valor esperado: lng", i, val2);
					assertEquals("No es el valor esperado: moveID", i, val3);
				}
				i++;
			}
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Dar Vertices");
		}
	}
	
	@Test
	public void TestUncheck()
	{
		setUp1();
		Interseccion<Integer, Informacion> inter = grafo.getInfoVertex(llave1);
		inter.marcar();
		assertEquals("No es el valor esperado", true, inter.estaMarcado());
		grafo.uncheck();
		try {
			for(Interseccion<Integer, Informacion> interseccion : grafo.darVertices())
			{
				if(interseccion != null)
				{
					assertEquals("No es el valor esperado", false, interseccion.estaMarcado());
				}
			}
		}
		catch (Exception e) {
			fail("Se ha generado un error en el m�todo Uncheck");
		}
	}
}
