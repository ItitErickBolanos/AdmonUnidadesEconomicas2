package com.objects.beans;

public class Session {

	Usuario usuario;
	int UsuarioID;
	String Password,Loggin;
	
	public Session(Usuario Usuario){
		this.usuario = Usuario;
	}
	
	public Session(){
		
	}
	
	public void setUsuarioID(int UsuarioID){this.UsuarioID=UsuarioID;}
	public void setLoggin(String Loggin){this.Loggin=Loggin;}
	public void setPassword(String Password){this.Password=Password;}

	public int getUsuarioID(){return UsuarioID;}
	public String getLoggin(){return Loggin;}
	public String getPassword(){return Password;}
	public Session getSession(){return this;}
	
}
