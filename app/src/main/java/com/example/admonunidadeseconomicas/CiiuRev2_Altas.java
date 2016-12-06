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

import com.example.admonunidadeseconomicas.CiiuRev2_List.listarCiiuRev2;
import com.example.admonunidadeseconomicas.CiiuRev2_List.listarCiiuRev3;
import com.objects.beans.CIIURev2;
import com.objects.beans.CIIURev3;
import com.objects.beans.CIIURev4;
import com.objects.beans.Clase;
import com.objects.beans.Rama;
import com.objects.beans.Sector;
import com.objects.beans.SubRama;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.CiiuRev3List;
import com.objects.beans.listerBeans.CiiuRev4List;
import com.objects.beans.listerBeans.ClaseList;
import com.objects.beans.listerBeans.RamaList;
import com.objects.beans.listerBeans.SectorList;
import com.objects.beans.listerBeans.SubRamaList;
import com.objects.beans.listerBeans.SubSectorList;

public class CiiuRev2_Altas extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
	EditText  Codigo,Nombre;
	TextView Titulo;
	Button  guardar,cancelar;
	SectorList sectorLister;
	SubSectorList subsectorLister;
	RamaList ramaLister;
	SubRamaList subRamaLister;
	ClaseList claseLister;
	CiiuRev4List ciiurev4Lister;
	CiiuRev3List ciiurev3Lister;
	Sector sector = null, objSector;
	SubSector subSector = null, objSubSector;
	Rama rama = null, objRama;
	SubRama subRama = null, objSubRama;
	Clase clase = null, objClase;
	CIIURev4 ciiurev4 = null, objCiiurev4;
	CIIURev3 ciiurev3 = null, objCiiurev3;
	CIIURev2 ciiurev2;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	Spinner sectores, subsectores, ramas, subramas, clases, ciiurev4s, ciiurev3s;
	ArrayList<SectorList> listadoSectores = new ArrayList<SectorList>();
	ArrayList<SubSectorList> listadoSubSectores = new ArrayList<SubSectorList>();
	ArrayList<RamaList> listadoRamas = new ArrayList<RamaList>();
	ArrayList<SubRamaList> listadoSubRamas = new ArrayList<SubRamaList>();
	ArrayList<ClaseList> listadoClases = new ArrayList<ClaseList>();
	ArrayList<CiiuRev4List> listadoCiiurev4 = new ArrayList<CiiuRev4List>();
	ArrayList<CiiuRev3List> listadoCiiurev3 = new ArrayList<CiiuRev3List>();
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ciiu_rev2__altas);
		
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
			
			case R.id.CiiuRev2AltaGuardar:
					ciiurev2 = new CIIURev2();
					ciiurev2.setCodigo(Codigo.getText().toString());
					ciiurev2.setNombre(Nombre.getText().toString());
					ciiurev2.CiiuRev3ID = (ciiurev3 != null) ? ciiurev3.CiiuRev3ID : objCiiurev3.CiiuRev3ID;
					if(Pattern.matches("[0-9a-z]{4,5}", Codigo.getText().toString()) && !Nombre.getText().toString().equals("") && ciiurev2.CiiuRev3ID != 0){
					
					try{
						
						doLogginBackground doLoggin = new doLogginBackground();
						doLoggin.setCiiuRev2(ciiurev2);
						doLoggin.execute();
						
					}catch(Exception e){
						
						e.printStackTrace();
						
					}
				} else {
					new AlertDialog.Builder(CiiuRev2_Altas.this)
		              .setTitle("Informacion")
		              .setMessage("El codigo CIIU Rev 2 debe tener una longitud de 4 a 5 digitos.\nEl nombre del CIIU Rev 2 no puede estar vacio.\nEl CIIU Rev 2 debe estar vinculado a un CIIU Rev 3.")
		              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int which) { 
		                  }
		               })
		               .show();
				}
				break;
			case R.id.CiiuRev2AltaCancelar:
				i = new Intent(this,CiiuRev2_List.class);
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
			Titulo = (TextView)findViewById(R.id.CiiuRev2AltasTitulo);
			Codigo = (EditText)findViewById(R.id.CiiuRev2AltaCodigo);
			Nombre = (EditText)findViewById(R.id.CiiuRev2AltaNombre);
			guardar = (Button)findViewById(R.id.CiiuRev2AltaGuardar);
			cancelar = (Button)findViewById(R.id.CiiuRev2AltaCancelar);
			sectores = (Spinner)findViewById(R.id.CiiuRev2AltaListadoSect);
			subsectores = (Spinner)findViewById(R.id.CiiuRev2AltaListadoSubS);
			ramas = (Spinner)findViewById(R.id.CiiuRev2AltaListadoRama);
			subramas = (Spinner)findViewById(R.id.CiiuRev2AltaListadoSubR);
			clases = (Spinner)findViewById(R.id.CiiuRev2AltaListadoClases);
			ciiurev4s = (Spinner)findViewById(R.id.CiiuRev2AltaListadoCR4);
			ciiurev3s = (Spinner)findViewById(R.id.CiiuRev2AltaListadoCR3);
			Titulo.setText("ALTA CIIU Rev 2");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fallo la insercion", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "CIIU Rev 2 registrado exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this, CiiuRev2_List.class);
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
			CIIURev2 thisCiiuRev2;
			String Respuesta = null;
		
			public void setCiiuRev2(CIIURev2 ciiurev2){
				thisCiiuRev2 = ciiurev2;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(CiiuRev2_Altas.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Agregando CIIU Rev 2");
	           progreso.setMessage("Verificando Codigo y Nombre, Espere...");
	           progreso.setCancelable(false);
	           progreso.setMax(100);
	           progreso.setProgress(0);
	           progreso.show();

			}

			@Override
			protected Void doInBackground(Void... array) {
				thisCiiuRev2.applyEdit();
				
				Respuesta = thisCiiuRev2.Respuesta;
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
								try{
									listarCiiuRev4 CiiuRev4 = new listarCiiuRev4();
									CiiuRev4.cargarContenido(getApplicationContext());
									CiiuRev4.setCiiuRev4List(listadoCiiurev4);
									CiiuRev4.execute(ciiurev4s);
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
			class listarCiiuRev4 extends
				AsyncTask<Spinner, Void, ArrayAdapter<CiiuRev4List>> {
				Context contexto;
				Spinner list;
				InputStream is;
				CIIURev4[] CiiuRev4Normal;
				ArrayList<CiiuRev4List> listaCiiuRev42 = new ArrayList<CiiuRev4List>();
				ArrayList<CiiuRev4List> listaCiiuRev4 = new ArrayList<CiiuRev4List>();

				public void setCiiuRev4List(ArrayList<CiiuRev4List> listaCiiuRev4) {
					this.listaCiiuRev42 = listaCiiuRev4;
				}

				public void cargarContenido(Context contexto) {
					this.contexto = contexto;
				}

				@Override
				protected ArrayAdapter<CiiuRev4List> doInBackground(Spinner... params) {
					// TODO Auto-generated method stub
					list = params[0];
					String resultado = "fallo";
					CiiuRev4List ciiuRev4;
					Clase enviarClase = (clase != null) ? clase : objClase;
					//SectorID = ;
					//SectorList sector;

					// Creamos la conexion HTTP
					HttpClient cliente = new DefaultHttpClient();
					HttpGet peticionGet = new HttpGet(
							"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=9&ScianID=" + enviarClase.ScianID);

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
									objCiiurev4 = new CIIURev4();
									objCiiurev4.setCiiuRev4ID(objetoJSON.getInt("CiiuRev4ID"));
									objCiiurev4.setCodigo(objetoJSON.getString("Codigo"));
									objCiiurev4.setNombre(objetoJSON.getString("Nombre"));
								}
								
								ciiuRev4 = new CiiuRev4List(objetoJSON.getInt("CiiuRev4ID"),
														objetoJSON.getString("Codigo"),
														objetoJSON.getString("Nombre"),
														objetoJSON.getInt("ScianID"));
								
								listaCiiuRev4.add(ciiuRev4);						
							}
							
						}catch(JSONException e){
							e.printStackTrace();
						}
					} else {
						ciiuRev4 = new CiiuRev4List(0,
								"0",
								"No hay ningun CIIU Rev 4 vinculado a esta clase",
								0);
						objCiiurev4.setCiiuRev4ID(0);
						listaCiiuRev4.add(ciiuRev4);
					}
					
					ArrayAdapter<CiiuRev4List> adaptador = new ArrayAdapter<CiiuRev4List>(contexto,android.R.layout.simple_list_item_1,listaCiiuRev4);
					
					return adaptador;
				}

				@Override
				protected void onPostExecute(ArrayAdapter<CiiuRev4List> result) {
					// TODO Auto-generated method stub
					
					listaCiiuRev42 = listaCiiuRev4;
					list.setAdapter(result);
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								ciiurev4Lister = new CiiuRev4List();
								ciiurev4Lister =(CiiuRev4List) parentView.getAdapter().getItem(position);
								ciiurev4 = new CIIURev4();
								ciiurev4.setCiiuRev4ID(ciiurev4Lister.getCiiuRev4ID());
								try{
									listarCiiuRev3 CiiuRev3 = new listarCiiuRev3();
									CiiuRev3.cargarContenido(getApplicationContext());
									CiiuRev3.setCiiuRev3List(listadoCiiurev3);
									CiiuRev3.execute(ciiurev3s);
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
			class listarCiiuRev3 extends
				AsyncTask<Spinner, Void, ArrayAdapter<CiiuRev3List>> {
				Context contexto;
				Spinner list;
				InputStream is;
				CIIURev3[] CiiuRev3Normal;
				ArrayList<CiiuRev3List> listaCiiuRev32 = new ArrayList<CiiuRev3List>();
				ArrayList<CiiuRev3List> listaCiiuRev3 = new ArrayList<CiiuRev3List>();

				public void setCiiuRev3List(ArrayList<CiiuRev3List> listaCiiuRev3) {
					this.listaCiiuRev32 = listaCiiuRev3;
				}

				public void cargarContenido(Context contexto) {
					this.contexto = contexto;
				}

				@Override
				protected ArrayAdapter<CiiuRev3List> doInBackground(Spinner... params) {
					// TODO Auto-generated method stub
					list = params[0];
					String resultado = "fallo";
					CiiuRev3List ciiuRev3;
					CIIURev4 enviarCiiuRev4 = (ciiurev4 != null) ? ciiurev4 : objCiiurev4;
					//SectorID = ;
					//SectorList sector;

					// Creamos la conexion HTTP
					HttpClient cliente = new DefaultHttpClient();
					HttpGet peticionGet = new HttpGet(
							"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=10&CiiuRev4ID=" + enviarCiiuRev4.CiiuRev4ID);

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
									objCiiurev3 = new CIIURev3();
									objCiiurev3.setCiiuRev3ID(objetoJSON.getInt("CiiuRev3ID"));
									objCiiurev3.setCodigo(objetoJSON.getString("Codigo"));
									objCiiurev3.setNombre(objetoJSON.getString("Nombre"));
								}
								
								ciiuRev3 = new CiiuRev3List(objetoJSON.getInt("CiiuRev3ID"),
														objetoJSON.getString("Codigo"),
														objetoJSON.getString("Nombre"),
														objetoJSON.getInt("CiiuRev4ID"));
								
								listaCiiuRev3.add(ciiuRev3);						
							}
							
						}catch(JSONException e){
							e.printStackTrace();
						}
					} else {
						ciiuRev3 = new CiiuRev3List(0,
								"0",
								"No hay ningun CIIU Rev 3 vinculado al CIIU Rev 4",
								0);
						objCiiurev3.setCiiuRev3ID(0);
						listaCiiuRev3.add(ciiuRev3);
					}
					
					ArrayAdapter<CiiuRev3List> adaptador = new ArrayAdapter<CiiuRev3List>(contexto,android.R.layout.simple_list_item_1,listaCiiuRev3);
					
					return adaptador;
				}

				@Override
				protected void onPostExecute(ArrayAdapter<CiiuRev3List> result) {
					// TODO Auto-generated method stub
					
					listaCiiuRev32 = listaCiiuRev3;
					list.setAdapter(result);
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								ciiurev3Lister = new CiiuRev3List();
								ciiurev3Lister =(CiiuRev3List) parentView.getAdapter().getItem(position);
								ciiurev3 = new CIIURev3();
								ciiurev3.setCiiuRev3ID(ciiurev3Lister.getCiiuRev3ID());
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