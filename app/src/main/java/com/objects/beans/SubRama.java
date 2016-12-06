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

public class SubRama implements DatabaseCommunication {


	//region Propiedades
		public int SubRamaID, RamaID;
		public String Codigo,Nombre, Actualizacion;

		public String Respuesta;

		public SubRama SubRama;
		public SubRama[] SubRamas;
		
		public boolean EDITANDO_OK=false;
		public boolean RESULTADO_OK;
	//endregion
		
	//region CONSTRUCTORS
	public SubRama(){
		SubRamaID = 0; Codigo = null; Nombre =null; RamaID = 0; Actualizacion=null;
		
		Respuesta = null;
	}
	
	//Recibe un Objeto de tipo subsector y lo instancia a este objeto
	public SubRama(int SubRamaID,String Codigo,String Nombre ,int RamaID){
		 this.SubRamaID = SubRamaID; this.Codigo = Codigo; this.Nombre = Nombre; this.RamaID = RamaID;
		
	}
	
	//Constructor que recibe un ID de un subsector, y lo carga desde la base de datos.
	public SubRama(int SubRamaID){ 
		Load(SubRamaID);
	}
	//endregion
	
	//region "GETTERS AND SETTERS"
	public int getSubRamaID() {
		return SubRamaID;
	}

	public void setSubRamaID(int SubRamaID) {
		this.SubRamaID = SubRamaID;
	}

	public int getRamaID() {
		return RamaID;
	}

	public void setRamaID(int RamaID) {
		this.RamaID = RamaID;
	}

	public String getCodigo() {
		return Codigo;
	}

	public void setCodigo(String codigo) {
		Codigo = codigo;
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

	public SubRama getRama() {
		return SubRama;
	}

	public void setSubRama(SubRama SubRama) {
		this.SubRama = SubRama;
	}

	public SubRama[] getSubRamas() {
		return SubRamas;
	}

	public void setSubRama(SubRama[] SubRama) {
		this.SubRamas = SubRama;
	}
	//endregion
	
	//region Metodos de la Interface 
	@Override
	public boolean Load(int SubRamaID) {
		// TODO Auto-generated method stub
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		RESULTADO_OK = false;
		
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadSubRama.php?opcion=1&SubRamaID="+SubRamaID);
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
				
				this.SubRamaID = objetoJSON.getInt("SubRamaID");
				Codigo= objetoJSON.getString("Codigo");
				Nombre= objetoJSON.getString("Nombre");
				RamaID = objetoJSON.getInt("RamaID");
				Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			
			if(this.SubRamaID!=0){
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
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=14&RamaID=" + RamaID);
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
				
				SubRamas[i].SubRamaID = objetoJSON.getInt("SubRamaID");
				SubRamas[i].Codigo = objetoJSON.getString("Codigo");
				SubRamas[i].Nombre = objetoJSON.getString("Nombre");
				SubRamas[i].RamaID = objetoJSON.getInt("RamaID");
				SubRamas[i].Actualizacion = objetoJSON.getString("Actualizacion");
				
			}
			if(SubRamas.length != 0 ){
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
	public boolean delete(int RamaID) {
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteSubRama.php?SubRamaID="+SubRamaID);
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
				
				SubRamaID = 0; Codigo = null; Nombre = null; RamaID = 0; Actualizacion  = null;
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
		
		if(SubRamaID==0){
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterSubRama.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("Codigo",Codigo));
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("RamaID",String.valueOf(RamaID)));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateSubRama.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("SubRamaID",String.valueOf(SubRamaID)));
				datos.add(new BasicNameValuePair("Codigo",Codigo));
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("RamaID",String.valueOf(RamaID)));
				
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

				SubRamaID = 0; Codigo = null; Nombre = null; RamaID = 0; Actualizacion = null;
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
