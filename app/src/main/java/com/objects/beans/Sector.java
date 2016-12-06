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

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * @author Gabriel Admin
 *
 */
public class Sector implements DatabaseCommunication {
	
		//region Propiedades
			public ArrayList<Sector> listaSectores;
			ArrayAdapter<Sector> adaptador;
			
			public int SectorID;
			public String Codigo,Nombre,Actualizacion;
			
			public String Respuesta;

			public Sector sector;
			
			public boolean EDITANDO_OK=false;
			public boolean RESULTADO_OK;
		//endregion
			
		//region CONSTRUCTORS
		public Sector(){
			super();
			SectorID = 0; Codigo = null; Nombre =null; Actualizacion=null;
			
			Respuesta = null;
		}
		
		//Recibe un Objeto de tipo sector y lo instancia a este objeto
		public Sector(int SectorID,String Codigo,String Nombre){
			this.SectorID = SectorID; this.Codigo = Codigo; this.Nombre = Nombre;
		}
		
		//Constructor que recibe un ID de un sector, y lo carga desde la base de datos.
		public Sector(int SectorID){ 
				Load(SectorID);
		}
		//endregion
		
		//region "GETTERS AND SETTERS"
		public int getSectorID() {
			return SectorID;
		}

		public void setSectorID(int sectorID) {
			SectorID = sectorID;
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

		public Sector getSector() {
			return sector;
		}

		public void setSector(Sector sector) {
			this.sector = sector;
		}

		public ArrayList<Sector> getArrayListSectores() {
			return listaSectores;
		}

		public ArrayAdapter<Sector> getArrayAdapter(){
			return adaptador;
		}
		
		//endregion
		
		//region Metodos de la Interface 
		
		@Override
		public boolean Load(int sectorID) {
			// TODO Auto-generated method stub
			//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			RESULTADO_OK = false;
			
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadSector.php?opcion=1&SectorID="+sectorID);
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
					
					this.SectorID = objetoJSON.getInt("SectorID");
					Codigo= objetoJSON.getString("Codigo");
					Nombre= objetoJSON.getString("Nombre");
					Actualizacion = objetoJSON.getString("Actualizacion");
					
				}
				
				if(this.Codigo!=null){
					RESULTADO_OK=true;
				}else{
					RESULTADO_OK=false;
				}
				
			}catch(JSONException e){
				e.printStackTrace();
			}
		
			return RESULTADO_OK;
		}

		
		public boolean Fetch(Context contexto) {
			boolean OPERACION_OK =false;
			
			listaSectores = new ArrayList<Sector>();
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=3");
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
					Sector  objeto = new Sector();
					objeto.SectorID = objetoJSON.getInt("SectorID");
					objeto.Codigo = objetoJSON.getString("Codigo");
					objeto.Nombre = objetoJSON.getString("Nombre");
					objeto.Actualizacion = objetoJSON.getString("Actualizacion");
					listaSectores.add(objeto);
					/*
					sectores[i].SectorID = objetoJSON.getInt("SectorID");
					sectores[i].Codigo = objetoJSON.getString("Codigo");
					sectores[i].Nombre = objetoJSON.getString("Nombre");
					sectores[i].Actualizacion = objetoJSON.getString("Actualizacion");
					 */					
				}
				//if(/*sectores.length != 0 */){
				if(!listaSectores.isEmpty()){
					OPERACION_OK = true;
				}else{
					OPERACION_OK = false;
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			//Este codigo es para devolver un elemento List View con los sectores Obtenidos del objeto JSON
			adaptador = new ArrayAdapter<Sector>(contexto, android.R.layout.simple_list_item_1,listaSectores);
			
			return OPERACION_OK;
		}

		@Override
		public boolean Fetch() {
			boolean OPERACION_OK =false;
			
			listaSectores = new ArrayList<Sector>();
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=3");
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
					Sector  objeto = new Sector();
					objeto.SectorID = objetoJSON.getInt("SectorID");
					objeto.Codigo = objetoJSON.getString("Codigo");
					objeto.Nombre = objetoJSON.getString("Nombre");
					objeto.Actualizacion = objetoJSON.getString("Actualizacion");
					listaSectores.add(objeto);
					/*
					sectores[i].SectorID = objetoJSON.getInt("SectorID");
					sectores[i].Codigo = objetoJSON.getString("Codigo");
					sectores[i].Nombre = objetoJSON.getString("Nombre");
					sectores[i].Actualizacion = objetoJSON.getString("Actualizacion");
					 */					
				}
				//if(/*sectores.length != 0 */){
				if(!listaSectores.isEmpty()){
					OPERACION_OK = true;
				}else{
					OPERACION_OK = false;
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			//Este codigo es para devolver un elemento List View con los sectores Obtenidos del objeto JSON
			//adaptador = new ArrayAdapter<Sector>(contexto, android.R.layout.simple_list_item_1,listaSectores);
			
			return OPERACION_OK;
		}

		
		@Override
		public boolean beginEdit() {
			//este metodo solo debe serinvocado una vez usado el metodo Load(int sectorID); para poner ese
			//objeto en modo edicion.
			EDITANDO_OK=true;
			
			return EDITANDO_OK;
		}

		@Override
		public boolean delete(int SectorID) {
			//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			boolean RESULTADO_OK = false;

			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteSector.php?SectorID="+SectorID);
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
					
					SectorID = 0;
					Codigo = null;
					Nombre = null;
					Actualizacion  = null;
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
			
			if(SectorID==0){
				
				HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterSector.php");
				
				try{
					List<NameValuePair> datos = new ArrayList<NameValuePair>();
					
					datos.add(new BasicNameValuePair("Codigo",Codigo));
					datos.add(new BasicNameValuePair("Nombre",Nombre));
					
					envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
					
					HttpResponse Respuesta = cliente.execute(envioPOST);
					HttpEntity contenidoRespuesta = Respuesta.getEntity();
							
					flujoDeRespuesta = contenidoRespuesta.getContent();
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				
				HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateSector.php?SectorID="+SectorID);
				
				try{
					List<NameValuePair> datos = new ArrayList<NameValuePair>();
									
					datos.add(new BasicNameValuePair("Codigo",Codigo));
					datos.add(new BasicNameValuePair("Nombre",Nombre));
					
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

				if(Respuesta=="OK"){

					SectorID = 0; Nombre = null; Codigo = null; Actualizacion = null;
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
		public String toString() {
			
			return  "Codigo: "+this.Codigo+"\nNombre: "+this.Nombre;		}

		//endregion

		
		
}
