package model.data_structures;

public class Arco3Costos<K> {
	private K destino;
	private K origen;
	private double costoHaversine;
	private double costoCSV;
	private double costoVelXTim;
	
	public Arco3Costos(K pdestino, double pcosto, K pOrigen)
	{
		destino = pdestino;
		costoHaversine = pcosto;
		origen = pOrigen;
	}
	
	public K darDestino()
	{
		return destino;
	}
	public K darOrigen()
	{
		return origen;
	}
	
	public double darCostoHaversine()
	{
		return costoHaversine; 
	}
	
	public double darCostoCSV()
	{
		return costoCSV; 
	}
	
	public double darCostoVelXTim()
	{
		return costoVelXTim; 
	}
	
	public void cambiarCostoHaversine(double pcosto)
	{
		costoHaversine = pcosto;
	}
	
	public void cambiarCostoCSV(double pcosto)
	{
		costoCSV = pcosto;
	}
	
	public void cambiarCostoVelXTim(double pcosto)
	{
		costoVelXTim = pcosto;
	}

}
