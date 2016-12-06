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

public class Municipio implements DatabaseCommunication{

	//region Propiedades
		public int MunicipioID,EstadoID;
		public String Municipio,Actualizacion;
		
		public String Respuesta;
		
		public Municipio municipio;
		public Municipio[] municipios;
		
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public Municipio(){
		MunicipioID = 0; Municipio = null; EstadoID = 0; Actualizacion = null;
	}
	
	//Recibe un Objeto de tipo usuario y lo instancia a este objeto
	public Municipio(int MunicipioID,String Municipio,int EstadoID){
		this.MunicipioID = MunicipioID; this.EstadoID = EstadoID;
		this.Municipio = Municipio;
	}
	
	//Constructor que recibe un ID de un usuario, y lo carga desde la base de datos.
	public Municipio(int MunicipioID){ 
			Load(MunicipioID);
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public int getMunicipioID() {
		return MunicipioID;
	}

	public void setMunicipioID(int municipioID) {
		MunicipioID = municipioID;
	}

	public String getNombre() {
		return this.Municipio;
	}

	public void setNombre(String Municipio) {
		this.Municipio = Municipio;
	}

	public String getActualizacion() {
		return Actualizacion;
	}

	public void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	public int getEstadoID() {
		return this.EstadoID;
	}

	public void setEstadoID(int estadoID) {
		this.EstadoID = estadoID;
	}

	public Municipio[] getMunicipios() {
		return municipios;
	}

	public void setMunicipios(Municipio[] municipios) {
		this.municipios = municipios;
	}

	//endregion
	
	//region Metodos de la Clase
	
	//endregion
	
	//region Metodos de la Interface 
	
	@Override //OK
	public boolean Load(int MunicipioID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;
		
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadMunicipio.php?opcion=1&MunicipioID="+MunicipioID);
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
				
				this.MunicipioID = objetoJSON.getInt("MunicipioID");
				Municipio= objetoJSON.getString("Municipio");
				EstadoID = objetoJSON.getInt("EstadoID");
				Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(MunicipioID!=0){
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
				
				municipios[i].MunicipioID = objetoJSON.getInt("MunicipioID");
				municipios[i].Municipio = objetoJSON.getString("Municipio");
				municipios[i].EstadoID = objetoJSON.getInt("EstadoID");
				municipios[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(municipios.length != 0 ){
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
	public boolean delete(int MunicipioID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteMunicipio.php?MunicipioID="+MunicipioID);
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
				
				MunicipioID = 0; Municipio = null; EstadoID = 0; Actualizacion = null;
				
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
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterMunicipio.php");
			
			try{
				
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				datos.add(new BasicNameValuePair("Municipio",Municipio));
				datos.add(new BasicNameValuePair("EstadoID",String.valueOf(EstadoID)));
				datos.add(new BasicNameValuePair("Actualizacion",Actualizacion));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateMunicipio.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				datos.add(new BasicNameValuePair("Municipio",Municipio));
				datos.add(new BasicNameValuePair("EstadoID",String.valueOf(EstadoID)));
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

				MunicipioID = 0; Municipio = null; EstadoID = 0; Actualizacion = null;
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





