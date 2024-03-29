package model.data_structures;

public class InterseccionConCostos<K, V> {

	private K id;
	private int CanArcos;
	private boolean check;
	private V info;
	private Interseccion<K, V> conectado;
	private Arco3Costos<K>[] arcos;

	public InterseccionConCostos(K pid, V pinfo,Arco3Costos<K>[] parcos)
	{
		id = pid;
		info = pinfo;
		if(parcos != null)
		{
			arcos = parcos;
		}
		else
		{
			arcos = new Arco3Costos[6];
		}
	}

	public int darCantidadArcos()
	{
		if(arcos == null)
		{
			return 0;
		}
		return CanArcos;
	}
	
	public Arco3Costos<K>[] darArcos()
	{
		return arcos;
	}

	public void agregarArco(Arco3Costos i)
	{
		if(arcos == null)
		{
			arcos[0] = i;
			CanArcos++;
		}
		else
		{
			for(int j = 1; j<6 ; j++)
			{
				if(arcos[j] == null)
				{
					arcos[j] = i;
				}
			}
			CanArcos++;
		}
	}
	
	public K darId()
	{
		return id;
	}
	
	public void cambiarInformacion(V newInfo)
	{
		info = newInfo;
	}
	
	public void marcar()
	{
		check = true;
	}
	
	public void desmarcar()
	{
		check = false;
	}
	
	public V darInfo()
	{
		return info;
	}
	
	public boolean estaMarcado()
	{
		return check;
	}
	
	public void conectadoA(Interseccion a)
	{
		conectado = a;
	}
	
	public Interseccion darConexion()
	{
		return conectado;
	}
}
