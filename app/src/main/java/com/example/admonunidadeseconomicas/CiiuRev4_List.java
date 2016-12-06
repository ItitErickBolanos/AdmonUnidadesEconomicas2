package com.example.admonunidadeseconomicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.objects.beans.CIIURev4;
import com.objects.beans.Clase;
import com.objects.beans.Rama;
import com.objects.beans.Sector;
import com.objects.beans.SubRama;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.ClaseList;
import com.objects.beans.listerBeans.RamaList;
import com.objects.beans.listerBeans.SectorList;
import com.objects.beans.listerBeans.SubRamaList;
import com.objects.beans.listerBeans.SubSectorList;

public class CiiuRev4_List extends ActionBarActivity implements
		OnItemClickListener {

	// region PROPIEDADES DE CLASE
	Bundle bundleReceptor, bundleUsuario, bundleSector;
	Intent i;
	Usuario usuarioFromABC;
	SubSector subsector;
	Rama rama = null, objRama;
	SubRama subrama = null, objSubRama;
	Sector sector = null, objSector;
	SubSector subSector = null, objSubSector;
	Clase clase = null, objClase;
	CIIURev4 ciiurev4;
	SectorList sectorLister;
	SubSectorList subsectorLister;
	RamaList ramaLister;
	SubRamaList subramaLister;
	ClaseList claseLister;
	TextView headerText;
	ListView listaciiurev4;
	ArrayList<Map<String, String>> listadociiurev4 = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listaElementos;
	ArrayList<Map<String, String>> listaCiiurev4 = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listarElementos;
	ArrayList<SectorList> listadoSectores = new ArrayList<SectorList>();
	ArrayList<SubSectorList> listadoSubSectores = new ArrayList<SubSectorList>();
	ArrayList<RamaList> listadoRamas = new ArrayList<RamaList>();
	ArrayList<SubRamaList> listadoSubRamas = new ArrayList<SubRamaList>();
	ArrayList<ClaseList> listadoClases = new ArrayList<ClaseList>();
	String Nombre, Codigo, ciiurev4ID, Actualizacion, mensaje = "";
	Spinner sectores, subsectores, ramas, subramas, clases;

	// endregion

	// region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ciiu_rev4__list);

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
		listaciiurev4.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sectores__list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.altaSectores:
			sigAltas();
			break;
		
		case R.id.menuAppSalir:
			System.exit(0);
			break;
		case R.id.menuAppCerrarSesion:
			System.exit(0);
			break;
		case R.id.menuAppSistema:
			Toast.makeText(
					this,
					"Creado por; Legendary Software 'Innovation and Solutions' www.legendarysoftware.com.mx",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.menuAppEstadoServidor:

			try {
				if (InetAddress.getByName("http://semestral.esy.es")
						.isReachable(30)) {
					Toast.makeText(this, "Servidor en linea", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(this, "Servidor fuera de servicio",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Log.i("no llego", "Excepcion de error");
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// endregion

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("Opciones");
		inflater.inflate(R.menu.menu_contextual, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.editar:
			//CODIGO PARA EDITAR
			sigCambios(ciiurev4);
			return true;

		case R.id.eliminar:
			//CODIGO PARA ELIMINACION
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Esta seguro de que desea eliminar el registro?")
		       .setCancelable(false)
		       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                sigBorrar(ciiurev4);
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		    AlertDialog alert = builder.create();
		    alert.show();
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	// region ENVIO DE DATOS A USUARIOS PROYECTOS y DEMAS
	public void sigAltas(){
		i = new Intent(this,CiiuRev4_Altas.class);
		Bundle b = new Bundle();
			b.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
			b.putString("Loggin", usuarioFromABC.getLoggin());
			b.putString("Password", usuarioFromABC.getPassword());
			b.putInt("PerfilID", usuarioFromABC.getPerfilID());
			b.putInt("DatosID",usuarioFromABC.getDatosID());
			b.putString("Email",usuarioFromABC.getEmail());
				
		i.putExtras(b);
		startActivity(i);
	}
	
	// Metodo para enviar session a Ramas_ABC
	public void sigCambios(CIIURev4 ciiurev4) {
		i = new Intent(this, CiiuRev4_Cambios.class);
		bundleUsuario = new Bundle();
		bundleUsuario.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
		bundleUsuario.putString("Loggin", usuarioFromABC.getLoggin());
		bundleUsuario.putString("Password", usuarioFromABC.getPassword());
		bundleUsuario.putInt("PerfilID", usuarioFromABC.getPerfilID());
		bundleUsuario.putInt("DatosID", usuarioFromABC.getDatosID());
		bundleUsuario.putString("Email", usuarioFromABC.getEmail());

		bundleUsuario.putInt("CiiuRev4ID", ciiurev4.getCiiuRev4ID());
		System.out.println("Soy el ciiurev4 id en la lista: " + ciiurev4.CiiuRev4ID);
		bundleUsuario.putInt("ScianID", ciiurev4.getScianID());
		bundleUsuario.putString("Codigo", ciiurev4.getCodigo());
		bundleUsuario.putString("Nombre", ciiurev4.getNombre());
		bundleUsuario.putInt("posicionSpinnerSect", sectores.getSelectedItemPosition());
		bundleUsuario.putInt("posicionSpinnerSubS", subsectores.getSelectedItemPosition());
		bundleUsuario.putInt("posicionSpinnerRama", ramas.getSelectedItemPosition());
		bundleUsuario.putInt("posicionSpinnerSubR", subramas.getSelectedItemPosition());
		bundleUsuario.putInt("posicionSpinnerClase", clases.getSelectedItemPosition());
		
		i.putExtras(bundleUsuario);

		startActivity(i);
	}
	
	public void sigBorrar(CIIURev4 ciiurev4) {
		try{
			
			Borrado borrar = new Borrado();
			borrar.setCiiuRev4(ciiurev4.getCiiuRev4ID());
			borrar.execute();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}

	// endregion

	public void populateView(){
		sectores = (Spinner)findViewById(R.id.sectoresSpinCiiuRev4);
		subsectores = (Spinner)findViewById(R.id.subsectoresSpinCiiuRev4);
		ramas = (Spinner)findViewById(R.id.ramasSpinCiiuRev4);
		subramas = (Spinner)findViewById(R.id.subRamasSpinCiiuRev4);
		clases = (Spinner)findViewById(R.id.clasesSpinCiiuRev4);
		listaciiurev4 = (ListView) findViewById(R.id.CiiuRev4Listado);
		headerText = (TextView) findViewById(R.id.headerCiiuRev4List);
	}
	
	// region RECUPERANDO USUARIO ( IMPORTANTE PARA ENVIARLO A LA OTRA
	// ACTIVIDAD)

	public void populateExtras() {
		bundleReceptor = getIntent().getExtras();
		if (!bundleReceptor.isEmpty()) {

			usuarioFromABC = new Usuario();
			rama = new Rama();

			rama.setRamaID(bundleReceptor.getInt("RamaID"));

			usuarioFromABC.setUsuarioID(bundleReceptor.getInt("UsuarioID"));
			usuarioFromABC.setLoggin(bundleReceptor.getString("Loggin"));
			usuarioFromABC.setPassword(bundleReceptor.getString("Password"));
			usuarioFromABC.setPerfilID(bundleReceptor.getInt("PerfilID"));
			usuarioFromABC.setDatosID(bundleReceptor.getInt("DatosID"));
			usuarioFromABC.setEmail(bundleReceptor.getString("Email"));
		}
	}

	// endregion
	
	// region MENSAJES DE ERROR, ALERTS , ETC.
	public void avisoError() {
		Toast.makeText(getApplicationContext(), "TITULO", Toast.LENGTH_SHORT)
				.show();
	}

	// endregion

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
											subsector = new SubSector();
											subsector.setSubSectorID(subsectorLister.getSubSectorID());
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
			    		
						subramaLister = new SubRamaList();
						subramaLister =(SubRamaList) parentView.getAdapter().getItem(position);
						subrama = new SubRama();
						subrama.setSubRamaID(subramaLister.getSubRamaID());
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
				SubRama enviarSubRama = (subrama != null) ? subrama : objSubRama;
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
								CiiuRev4.setCiiuRev4List(listadociiurev4);
								CiiuRev4.execute(listaciiurev4);
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
					AsyncTask<ListView, Void, SimpleAdapter> {
					Context contexto;
					ListView list;
					InputStream is;
					CIIURev4[] CIIURev4Normal;
					ArrayList<Map<String, String>> listaCiiuRev42 = new ArrayList<Map<String, String>> ();
					ArrayList<Map<String, String>>  listaCiiuRev4 = new ArrayList<Map<String, String>> ();

					public void setCiiuRev4List(ArrayList<Map<String, String>> listaCiiuRev4) {
						this.listaCiiuRev42 = listaCiiuRev4;
					}

					public void cargarContenido(Context contexto) {
						this.contexto = contexto;
					}

					@Override
					protected SimpleAdapter doInBackground(ListView... params) {
						// TODO Auto-generated method stub
						list = params[0];
						String resultado = "fallo";
						Clase enviarClase = (clase != null) ? clase : objClase;
						//SectorID = ;
						//SectorList sector;

						// Creamos la conexion HTTP
						HttpClient cliente = new DefaultHttpClient();
						HttpGet peticionGet = new HttpGet(
								"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=9&ScianID=" + enviarClase.ScianID);
						System.out.println("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=9&ScianID=" + enviarClase.ScianID);
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
						System.out.println(resultado);
						if(!resultado.equals("null")){
							try {
				
								JSONArray arrayJSON = new JSONArray(resultado);
								String subTitle;
								
								
									for (int i = 0; i < arrayJSON.length(); i++) {
										System.out.println(i);
										Map<String, String> datos = new HashMap<String, String>(2);
										JSONObject jsonIterator = new JSONObject(arrayJSON.get(i).toString());
										subTitle = "\nNombre: "+ jsonIterator.get("Nombre").toString();
										datos.put("subtitle", "Codigo: " + jsonIterator.get("Codigo").toString());
										datos.put("title", subTitle);
										datos.put("CiiuRev4ID", jsonIterator.get("CiiuRev4ID").toString());
										datos.put("Codigo", jsonIterator.get("Codigo").toString());
										datos.put("Nombre", jsonIterator.get("Nombre").toString());
										datos.put("ScianID", jsonIterator.get("ScianID").toString());
										datos.put("Actualizacion", jsonIterator.get("Actualizacion").toString());
										listaCiiuRev4.add(datos);
									}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
							mensaje = "";
						} else
							mensaje = "La clase no tiene equivalente CIIU Rev 4";

						SimpleAdapter adaptador = new SimpleAdapter(contexto, listaCiiuRev4, 
								android.R.layout.simple_list_item_2,
								new String[] { "title", "subtitle" }, new int[] {android.R.id.text1, android.R.id.text2 });

						return adaptador;
					}

					@Override
					protected void onPostExecute(SimpleAdapter result) {
						// TODO Auto-generated method stub
						
						if (!mensaje.equals("")){
							headerText.setVisibility(View.VISIBLE);
							headerText.setText(mensaje);
							list.setVisibility(View.GONE);
						} else {
							headerText.setVisibility(View.GONE);
							listaCiiuRev42 = listaCiiuRev4;
							list.setAdapter(result);
							list.setVisibility(View.VISIBLE);
							registerForContextMenu(list);
						}
							
						
					}

					@Override
					protected void onPreExecute() {

					}

				}
				// endregion		

	// region AsyncTask para borrado de registros
	//region CLASE QUE IMPLEMENTA ASYNCTASK
  	class Borrado extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progreso;
		CIIURev4 thisCiiuRev4 = new CIIURev4();
		String Respuesta = null,Nombre=null,Codigo=null;;
		int ID = 0;
		
	
		public void setCiiuRev4(int ID){
			this.ID = ID;
			thisCiiuRev4.CiiuRev4ID = ID;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(CiiuRev4_List.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progreso.setTitle("Eliminando Equivalencia CIIU Rev 4");
           progreso.setMessage("Borrando Equivalencia CIIU Rev 4, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisCiiuRev4.delete(thisCiiuRev4.CiiuRev4ID);
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
				
			progreso.dismiss();
			try {

				listarCiiuRev4 CiiuRev4 = new listarCiiuRev4();
				CiiuRev4.cargarContenido(getApplicationContext());
				CiiuRev4.setCiiuRev4List(listadociiurev4);
				CiiuRev4.execute(listaciiurev4);

			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}

  	}
  	
	// endregion
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (!parent.getAdapter().isEmpty()){
			
			Map<String, String>  ciiuRev4Lister = new HashMap<String, String>(2);
			ciiurev4 = new CIIURev4();
			ciiuRev4Lister  =  (Map<String, String>) parent.getAdapter().getItem(position);
			
			ciiurev4.setCiiuRev4ID(Integer.parseInt(ciiuRev4Lister.get("CiiuRev4ID")));
			ciiurev4.setCodigo(ciiuRev4Lister.get("Codigo").toString());
			ciiurev4.setNombre(ciiuRev4Lister.get("Nombre").toString());
			ciiurev4.setScianID(Integer.parseInt(ciiuRev4Lister.get("ScianID")));
			System.out.println(subrama.SubRamaID);
			
			view.showContextMenu();
		}
		
	}
}
