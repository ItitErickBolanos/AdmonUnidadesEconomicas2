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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admonunidadeseconomicas.Sectores_Cambios.doLogginBackground;
import com.objects.beans.Sector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.SectorList;

public class Sectores_List extends ActionBarActivity implements
		OnItemClickListener {

	// region PROPIEDADES DE CLASE
	Bundle bundleReceptor, bundleUsuario, bundleSector;
	Intent i;
	Usuario usuarioFromABC;
	Sector sector;
	SectorList sectorLister;
	TextView headerText;
	ListView listaSectores;
	ArrayList<Map<String, String>> listadoSectores = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listaElementos;
	String Nombre, Codigo, SectorID, Actualizacion;

	// endregion

	// region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sectores__list);

		populateExtras();
		listaSectores = (ListView) findViewById(R.id.menuSectoresList);
		headerText = (TextView) findViewById(R.id.headerSectoresList);

		try {

			listarSectores Sectores = new listarSectores();
			Sectores.cargarContenido(getApplicationContext());
			Sectores.setSectorList(listadoSectores);
			Sectores.execute(listaSectores);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listaSectores.setOnItemClickListener(this);

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
			sigCambios(sector);
			return true;

		case R.id.eliminar:
			//CODIGO PARA ELIMINACION
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Esta seguro de que desea eliminar el registro?")
		       .setCancelable(false)
		       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                sigBorrar(sector);
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
		i = new Intent(this,Sectores_Alta.class);
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
	public void sigCambios(Sector sector) {
		i = new Intent(this, Sectores_Cambios.class);
		bundleUsuario = new Bundle();
		bundleUsuario.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
		bundleUsuario.putString("Loggin", usuarioFromABC.getLoggin());
		bundleUsuario.putString("Password", usuarioFromABC.getPassword());
		bundleUsuario.putInt("PerfilID", usuarioFromABC.getPerfilID());
		bundleUsuario.putInt("DatosID", usuarioFromABC.getDatosID());
		bundleUsuario.putString("Email", usuarioFromABC.getEmail());

		bundleUsuario.putInt("SectorID", sector.getSectorID());
		bundleUsuario.putString("Codigo", sector.getCodigo());
		bundleUsuario.putString("Nombre", sector.getNombre());
		i.putExtras(bundleUsuario);

		startActivity(i);
	}
	
	public void sigBorrar(Sector sector) {
		try{
			
			Borrado borrar = new Borrado();
			borrar.setSector(sector.getSectorID());
			borrar.execute();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}

	// endregion

	// region RECUPERANDO USUARIO ( IMPORTANTE PARA ENVIARLO A LA OTRA
	// ACTIVIDAD)

	public void populateExtras() {
		bundleReceptor = getIntent().getExtras();
		if (!bundleReceptor.isEmpty()) {

			usuarioFromABC = new Usuario();
			sector = new Sector();

			sector.setSectorID(bundleReceptor.getInt("SectorID"));

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

	// region CLASE PARA LISTAR ASYNCRONAMENTE
	class listarSectores extends
		AsyncTask<ListView, Void, SimpleAdapter> {
		Context contexto;
		ListView list;
		InputStream is;
		Sector[] sectorNormal;
		ArrayList<Map<String, String>> listaSectores2 = new ArrayList<Map<String, String>> ();
		ArrayList<Map<String, String>>  listaSectores = new ArrayList<Map<String, String>> ();

		public void setSectorList(ArrayList<Map<String, String>> listaSectores) {
			this.listaSectores2 = listaSectores;
		}

		public void cargarContenido(Context contexto) {
			this.contexto = contexto;
		}

		@Override
		protected SimpleAdapter doInBackground(ListView... params) {
			// TODO Auto-generated method stub
			list = params[0];
			String resultado = "fallo";
			//SectorList sector;

			// Creamos la conexion HTTP
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticionGet = new HttpGet(
					"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=3");

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
					datos.put("SectorID", jsonIterator.get("SectorID").toString());
					datos.put("Codigo", jsonIterator.get("Codigo").toString());
					datos.put("Nombre", jsonIterator.get("Nombre").toString());
					datos.put("Actualizacion", jsonIterator.get("Actualizacion").toString());
					listaSectores.add(datos);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			SimpleAdapter adaptador = new SimpleAdapter(contexto, listaSectores, 
					android.R.layout.simple_list_item_2,
					new String[] { "title", "subtitle" }, new int[] {android.R.id.text1, android.R.id.text2 });

			return adaptador;
		}

		@Override
		protected void onPostExecute(SimpleAdapter result) {
			// TODO Auto-generated method stub
			listaSectores2 = listaSectores;
			list.setAdapter(result);
			registerForContextMenu(list);
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
		Sector thisSector = new Sector();
		String Respuesta = null,Nombre=null,Codigo=null;;
		int ID = 0;
		
	
		public void setSector(int ID){
			this.ID = ID;
			thisSector.SectorID = ID;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(Sectores_List.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progreso.setTitle("Eliminando Sector");
           progreso.setMessage("Borrando sector, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisSector.delete(thisSector.SectorID);
			
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

				listarSectores Sectores = new listarSectores();
				Sectores.cargarContenido(getApplicationContext());
				Sectores.setSectorList(listadoSectores);
				Sectores.execute(listaSectores);

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
			
			Map<String, String>  sectorLister = new HashMap<String, String>(2);
			sector = new Sector();
			sectorLister  =  (Map<String, String>) parent.getAdapter().getItem(position);
			
			sector.setSectorID(Integer.parseInt(sectorLister.get("SectorID")));
			sector.setCodigo(sectorLister.get("Codigo").toString());
			sector.setNombre(sectorLister.get("Nombre").toString());
			
			view.showContextMenu();
		}
		
	}
}
