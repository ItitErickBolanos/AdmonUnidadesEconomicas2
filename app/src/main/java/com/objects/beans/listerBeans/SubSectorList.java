package com.objects.beans.listerBeans;

public class SubSectorList {
	
	//region PROPIEDADES DE LA CLASE
	public int SubSectorID,SectorID;
	public String Codigo,Nombre,Actualizacion;
	//endregion
	
	//region CONSTRUCTORES
	public SubSectorList(){
		super();
		SubSectorID = 0; SectorID =0; Codigo = null; Nombre = null; Actualizacion = null;
	}

	public SubSectorList(int SubSectorID,String Codigo,String Nombre,int SectorID){
		
		this.SubSectorID = SubSectorID; this.Codigo = Codigo; this.Nombre = Nombre; this.SectorID = SectorID;
	}
	
	public SubSectorList(SubSectorList subsector){
		SubSectorID = subsector.getSubSectorID();
		Codigo = subsector.getCodigo();
		Nombre = subsector.getNombre();
		SectorID = subsector.getSectorID();
	}
	//endregion
	
	//region GETTERS AND SETTERS
	public final int getSubSectorID() {
		return SubSectorID;
	}

	public final void setSubSectorID(int subSectorID) {
		SubSectorID = subSectorID;
	}

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
	//endregion
	
	//region toString Heredado
	@Override
	public String toString() {
		return "Codigo: "+this.Codigo+"\nNombre: "+this.Nombre;
	}
	
	//endregion
	
}
