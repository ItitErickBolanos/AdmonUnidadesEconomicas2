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

public class Localidad implements DatabaseCommunication{


	//region Propiedades
		public int LocalidadID,MunicipioID;
		public String Localidad,Actualizacion;
		
		public String Respuesta;
		
		public Localidad localidad;
		public Localidad[] localidades;
		
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public Localidad(){
		LocalidadID = 0; MunicipioID = 0; Localidad = null; Actualizacion = null;
	}
	
	//Recibe un Objeto de tipo usuario y lo instancia a este objeto
	public Localidad(int LocalidadID,String Localidad,String Actualizacion,int MunicipioID){
		this.LocalidadID = LocalidadID; this.MunicipioID = MunicipioID;
		this.Localidad = Localidad;	this.Actualizacion = Actualizacion;
	}
	
	//Constructor que recibe un ID de un usuario, y lo carga desde la base de datos.
	public Localidad(int LocalidadID){ 
			Load(LocalidadID);
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public int getLocalidadID() {
		return LocalidadID;
	}

	public void setLocalidadID(int localidadID) {
		LocalidadID = localidadID;
	}

	public int getMunicipioID() {
		return MunicipioID;
	}

	public void setMunicipioID(int municipioID) {
		MunicipioID = municipioID;
	}

	public String getNombre() {
		return Localidad;
	}

	public void setLocalidad(String localidad) {
		Localidad = localidad;
	}

	public String getActualizacion() {
		return Actualizacion;
	}

	public void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public Localidad[] getLocalidades() {
		return localidades;
	}

	public void setLocalidades(Localidad[] localidades) {
		this.localidades = localidades;
	}
	//endregion
	
	//region Metodos de la Clase
	
	//endregion
	
	//region Metodos de la Interface 
	
	@Override //OK
	public boolean Load(int LocalidadID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;
		
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadLocalidad.php?opcion=1&LocalidadID="+LocalidadID);
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
				
				LocalidadID= objetoJSON.getInt("LocalidadID");
				this.Localidad= objetoJSON.getString("Localidad");
				this.MunicipioID = objetoJSON.getInt("MunicipioID");
				Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(this.LocalidadID!=0){
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
				
				localidades[i].LocalidadID = objetoJSON.getInt("LocalidadID");
				localidades[i].Localidad = objetoJSON.getString("Municipio");
				localidades[i].MunicipioID = objetoJSON.getInt("EstadoID");
				localidades[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(localidades.length != 0 ){
				OPERACION_OK = true;
			}else{
				OPERACION_OK = false;
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		//Este codigo es para devolver un elemento List View con los Usuarios Obtenidos del objeto JSON
		//ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Clientes>(contexto, android.R.layout.Municipio_ListView);
		
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
	public boolean delete(int LocalidadID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteMunicipio.php?LocalidadID="+LocalidadID);
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
				
				this.LocalidadID = 0; MunicipioID = 0; Localidad = null; Actualizacion = null;
				
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
		
		if(MunicipioID==0){
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterLocalidad.php");
			
			try{
				
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("Localidad",Localidad));
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				datos.add(new BasicNameValuePair("Actualizacion",Actualizacion));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateLocalidad.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("LocalidadID",String.valueOf(LocalidadID)));
				datos.add(new BasicNameValuePair("Localidad",Localidad));
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
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

				LocalidadID = 0; Localidad = null; MunicipioID = 0; Actualizacion = null;
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
