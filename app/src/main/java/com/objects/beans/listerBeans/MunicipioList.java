package com.objects.beans.listerBeans;

public class MunicipioList {
//region PROPIEDADES DE LA CLASE
	public int MunicipioID;
	public String Municipio;
	public int EstadoID;
	
//endregion

//region CONSTRUCTOR
	public MunicipioList(){
		super();
		MunicipioID = 0; Municipio = null;
	}
	public MunicipioList(int MunicipioID, String Municipio, int EstadoID){
		this.MunicipioID = MunicipioID; this.Municipio = Municipio; this.EstadoID = EstadoID;
	}
	
	public MunicipioList(MunicipioList usuarioList){
		
		MunicipioID = usuarioList.getMunicipioID();
		Municipio = usuarioList.getMunicipio();
		
	}
//endregion
	
//region GETTERS Y SETTERS
	
	public final int getMunicipioID() {
		return MunicipioID;
	}

	public final void setMunicipioID(int MunicipioID) {
		this.MunicipioID = MunicipioID;
	}
	
	public final int getEstadoID() {
		return MunicipioID;
	}

	public final void setEstadoID(int EstadoID) {
		this.EstadoID = EstadoID;
	}

	public final String getMunicipio() {
		return Municipio;
	}

	public final void setMunicipio(String Municipio) {
		this.Municipio = Municipio;
	}
//endregion

//region TOSTRING
	@Override
	public String toString() {
		return "Municipio: "+this.Municipio;
	}
//endregion
	
}
