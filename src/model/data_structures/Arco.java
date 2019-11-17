package model.data_structures;

public class Arco<K> {

	private K destino;
	private double costo;
	
	public Arco(K pdestino, double pcosto)
	{
		destino = pdestino;
		costo = pcosto;
	}
	
	public K darDestino()
	{
		return destino;
	}
	
	public double darCosto()
	{
		return costo;
	}
	
	public void cambiarCosto(double pcosto)
	{
		costo = pcosto;
	}
}
