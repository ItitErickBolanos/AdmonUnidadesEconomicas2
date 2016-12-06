package com.objects.beans.listerBeans;

public class CiiuRev2List {
	
	//region PROPIEDADES DE LA CLASE
	public int CiiuRev3ID,CiiuRev2ID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public CiiuRev2List(){
		super();
		CiiuRev3ID = 0; CiiuRev2ID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public CiiuRev2List(int CiiuRev2ID,String Codigo,String Nombre,int CiiuRev3ID){
		
		this.CiiuRev3ID = CiiuRev3ID; this.Codigo = Codigo; this.Nombre = Nombre; this.CiiuRev2ID = CiiuRev2ID;
	}
	
	public CiiuRev2List(CiiuRev2List ciiuRev2){
		CiiuRev3ID = ciiuRev2.getCiiuRev3ID();
		Codigo = ciiuRev2.getCodigo();
		Nombre = ciiuRev2.getNombre();
		CiiuRev2ID = ciiuRev2.getCiiuRev2ID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getCiiuRev3ID() {
		return CiiuRev3ID;
	}

	public final void setCiiuRev3ID(int ciiuRev3ID) {
		CiiuRev3ID = ciiuRev3ID;
	}

	public final int getCiiuRev2ID() {
		return CiiuRev2ID;
	}

	public final void setCiiuRev2ID(int ciiuRev2ID) {
		CiiuRev2ID = ciiuRev2ID;
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
