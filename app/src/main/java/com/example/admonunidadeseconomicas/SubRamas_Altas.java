package com.example.admonunidadeseconomicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.objects.beans.Rama;
import com.objects.beans.Sector;
import com.objects.beans.SubRama;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.RamaList;
import com.objects.beans.listerBeans.SectorList;
import com.objects.beans.listerBeans.SubSectorList;

public class SubRamas_Altas extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
	EditText  Codigo,Nombre;
	TextView Titulo;
	Button  guardar,cancelar;
	SectorList sectorLister;
	SubSectorList subsectorLister;
	RamaList ramaLister;
	Sector sector = null, objSector;
	SubSector subSector = null, objSubSector;
	Rama rama = null, objRama;
	SubRama subRama;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	Spinner sectores, subsectores, ramas;
	ArrayList<SectorList> listadoSectores = new ArrayList<SectorList>();
	ArrayList<SubSectorList> listadoSubSectores = new ArrayList<SubSectorList>();
	ArrayList<RamaList> listadoRamas = new ArrayList<RamaList>();
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_rama__altas);
		
		populateView();
		populateExtras();
		
		try{
			
			listarSectores Sectores = new listarSectores();
			Sectores.cargarContenido(getApplicationContext());
			Sectores.setSectorList(listadoSectores);
			Sectores.execute(sectores);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
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
			
			case R.id.subRamasAltaGuardar:
					subRama = new SubRama();
					subRama.setCodigo(Codigo.getText().toString());
					subRama.setNombre(Nombre.getText().toString());
					subRama.RamaID = (rama != null) ? rama.RamaID : objRama.RamaID;
					if(Pattern.matches("[0-9]{5}", Codigo.getText().toString()) && !Nombre.getText().toString().equals("") && subRama.RamaID != 0){
					try{
						
						doLogginBackground doLoggin = new doLogginBackground();
						doLoggin.setSubRama(subRama);
						doLoggin.execute();
						
					}catch(Exception e){
						
						e.printStackTrace();
						
					}
				} else {
					new AlertDialog.Builder(SubRamas_Altas.this)
		              .setTitle("Informacion")
		              .setMessage("El codigo de la subrama debe tener una longitud de 5 digitos y ser un numero.\nEl nombre de la subrama no puede estar vacio.\nLa subrama debe estar vinculada a una rama.")
		              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int which) { 
		                  }
		               })
		               .show();
				}
				break;
			case R.id.subRamasAltaCancelar:
				i = new Intent(this,SubRamas_List.class);
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
			Titulo = (TextView)findViewById(R.id.subRamasAltasTitulo);
			Codigo = (EditText)findViewById(R.id.subRamasAltaCodigo);
			Nombre = (EditText)findViewById(R.id.subRamasAltaNombre);
			guardar = (Button)findViewById(R.id.subRamasAltaGuardar);
			cancelar = (Button)findViewById(R.id.subRamasAltaCancelar);
			sectores = (Spinner)findViewById(R.id.subRamasAltaListadoSect);
			subsectores = (Spinner)findViewById(R.id.subRamasAltaListadoSubS);
			ramas = (Spinner)findViewById(R.id.subRamasAltaListadoRama);
			Titulo.setText("ALTA DE SUBRAMAS");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fallo la insercion", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "SubRama registrada exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this, SubRamas_List.class);
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
			SubRama thisSubRama;
			String Respuesta = null;
		
			public void setSubRama(SubRama subrama){
				thisSubRama = subrama;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(SubRamas_Altas.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Agregando SubRama");
	           progreso.setMessage("Verificando Codigo y Nombre, Espere...");
	           progreso.setCancelable(false);
	           progreso.setMax(100);
	           progreso.setProgress(0);
	           progreso.show();

			}

			@Override
			protected Void doInBackground(Void... array) {
				thisSubRama.applyEdit();
				
				Respuesta = thisSubRama.Respuesta;
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
	
}
