package com.objects.beans;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements DatabaseCommunication{
	
	//region Propiedades
		public int UsuarioID,PerfilID,DatosID;
		public String Loggin,Password,Actualizacion,Respuesta,Email;
		public Usuario usuario;
		public Usuario[] usuarios;
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public Usuario(){
		UsuarioID=0; Loggin=null; Password=null; PerfilID=0; Actualizacion=null; DatosID=0;
	}
	
	//Recibe un Objeto de tipo usuario y lo instancia a este objeto
	public Usuario(Usuario usuario){
		this.UsuarioID = usuario.UsuarioID;	this.PerfilID = usuario.PerfilID;
		this.Loggin = usuario.Loggin; this.Password = usuario.Password; this.Actualizacion = usuario.Actualizacion;
		this.EDITANDO_OK = usuario.EDITANDO_OK;	this.usuario  = usuario.usuario; this.usuarios = usuario.usuarios;
	}
	
	//Constructor que recibe un ID de un usuario, y lo carga desde la base de datos.
	public Usuario(int UsuarioID){ 
			Load(UsuarioID);
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public void setLoggin(String Loggin,String Password){
		this.Loggin=Loggin;	this.Password=Password;
	}
	
	public int getUsuarioID() {
		return UsuarioID;
	}
	
	public void setUsuarioID(int usuarioID) {
		UsuarioID = usuarioID;
	}
	
	public int getPerfilID() {
		return PerfilID;
	}
	
	public void setPerfilID(int perfilID) {
		PerfilID = perfilID;
	}

	public int getDatosID() {
		return DatosID;
	}
	
	public void setDatosID(int datosID) {
		DatosID = datosID;
	}
	
	public String getLoggin() {
		return Loggin;
	}
	
	public void setLoggin(String loggin) {
		Loggin = loggin;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String password) {
		Password = password;
	}
	
	public String getActualizacion() {
		return Actualizacion;
	}
	
	public void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public Usuario getUsuario(){
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario[] getUsuarios(){
		return usuarios;
	}
	
	public void setUsuarios(Usuario[] usuarios) {
		this.usuarios = usuarios;
	}
	//endregion
	
	//region Metodos de la Clase
	public boolean Loggin() throws IOException{
		boolean RESULTADO_OK = false;
		
		//metodo que carga un usuario a partir del Loggin, cada metodo se implementara diferente
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado = "Fallo";

		try{

            //Seccion donde se genera la peticion al servidor, y se obtiene la respuesta
            URLConnection conexion = new URL("http://semestral.esy.es/ConvertidorUnidades/LoadUser.php?opcion=2&Loggin="+Loggin+"&Password="+Password).openConnection();

            flujoDeRespuesta = conexion.getInputStream();

		} catch(IOException e){
			e.printStackTrace();
		}
		
		//Lectura de el Objeto de FlujoDeRespuesta( contiene todo lo que se obtuvo del phpscript)
		BufferedReader lecturaDeRespuesta = new BufferedReader(new InputStreamReader(flujoDeRespuesta));
		StringBuilder constructorRespuesta = new StringBuilder();
		String Linea = null;
		
		try{
			while((Linea = lecturaDeRespuesta.readLine())!= null){
				constructorRespuesta.append(Linea);
			}
			
			flujoDeRespuesta.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Resultado = constructorRespuesta.toString();
		
		try{
			JSONArray arregloJSON = new JSONArray(Resultado);
			for(int i=0; i < arregloJSON.length();i++){
				
				JSONObject objetoJSON = arregloJSON.getJSONObject(i);
				
				UsuarioID = objetoJSON.getInt("UsuarioID");
				Loggin = objetoJSON.getString("Loggin");
				Password = objetoJSON.getString("Password");
				PerfilID = objetoJSON.getInt("PerfilID");
				DatosID = objetoJSON.getInt("DatosID");
				Email = objetoJSON.getString("Email");
				Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			//condicionamos si el usuario es dif de 0 entonces encontro un usuario.
			if(UsuarioID!=0)
				RESULTADO_OK = true;
			else
				RESULTADO_OK=false;
				
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		//Este codigo es para devolver un elemento List View con los Usuarios Obtenidos del objeto JSON
		//ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Clientes>(contexto, android.R.layout.nombre_ListView);
		
		return RESULTADO_OK;
	}
	//endregion
	
	//region Metodos de la Interface 
	
	@Override
	public boolean Load(int UsuarioID) {
		// TODO Auto-generated method stub
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;

        try {
            URLConnection conexion = new URL("http://semestral.esy.es/ConvertidorUnidades/LoadUser.php?opcion=1&UsuarioID=" + UsuarioID).openConnection();

            flujoDeRespuesta = conexion.getInputStream();

        } catch (IOException e){
           throw new RuntimeException(e);
        }
					
			//Lectura de el Objeto de FlujoDeRespuesta( contiene todo lo que se obtuvo del phpscript)
		BufferedReader lecturaDeRespuesta = new BufferedReader(new InputStreamReader(flujoDeRespuesta));
		StringBuilder constructorRespuesta = new StringBuilder();
		String Linea = null;
			
		try{
			while((Linea = lecturaDeRespuesta.readLine())!= null){
				constructorRespuesta.append(Linea);
			}
			
			flujoDeRespuesta.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}

		
		Resultado = constructorRespuesta.toString();
			
		try{
			//JSONArray arregloJSON = new JSONArray(Resultado); USAR CUANDO ES FETCH PARA OBTENER TODOS CON UN FOR
			JSONObject objetoJSON = new JSONObject(Resultado);
			this.UsuarioID = objetoJSON.getInt("UsuarioID");
			Loggin= objetoJSON.getString("Loggin");
			Password = objetoJSON.getString("Password");
			PerfilID = objetoJSON.getInt("PerfilID");
			Actualizacion = objetoJSON.getString("Actualizacion");
			DatosID = objetoJSON.getInt("DatosID");
				
			if(UsuarioID!=0){
				RESULTADO_OK=true;
			}else{
				RESULTADO_OK=false;
			}
			
		}catch(JSONException e){
			e.printStackTrace();
		}
	
		return RESULTADO_OK;
	}

	@Override
	public boolean Fetch() {
		boolean OPERACION_OK =false;
		
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";

        try{

            URLConnection conexion = new URL("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=1").openConnection();
			
			flujoDeRespuesta = conexion.getInputStream();

		}catch(IOException e){
			e.printStackTrace();
		}
		
		//Lectura de el Objeto de FlujoDeRespuesta( contiene todo lo que se obtuvo del phpscript)
		BufferedReader lecturaDeObjeto = new BufferedReader(new InputStreamReader(flujoDeRespuesta));
		StringBuilder constructorRespuesta = new StringBuilder();
		String Linea = null;
		
		try{
			while((Linea = lecturaDeObjeto.readLine())!= null){
				constructorRespuesta.append(Linea);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			
			flujoDeRespuesta.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Resultado = constructorRespuesta.toString();
		
		try{
			JSONArray arregloJSON = new JSONArray(Resultado);
			for(int i=0; i<arregloJSON.length();i++){
				JSONObject objetoJSON = arregloJSON.getJSONObject(i);
				usuarios[i].UsuarioID = objetoJSON.getInt("UsuarioID");
				usuarios[i].Loggin = objetoJSON.getString("Loggin");
				usuarios[i].Password = objetoJSON.getString("Password");
				usuarios[i].PerfilID = objetoJSON.getInt("PerfilID");
				usuarios[i].Actualizacion = objetoJSON.getString("Actualizacion");
				usuarios[i].DatosID = objetoJSON.getInt("DatosID");
				
			}
			if(usuarios.length != 0 ){
				OPERACION_OK = true;
			}else{
				OPERACION_OK = false;
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		//Este codigo es para devolver un elemento List View con los Usuarios Obtenidos del objeto JSON
		//ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Clientes>(contexto, android.R.layout.nombre_ListView);
		
		return OPERACION_OK;
	}

	@Override
	public boolean beginEdit() {
		//este metodo solo debe serinvocado una vez usado el metodo Load(int UsuarioID); para poner ese
		//objeto en modo edicion.
		EDITANDO_OK=true;
		
		return EDITANDO_OK;
	}

	@Override
	public boolean delete(int UsuarioID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		try{
            URLConnection conexion = new URL("http://semestral.esy.es/ConvertidorUnidades/DeleteUser.php?UsuarioID="+UsuarioID).openConnection();

			flujoDeRespuesta = conexion.getInputStream();

		}catch(IOException e){
			e.printStackTrace();
		}
		
		//Lectura de el Objeto de FlujoDeRespuesta( contiene todo lo que se obtuvo del phpscript)
		BufferedReader lecturaDeRespuesta = new BufferedReader(new InputStreamReader(flujoDeRespuesta));
		StringBuilder constructorRespuesta = new StringBuilder();
		String Linea = null;
		
		try{
			while((Linea = lecturaDeRespuesta.readLine())!= null){
				constructorRespuesta.append(Linea);
			}
			}catch(IOException e){
			e.printStackTrace();
		}
		
		try{				
			flujoDeRespuesta.close();
		}catch(IOException e){
			e.printStackTrace();
		}
						
		Resultado = constructorRespuesta.toString();
				
		try{
			
			JSONObject respuestaJSON = new JSONObject(Resultado);
			
			Respuesta = respuestaJSON.getString("Respuesta");
			
			if(Respuesta == "OK"){
				
				Loggin = null;
				Password=null;
				UsuarioID=0;
				PerfilID=0;
				Actualizacion=null;
				DatosID=0;
				Email=null;
				Respuesta=null;
				RESULTADO_OK = true;
				
			}else{
				
				RESULTADO_OK = false;
				
			}
							
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return RESULTADO_OK;
	}

	@Override
	public boolean applyEdit() {
		InputStream flujoDeRespuesta=null;
		boolean RESULTADO_OK = false;
		String Resultado = null;
		
		if(UsuarioID == 0){

            try {

                //PENDIENTE REVISAR INPUT STREAM Y HTTP POST

                URL url = new URL("http://semestral.esy.es/ConvertidorUnidades/RegisterUser.php");
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setReadTimeout(10000);
                conexion.setConnectTimeout(15000);
                conexion.setRequestMethod("POST");
                conexion.setDoInput(true);
                conexion.setDoOutput(true);

                List<Pair<String, String>> datos = new ArrayList<>();
                datos.add(new Pair("Loggin",Loggin));
                datos.add(new Pair("Password",Password));
                datos.add(new Pair("PerfilID", String.valueOf(PerfilID)));
                datos.add(new Pair("DatosID", String.valueOf(DatosID)));
                datos.add(new Pair("Email",Email));

                flujoDeRespuesta = conexion.getInputStream();

            } catch (IOException e){
                e.printStackTrace();
            }


			try{

				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						

				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateUser.php");
			
			try{
				List<Pair<String, String>> datos = new ArrayList<>();
				datos.add(new Pair("UsuarioID",String.valueOf(UsuarioID)));
				datos.add(new Pair("Loggin",Loggin));
				datos.add(new Pair("Password",Password));
				datos.add(new Pair("PerfilID", String.valueOf(PerfilID)));
				datos.add(new Pair("DatosID", String.valueOf(DatosID)));
				datos.add(new Pair("Email",Email));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Lectura de el Objeto de FlujoDeRespuesta( contiene todo lo que se obtuvo del phpscript)
		BufferedReader lecturaDeRespuesta = new BufferedReader(new InputStreamReader(flujoDeRespuesta));
		StringBuilder constructorRespuesta = new StringBuilder();
		String Linea = null;

		try{
			while((Linea = lecturaDeRespuesta.readLine())!= null){
				constructorRespuesta.append(Linea);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		try{				
			flujoDeRespuesta.close();
		}catch(IOException e){
			e.printStackTrace();
		}

		Resultado = constructorRespuesta.toString();
		
		try{

			JSONObject respuestaJSON = new JSONObject(Resultado);
			Respuesta = respuestaJSON.getString("Respuesta");

			if(Respuesta == "OK"){

				Loggin = null; Password=null; UsuarioID=0; PerfilID=0; Actualizacion=null; DatosID=0; Email=null;
				Respuesta=null;	RESULTADO_OK = true;

			}else{
				RESULTADO_OK = false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULTADO_OK;
	}

	//endregion

}