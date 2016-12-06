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

import com.example.admonunidadeseconomicas.Proyectos_List.listarProyectos;
import com.objects.beans.Empresa;
import com.objects.beans.Proyecto;
import com.objects.beans.Usuario;

public class Empresas_List extends ActionBarActivity implements
		OnItemClickListener {

	// region PROPIEDADES DE CLASE
	Bundle bundleReceptor, bundleUsuario, bundleSector;
	Intent i;
	Usuario usuarioFromABC;
	Empresa empresa;
	TextView headerText, headerTextE;
	ListView listaEmpresas;
	ArrayList<Map<String, String>> listadoEmpresas = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> listaElementos;
	String Nombre, Anio, UsuarioID, Actualizacion, mensaje;

	// endregion

	// region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empresas__list);

		populateExtras();
		listaEmpresas = (ListView) findViewById(R.id.menuEmpresasList);
		headerText = (TextView) findViewById(R.id.headerEmpresasList);
		headerTextE = (TextView) findViewById(R.id.headerEmpresasListE);

		try {

			listarEmpresas Empresas = new listarEmpresas();
			Empresas.cargarContenido(getApplicationContext());
			Empresas.setEmpresaList(listadoEmpresas);
			Empresas.execute(listaEmpresas);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listaEmpresas.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.empresas__map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.altaSectores:
			sigAltas();
			break;
			
		case R.id.empresasMapas:
			sigMapas();
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
			sigCambios(empresa);
			return true;

		case R.id.eliminar:
			//CODIGO PARA ELIMINACION
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Esta seguro de que desea eliminar el registro?")
		       .setCancelable(false)
		       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                sigBorrar(empresa);
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
		i = new Intent(this, Empresas_Altas.class);
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
	public void sigCambios(Empresa empresa) {
		i = new Intent(this, Empresas_Cambios.class);
		bundleUsuario = new Bundle();
		bundleUsuario.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
		bundleUsuario.putString("Loggin", usuarioFromABC.getLoggin());
		bundleUsuario.putString("Password", usuarioFromABC.getPassword());
		bundleUsuario.putInt("PerfilID", usuarioFromABC.getPerfilID());
		bundleUsuario.putInt("DatosID", usuarioFromABC.getDatosID());
		bundleUsuario.putString("Email", usuarioFromABC.getEmail());

		//AGREGAR LAS DEMAS PROPIEDADES DE LA EMPRESA
		bundleUsuario.putInt("EmpresaID", empresa.getEmpresaID());
		bundleUsuario.putString("Nombre", empresa.getNombre());
		bundleUsuario.putInt("MunicipioID", empresa.getMunicipioID());
		bundleUsuario.putString("RazonSocial", empresa.getRazonSocial());
		bundleUsuario.putInt("TipoVialidad", empresa.getTipoVialidad());
		bundleUsuario.putString("CodigoPostal", empresa.getCodigoPostal());
		bundleUsuario.putString("NombreVialidad", empresa.getNombreVialidad());
		bundleUsuario.putInt("TipoAsentamiento", empresa.getTipoAsentamiento());
		bundleUsuario.putString("NumeroExterior", empresa.getNumeroExterior());
		bundleUsuario.putString("NumeroLetraInterior", empresa.getNumeroLetraInterior());
		bundleUsuario.putInt("TipoUnidadEconomica", empresa.getTipoUnidadEconomica());
		bundleUsuario.putString("Longitud", empresa.getLongitud());
		bundleUsuario.putString("Latitud", empresa.getLatitud());
		bundleUsuario.putString("NombreAsentamientoHumano", empresa.getNombreAsentamientoHumano());
		bundleUsuario.putString("Telefono", empresa.getTelefono());
		bundleUsuario.putString("Email", empresa.getEmail());
		bundleUsuario.putString("Web", empresa.getWeb());
		bundleUsuario.putString("FechaIncorporacion", empresa.getFechaIncorporacion());
		bundleUsuario.putInt("RangoEmpleados", empresa.getRangoEmpleados());
		bundleUsuario.putInt("ScianID", empresa.getScianID());
		i.putExtras(bundleUsuario);

		startActivity(i);
	}
	
	public void sigBorrar(Empresa empresa) {
		try{
			
			Borrado borrar = new Borrado();
			borrar.setEmpresa(empresa.getEmpresaID());
			borrar.execute();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}
	
	public void sigMapas(){
		i = new Intent(this, Empresas_Mapa.class);
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
	class listarEmpresas extends
		AsyncTask<ListView, Void, SimpleAdapter> {
		Context contexto;
		ListView list;
		InputStream is;
		ArrayList<Map<String, String>> listaEmpresas2 = new ArrayList<Map<String, String>> ();
		ArrayList<Map<String, String>>  listaEmpresas = new ArrayList<Map<String, String>> ();

		public void setEmpresaList(ArrayList<Map<String, String>> listaEmpresas) {
			this.listaEmpresas2 = listaEmpresas;
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
					"http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=16");

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
						datos.put("subtitle", "Municipio: " + jsonIterator.get("Municipio").toString());
						datos.put("title", subTitle);
						datos.put("EmpresaID", jsonIterator.get("EmpresaID").toString());
						datos.put("MunicipioID", jsonIterator.get("MunicipioID").toString());
						datos.put("ScianID", jsonIterator.get("ScianID").toString());
						datos.put("Nombre", jsonIterator.get("Nombre").toString());
						datos.put("RazonSocial", jsonIterator.get("RazonSocial").toString());
						datos.put("CodigoPostal", jsonIterator.get("CodigoPostal").toString());
						datos.put("NombreVialidad", jsonIterator.get("NombreVialidad").toString());
						datos.put("NumeroExterior", jsonIterator.get("NumeroExterior").toString());
						datos.put("NumeroLetraInterior", jsonIterator.get("NumeroLetraInterior").toString());
						datos.put("Longitud", jsonIterator.get("Longitud").toString());
						datos.put("Latitud", jsonIterator.get("Latitud").toString());
						datos.put("NombreAsentamientoHumano", jsonIterator.get("NombreAsentamientoHumano").toString());
						datos.put("FechaIncorporacion", jsonIterator.get("FechaIncorporacion").toString());
						datos.put("RangoEmpleados", jsonIterator.get("RangoEmpleados").toString());
						datos.put("Telefono", jsonIterator.get("Telefono").toString());
						datos.put("Email", jsonIterator.get("Email").toString());
						datos.put("Web", jsonIterator.get("Web").toString());
						datos.put("TipoVialidad", jsonIterator.get("TipoVialidad").toString());
						datos.put("TipoAsentamiento", jsonIterator.get("TipoAsentamiento").toString());
						datos.put("TipoUnidadEconomica", jsonIterator.get("TipoUnidadEconomica").toString());

						listaEmpresas.add(datos);
					}
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mensaje = "";
			} else
				mensaje = "No hay ninguna empresa registrada";

			SimpleAdapter adaptador = new SimpleAdapter(contexto, listaEmpresas, 
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
				listaEmpresas2 = listaEmpresas;
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
		Empresa thisEmpresa = new Empresa();
		String Respuesta = null,Nombre=null,Codigo=null;;
		int ID = 0;
		
		public void setEmpresa(int ID){
			this.ID = ID;
			thisEmpresa.EmpresaID = ID;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(Empresas_List.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progreso.setTitle("Eliminando Empresa");
           progreso.setMessage("Borrando empresa, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisEmpresa.delete(thisEmpresa.EmpresaID);
			
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

				listarEmpresas Empresas = new listarEmpresas();
				Empresas.cargarContenido(getApplicationContext());
				Empresas.setEmpresaList(listadoEmpresas);
				Empresas.execute(listaEmpresas);

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
			
			Map<String, String>  empresaLister = new HashMap<String, String>(2);
			empresa = new Empresa();
			empresaLister  =  (Map<String, String>) parent.getAdapter().getItem(position);
			
			//Agregar las demas propiedades de la empresa
			empresa.setEmpresaID(Integer.parseInt(empresaLister.get("EmpresaID")));
			empresa.setNombre(empresaLister.get("Nombre").toString());
			empresa.setRazonSocial(empresaLister.get("RazonSocial").toString());
			empresa.setCodigoPostal(empresaLister.get("CodigoPostal").toString());
			empresa.setNombreVialidad(empresaLister.get("NombreVialidad").toString());
			empresa.setNumeroExterior(empresaLister.get("NumeroExterior").toString());
			empresa.setNumeroLetraInterior(empresaLister.get("NumeroLetraInterior").toString());
			empresa.setLongitud(empresaLister.get("Longitud").toString());
			empresa.setNombreAsentamientoHumano(empresaLister.get("NombreAsentamientoHumano").toString());
			empresa.setLatitud(empresaLister.get("Latitud").toString());
			empresa.setTelefono(empresaLister.get("Telefono").toString());
			empresa.setEmail(empresaLister.get("Email").toString());
			empresa.setWeb(empresaLister.get("Web").toString());
			empresa.setFechaIncorporacion(empresaLister.get("FechaIncorporacion").toString());
			empresa.setRangoEmpleados(Integer.parseInt(empresaLister.get("RangoEmpleados").toString()));
			empresa.setTipoVialidad(Integer.parseInt(empresaLister.get("TipoVialidad").toString()));
			empresa.setTipoAsentamiento(Integer.parseInt(empresaLister.get("TipoAsentamiento").toString()));
			empresa.setTipoUnidadEconomica(Integer.parseInt(empresaLister.get("TipoUnidadEconomica").toString()));
			empresa.setScianID(Integer.parseInt(empresaLister.get("ScianID").toString()));
			empresa.setMunicipioID(Integer.parseInt(empresaLister.get("MunicipioID").toString()));
			
			view.showContextMenu();
		}
		
	}
}
