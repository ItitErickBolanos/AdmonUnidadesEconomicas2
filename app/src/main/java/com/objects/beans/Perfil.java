package com.objects.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Perfil implements DatabaseCommunication{


	//region Propiedades
		public int PerfilID;
		public String Nombre,Actualizacion;
		public boolean Altas,Bajas,Cambios,Consultas;
		
		public String Respuesta;
		
		public Perfil perfil;
		public Perfil[] perfiles;
		
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public Perfil(){
		PerfilID = 0; Nombre = null; Altas = false; Bajas = false; Cambios = false;
		Consultas = false;
	}
	
	//Recibe un Objeto de tipo perfil y lo instancia a este objeto
	public Perfil(int PerfilID,String Nombre,boolean Altas,boolean Bajas,boolean Cambios,boolean Consultas,String Actualizacion){
		this.PerfilID = PerfilID; this.Nombre = Nombre; this.Actualizacion = Actualizacion;
		this.Altas = Altas; this.Bajas = Bajas; this.Cambios = Cambios; this.Consultas = Consultas;
	}
	
	//Constructor que recibe un ID de un usuario, y lo carga desde la base de datos.
	public Perfil(int PerfilID){ 
			Load(PerfilID);
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public int getPerfilID() {
		return PerfilID;
	}

	public void setPerfilID(int perfilID) {
		PerfilID = perfilID;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getActualizacion() {
		return Actualizacion;
	}

	public void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}

	public boolean isAltas() {
		return Altas;
	}

	public void setAltas(boolean altas) {
		Altas = altas;
	}

	public boolean isBajas() {
		return Bajas;
	}

	public void setBajas(boolean bajas) {
		Bajas = bajas;
	}

	public boolean isCambios() {
		return Cambios;
	}

	public void setCambios(boolean cambios) {
		Cambios = cambios;
	}

	public boolean isConsultas() {
		return Consultas;
	}

	public void setConsultas(boolean consultas) {
		Consultas = consultas;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Perfil[] getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(Perfil[] perfiles) {
		this.perfiles = perfiles;
	}
	//endregion
	
	//region Metodos de la Clase
	
	//endregion
	
	

	//region Metodos de la Interface 
	@Override //OK
	public boolean Load(int PerfilID) {
		// TODO Auto-generated method stub
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;
		
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadPerfil.php?opcion=1&PerfilID="+PerfilID);
		try{
				
			HttpResponse Respuesta = cliente.execute(peticionGet);
			HttpEntity contenidoRespuesta = Respuesta.getEntity();
			
			flujoDeRespuesta = contenidoRespuesta.getContent();
			
		}catch(ClientProtocolException e){
			e.printStackTrace();
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
			
			flujoDeRespuesta.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}

		
		Resultado = constructorRespuesta.toString();
			
		try{
			JSONArray arregloJSON = new JSONArray(Resultado);
			
			for(int i=0; i<arregloJSON.length(); i++){
				
				JSONObject objetoJSON = arregloJSON.getJSONObject(i);
				
				this.PerfilID = objetoJSON.getInt("PerfilID");
				
				Nombre= objetoJSON.getString("Nombre");
				Altas = objetoJSON.getBoolean("Altas");
				Bajas = objetoJSON.getBoolean("Bajas");
				Cambios = objetoJSON.getBoolean("Cambios");
				Consultas = objetoJSON.getBoolean("Consultas");
				
				Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(this.PerfilID!=0){
				RESULTADO_OK=true;
			}else{
				RESULTADO_OK=false;
			}
			
		}catch(JSONException e){
			e.printStackTrace();
		}
	
		return RESULTADO_OK;
	}

	//OK
	@Override
	public boolean Fetch() {
		boolean OPERACION_OK =false;
		
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		
		// TODO Auto-generated method stub
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=1");
		try{
			
			HttpResponse Respuesta = cliente.execute(peticionGET);
			HttpEntity contenidoRespuesta = Respuesta.getEntity();
			
			flujoDeRespuesta = contenidoRespuesta.getContent();
			
		}catch(ClientProtocolException e){
			e.printStackTrace();
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
				
				perfiles[i].PerfilID = objetoJSON.getInt("PerfilID");
				perfiles[i].Nombre = objetoJSON.getString("Nombre");
				perfiles[i].Altas = objetoJSON.getBoolean("Altas");
				perfiles[i].Bajas = objetoJSON.getBoolean("Bajas");
				perfiles[i].Cambios = objetoJSON.getBoolean("Cambios");
				perfiles[i].Consultas = objetoJSON.getBoolean("Consultas");
				perfiles[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(perfiles.length != 0 ){
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
	public boolean delete(int PerfilID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeletePerfil.php?PerfilID="+PerfilID);
		try{
			
			HttpResponse Respuesta = cliente.execute(peticionGET);
			HttpEntity contenidoRespuesta = Respuesta.getEntity();
					
			flujoDeRespuesta = contenidoRespuesta.getContent();
					
		}catch(ClientProtocolException e){
			e.printStackTrace();
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
				
				this.PerfilID = 0; Nombre = null; Altas = false; Bajas = false; Cambios = false; Consultas = false;
				Actualizacion = null;
				
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
		HttpClient cliente = new DefaultHttpClient();
		String Resultado = null;
		
		if(this.PerfilID==0){
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterPerfil.php");
			
			try{
				
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("Altas",String.valueOf(Altas)));
				datos.add(new BasicNameValuePair("Bajas",String.valueOf(Bajas)));
				datos.add(new BasicNameValuePair("Cambios",String.valueOf(Cambios)));
				datos.add(new BasicNameValuePair("Consultas",String.valueOf(Consultas)));
				datos.add(new BasicNameValuePair("Actualizacion",Actualizacion));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdatePerfil.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("PerfilID",String.valueOf(PerfilID)));
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("Altas",String.valueOf(Altas)));
				datos.add(new BasicNameValuePair("Bajas",String.valueOf(Bajas)));
				datos.add(new BasicNameValuePair("Cambios",String.valueOf(Cambios)));
				datos.add(new BasicNameValuePair("Consultas",String.valueOf(Consultas)));
				datos.add(new BasicNameValuePair("Actualizacion",Actualizacion));
				
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

				PerfilID = 0; Nombre = null; Altas = false; Bajas = false; Cambios = false; Consultas = false;
				Actualizacion = null;
				
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
