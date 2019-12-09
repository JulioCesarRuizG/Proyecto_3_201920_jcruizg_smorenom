package view;

import model.logic.MVCModelo;

public class MVCView 
{
	    /**
	     * Metodo constructor
	     */
	    public MVCView()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Crear grafo");
			System.out.println("2. Encontrar el camino de costo m�nimo entre dos puntos");
			System.out.println("3. Determinar los n v�rtices con menor velocidad promedio");
			System.out.println("4. Calcular el �rbol de expansi�n m�nima con prim");
			System.out.println("5. Encontrar el camino de menor costo con criterio haversine");
			System.out.println("6. Localizaciones alcanzables");
			System.out.println("7. Calcular el �rbol de expansi�n m�nima con kruzkal");
			System.out.println("8. Grafo simplificado");
			System.out.println("9. Camino de costo m�nimo con tiempo promedio");
			System.out.println("10. Camino m�s largo");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}		
		
		public void printModelo(MVCModelo modelo)
		{
			// TODO implementar
		}
}
