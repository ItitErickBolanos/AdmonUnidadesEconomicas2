package com.objects.beans.listerBeans;

public class EstadoList {
//region PROPIEDADES DE LA CLASE
	public int EstadoID;
	public String Nombre;
	
//endregion

//region CONSTRUCTOR
	public EstadoList(){
		super();
		EstadoID = 0; Nombre = null;
	}
	public EstadoList(int EstadoID, String Nombre){
		this.EstadoID = EstadoID; this.Nombre = Nombre;
	}
	
	public EstadoList(EstadoList estadoList){
		
		EstadoID = estadoList.getEstadoID();
		Nombre = estadoList.getNombre();
		
	}
//endregion
	
//region GETTERS Y SETTERS
	
	public final int getEstadoID() {
		return EstadoID;
	}

	public final void setEstadoID(int EstadoID) {
		this.EstadoID = EstadoID;
	}

	public final String getNombre() {
		return Nombre;
	}

	public final void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
//endregion

//region TOSTRING
	@Override
	public String toString() {
		return "Estado: "+this.Nombre;
	}
//endregion
	
}
