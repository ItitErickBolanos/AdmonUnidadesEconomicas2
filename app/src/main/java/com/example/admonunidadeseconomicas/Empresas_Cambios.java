package com.example.admonunidadeseconomicas;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.objects.beans.CIIURev4;
import com.objects.beans.Clase;
import com.objects.beans.Empresa;
import com.objects.beans.Estado;
import com.objects.beans.Municipio;
import com.objects.beans.Rama;
import com.objects.beans.Sector;
import com.objects.beans.SubRama;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.ClaseList;
import com.objects.beans.listerBeans.EstadoList;
import com.objects.beans.listerBeans.MunicipioList;
import com.objects.beans.listerBeans.RamaList;
import com.objects.beans.listerBeans.SectorList;
import com.objects.beans.listerBeans.SubRamaList;
import com.objects.beans.listerBeans.SubSectorList;

public class Empresas_Cambios extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
	EditText  RazonSocial,Nombre,NombreVialidad,NumeroExterior,NumeroLetraInterior,
	  NombreAsentamiento,CodigoPostal,Estado,Municipio,Localidad,Telefono,Email,Web,
	  Latitud,Longitud,FechaIncorporacion, NombreAsentamientoHumano,RangoEmpleados,TipoVialidad,
	  TipoAsentamiento,TipoUnidadEconomica;
	TextView Titulo;
	Button  guardar,cancelar;
	SectorList sectorLister;
	SubSectorList subsectorLister;
	RamaList ramaLister;
	SubRamaList subRamaLister;
	ClaseList claseLister;
	EstadoList estadoLister;
	MunicipioList municipioLister;
	Sector sector = null, objSector;
	SubSector subSector = null, objSubSector;
	Rama rama = null, objRama;
	SubRama subRama = null, objSubRama;
	Clase clase = null, objClase;
	Estado estado = null, objEstado;
	Municipio municipio = null, objMunicipio;
	Empresa empresa;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	Spinner sectores, subsectores, ramas, subramas, clases, estados, municipios;
	ArrayList<SectorList> listadoSectores = new ArrayList<SectorList>();
	ArrayList<SubSectorList> listadoSubSectores = new ArrayList<SubSectorList>();
	ArrayList<RamaList> listadoRamas = new ArrayList<RamaList>();
	ArrayList<SubRamaList> listadoSubRamas = new ArrayList<SubRamaList>();
	ArrayList<ClaseList> listadoClases = new ArrayList<ClaseList>();
	ArrayList<EstadoList> listadoEstados = new ArrayList<EstadoList>();
	ArrayList<MunicipioList> listadoMunicipios = new ArrayList<MunicipioList>();
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empresas__cambios);
		
		populateView();
		populateExtras();
		
		try{
			
			listarSectores Sectores = new listarSectores();
			Sectores.cargarContenido(getApplicationContext());
			Sectores.setSectorList(listadoSectores);
			Sectores.execute(sectores);
			listarEstados Estados = new listarEstados();
			Estados.cargarContenido(getApplicationContext());
			Estados.setEstadosList(listadoEstados);
			Estados.execute(estados);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
		//EDITTEXT
		RazonSocial.setText(empresa.getRazonSocial());
		Nombre.setText(empresa.getNombre());
		Telefono.setText(empresa.getTelefono());
		Email.setText(empresa.getEmail());
		Web.setText(empresa.getWeb());
		NombreVialidad.setText(empresa.getNombreVialidad());
		NumeroExterior.setText(empresa.getNumeroExterior());
		NumeroLetraInterior.setText(empresa.getNumeroLetraInterior());
		NombreAsentamientoHumano.setText(empresa.getNombreAsentamientoHumano());
		FechaIncorporacion.setText(empresa.getFechaIncorporacion());
		TipoUnidadEconomica.setText(Integer.toString(empresa.getTipoUnidadEconomica()));
		CodigoPostal.setText(empresa.getCodigoPostal());
		TipoAsentamiento.setText(Integer.toString(empresa.getTipoAsentamiento()));
		TipoVialidad.setText(Integer.toString(empresa.getTipoVialidad()));
		Longitud.setText(empresa.getLongitud());
		Latitud.setText(empresa.getLatitud());
		RangoEmpleados.setText(Integer.toString(empresa.getRangoEmpleados()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sectores__alta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
//endregion


//region METODOS HECHOS PARA LA CLASE
	    @Override
	    public void onClick(View v) {
			switch(v.getId()){
			
			case R.id.empresasCambiosGuardar:
					int EmpresaID = empresa.getEmpresaID();
					empresa = new Empresa();
					empresa.setEmpresaID(EmpresaID);
					empresa.setRazonSocial(RazonSocial.getText().toString());
					empresa.setNombre(Nombre.getText().toString());
					empresa.setCodigoPostal(CodigoPostal.getText().toString());
					empresa.setNombreVialidad(NombreVialidad.getText().toString());
					empresa.setNumeroExterior(NumeroExterior.getText().toString());
					empresa.setNumeroLetraInterior(NumeroLetraInterior.getText().toString());
					empresa.setLongitud(Longitud.getText().toString());
					empresa.setNombreAsentamientoHumano(NombreAsentamientoHumano.getText().toString());
					empresa.setLatitud(Latitud.getText().toString());
					empresa.setTelefono(Telefono.getText().toString());
					empresa.setEmail(Email.getText().toString());
					empresa.setWeb(Web.getText().toString());
					empresa.setFechaIncorporacion(FechaIncorporacion.getText().toString());
					empresa.setRangoEmpleados(Integer.parseInt(RangoEmpleados.getText().toString()));
					empresa.setTipoVialidad(Integer.parseInt(TipoVialidad.getText().toString()));
					empresa.setTipoAsentamiento(Integer.parseInt(TipoAsentamiento.getText().toString()));
					empresa.setTipoUnidadEconomica(Integer.parseInt(TipoUnidadEconomica.getText().toString()));
					
					empresa.ScianID = (clase != null) ? clase.ScianID : objClase.ScianID;
					empresa.MunicipioID = (municipio != null) ? municipio.MunicipioID : objMunicipio.MunicipioID;
					
					try{
						
						doLogginBackground doLoggin = new doLogginBackground();
						doLoggin.setEmpresa(empresa);
						doLoggin.execute();
						
					}catch(Exception e){
						
						e.printStackTrace();
						
					}
				break;
			case R.id.empresasCambiosCancelar:
				i = new Intent(this,Empresas_List.class);
				b = new Bundle();
		  			b.putInt("UsuarioID", usuario.getUsuarioID());
		  			b.putString("Loggin", usuario.getLoggin());
		  			b.putString("Password", usuario.getPassword());
		  			b.putInt("PerfilID", usuario.getPerfilID());
		  			b.putInt("DatosID",usuario.getDatosID());
		  			b.putString("Email",usuario.getEmail());
					
		  		i.putExtras(b);
				startActivity(i);
				break;
			
			}
			
		}
	    
	    public void populateView(){
			Titulo = (TextView)findViewById(R.id.empresasCambiosTitulo);
			RazonSocial = (EditText)findViewById(R.id.empresasCambiosRazonS);
			Nombre = (EditText)findViewById(R.id.empresasCambiosNombre);
			Telefono = (EditText)findViewById(R.id.empresasCambiosTel);
			Email = (EditText)findViewById(R.id.empresasCambiosEmail);
			Web = (EditText)findViewById(R.id.empresasCambiosWeb);
			NombreVialidad = (EditText)findViewById(R.id.empresasCambiosNombreVial);
			NumeroExterior = (EditText)findViewById(R.id.empresasCambiosNumExt);
			NumeroLetraInterior = (EditText)findViewById(R.id.empresasCambiosNumeroLI);
			NombreAsentamientoHumano = (EditText)findViewById(R.id.empresasCambiosNombreAs);
			FechaIncorporacion = (EditText)findViewById(R.id.empresasCambiosFechaInc);
			TipoUnidadEconomica = (EditText)findViewById(R.id.empresasCambiosTipoUn);
			CodigoPostal = (EditText)findViewById(R.id.empresasCambiosCP);
			TipoAsentamiento = (EditText)findViewById(R.id.empresasCambiosTipoAs);
			TipoVialidad = (EditText)findViewById(R.id.empresasCambiosTipoVial);
			Longitud = (EditText)findViewById(R.id.empresasCambiosLon);
			Latitud = (EditText)findViewById(R.id.empresasCambiosLat);
			RangoEmpleados = (EditText)findViewById(R.id.empresasCambiosRangoEmp);
			guardar = (Button)findViewById(R.id.empresasCambiosGuardar);
			cancelar = (Button)findViewById(R.id.empresasCambiosCancelar);
			sectores = (Spinner)findViewById(R.id.empresasCambiosListadoSect);
			subsectores = (Spinner)findViewById(R.id.empresasCambiosListadoSubS);
			ramas = (Spinner)findViewById(R.id.empresasCambiosListadoRama);
			subramas = (Spinner)findViewById(R.id.empresasCambiosListadoSubR);
			clases = (Spinner)findViewById(R.id.empresasCambiosListadoClases);
			estados = (Spinner)findViewById(R.id.empresasCambiosListadoEstados);
			municipios = (Spinner)findViewById(R.id.empresasCambiosListadoMunicipios);
			Titulo.setText("EDICION DE EMPRESAS");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fallo la insercion", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "Empresa registrada exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this, Empresas_List.class);
	  		altaOK();
	  		b = new Bundle();
	  			b.putInt("UsuarioID", usuario.getUsuarioID());
	  			b.putString("Loggin", usuario.getLoggin());
	  			b.putString("Password", usuario.getPassword());
	  			b.putInt("PerfilID", usuario.getPerfilID());
	  			b.putInt("DatosID",usuario.getDatosID());
	  			b.putString("Email",usuario.getEmail());
				
	  		i.putExtras(b);
	  		startActivity(i);
	  	}
	    
	//endregion
	 
//region METODO PARA RECUPERAR BUNDLE y SU CONTENIDO
		
//metodo para recuperar el bundle de la actividad anterior.
 	public void populateExtras(){
	  		b = getIntent().getExtras();
	  		if(!b.isEmpty()){
	  			empresa = new Empresa();
	  			empresa.setEmpresaID(b.getInt("EmpresaID"));
				empresa.setRazonSocial(b.getString("RazonSocial"));
				empresa.setNombre(b.getString("Nombre"));
				empresa.setCodigoPostal(b.getString("CodigoPostal"));
				empresa.setNombreVialidad(b.getString("NombreVialidad"));
				empresa.setNumeroExterior(b.getString("NumeroExterior"));
				empresa.setNumeroLetraInterior(b.getString("NumeroLetraInterior"));
				empresa.setLongitud(b.getString("Longitud"));
				empresa.setNombreAsentamientoHumano(b.getString("NombreAsentamientoHumano"));
				empresa.setLatitud(b.getString("Latitud"));
				empresa.setTelefono(b.getString("Telefono"));
				empresa.setEmail(b.getString("Email"));
				empresa.setWeb(b.getString("Web"));
				empresa.setFechaIncorporacion(b.getString("FechaIncorporacion"));
				empresa.setRangoEmpleados(b.getInt("RangoEmpleados"));
				empresa.setTipoVialidad(b.getInt("TipoVialidad"));
				empresa.setTipoAsentamiento(b.getInt("TipoAsentamiento"));
				empresa.setTipoUnidadEconomica(b.getInt("TipoUnidadEconomica"));
				empresa.setScianID(b.getInt("ScianID"));
				empresa.setMunicipioID(b.getInt("MunicipioID"));
	  			
	  			usuario = new Usuario();
	  				usuario.setUsuarioID(b.getInt("UsuarioID"));
	  				usuario.setLoggin(b.getString("Loggin"));
	  				usuario.setPassword(b.getString("Password"));
	  				usuario.setPerfilID(b.getInt("PerfilID"));
	  				usuario.setDatosID(b.getInt("DatosID"));
	  			usuario.setEmail(b.getString("Email"));
	  		}
	  	}
//endregion
	  	
	  	
//region CLASE QUE IMPLEMENTA ASYNCTASK
	  	class doLogginBackground extends AsyncTask<Void, Void, Void> {

			private ProgressDialog progreso;
			Empresa thisEmpresa;
			String Respuesta = null;
		
			public void setEmpresa(Empresa empresa){
				thisEmpresa = empresa;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(Empresas_Cambios.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Agregando Empresa");
	           progreso.setMessage("Verificando Campos, Espere...");
	           progreso.setCancelable(false);
	           progreso.setMax(100);
	           progreso.setProgress(0);
	           progreso.show();

			}

			@Override
			protected Void doInBackground(Void... array) {
				thisEmpresa.applyEdit();
				
				Respuesta = thisEmpresa.Respuesta;
				return null;
			}

			@Override
	    	protected void onProgressUpdate(Void... porc) {

	    	for(int i=1; (i*10)==100; i++){
	    		progreso.setProgress(i);
	    	}

	    }

			@Override
			protected void onPostExecute(Void value) {

				if(Respuesta.length()>0){
					progreso.dismiss();
					sig();
				}else{
					progreso.dismiss();
					errorDeOperacion();
				}
			}

	  	}
	  	
	  	
	  	
//endregion

	  //region CLASE PARA SPINNER ASYNCRONO
	  		class listarSectores extends AsyncTask<Spinner, Void, ArrayAdapter<SectorList>>{
	  			Context contexto;
	  			Spinner list;
	  			InputStream is;
	  			Sector[] sectorNormal;
	  			ArrayList<SectorList> listaSectores2 = new ArrayList<SectorList>();
	  			ArrayList<SectorList> listaSectores = new ArrayList<SectorList>();
	  			
	  			public void setSectorList(ArrayList<SectorList> listaSectores){
	  				this.listaSectores2 = listaSectores;
	  			}
	  			
	  			public void cargarContenido(Context contexto){
	  				this.contexto = contexto;
	  			}
	  			
	  				@Override
	  				protected ArrayAdapter<SectorList> doInBackground(Spinner... params) {
	  					// TODO Auto-generated method stub
	  					list = params[0];
	  					String resultado = "fallo";
	  					SectorList sector;
	  					
	  					//Creamos la conexion HTTP
	  					HttpClient cliente = new DefaultHttpClient();
	  					HttpGet peticionGet =  new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=3");
	  					
	  					try{
	  						
	  						HttpResponse response = cliente.execute(peticionGet);
	  						HttpEntity contenido = response.getEntity();
	  						
	  						is = contenido.getContent();
	  						
	  					}catch(ClientProtocolException e){
	  						e.printStackTrace();
	  					}catch(IOException e){
	  						e.printStackTrace();
	  					}
	  					
	  					BufferedReader buferLector = new BufferedReader(new InputStreamReader(is));
	  					StringBuilder sb = new StringBuilder();
	  					String linea = null;
	  					
	  					try{
	  						while((linea = buferLector.readLine())!=null){
	  							sb.append(linea);
	  						}
	  					}catch(IOException e){
	  						e.printStackTrace();
	  					}
	  					
	  					try{
	  						is.close();
	  					}catch(IOException e){
	  						e.printStackTrace();
	  					}
	  					
	  					resultado = sb.toString();
	  					if(!resultado.equals("null")){
							try{
								
								JSONArray arrayJSON = new JSONArray(resultado);
								
								
								for(int i=0; i<arrayJSON.length(); i++){
									JSONObject objetoJSON = arrayJSON.getJSONObject(i);
									
									if(i == 0){
										objSector = new Sector();
										objSector.setSectorID(objetoJSON.getInt("SectorID"));
										objSector.setCodigo(objetoJSON.getString("Codigo"));
										objSector.setNombre(objetoJSON.getString("Nombre"));
									}
									
									sector = new SectorList(objetoJSON.getInt("SectorID"),
															objetoJSON.getString("Codigo"),
															objetoJSON.getString("Nombre"),
															objetoJSON.getString("Actualizacion"));
									
									listaSectores.add(sector);						
								}
								
							}catch(JSONException e){
								e.printStackTrace();
							}
						} else {
							sector = new SectorList(0,
									"0",
									"No hay ningun sector",
									"0");
			
							listaSectores.add(sector);
						}
						
	  					
	  					ArrayAdapter<SectorList> adaptador = new ArrayAdapter<SectorList>(contexto,android.R.layout.simple_list_item_1,listaSectores);
	  					
	  					return adaptador;
	  			}

	  					@Override
	  					protected void onPostExecute(ArrayAdapter<SectorList> result) {
	  						// TODO Auto-generated method stub
	  						listaSectores2 = listaSectores;
	  						list.setAdapter(result);
	  						list.setOnItemSelectedListener(new OnItemSelectedListener() {

	  						    @Override
	  						    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	  						    	if (!parentView.getAdapter().isEmpty()){
	  						    		
	  									sectorLister = new SectorList();
	  									sectorLister =(SectorList) parentView.getAdapter().getItem(position);
	  									sector = new Sector();
	  									sector.setSectorID(sectorLister.getSectorID());
	  									try{
	  										listarSubSectores SubSectores = new listarSubSectores();
	  										SubSectores.cargarContenido(getApplicationContext());
	  										SubSectores.setSubSectorList(listadoSubSectores);
	  										SubSectores.execute(subsectores);
	  										
	  									}catch(Exception e){
	  										e.printStackTrace();
	  									}
	  								}
	  						    }

	  						    @Override
	  						    public void onNothingSelected(AdapterView<?> parentView) {
	  						        // your code here
	  						    }
	  						});
	  						
	  					}

	  					@Override
	  					protected void onPreExecute() {

	  						
	  					}
	  					
	  				}
	  			//endregion
	  		
	  	//region CLASE PARA SPINNER ASYNCRONO
			class listarSubSectores extends AsyncTask<Spinner, Void, ArrayAdapter<SubSectorList>>{
				Context contexto;
				Spinner list;
				InputStream is;
				SubSector[] subsectorNormal;
				ArrayList<SubSectorList> listaSubSectores2 = new ArrayList<SubSectorList>();
				ArrayList<SubSectorList> listaSubSectores = new ArrayList<SubSectorList>();
				
				public void setSubSectorList(ArrayList<SubSectorList> listaSubSectores){
					this.listaSubSectores2 = listaSubSectores;
				}
				
				public void cargarContenido(Context contexto){
					this.contexto = contexto;
				}
				
					@Override
					protected ArrayAdapter<SubSectorList> doInBackground(Spinner... params) {
						// TODO Auto-generated method stub
						list = params[0];
						String resultado = "fallo";
						SubSectorList subsector;
						int SectorID = (sector == null) ? objSector.SectorID : sector.SectorID;
						
						//Creamos la conexion HTTP
						HttpClient cliente = new DefaultHttpClient();
						HttpGet peticionGet =  new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=12&SectorID=" + SectorID);
						
						try{
							
							HttpResponse response = cliente.execute(peticionGet);
							HttpEntity contenido = response.getEntity();
							
							is = contenido.getContent();
							
						}catch(ClientProtocolException e){
							e.printStackTrace();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						BufferedReader buferLector = new BufferedReader(new InputStreamReader(is));
						StringBuilder sb = new StringBuilder();
						String linea = null;
						
						try{
							while((linea = buferLector.readLine())!=null){
								sb.append(linea);
							}
						}catch(IOException e){
							e.printStackTrace();
						}
						
						try{
							is.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						resultado = sb.toString();
						if(!resultado.equals("null")){
							try{
								
								JSONArray arrayJSON = new JSONArray(resultado);
								
								for(int i=0; i<arrayJSON.length(); i++){
									JSONObject objetoJSON = arrayJSON.getJSONObject(i);
									
									if(i == 0){
										objSubSector = new SubSector();
										objSubSector.setSubSectorID(objetoJSON.getInt("SubSectorID"));
										objSubSector.setCodigo(objetoJSON.getString("Codigo"));
										objSubSector.setNombre(objetoJSON.getString("Nombre"));
									}
									
									subsector = new SubSectorList(objetoJSON.getInt("SubSectorID"),
															objetoJSON.getString("Codigo"),
															objetoJSON.getString("Nombre"),
															objetoJSON.getInt("SectorID"));
									
									listaSubSectores.add(subsector);						
								}
								
							}catch(JSONException e){
								e.printStackTrace();
							}
						} else {
							subsector = new SubSectorList(0,
									"0",
									"No hay ningun subsector vinculado a este sector",
									0);
							objSubSector.setSubSectorID(0);
							listaSubSectores.add(subsector);
						}
						
						ArrayAdapter<SubSectorList> adaptador = new ArrayAdapter<SubSectorList>(contexto,android.R.layout.simple_list_item_1,listaSubSectores);
						
						return adaptador;
				}

						@Override
						protected void onPostExecute(ArrayAdapter<SubSectorList> result) {
							// TODO Auto-generated method stub
							listaSubSectores2 = listaSubSectores;
							list.setAdapter(result);
							list.setOnItemSelectedListener(new OnItemSelectedListener() {

							    @Override
							    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
							    	if (!parentView.getAdapter().isEmpty()){
							    		
										subsectorLister = new SubSectorList();
										subsectorLister =(SubSectorList) parentView.getAdapter().getItem(position);
										subSector = new SubSector();
										subSector.setSubSectorID(subsectorLister.getSubSectorID());
										try{
											listarRamas Ramas = new listarRamas();
											Ramas.cargarContenido(getApplicationContext());
											Ramas.setRamaList(listadoRamas);
											Ramas.execute(ramas);
										}catch(Exception e){
											e.printStackTrace();
										}
									}
							    }

							    @Override
							    public void onNothingSelected(AdapterView<?> parentView) {
							        // your code here
							    }
							});
							
						}

						@Override
						protected void onPreExecute() {

							
						}
						
					}
				//endregion

			//region CLASE PARA SPINNER ASYNCRONO
			class listarRamas extends AsyncTask<Spinner, Void, ArrayAdapter<RamaList>>{
				Context contexto;
				Spinner list;
				InputStream is;
				Rama[] ramaNormal;
				ArrayList<RamaList> listaRamas2 = new ArrayList<RamaList>();
				ArrayList<RamaList> listaRamas = new ArrayList<RamaList>();
				
				public void setRamaList(ArrayList<RamaList> listaRamas){
					this.listaRamas2 = listaRamas;
				}
				
				public void cargarContenido(Context contexto){
					this.contexto = contexto;
				}
				
					@Override
					protected ArrayAdapter<RamaList> doInBackground(Spinner... params) {
						// TODO Auto-generated method stub
						list = params[0];
						String resultado = "fallo";
						RamaList rama;
						int SubSectorID = (subSector == null) ? objSubSector.SubSectorID : subSector.SubSectorID;
						
						//Creamos la conexion HTTP
						HttpClient cliente = new DefaultHttpClient();
						HttpGet peticionGet =  new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=13&SubSectorID=" + SubSectorID);
						
						try{
							
							HttpResponse response = cliente.execute(peticionGet);
							HttpEntity contenido = response.getEntity();
							
							is = contenido.getContent();
							
						}catch(ClientProtocolException e){
							e.printStackTrace();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						BufferedReader buferLector = new BufferedReader(new InputStreamReader(is));
						StringBuilder sb = new StringBuilder();
						String linea = null;
						
						try{
							while((linea = buferLector.readLine())!=null){
								sb.append(linea);
							}
						}catch(IOException e){
							e.printStackTrace();
						}
						
						try{
							is.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						resultado = sb.toString();
						if(!resultado.equals("null")){
							try{
								
								JSONArray arrayJSON = new JSONArray(resultado);
								
								
								for(int i=0; i<arrayJSON.length(); i++){
									JSONObject objetoJSON = arrayJSON.getJSONObject(i);
									
									if(i == 0){
										objRama = new Rama();
										objRama.setRamaID(objetoJSON.getInt("RamaID"));
										objRama.setCodigo(objetoJSON.getString("Codigo"));
										objRama.setNombre(objetoJSON.getString("Nombre"));
									}
									
									rama = new RamaList(objetoJSON.getInt("RamaID"),
															objetoJSON.getString("Codigo"),
															objetoJSON.getString("Nombre"),
															objetoJSON.getInt("SubSectorID"));
									
									listaRamas.add(rama);						
								}
								
							}catch(JSONException e){
								e.printStackTrace();
							}
						} else {
							rama = new RamaList(0,
									"0",
									"No hay ninguna rama vinculada a este subsector",
									0);
							objRama.setRamaID(0);
							listaRamas.add(rama);
						}
						
						ArrayAdapter<RamaList> adaptador = new ArrayAdapter<RamaList>(contexto,android.R.layout.simple_list_item_1,listaRamas);
						
						return adaptador;
				}

						@Override
						protected void onPostExecute(ArrayAdapter<RamaList> result) {
							// TODO Auto-generated method stub
							listaRamas2 = listaRamas;
							list.setAdapter(result);
							list.setOnItemSelectedListener(new OnItemSelectedListener() {

							    @Override
							    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
							    	if (!parentView.getAdapter().isEmpty()){
							    		
										ramaLister = new RamaList();
										ramaLister =(RamaList) parentView.getAdapter().getItem(position);
										rama = new Rama();
										rama.setRamaID(ramaLister.getRamaID());
										try{
											listarSubRamas SubRamas = new listarSubRamas();
											SubRamas.cargarContenido(getApplicationContext());
											SubRamas.setSubRamaList(listadoSubRamas);
											SubRamas.execute(subramas);
										}catch(Exception e){
											e.printStackTrace();
										}
									}
							    }

							    @Override
							    public void onNothingSelected(AdapterView<?> parentView) {
							        // your code here
							    }
							});
							
						}

						@Override
						protected void onPreExecute() {

							
						}
						
					}
				//endregion
	
			// region CLASE PARA LISTAR ASYNCRONAMENTE
			class listarSubRamas extends
				AsyncTask<Spinner, Void, ArrayAdapter<SubRamaList>> {
				Context contexto;
				Spinner list;
				InputStream is;
				SubRama[] SubRamaNormal;
				ArrayList<SubRamaList> listaSubRamas2 = new ArrayList<SubRamaList>();
				ArrayList<SubRamaList> listaSubRamas = new ArrayList<SubRamaList>();

				public void setSubRamaList(ArrayList<SubRamaList> listaSubRamas) {
					this.listaSubRamas2 = listaSubRamas;
				}

				public void cargarContenido(Context contexto) {
					this.contexto = contexto;
				}

				@Override
				protected ArrayAdapter<SubRamaList> doInBackground(Spinner... params) {
					// TODO Auto-generated method stub
					list = params[0];
					String resultado = "fallo";
					SubRamaList subrama;
					Rama enviarRama = (rama != null) ? rama : objRama;
					//SectorID = ;
					//SectorList sector;

					// Creamos la conexion HTTP
					HttpClient cliente = new DefaultHttpClient();
					HttpGet peticionGet = new HttpGet(
							"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=14&RamaID=" + enviarRama.RamaID);

					try {

						HttpResponse response = cliente.execute(peticionGet);
						HttpEntity contenido = response.getEntity();

						is = contenido.getContent();

					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					BufferedReader buferLector = new BufferedReader(
							new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String linea = null;

					try {
						while ((linea = buferLector.readLine()) != null) {
							sb.append(linea);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					resultado = sb.toString();
					if(!resultado.equals("null")){
						try{
							
							JSONArray arrayJSON = new JSONArray(resultado);
							
							
							for(int i=0; i<arrayJSON.length(); i++){
								JSONObject objetoJSON = arrayJSON.getJSONObject(i);
								
								if(i == 0){
									objSubRama = new SubRama();
									objSubRama.setRamaID(objetoJSON.getInt("SubRamaID"));
									objSubRama.setCodigo(objetoJSON.getString("Codigo"));
									objSubRama.setNombre(objetoJSON.getString("Nombre"));
								}
								
								subrama = new SubRamaList(objetoJSON.getInt("SubRamaID"),
														objetoJSON.getString("Codigo"),
														objetoJSON.getString("Nombre"),
														objetoJSON.getInt("RamaID"));
								
								listaSubRamas.add(subrama);						
							}
							
						}catch(JSONException e){
							e.printStackTrace();
						}
					} else {
						subrama = new SubRamaList(0,
								"0",
								"No hay ninguna subrama vinculada a esta rama",
								0);
						objSubRama.setSubRamaID(0);
						listaSubRamas.add(subrama);
					}
					
					ArrayAdapter<SubRamaList> adaptador = new ArrayAdapter<SubRamaList>(contexto,android.R.layout.simple_list_item_1,listaSubRamas);
					
					return adaptador;
				}

				@Override
				protected void onPostExecute(ArrayAdapter<SubRamaList> result) {
					// TODO Auto-generated method stub
					
					listaSubRamas2 = listaSubRamas;
					list.setAdapter(result);
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								subRamaLister = new SubRamaList();
								subRamaLister =(SubRamaList) parentView.getAdapter().getItem(position);
								subRama = new SubRama();
								subRama.setSubRamaID(subRamaLister.getSubRamaID());
								try{
									listarClases Clases = new listarClases();
									Clases.cargarContenido(getApplicationContext());
									Clases.setClasesList(listadoClases);
									Clases.execute(clases);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
					    }

					    @Override
					    public void onNothingSelected(AdapterView<?> parentView) {
					        // your code here
					    }
					});
					
						
					
				}

				@Override
				protected void onPreExecute() {

				}

			}
			// endregion
			
			// region CLASE PARA LISTAR ASYNCRONAMENTE
			class listarClases extends
				AsyncTask<Spinner, Void, ArrayAdapter<ClaseList>> {
				Context contexto;
				Spinner list;
				InputStream is;
				Clase[] ClaseNormal;
				ArrayList<ClaseList> listaClases2 = new ArrayList<ClaseList>();
				ArrayList<ClaseList> listaClases = new ArrayList<ClaseList>();

				public void setClasesList(ArrayList<ClaseList> listaClase) {
					this.listaClases2 = listaClase;
				}

				public void cargarContenido(Context contexto) {
					this.contexto = contexto;
				}

				@Override
				protected ArrayAdapter<ClaseList> doInBackground(Spinner... params) {
					// TODO Auto-generated method stub
					list = params[0];
					String resultado = "fallo";
					ClaseList clase;
					SubRama enviarSubRama = (subRama != null) ? subRama : objSubRama;
					//SectorID = ;
					//SectorList sector;

					// Creamos la conexion HTTP
					HttpClient cliente = new DefaultHttpClient();
					HttpGet peticionGet = new HttpGet(
							"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=8&SubRamaID=" + enviarSubRama.SubRamaID);

					try {

						HttpResponse response = cliente.execute(peticionGet);
						HttpEntity contenido = response.getEntity();

						is = contenido.getContent();

					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					BufferedReader buferLector = new BufferedReader(
							new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String linea = null;

					try {
						while ((linea = buferLector.readLine()) != null) {
							sb.append(linea);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					resultado = sb.toString();
					if(!resultado.equals("null")){
						try{
							
							JSONArray arrayJSON = new JSONArray(resultado);
							
							
							for(int i=0; i<arrayJSON.length(); i++){
								JSONObject objetoJSON = arrayJSON.getJSONObject(i);
								
								if(i == 0){
									objClase = new Clase();
									objClase.setScianID(objetoJSON.getInt("ScianID"));
									objClase.setCodigo(objetoJSON.getString("Codigo"));
									objClase.setNombre(objetoJSON.getString("Nombre"));
								}
								
								clase = new ClaseList(objetoJSON.getInt("ScianID"),
														objetoJSON.getString("Codigo"),
														objetoJSON.getString("Nombre"),
														objetoJSON.getInt("SubRamaID"));
								
								listaClases.add(clase);						
							}
							
						}catch(JSONException e){
							e.printStackTrace();
						}
					} else {
						clase = new ClaseList(0,
								"0",
								"No hay ninguna clase vinculada a esta subrama",
								0);
						objClase.setScianID(0);
						listaClases.add(clase);		
					}
					
					ArrayAdapter<ClaseList> adaptador = new ArrayAdapter<ClaseList>(contexto,android.R.layout.simple_list_item_1,listaClases);
					
					return adaptador;
				}

				@Override
				protected void onPostExecute(ArrayAdapter<ClaseList> result) {
					// TODO Auto-generated method stub
					
					listaClases2 = listaClases;
					list.setAdapter(result);
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								claseLister = new ClaseList();
								claseLister =(ClaseList) parentView.getAdapter().getItem(position);
								clase = new Clase();
								clase.setScianID(claseLister.getScianID());
							}
					    }

					    @Override
					    public void onNothingSelected(AdapterView<?> parentView) {
					        // your code here
					    }
					});
					
				}

				@Override
				protected void onPreExecute() {

				}

			}
			// endregion
			
	
			// region CLASE PARA LISTAR ASYNCRONAMENTE
						class listarEstados extends
							AsyncTask<Spinner, Void, ArrayAdapter<EstadoList>> {
							Context contexto;
							Spinner list;
							InputStream is;
							ArrayList<EstadoList> listaEstado2 = new ArrayList<EstadoList>();
							ArrayList<EstadoList> listaEstado = new ArrayList<EstadoList>();

							public void setEstadosList(ArrayList<EstadoList> listaEstado) {
								this.listaEstado2 = listaEstado;
							}

							public void cargarContenido(Context contexto) {
								this.contexto = contexto;
							}

							@Override
							protected ArrayAdapter<EstadoList> doInBackground(Spinner... params) {
								// TODO Auto-generated method stub
								list = params[0];
								String resultado = "fallo";
								EstadoList estado;
								
								//SectorID = ;
								//SectorList sector;

								// Creamos la conexion HTTP
								HttpClient cliente = new DefaultHttpClient();
								HttpGet peticionGet = new HttpGet(
										"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=5");

								try {

									HttpResponse response = cliente.execute(peticionGet);
									HttpEntity contenido = response.getEntity();

									is = contenido.getContent();

								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								BufferedReader buferLector = new BufferedReader(
										new InputStreamReader(is));
								StringBuilder sb = new StringBuilder();
								String linea = null;

								try {
									while ((linea = buferLector.readLine()) != null) {
										sb.append(linea);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}

								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}

								resultado = sb.toString();
								if(!resultado.equals("null")){
									try{
										
										JSONArray arrayJSON = new JSONArray(resultado);
										
										
										for(int i=0; i<arrayJSON.length(); i++){
											JSONObject objetoJSON = arrayJSON.getJSONObject(i);
											
											if(i == 0){
												objEstado = new Estado();
												objEstado.setEstadoID(objetoJSON.getInt("EstadoID"));
												objEstado.setNombre(objetoJSON.getString("Nombre"));
											}
											
											estado = new EstadoList(objetoJSON.getInt("EstadoID"),
																	objetoJSON.getString("Nombre"));
											
											listaEstado.add(estado);						
										}
										
									}catch(JSONException e){
										e.printStackTrace();
									}
								} else {
									estado = new EstadoList(0,
											"No hay ninguna clase vinculada a esta subrama");
									objEstado.setEstadoID(0);
									listaEstado.add(estado);		
								}
								
								ArrayAdapter<EstadoList> adaptador = new ArrayAdapter<EstadoList>(contexto,android.R.layout.simple_list_item_1,listaEstado);
								
								return adaptador;
							}

							@Override
							protected void onPostExecute(ArrayAdapter<EstadoList> result) {
								// TODO Auto-generated method stub
								
								listaEstado2 = listaEstado;
								list.setAdapter(result);
								list.setOnItemSelectedListener(new OnItemSelectedListener() {

								    @Override
								    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
								    	if (!parentView.getAdapter().isEmpty()){
								    		
											estadoLister = new EstadoList();
											estadoLister =(EstadoList) parentView.getAdapter().getItem(position);
											estado = new Estado();
											estado.setEstadoID(estadoLister.getEstadoID());
											try{
												listarMunicipios Municipios = new listarMunicipios();
												Municipios.cargarContenido(getApplicationContext());
												Municipios.setMunicipiosList(listadoMunicipios);
												Municipios.execute(municipios);
											}catch(Exception e){
												e.printStackTrace();
											}
										}
								    }

								    @Override
								    public void onNothingSelected(AdapterView<?> parentView) {
								        // your code here
								    }
								});
								
							}

							@Override
							protected void onPreExecute() {

							}

						}
						// endregion
						
						// region CLASE PARA LISTAR ASYNCRONAMENTE
						class listarMunicipios extends
							AsyncTask<Spinner, Void, ArrayAdapter<MunicipioList>> {
							Context contexto;
							Spinner list;
							InputStream is;
							ArrayList<MunicipioList> listaMunicipios2 = new ArrayList<MunicipioList>();
							ArrayList<MunicipioList> listaMunicipios = new ArrayList<MunicipioList>();

							public void setMunicipiosList(ArrayList<MunicipioList> listaMunicipio) {
								this.listaMunicipios2 = listaMunicipio;
							}

							public void cargarContenido(Context contexto) {
								this.contexto = contexto;
							}

							@Override
							protected ArrayAdapter<MunicipioList> doInBackground(Spinner... params) {
								// TODO Auto-generated method stub
								list = params[0];
								String resultado = "fallo";
								MunicipioList Municipio;
								Estado enviarEstado = (estado != null) ? estado : objEstado;
								//SectorID = ;
								//SectorList sector;

								// Creamos la conexion HTTP
								HttpClient cliente = new DefaultHttpClient();
								HttpGet peticionGet = new HttpGet(
										"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=6&EstadoID=" + enviarEstado.EstadoID);

								try {

									HttpResponse response = cliente.execute(peticionGet);
									HttpEntity contenido = response.getEntity();

									is = contenido.getContent();

								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								BufferedReader buferLector = new BufferedReader(
										new InputStreamReader(is));
								StringBuilder sb = new StringBuilder();
								String linea = null;

								try {
									while ((linea = buferLector.readLine()) != null) {
										sb.append(linea);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}

								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}

								resultado = sb.toString();
								if(!resultado.equals("null")){
									try{
										
										JSONArray arrayJSON = new JSONArray(resultado);
										
										
										for(int i=0; i<arrayJSON.length(); i++){
											JSONObject objetoJSON = arrayJSON.getJSONObject(i);
											
											if(i == 0){
												objMunicipio = new Municipio();
												objMunicipio.setMunicipioID(objetoJSON.getInt("MunicipioID"));
												objMunicipio.setNombre(objetoJSON.getString("Municipio"));
												objMunicipio.setEstadoID(objetoJSON.getInt("EstadoID"));
											}
											
											Municipio = new MunicipioList(objetoJSON.getInt("MunicipioID"),
																	objetoJSON.getString("Municipio"),
																	objetoJSON.getInt("EstadoID"));
											
											listaMunicipios.add(Municipio);						
										}
										
									}catch(JSONException e){
										e.printStackTrace();
									}
								} else {
									Municipio = new MunicipioList(0,
											"No hay ningun municipio vinculado a este estado",
											0);
									objMunicipio.setMunicipioID(0);
									listaMunicipios.add(Municipio);		
								}
								
								ArrayAdapter<MunicipioList> adaptador = new ArrayAdapter<MunicipioList>(contexto,android.R.layout.simple_list_item_1,listaMunicipios);
								
								return adaptador;
							}

							@Override
							protected void onPostExecute(ArrayAdapter<MunicipioList> result) {
								// TODO Auto-generated method stub
								
								listaMunicipios2 = listaMunicipios;
								list.setAdapter(result);
								list.setOnItemSelectedListener(new OnItemSelectedListener() {

								    @Override
								    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
								    	if (!parentView.getAdapter().isEmpty()){
								    		
											municipioLister = new MunicipioList();
											municipioLister =(MunicipioList) parentView.getAdapter().getItem(position);
											municipio = new Municipio();
											municipio.setMunicipioID(municipioLister.getMunicipioID());
										}
								    }

								    @Override
								    public void onNothingSelected(AdapterView<?> parentView) {
								        // your code here
								    }
								});
								
							}

							@Override
							protected void onPreExecute() {

							}

						}
						// endregion
				
			
			
	
}
