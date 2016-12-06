package com.objects.beans.listerBeans;

public class SubRamaList {
	
	//region PROPIEDADES DE LA CLASE
	public int SubRamaID,RamaID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public SubRamaList(){
		super();
		RamaID = 0; SubRamaID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public SubRamaList(int SubRamaID,String Codigo,String Nombre,int RamaID){
		
		this.RamaID = RamaID; this.Codigo = Codigo; this.Nombre = Nombre; this.SubRamaID = SubRamaID;
	}
	
	public SubRamaList(SubRamaList subrama){
		RamaID = subrama.getRamaID();
		Codigo = subrama.getCodigo();
		Nombre = subrama.getNombre();
		SubRamaID = subrama.getSubRamaID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getSubRamaID() {
		return SubRamaID;
	}

	public final void setSubRamaID(int subRamaID) {
		SubRamaID = subRamaID;
	}

	public final int getRamaID() {
		return RamaID;
	}

	public final void setRamaID(int ramaID) {
		RamaID = ramaID;
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
	//endregion
	
	//region toString Heredado
	@Override
	public String toString() {
		return "Codigo: "+this.Codigo+"\nNombre: "+this.Nombre;
	}
	
	//endregion
	
}
