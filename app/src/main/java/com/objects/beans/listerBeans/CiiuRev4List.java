package com.objects.beans.listerBeans;

public class CiiuRev4List {
	
	//region PROPIEDADES DE LA CLASE
	public int CiiuRev4ID,ScianID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public CiiuRev4List(){
		super();
		ScianID = 0; CiiuRev4ID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public CiiuRev4List(int CiiuRev4ID,String Codigo,String Nombre,int ScianID){
		
		this.ScianID = ScianID; this.Codigo = Codigo; this.Nombre = Nombre; this.CiiuRev4ID = CiiuRev4ID;
	}
	
	public CiiuRev4List(CiiuRev4List ciiuRev4){
		ScianID = ciiuRev4.getScianID();
		Codigo = ciiuRev4.getCodigo();
		Nombre = ciiuRev4.getNombre();
		CiiuRev4ID = ciiuRev4.getCiiuRev4ID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getCiiuRev4ID() {
		return CiiuRev4ID;
	}

	public final void setCiiuRev4ID(int ciiuRev4ID) {
		CiiuRev4ID = ciiuRev4ID;
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
