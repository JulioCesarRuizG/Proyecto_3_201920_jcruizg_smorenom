package model.data_structures;

public class Node<T> {

	/**
	 * Atributos de la clase node
	 */
	private T item;
	private Node siguiente;
	
	/**
	 * Constructor de la clase Node
	 */
	public Node(T pItem, Node pSiguiente)
	{
		item = pItem;
		siguiente = pSiguiente;
	}
	
	/**
	 * Devuelve el objeto que guarda el nodo
	 * @return item
	 */
	public T darItem()
	{
		return item;
	}
	
	/**
	 * Devuelve al nodo que estamos apuntando
	 * @return siguiente
	 */
	public Node darSiguiente()
	{
		return siguiente;
	}
	
	public void cambiarSiguiente(Node pNuevo)
	{
		siguiente = pNuevo;
	}
	
	public void cambiarItem(T nuevo)
	{
		item = nuevo;
	}
}
