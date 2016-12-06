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

public class SubSector implements DatabaseCommunication {
	
		//region Propiedades
			public int SubSectorID, SectorID;
			public String Codigo,Nombre, Actualizacion;

			public String Respuesta;

			public SubSector subsector;
			public SubSector[] subsectores;
			
			public boolean EDITANDO_OK=false;
			public boolean RESULTADO_OK;
		//endregion
			
		//region CONSTRUCTORS
		public SubSector(){
			SubSectorID = 0; SectorID = 0; Codigo = null; Nombre =null; Actualizacion=null;
			
			Respuesta = null;
		}
		
		//Recibe un Objeto de tipo subsector y lo instancia a este objeto
		public SubSector(int SubSectorID,String Codigo,String Nombre,int SectorID){
			this.SubSectorID = SubSectorID; this.Codigo = Codigo; this.Nombre = Nombre;
			this.SectorID = SectorID;
		}
		
		//Constructor que recibe un ID de un subsector, y lo carga desde la base de datos.
		public SubSector(int SubSectorID){ 
			Load(SubSectorID);
		}
		//endregion
		
		//region "GETTERS AND SETTERS"
		public int getSubSectorID() {
			return SubSectorID;
		}

		public void setSubSectorID(int subsectorID) {
			SubSectorID = subsectorID;
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
		
		public int getSectorID() {
			return SectorID;
		}

		public void setSectorID(int sectorID) {
			SectorID = sectorID;
		}

		public SubSector getsubsector() {
			return subsector;
		}

		public void setsubsector(SubSector subsector) {
			this.subsector = subsector;
		}

		public SubSector[] getsubsectores() {
			return subsectores;
		}

		public void setSubSectores(SubSector[] subsectores) {
			this.subsectores = subsectores;
		}
		//endregion
		
		//region Metodos de la Interface 
		
		@Override
		public boolean Load(int SubSectorID) {
			// TODO Auto-generated method stub
			//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			RESULTADO_OK = false;
			
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadSubSector.php?opcion=1&SubSectorID="+SubSectorID);
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
					
					this.SubSectorID = objetoJSON.getInt("SubSectorID");
					Codigo= objetoJSON.getString("Codigo");
					Nombre= objetoJSON.getString("Nombre");
					SectorID = objetoJSON.getInt("SectorID");
					Actualizacion = objetoJSON.getString("Actualizacion");
					
				}
				
				if(SubSectorID!=0){
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
					
					subsectores[i].SubSectorID = objetoJSON.getInt("SubSectorID");
					subsectores[i].Codigo = objetoJSON.getString("Codigo");
					subsectores[i].Nombre = objetoJSON.getString("Nombre");
					subsectores[i].SectorID = objetoJSON.getInt("SectorID");
					subsectores[i].Actualizacion = objetoJSON.getString("Actualizacion");
					
				}
				if(subsectores.length != 0 ){
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
			//este metodo solo debe ser invocado una vez usado el metodo Load(int subsectorID); para poner ese
			//objeto en modo edicion.
			EDITANDO_OK=true;
			
			return EDITANDO_OK;
		}

		@Override
		public boolean delete(int subsectorID) {
			//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
			InputStream flujoDeRespuesta = null;
			String Resultado="Fallo";
			boolean RESULTADO_OK = false;

			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteSubSector.php?SubSectorID="+subsectorID);
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
					
					SubSectorID = 0;
					Codigo = null;
					Nombre = null;
					SectorID = 0;
					Actualizacion  = null;
					Respuesta = null;
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
			
			if(SubSectorID==0){
				
				HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterSubSector.php");
				
				try{
					List<NameValuePair> datos = new ArrayList<NameValuePair>();
					
					datos.add(new BasicNameValuePair("Codigo",Codigo));
					datos.add(new BasicNameValuePair("Nombre",Nombre));
					datos.add(new BasicNameValuePair("SectorID",String.valueOf(SectorID)));
					
					envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
					
					HttpResponse Respuesta = cliente.execute(envioPOST);
					HttpEntity contenidoRespuesta = Respuesta.getEntity();
							
					flujoDeRespuesta = contenidoRespuesta.getContent();
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				
				HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateSubSector.php?SubSectorID="+SubSectorID+"&SectorID="+SectorID);
				
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

				if(Respuesta == "OK"){

					SubSectorID = 0; SectorID = 0; Nombre = null; Codigo = null; Actualizacion = null;
					RESULTADO_OK = true;

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
