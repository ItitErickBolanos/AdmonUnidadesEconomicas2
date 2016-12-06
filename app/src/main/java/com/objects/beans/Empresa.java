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

public class Empresa implements DatabaseCommunication{

	//region PROPIEDADES DE LA CLASE
	public String RazonSocial,Nombre,NombreVialidad,NumeroExterior,NumeroLetraInterior,
				  NombreAsentamiento,CodigoPostal,Estado,Municipio,Localidad,Telefono,Email,Web,
				  Latitud,Longitud,FechaIncorporacion, NombreAsentamientoHumano;

	public int EmpresaID,ScianID,RangoEmpleados,TipoVialidad,TipoAsentamiento,EstadoID,MunicipioID,
	   		   LocalidadID,TipoUnidadEconomica;
	
	public String Respuesta;
	
	public static Empresa thisEmpresa;
	public static Empresa[] thisEmpresas;
	
	public boolean EDITANDO_OK=false;
	public boolean RESULTADO_OK;
	//endregion
	
	//region CONSTRUCTORES
	public Empresa(){

	}
	public Empresa(int EmpresaID){
		this.EmpresaID = EmpresaID;
	}
	public Empresa(Empresa empresa){
		thisEmpresa = empresa;
	}
	//endregion
	
	//region metodos de la clase
		public String toString(){
		String Empresa = null;
		
		// FALTA CREAR ESTEMETODO.
		
		return Empresa;
	}
	
		public Empresa[] getEmpresaArrayObject(Empresa[] arrayObject){
		
		thisEmpresas = arrayObject;
		
		return thisEmpresas;
	}
		
	public void setEmpresaID(int empresaID){
		this.EmpresaID = empresaID;
	}
	
	public int getEmpresaID(){
		return this.EmpresaID;
	}
	
	public void setNombre(String nombre){
		this.Nombre = nombre;
	}
	
	public String getNombre(){
		return this.Nombre;
	}
	
	public void setMunicipioID(int municipioID){
		this.MunicipioID = municipioID;
	}
	
	public int getMunicipioID(){
		return this.MunicipioID;
	}
	
	public void setMunicipio(String municipio){
		this.Municipio = municipio;
	}
	
	public String getMunicipio(){
		return this.Municipio;
	}
	
	public void setScianID(int scianID){
		this.ScianID = scianID;
	}
	
	public int getScianID(){
		return this.ScianID;
	}
	
	public void setTipoAsentamiento(int tipoAsentamiento){
		this.TipoAsentamiento = tipoAsentamiento;
	}
	
	public int getTipoAsentamiento(){
		return this.TipoAsentamiento;
	}
	
	public void setRangoEmpleados(int rangoEmpleados){
		this.RangoEmpleados = rangoEmpleados;
	}
	
	public int getRangoEmpleados(){
		return this.RangoEmpleados;
	}
	
	public void setTipoVialidad(int TipoVialidad){
		this.TipoVialidad = TipoVialidad;
	}
	
	public int getTipoVialidad(){
		return this.TipoVialidad;
	}
	
	public void setTipoUnidadEconomica(int TipoUnidadEconomica){
		this.TipoUnidadEconomica = TipoUnidadEconomica;
	}
	
	public int getTipoUnidadEconomica(){
		return this.TipoUnidadEconomica;
	}
	
	public void setNombreVialidad(String NombreVialidad){
		this.NombreVialidad = NombreVialidad;
	}
	
	public String getNombreVialidad(){
		return this.NombreVialidad;
	}
	
	public void setNumeroExterior(String NumeroExterior){
		this.NumeroExterior = NumeroExterior;
	}
	
	public String getNumeroExterior(){
		return this.NumeroExterior;
	}
	
	public void setNumeroLetraInterior(String NumeroLetraInterior){
		this.NumeroLetraInterior = NumeroLetraInterior;
	}
	
	public String getNumeroLetraInterior(){
		return this.NumeroLetraInterior;
	}
	
	public void setTelefono(String Telefono){
		this.Telefono = Telefono;
	}
	
	public String getTelefono(){
		return this.Telefono;
	}
	
	public void setEmail(String Email){
		this.Email = Email;
	}
	
	public String getEmail(){
		return this.Email;
	}
	
	public void setWeb(String Web){
		this.Web = Web;
	}
	
