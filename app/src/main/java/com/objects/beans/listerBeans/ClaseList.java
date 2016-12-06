package com.objects.beans.listerBeans;

public class ClaseList {
	
	//region PROPIEDADES DE LA CLASE
	public int SubRamaID,ScianID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public ClaseList(){
		super();
		ScianID = 0; SubRamaID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public ClaseList(int ScianID,String Codigo,String Nombre,int SubRamaID){
		
		this.ScianID = ScianID; this.Codigo = Codigo; this.Nombre = Nombre; this.SubRamaID = SubRamaID;
	}
	
	public ClaseList(ClaseList clase){
		ScianID = clase.getScianID();
		Codigo = clase.getCodigo();
		Nombre = clase.getNombre();
		SubRamaID = clase.getSubRamaID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getSubRamaID() {
		return SubRamaID;
	}

	public final void setSubRamaID(int subRamaID) {
		SubRamaID = subRamaID;
	}

	public final int getScianID() {
		return ScianID;
	}

	public final void setScianID(int scianID) {
		ScianID = scianID;
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
