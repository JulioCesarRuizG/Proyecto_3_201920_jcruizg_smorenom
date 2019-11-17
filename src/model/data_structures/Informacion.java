package model.data_structures;

public class Informacion {

	private double lat;
	private double lon;
	private int move;
	
	public Informacion(double plat, double plon, int pmove)
	{
		lat = plat;
		lon = plon;
		move=pmove;
	}
	
	public double darLatitud()
	{
		return lat;
	}
	
	public double darLongitud()
	{
		return lon;
	}
	
	public int darMovementeID()
	{
		return move;
	}
}
