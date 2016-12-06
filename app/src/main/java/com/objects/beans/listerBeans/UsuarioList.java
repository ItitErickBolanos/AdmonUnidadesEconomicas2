package com.objects.beans.listerBeans;

public class UsuarioList {
//region PROPIEDADES DE LA CLASE
	public int UsuarioID;
	public String Loggin;
	
//endregion

//region CONSTRUCTOR
	public UsuarioList(){
		super();
		UsuarioID = 0; Loggin = null;
	}
	public UsuarioList(int UsuarioID, String Loggin){
		this.UsuarioID = UsuarioID; this.Loggin = Loggin;
	}
	
	public UsuarioList(UsuarioList usuarioList){
		
		UsuarioID = usuarioList.getUsuarioID();
		Loggin = usuarioList.getLoggin();
		
	}
//endregion
	
//region GETTERS Y SETTERS
	
	public final int getUsuarioID() {
		return UsuarioID;
	}

	public final void setUsuarioID(int usuarioID) {
		UsuarioID = usuarioID;
	}

	public final String getLoggin() {
		return Loggin;
	}

	public final void setLoggin(String loggin) {
		Loggin = loggin;
	}
//endregion

//region TOSTRING
	@Override
	public String toString() {
		return "Responsable: "+this.Loggin;
	}
//endregion
	
}
