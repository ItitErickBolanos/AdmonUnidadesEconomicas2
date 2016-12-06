package com.objects.beans.listerBeans;

public class CiiuRev3List {
	
	//region PROPIEDADES DE LA CLASE
	public int CiiuRev4ID,CiiuRev3ID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public CiiuRev3List(){
		super();
		CiiuRev4ID = 0; CiiuRev3ID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public CiiuRev3List(int CiiuRev3ID,String Codigo,String Nombre,int CiiuRev4ID){
		
		this.CiiuRev4ID = CiiuRev4ID; this.Codigo = Codigo; this.Nombre = Nombre; this.CiiuRev3ID = CiiuRev3ID;
	}
	
	public CiiuRev3List(CiiuRev3List ciiuRev3){
		CiiuRev4ID = ciiuRev3.getCiiuRev4ID();
		Codigo = ciiuRev3.getCodigo();
		Nombre = ciiuRev3.getNombre();
		CiiuRev3ID = ciiuRev3.getCiiuRev3ID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getCiiuRev4ID() {
		return CiiuRev4ID;
	}

	public final void setCiiuRev4ID(int ciiuRev4ID) {
		CiiuRev4ID = ciiuRev4ID;
	}

	public final int getCiiuRev3ID() {
		return CiiuRev3ID;
	}

	public final void setCiiuRev3ID(int ciiuRev3ID) {
		CiiuRev3ID = ciiuRev3ID;
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
