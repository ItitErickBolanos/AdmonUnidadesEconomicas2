package com.objects.beans.listerBeans;

public class RamaList {
	
	//region PROPIEDADES DE LA CLASE
	public int RamaID,SubSectorID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public RamaList(){
		super();
		RamaID = 0; SubSectorID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public RamaList(int RamaID,String Codigo,String Nombre,int SubSectorID){
		
		this.RamaID = RamaID; this.Codigo = Codigo; this.Nombre = Nombre; this.SubSectorID = SubSectorID;
	}
	
	public RamaList(RamaList rama){
		RamaID = rama.getRamaID();
		Codigo = rama.getCodigo();
		Nombre = rama.getNombre();
		SubSectorID = rama.getSubSectorID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getSubSectorID() {
		return SubSectorID;
	}

	public final void setSubSectorID(int subSectorID) {
		SubSectorID = subSectorID;
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
