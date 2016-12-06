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

public class ZonaEstudio implements DatabaseCommunication{
	

	//region Propiedades
		public int ProyectoID, MunicipioID,EstadoID;
		public String Actualizacion;

		public String Respuesta;

		public ZonaEstudio zonestudio;
		public ZonaEstudio[] zonasEstudioArray;
		
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public ZonaEstudio(){
		ProyectoID = 0; EstadoID=0; MunicipioID=0; Actualizacion=null;
		Respuesta = null;
	}
	
	//Recibe un Objeto de tipo subsector y lo instancia a este objeto
	public ZonaEstudio(int ProyectoID,int EstadoID,int MunicipioID){
		 this.ProyectoID = ProyectoID; this.EstadoID = EstadoID; this.MunicipioID = MunicipioID;		
	}
	
	//Constructor que recibe un ID de un subsector, y lo carga desde la base de datos.
	public ZonaEstudio(ZonaEstudio zonaEstudio){ 
		this.zonestudio = zonaEstudio;
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public int getProyectoID() {
		return ProyectoID;
	}

	public void setProyectoID(int proyectoID) {
		ProyectoID = proyectoID;
	}

	public int getMunicipioID() {
		return MunicipioID;
	}

	public void setMunicipioID(int municipioID) {
		MunicipioID = municipioID;
	}

	public int getEstadoID() {
		return EstadoID;
	}

	public void setEstadoID(int estadoID) {
		EstadoID = estadoID;
	}

	public String getActualizacion() {
		return Actualizacion;
	}

	public void setActualizacion(String actualizacion) {
		Actualizacion = actualizacion;
	}

	public ZonaEstudio getZonestudio() {
		return zonestudio;
	}

	public void setZonestudio(ZonaEstudio zonestudio) {
		this.zonestudio = zonestudio;
	}

	public ZonaEstudio[] getZonasEstudioArray() {
		return zonasEstudioArray;
	}

	public void setZonasEstudioArray(ZonaEstudio[] zonasEstudioArray) {
		this.zonasEstudioArray = zonasEstudioArray;
	}
	//endregion
	
	//region Metodos de la Interface
	@Override
	public boolean Load(int ProyectoID) {
		// TODO Auto-generated method stub
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;
		
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadZonaEstudio.php?opcion=1&ProyectoID="+ProyectoID);
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
				
				zonasEstudioArray[i].ProyectoID = objetoJSON.getInt("ProyectoID");
				zonasEstudioArray[i].EstadoID = objetoJSON.getInt("EstadoID");
				zonasEstudioArray[i].MunicipioID = objetoJSON.getInt("MunicipioID");
				zonasEstudioArray[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			
			if(zonasEstudioArray.length != 0){
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
				
				zonasEstudioArray[i].ProyectoID = objetoJSON.getInt("ProyectoID");
				zonasEstudioArray[i].EstadoID = objetoJSON.getInt("EstadoID");
				zonasEstudioArray[i].MunicipioID = objetoJSON.getInt("MunicipioID");
				zonasEstudioArray[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(zonasEstudioArray.length != 0 ){
				OPERACION_OK = true;
			}else{
				OPERACION_OK = false;
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		//Este codigo es para devolver un elemento List View con los subsectores Obtenidos del objeto JSON
		//ArrayAdapter<subsector> adaptador = new ArrayAdapter<Clientes>(contexto, android.R.layout.nombre_ListView);
		
		return OPERACION_OK;
	}

	@Override
	public boolean beginEdit() {
		//este metodo solo debe ser invocado una vez usado el metodo Load(int RamaID); para poner ese
		//objeto en modo edicion.
		EDITANDO_OK=true;
		
		return EDITANDO_OK;
	}

	@Override
	public boolean delete(int ProyectoID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteZonaEstudio.php?ProyectoID="+ProyectoID);
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
				
				this.ProyectoID = 0; EstadoID = 0; MunicipioID = 0; Actualizacion  = null;
				Respuesta = null; RESULTADO_OK = true;
				
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
		
		if(ProyectoID==0){
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterZonaEstudio.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("ProyectoID",String.valueOf(ProyectoID)));
				datos.add(new BasicNameValuePair("EstadoID",String.valueOf(EstadoID)));
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateZonaEstudio.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("ProyectoID",String.valueOf(ProyectoID)));
				datos.add(new BasicNameValuePair("EstadoID",String.valueOf(EstadoID)));
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				
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

				ProyectoID = 0; EstadoID = 0; MunicipioID = 0; Actualizacion = null;
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
