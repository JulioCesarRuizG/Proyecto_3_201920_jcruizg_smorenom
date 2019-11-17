package model.data_structures;

import java.util.Iterator;

public interface IQueue<T>{
	
	
	/**
	 * Agrega un objeto T a la cola
	 * @param valor a agregar
	 */
	public void enQueue(T valor);
	
	/**
	 * Elimina un objeto de la cola
	 * @return Sí lo elimina retorna el objeto, null en caso contrario
	 */
	public T deQueue();
	
	/**
	 * Comprueba que la cola esté vacía
	 * @return True si está vacía, false en caso contrario
	 */
	public boolean isEmpty();
	
	/**
	 * Devuelve el tamaño de la cola
	 * @return tamaño de la cola
	 */
	public int size();
	
	/**
	 * Convierte la cola de objetos en un iterator
	 * @return cola de objetos iterables
	 */
	public Iterator<T> iterator();

}
