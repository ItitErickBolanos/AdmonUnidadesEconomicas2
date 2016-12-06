package com.objects.beans.listerBeans;

public class SectorList {
//region PROPIEDADES DE LA CLASE
	public int SectorID;
	public String Codigo,Nombre,Actualizacion;
	
//endregion

//region CONSTRUCTOR
	public SectorList(){
		super();
		SectorID = 0; Codigo = null; Nombre = null; Actualizacion = null;
	}
	public SectorList(int SectorID,String Codigo,String Nombre, String Actualizacion){
		this.SectorID = SectorID; this.Codigo = Codigo; this.Nombre = Nombre; this.Actualizacion = Actualizacion;
	}
	
	public SectorList(SectorList sectorList){
		
		SectorID = sectorList.getSectorID();
		Nombre = sectorList.getNombre();
		Codigo = sectorList.getCodigo();
		Actualizacion = sectorList.getActualizacion();
		
	}
//endregion
	
//region GETTERS Y SETTERS
	
	public final int getSectorID() {
		return SectorID;
	}

	public final void setSectorID(int sectorID) {
		SectorID = sectorID;
	}

	public final String getCodigo() {
		return Codigo;
	}

	public final void setCodigo(String codigo) {
		Codigo = codigo;
	}

	public final String getNombre() {
		return Nombre;
	}

	public final void setNombre(String nombre) {
		Nombre = nombre;
	}

	public final String getActualizacion() {
		return Actualizacion;
	}

	public final void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}
//endregion

//region TOSTRING
	@Override
	public String toString() {
		return "Codigo: "+this.Codigo+"\nNombre: "+this.Nombre;
	}
//endregion
	
}
