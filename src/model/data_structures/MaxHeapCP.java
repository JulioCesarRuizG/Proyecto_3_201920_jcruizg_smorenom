package model.data_structures;

import java.util.ArrayList;

public class MaxHeapCP<T extends Comparable<T>> {

	private T[] llaves;
	private int size;

	public MaxHeapCP()
	{
		llaves = (T[]) new Comparable[1];
		size = 0;
	}

	public int darNumElementos()
	{return size;

	}

	public void agregar(T dato) 
	{     if (size == llaves.length - 1){
		T[] temp = (T[]) new Comparable[llaves.length*2];
		for (int i = 1; i <= size; i++) {
			temp[i] = llaves[i];
		}
		llaves = temp;
	}
	size++;
	llaves[size]= dato;
	swim(size);

	}
	private void swim(int posicion) {
		while (posicion > 1 && (llaves[posicion/2].compareTo(llaves[posicion])<0)) {

			T exch = llaves[posicion];
			llaves[posicion] = llaves[posicion/2];
			llaves[posicion/2] = exch;
			posicion = posicion/2;
		}
	}
	public T sacarMax() throws Exception{ 
		if (esVacia()) throw new Exception("No hay elementos en la cola de prioridad");
		T max = llaves[1];
		T exch = llaves[1];
		llaves[1] = llaves[size];
		llaves[size] = exch;
		size--;
		sink(1);
		llaves[size+1] = null;     
		if ((size > 0) && (size == (llaves.length - 1) / 4)){
			T[] temp = (T[]) new Comparable[llaves.length/2];
			for (int i = 1; i <= size; i++) {
				temp[i] = llaves[i];
			}
			llaves = temp;
			}
		return max;	
	}

	private void sink(int posicion) {
		while (2*posicion <= size) {
			int j = 2*posicion;
			if (j < size && (llaves[j].compareTo(llaves[j+1])<0)) j++;
			if (!(llaves[posicion].compareTo(llaves[j])<0)) break;
			T exch = llaves[posicion];
			llaves[posicion] = llaves[j];
			llaves[j] = exch;
			posicion = j;
		}
	}
	public T darMax()
	{
		return llaves[1];
	}

	public boolean esVacia()
	{
		if(size==0)
			return true;
		else 
			return false;
	}
	
	public T[] darLLaves(){
        return llaves;
    }
	
	public T darMinimo(){
        return llaves[size];
    }
    public T darMedio(){
        return llaves[size/2];
    }
}