	public String getWeb(){
		return this.Web;
	}
	
	public void setLatitud(String Latitud){
		this.Latitud = Latitud;
	}
	
	public String getLatitud(){
		return this.Latitud;
	}
	
	public void setLongitud(String Longitud){
		this.Longitud = Longitud;
	}
	
	public String getLongitud(){
		return this.Longitud;
	}
	
	public void setFechaIncorporacion(String FechaIncorporacion){
		this.FechaIncorporacion = FechaIncorporacion;
	}
	
	public String getFechaIncorporacion(){
		return this.FechaIncorporacion;
	}
	
	public void setRazonSocial(String RazonSocial){
		this.RazonSocial = RazonSocial;
	}
	
	public String getRazonSocial(){
		return this.RazonSocial;
	}
	
	public void setCodigoPostal(String CodigoPostal){
		this.CodigoPostal = CodigoPostal;
	}
	
	public String getCodigoPostal(){
		return this.CodigoPostal;
	}
	
	public void setNombreAsentamientoHumano(String NombreAsentamientoHumano){
		this.NombreAsentamientoHumano = NombreAsentamientoHumano;
	}
	
	public String getNombreAsentamientoHumano(){
		return this.NombreAsentamientoHumano;
	}
	
	//endregion
	
	//region Metodos de la Interface
	//OK
	@Override
	public boolean Fetch() {

		boolean OPERACION_OK =false;
		
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		
		// TODO Auto-generated method stub
		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=4");
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
				
				thisEmpresas[i].EmpresaID = objetoJSON.getInt("DatosID");
				thisEmpresas[i].Nombre = objetoJSON.getString("Nombre");
				thisEmpresas[i].RazonSocial = objetoJSON.getString("RazonSocial");
				thisEmpresas[i].ScianID = objetoJSON.getInt("ScianID");
				thisEmpresas[i].RangoEmpleados = objetoJSON.getInt("RangoEmpleados");
				thisEmpresas[i].TipoVialidad = objetoJSON.getInt("TipoVialidad");
				thisEmpresas[i].NombreVialidad = objetoJSON.getString("NombreVialidad");
				thisEmpresas[i].NumeroExterior = objetoJSON.getString("NumeroExterior");
				thisEmpresas[i].NumeroLetraInterior = objetoJSON.getString("NumeroLetraInterior");
				thisEmpresas[i].TipoAsentamiento = objetoJSON.getInt("TipoAsentamiento");
				thisEmpresas[i].NombreAsentamiento = objetoJSON.getString("NombreAsentamientoHumano");
				thisEmpresas[i].CodigoPostal = objetoJSON.getString("CodigoPostal");
				thisEmpresas[i].EstadoID = objetoJSON.getInt("EstadoID");
				thisEmpresas[i].Estado = objetoJSON.getString("Estado");
				thisEmpresas[i].MunicipioID = objetoJSON.getInt("MunicipioID");
				thisEmpresas[i].Municipio = objetoJSON.getString("Municipio");
				thisEmpresas[i].LocalidadID = objetoJSON.getInt("EstadoID");
				thisEmpresas[i].Localidad = objetoJSON.getString("Localidad");
				thisEmpresas[i].Telefono = objetoJSON.getString("Telefono");
				thisEmpresas[i].Email = objetoJSON.getString("Email");
				thisEmpresas[i].Web = objetoJSON.getString("Web");
				thisEmpresas[i].TipoUnidadEconomica = objetoJSON.getInt("TipoUnidadEconomica");
				thisEmpresas[i].Latitud = objetoJSON.getString("Latitud");
				thisEmpresas[i].Longitud = objetoJSON.getString("Longitud");
				thisEmpresas[i].FechaIncorporacion = objetoJSON.getString("FechaIncorporacion");
			}
			if(thisEmpresas.length != 0 ){
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
	
	//OK
	@Override
	public boolean beginEdit() {
		EDITANDO_OK = true;
		
		return EDITANDO_OK;
	}

	//OK
	@Override
	public boolean delete(int ID) {

		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGET = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/DeleteEmpresa.php?EmpresaID="+ID);
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
				//region Reseteando Propiedades del Objeto
					EmpresaID = 0;
					Nombre = null;
					RazonSocial = null;
					ScianID = 0;
					RangoEmpleados = 0;
					TipoVialidad = 0;
					NombreVialidad = null;
					NumeroExterior = null;
					NumeroLetraInterior = null;
					TipoAsentamiento = 0;
					NombreAsentamiento = null;
					CodigoPostal = null;
					EstadoID = 0;
					MunicipioID = 0;
					LocalidadID = 0;
					Estado = null;
					Municipio = null;
					Localidad = null;
					Telefono = null;
					Web = null;
					Email = null;
					TipoUnidadEconomica = 0;
					Latitud = null;
					Longitud = null;
					FechaIncorporacion = null;
				//endregion
				
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

	//OK
	@Override
	public boolean applyEdit() {

		InputStream flujoDeRespuesta=null;
		boolean RESULTADO_OK = false;
		HttpClient cliente = new DefaultHttpClient();
		String Resultado = null;
		
		if(EmpresaID==0){
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/RegisterEmpresa.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("RazonSocial",RazonSocial));
				datos.add(new BasicNameValuePair("ScianID",String.valueOf(ScianID)));
				datos.add(new BasicNameValuePair("RangoEmpleados",String.valueOf(RangoEmpleados)));
				datos.add(new BasicNameValuePair("TipoVialidad",String.valueOf(TipoVialidad)));
				datos.add(new BasicNameValuePair("NombreVialidad",NombreVialidad));
				datos.add(new BasicNameValuePair("NumeroExterior",NumeroExterior));
				datos.add(new BasicNameValuePair("NumeroLetraInterior",NumeroLetraInterior));				
				datos.add(new BasicNameValuePair("TipoAsentamiento",String.valueOf(TipoAsentamiento)));
				datos.add(new BasicNameValuePair("NombreAsentamientoHumano",NombreAsentamientoHumano));
				datos.add(new BasicNameValuePair("CodigoPostal",CodigoPostal));				
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				datos.add(new BasicNameValuePair("Telefono",Telefono));
				datos.add(new BasicNameValuePair("Email",Email));
				datos.add(new BasicNameValuePair("Web",Web));
				datos.add(new BasicNameValuePair("TipoUnidadEconomica",String.valueOf(TipoUnidadEconomica)));
				datos.add(new BasicNameValuePair("Latitud",Latitud));
				datos.add(new BasicNameValuePair("Longitud",Longitud));
				datos.add(new BasicNameValuePair("FechaIncorporacion",FechaIncorporacion));
				
				envioPOST.setEntity(new UrlEncodedFormEntity(datos,HTTP.UTF_8));
				
				HttpResponse Respuesta = cliente.execute(envioPOST);
				HttpEntity contenidoRespuesta = Respuesta.getEntity();
						
				flujoDeRespuesta = contenidoRespuesta.getContent();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			
			HttpPost envioPOST = new HttpPost("http://semestral.esy.es/ConvertidorUnidades/UpdateEmpresa.php");
			
			try{
				List<NameValuePair> datos = new ArrayList<NameValuePair>();
				
				datos.add(new BasicNameValuePair("EmpresaID",String.valueOf(EmpresaID)));
				datos.add(new BasicNameValuePair("Nombre",Nombre));
				datos.add(new BasicNameValuePair("RazonSocial",RazonSocial));
				datos.add(new BasicNameValuePair("ScianID",String.valueOf(ScianID)));
				datos.add(new BasicNameValuePair("RangoEmpleados",String.valueOf(RangoEmpleados)));
				datos.add(new BasicNameValuePair("TipoVialidad",String.valueOf(TipoVialidad)));
				datos.add(new BasicNameValuePair("NombreVialidad",NombreVialidad));
				datos.add(new BasicNameValuePair("NumeroExterior",NumeroExterior));
				datos.add(new BasicNameValuePair("NumeroLetraInterior",NumeroLetraInterior));				
				datos.add(new BasicNameValuePair("TipoAsentamiento",String.valueOf(TipoAsentamiento)));
				datos.add(new BasicNameValuePair("NombreAsentamientoHumano",NombreAsentamiento));
				datos.add(new BasicNameValuePair("CodigoPostal",CodigoPostal));			
				datos.add(new BasicNameValuePair("MunicipioID",String.valueOf(MunicipioID)));
				datos.add(new BasicNameValuePair("Telefono",Telefono));
				datos.add(new BasicNameValuePair("Email",Email));
				datos.add(new BasicNameValuePair("Web",Web));
				datos.add(new BasicNameValuePair("TipoUnidadEconomica",String.valueOf(TipoUnidadEconomica)));
				datos.add(new BasicNameValuePair("Latitud",Latitud));
				datos.add(new BasicNameValuePair("Longitud",Longitud));
				datos.add(new BasicNameValuePair("FechaIncorporacion",FechaIncorporacion));
				
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
				
				EmpresaID = 0;
				Nombre = null;
				RazonSocial = null;
				ScianID = 0;
				RangoEmpleados = 0;
				TipoVialidad = 0;
				NombreVialidad = null;
				NumeroExterior = null;
				NumeroLetraInterior = null;
				TipoAsentamiento = 0;
				NombreAsentamiento = null;
				CodigoPostal = null;
				EstadoID = 0;
				MunicipioID = 0;
				LocalidadID = 0;
				Estado = null;
				Municipio = null;
				Localidad = null;
				Telefono = null;
				Web = null;
				Email = null;
				TipoUnidadEconomica = 0;
				Latitud = null;
				Longitud = null;
				FechaIncorporacion = null;
				
				RESULTADO_OK=true;
				
			}else{
				
				RESULTADO_OK = false;
				
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULTADO_OK;
		
	}
	
	@Override
	public boolean Load(int ID) {
		// TODO Auto-generated method stub
		//flujo de respuesta, sirve para obtener el contenido de la respuesta del HttpEntity
		InputStream flujoDeRespuesta = null;
		String Resultado="Fallo";
		boolean RESULTADO_OK = false;

		HttpClient cliente = new DefaultHttpClient();
		HttpGet peticionGet = new HttpGet("http://semestral.esy.es/ConvertidorUnidades/LoadEmpresa.php?EmpresaID="+ID);
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
				
				thisEmpresas[i].EmpresaID = objetoJSON.getInt("DatosID");
				thisEmpresas[i].Nombre = objetoJSON.getString("Nombre");
				thisEmpresas[i].RazonSocial = objetoJSON.getString("RazonSocial");
				thisEmpresas[i].ScianID = objetoJSON.getInt("ScianID");
				thisEmpresas[i].RangoEmpleados = objetoJSON.getInt("RangoEmpleados");
				thisEmpresas[i].TipoVialidad = objetoJSON.getInt("TipoVialidad");
				thisEmpresas[i].NombreVialidad = objetoJSON.getString("NombreVialidad");
				thisEmpresas[i].NumeroExterior = objetoJSON.getString("NumeroExterior");
				thisEmpresas[i].NumeroLetraInterior = objetoJSON.getString("NumeroLetraInterior");
				thisEmpresas[i].TipoAsentamiento = objetoJSON.getInt("TipoAsentamiento");
				thisEmpresas[i].NombreAsentamiento = objetoJSON.getString("NombreAsentamientoHumano");
				thisEmpresas[i].CodigoPostal = objetoJSON.getString("CodigoPostal");
				thisEmpresas[i].EstadoID = objetoJSON.getInt("EstadoID");
				thisEmpresas[i].Estado = objetoJSON.getString("Estado");
				thisEmpresas[i].MunicipioID = objetoJSON.getInt("MunicipioID");
				thisEmpresas[i].Municipio = objetoJSON.getString("Municipio");
				thisEmpresas[i].LocalidadID = objetoJSON.getInt("EstadoID");
				thisEmpresas[i].Localidad = objetoJSON.getString("Localidad");
				thisEmpresas[i].Telefono = objetoJSON.getString("Telefono");
				thisEmpresas[i].Email = objetoJSON.getString("Email");
				thisEmpresas[i].Web = objetoJSON.getString("Web");
				thisEmpresas[i].TipoUnidadEconomica = objetoJSON.getInt("TipoUnidadEconomica");
				thisEmpresas[i].Latitud = objetoJSON.getString("Latitud");
				thisEmpresas[i].Longitud = objetoJSON.getString("Longitud");
				thisEmpresas[i].FechaIncorporacion = objetoJSON.getString("FechaIncorporacion");
			}
			if(EmpresaID!=0){
				RESULTADO_OK=true;
			}else{
				RESULTADO_OK=false;
			}

		}catch(JSONException e){
			e.printStackTrace();
		}

		return RESULTADO_OK;
	}

	//endregion

}
