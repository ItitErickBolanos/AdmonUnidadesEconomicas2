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

import com.objects.beans.Rama;
import com.objects.beans.Sector;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.RamaList;
import com.objects.beans.listerBeans.SectorList;
import com.objects.beans.listerBeans.SubSectorList;

public class Ramas_List extends ActionBarActivity implements
		OnItemClickListener {

	// region PROPIEDADES DE CLASE
	Bundle bundleReceptor, bundleUsuario, bundleSector;
	Intent i;
	Usuario usuarioFromABC;
	SubSector subsector;
	Rama rama;
	Sector sector = null, objSector;
	SubSector subSector = null, objSubSector;
	SectorList sectorLister;
	SubSectorList subsectorLister;
	RamaList ramaLister;
	TextView headerText;
	ListView listaramas;
	//ArrayList<Map<String, String>> listadosubsectores = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listadoramas = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listaElementos;
	ArrayList<Map<String, String>> listaRamas = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listarElementos;
	ArrayList<SectorList> listadoSectores = new ArrayList<SectorList>();
	ArrayList<SubSectorList> listadoSubSectores = new ArrayList<SubSectorList>();
	String Nombre, Codigo, ramaID, Actualizacion, mensaje = "";
	Spinner sectores, subsectores;

	// endregion

	// region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ramas__list);

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
		listaramas.setOnItemClickListener(this);
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
			sigCambios(rama);
			return true;

		case R.id.eliminar:
			//CODIGO PARA ELIMINACION
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Esta seguro de que desea eliminar el registro?")
		       .setCancelable(false)
		       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                sigBorrar(rama);
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
		i = new Intent(this,Ramas_Altas.class);
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
	public void sigCambios(Rama rama) {
		i = new Intent(this, Ramas_Cambios.class);
		bundleUsuario = new Bundle();
		bundleUsuario.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
		bundleUsuario.putString("Loggin", usuarioFromABC.getLoggin());
		bundleUsuario.putString("Password", usuarioFromABC.getPassword());
		bundleUsuario.putInt("PerfilID", usuarioFromABC.getPerfilID());
		bundleUsuario.putInt("DatosID", usuarioFromABC.getDatosID());
		bundleUsuario.putString("Email", usuarioFromABC.getEmail());

		bundleUsuario.putInt("SubSectorID", rama.getSubSectorID());
		bundleUsuario.putInt("RamaID", rama.getRamaID());
		bundleUsuario.putString("Codigo", rama.getCodigo());
		bundleUsuario.putString("Nombre", rama.getNombre());
		bundleUsuario.putInt("posicionSpinnerSect", sectores.getSelectedItemPosition());
		bundleUsuario.putInt("posicionSpinnerSubS", subsectores.getSelectedItemPosition());
		
		i.putExtras(bundleUsuario);

		startActivity(i);
	}
	
	public void sigBorrar(Rama rama) {
		try{
			
			Borrado borrar = new Borrado();
			borrar.setRama(rama.getRamaID());
			borrar.execute();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}

	// endregion

	public void populateView(){
		sectores = (Spinner)findViewById(R.id.sectoresSpinR);
		subsectores = (Spinner)findViewById(R.id.subsectoresSpin);
		listaramas = (ListView) findViewById(R.id.ramasListado);
		headerText = (TextView) findViewById(R.id.headerRamasList);
	}
	
	// region RECUPERANDO USUARIO ( IMPORTANTE PARA ENVIARLO A LA OTRA
	// ACTIVIDAD)

	public void populateExtras() {
		bundleReceptor = getIntent().getExtras();
		if (!bundleReceptor.isEmpty()) {

			usuarioFromABC = new Usuario();
			subsector = new SubSector();

			subsector.setSubSectorID(bundleReceptor.getInt("SectorID"));

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
						
						try{
							listarSubSectores SubSectores = new listarSubSectores();
							SubSectores.cargarContenido(getApplicationContext());
							SubSectores.setSubSectorList(listadoSubSectores);
							SubSectores.execute(subsectores);
							
						}catch(Exception e){
							e.printStackTrace();
						}
						
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
												Ramas.setRamaList(listaRamas);
												Ramas.execute(listaramas);
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
								
								try{
									listarRamas Ramas = new listarRamas();
									Ramas.cargarContenido(getApplicationContext());
									Ramas.setRamaList(listaRamas);
									Ramas.execute(listaramas);
								}catch(Exception e){
									e.printStackTrace();
								}
								
							}

							@Override
							protected void onPreExecute() {

								
							}
							
						}
					//endregion
	
	// region CLASE PARA LISTAR ASYNCRONAMENTE
	class listarRamas extends
		AsyncTask<ListView, Void, SimpleAdapter> {
		Context contexto;
		ListView list;
		InputStream is;
		Rama[] RamaNormal;
		ArrayList<Map<String, String>> listaRamas2 = new ArrayList<Map<String, String>> ();
		ArrayList<Map<String, String>>  listaRamas = new ArrayList<Map<String, String>> ();

		public void setRamaList(ArrayList<Map<String, String>> listaRamas) {
			this.listaRamas2 = listaRamas;
		}

		public void cargarContenido(Context contexto) {
			this.contexto = contexto;
		}

		@Override
		protected SimpleAdapter doInBackground(ListView... params) {
			// TODO Auto-generated method stub
			list = params[0];
			String resultado = "fallo";
			SubSector enviarSubSector = (subsector != null) ? subsector : objSubSector;
			//SectorID = ;
			//SectorList sector;

			// Creamos la conexion HTTP
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGet = new HttpGet(
					"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=13&SubSectorID=" + enviarSubSector.SubSectorID);

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
							datos.put("RamaID", jsonIterator.get("RamaID").toString());
							datos.put("Codigo", jsonIterator.get("Codigo").toString());
							datos.put("Nombre", jsonIterator.get("Nombre").toString());
							datos.put("SubSectorID", jsonIterator.get("SubSectorID").toString());
							datos.put("Actualizacion", jsonIterator.get("Actualizacion").toString());
							listaRamas.add(datos);
						}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mensaje = "";
			} else
				mensaje = "El subsector no tiene ramas vinculadas";

			SimpleAdapter adaptador = new SimpleAdapter(contexto, listaRamas, 
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
				listaRamas2 = listaRamas;
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
		Rama thisRama = new Rama();
		String Respuesta = null,Nombre=null,Codigo=null;;
		int ID = 0;
		
	
		public void setRama(int ID){
			this.ID = ID;
			thisRama.RamaID = ID;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(Ramas_List.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progreso.setTitle("Eliminando SubSector");
           progreso.setMessage("Borrando SubSector, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisRama.delete(thisRama.RamaID);
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

				listarRamas Ramas = new listarRamas();
				Ramas.cargarContenido(getApplicationContext());
				Ramas.setRamaList(listadoramas);
				Ramas.execute(listaramas);

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
			
			Map<String, String>  ramaLister = new HashMap<String, String>(2);
			rama = new Rama();
			ramaLister  =  (Map<String, String>) parent.getAdapter().getItem(position);
			
			rama.setSubSectorID(Integer.parseInt(ramaLister.get("RamaID")));
			rama.setCodigo(ramaLister.get("Codigo").toString());
			rama.setNombre(ramaLister.get("Nombre").toString());
			rama.setRamaID(Integer.parseInt(ramaLister.get("RamaID")));
			
			view.showContextMenu();
		}
		
	}
}
