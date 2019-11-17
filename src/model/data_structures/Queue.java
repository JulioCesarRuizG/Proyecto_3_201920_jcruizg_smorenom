package model.data_structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Queue<T> implements Iterable<T>, IQueue<T>{

	private Node primero;
	private Node ultimo;

	/**
	 * Crea una nueva cola
	 */
	public Queue(Node pPrimero)
	{
		primero = pPrimero;
		Node actual = primero;
		while(actual!=null&&actual.darSiguiente() != null)
		{
			actual = actual.darSiguiente();
		}
		ultimo = actual;
	}

	/**
	 * Devuelve el primer viaje de la cola
	 * @return primer viaje
	 */
	public T darPrimero()
	{   if(primero!=null)
		return (T) primero.darItem();
	else return null;
	}
	public Node darContenedorPrimero()
	{
		return primero;
	}

	public Node darPrimerNodo()
	{
		return primero;
	}

	/**
	 * Devuelve el ultimo viaje de la cola
	 * @return ultimo viaje
	 */
	public T darUltimo()
	{
		  if(ultimo!=null)
				return (T) ultimo.darItem();
			else return null;
	}

	/**
	 * Agrega un objeto T a la cola
	 * @param valor a agregar
	 */
	public void enQueue(Object valor) 
	{
		Node i = new Node(valor, null);
		if(darPrimero() == null)
		{
			primero = i;
			ultimo =  i;
		}
		else
		{
			ultimo.cambiarSiguiente(i);
			ultimo = ultimo.darSiguiente();
		}
	}

	/**
	 * Elimina un objeto de la cola
	 * @return Sí lo elimina retorna el objeto, null en caso contrario
	 */
	public T deQueue() {

		if(darPrimero() == null)
		{
			return null;
		}
		else if(primero.darSiguiente() == null)
		{
			Node eliminar = primero;
			primero = null;
			ultimo = null;
			return  (T) eliminar.darItem();
		}
		else
		{
			Node eliminar = primero;
			primero = primero.darSiguiente();
			return  (T) eliminar.darItem();
		}
	}

	/**
	 * Comprueba que la cola esté vacía
	 * @return True si está vacía, false en caso contrario
	 */
	public boolean isEmpty() {
		if(darPrimero() == null)
		{
			return true;
		}
		else return false;
	}

	/**
	 * Devuelve el tamaño de la cola
	 * @return tamaño de la cola
	 */
	public int size() {
		if(darPrimero() == null)
		{
			return 0;
		}
		else
		{
			int cantidad = 1;
			Node actual = primero;
			while(actual.darSiguiente() != null)
			{
				actual = actual.darSiguiente();
				cantidad ++;
			}
			return cantidad;
		}
	}

	public Node darElemento(int i)
	{
		int valor = i;
		Node actual = this.darPrimerNodo();
		while(valor != 0)
		{
			if(actual.darSiguiente() != null)
			{
				actual = actual.darSiguiente();
			}
			valor --;
		}
		return actual;
	}

	 @Override
	    public Iterator<T> iterator() {
	        Iterator<T> it = new Iterator<T>() {

	            private int currentIndex = 0;
                private Queue<T> aux= new Queue<T>(primero);
	            @Override
	            public boolean hasNext() {
	                return currentIndex < size() && aux.primero != null;
	            }

	            @Override
	            public T next() {
	            	currentIndex++;
	                return aux.deQueue();
	            }

	         
	        };
	        return it;
	    }

}
