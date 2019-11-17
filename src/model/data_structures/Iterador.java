package model.data_structures;

public class Iterador<K> {
	
	public Node primero;
	public Node actual;
	public Iterador(K[] lista)
	{   
		primero = new Node<K>(lista[0], null);
		 actual=null;
		Node actualS = primero;
		for(int i=1 ; i<lista.length ; i++)
		{
			Node k = new Node<K>(lista[i], null);
			actual.cambiarSiguiente(k);
			actual = actual.darSiguiente();
		}
	}
	
	public boolean hasNext()
	{    
		if(primero==null)
			return false;
		if(actual!=null&&actual.darSiguiente() == null)
		{
			return false;
		}
	
		
		return true;
	}
	
	public K Next()
	{        if(actual==null&&primero!=null) {
		 actual=primero;
		return (K) primero.darItem();} else {actual=actual.darSiguiente();
		return (K) actual.darItem();
	}
}}
