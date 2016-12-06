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

import com.objects.beans.Proyecto;
import com.objects.beans.Usuario;

public class Proyectos_List extends ActionBarActivity implements
		OnItemClickListener {

	// region PROPIEDADES DE CLASE
	Bundle bundleReceptor, bundleUsuario, bundleSector;
	Intent i;
	Usuario usuarioFromABC;
	Proyecto proyecto;
	TextView headerText, headerTextE;
	ListView listaProyectos;
	ArrayList<Map<String, String>> listadoProyectos = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listaElementos;
	String Nombre, Anio, UsuarioID, Actualizacion, mensaje;

	// endregion

	// region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proyectos__list);

		populateExtras();
		listaProyectos = (ListView) findViewById(R.id.menuProyectosList);
		headerText = (TextView) findViewById(R.id.headerProyectosList);
		headerTextE = (TextView) findViewById(R.id.headerProyectosListE);

		try {

			listarProyectos Proyectos = new listarProyectos();
			Proyectos.cargarContenido(getApplicationContext());
			Proyectos.setProyectoList(listadoProyectos);
			Proyectos.execute(listaProyectos);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listaProyectos.setOnItemClickListener(this);

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
			//C�DIGO PARA EDITAR
			sigCambios(proyecto);
			return true;

		case R.id.eliminar:
			//C�DIGO PARA ELIMINACI�N
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("�Est� seguro de que desea eliminar el registro?")
		       .setCancelable(false)
		       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                sigBorrar(proyecto);
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
		i = new Intent(this, Proyectos_Altas.class);
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
	public void sigCambios(Proyecto proyecto) {
		i = new Intent(this, Proyectos_Cambios.class);
		bundleUsuario = new Bundle();
		bundleUsuario.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
		bundleUsuario.putString("Loggin", usuarioFromABC.getLoggin());
		bundleUsuario.putString("Password", usuarioFromABC.getPassword());
		bundleUsuario.putInt("PerfilID", usuarioFromABC.getPerfilID());
		bundleUsuario.putInt("DatosID", usuarioFromABC.getDatosID());
		bundleUsuario.putString("Email", usuarioFromABC.getEmail());

		bundleUsuario.putInt("ProyectoID", proyecto.getProyectoID());
		bundleUsuario.putString("Nombre", proyecto.getNombre());
		bundleUsuario.putString("Anio", proyecto.getAnio());
		i.putExtras(bundleUsuario);

		startActivity(i);
	}
	
	public void sigBorrar(Proyecto proyecto) {
		try{
			
			Borrado borrar = new Borrado();
			borrar.setProyecto(proyecto.getProyectoID());
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
	class listarProyectos extends
		AsyncTask<ListView, Void, SimpleAdapter> {
		Context contexto;
		ListView list;
		InputStream is;
		Proyecto[] proyectoNormal;
		ArrayList<Map<String, String>> listaProyectos2 = new ArrayList<Map<String, String>> ();
		ArrayList<Map<String, String>>  listaProyectos = new ArrayList<Map<String, String>> ();

		public void setProyectoList(ArrayList<Map<String, String>> listaProyectos) {
			this.listaProyectos2 = listaProyectos;
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
					"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=15");

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
				try {
	
					JSONArray arrayJSON = new JSONArray(resultado);
					String subTitle;
					
					for (int i = 0; i < arrayJSON.length(); i++) {
						
						System.out.println(i);
						Map<String, String> datos = new HashMap<String, String>(2);
						JSONObject jsonIterator = new JSONObject(arrayJSON.get(i).toString());
						subTitle = "\nNombre: "+ jsonIterator.get("Nombre").toString();
						datos.put("subtitle", "A�o: " + jsonIterator.get("Anio").toString());
						datos.put("title", subTitle);
						datos.put("ProyectoID", jsonIterator.get("ProyectoID").toString());
						datos.put("A�o", jsonIterator.get("Anio").toString());
						datos.put("Nombre", jsonIterator.get("Nombre").toString());
						datos.put("UsuarioID", jsonIterator.get("UsuarioID").toString());
						listaProyectos.add(datos);
					}
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mensaje = "";
			} else
				mensaje = "No hay ningun proyecto registrado";

			SimpleAdapter adaptador = new SimpleAdapter(contexto, listaProyectos, 
					android.R.layout.simple_list_item_2,
					new String[] { "title", "subtitle" }, new int[] {android.R.id.text1, android.R.id.text2 });

			return adaptador;
		}

		@Override
		protected void onPostExecute(SimpleAdapter result) {
			// TODO Auto-generated method stub
			if (!mensaje.equals("")){
				headerTextE.setVisibility(View.VISIBLE);
				headerTextE.setText(mensaje);
				list.setVisibility(View.GONE);
			} else {
				headerTextE.setVisibility(View.GONE);
				listaProyectos2 = listaProyectos;
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
		Proyecto thisProyecto = new Proyecto();
		String Respuesta = null,Nombre=null,Codigo=null;;
		int ID = 0;
		
		public void setProyecto(int ID){
			this.ID = ID;
			thisProyecto.ProyectoID = ID;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(Proyectos_List.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progreso.setTitle("Eliminando Proyecto");
           progreso.setMessage("Borrando proyecto, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisProyecto.delete(thisProyecto.ProyectoID);
			
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

				listarProyectos Proyectos = new listarProyectos();
				Proyectos.cargarContenido(getApplicationContext());
				Proyectos.setProyectoList(listadoProyectos);
				Proyectos.execute(listaProyectos);

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
			
			Map<String, String>  proyectoLister = new HashMap<String, String>(2);
			proyecto = new Proyecto();
			proyectoLister  =  (Map<String, String>) parent.getAdapter().getItem(position);
			
			proyecto.setProyectoID(Integer.parseInt(proyectoLister.get("ProyectoID")));
			proyecto.setAnio(proyectoLister.get("Anio").toString());
			proyecto.setNombre(proyectoLister.get("Nombre").toString());
			proyecto.setUsuarioID(Integer.parseInt(proyectoLister.get("UsuarioID")));
			
			view.showContextMenu();
		}
		
	}
}
