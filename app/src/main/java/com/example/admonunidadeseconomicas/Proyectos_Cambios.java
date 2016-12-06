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

import com.objects.beans.Proyecto;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.UsuarioList;

public class Proyectos_Cambios extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
	EditText  Anio,Nombre;
	TextView Titulo;
	Button  guardar,cancelar;
	Usuario usuarioL = null, objUsuarioL;
	UsuarioList usuarioList;
	Proyecto proyecto;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	Spinner usuarios;
	ArrayList<UsuarioList> listadoUsuarios = new ArrayList<UsuarioList>();
	int ID, posicionSpinner;
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proyectos__cambios);
		
		populateView();
		populateExtras();
		
		try{
			
			listarUsuarios Usuarios = new listarUsuarios();
			Usuarios.cargarContenido(getApplicationContext());
			Usuarios.setUsuarioList(listadoUsuarios);
			Usuarios.execute(usuarios);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
		Anio.setText(proyecto.getAnio());
		Nombre.setText(proyecto.getNombre());
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
			
			case R.id.proyectosCambiosGuardar:
					int ProyectoID = proyecto.getProyectoID();
					proyecto = new Proyecto();
					proyecto.setProyectoID(ProyectoID);
					proyecto.setAnio(Anio.getText().toString());
					proyecto.setNombre(Nombre.getText().toString());
					proyecto.UsuarioID = (usuarioL != null) ? usuarioL.UsuarioID : objUsuarioL.UsuarioID;
					if(Pattern.matches("[0-9]{4}", Anio.getText().toString()) && !Nombre.getText().toString().equals("") && proyecto.UsuarioID != 0){
						try{
							doLogginBackground doLoggin = new doLogginBackground();
							doLoggin.setProyecto(proyecto);
							doLoggin.execute();
						}catch(Exception e){
							
							e.printStackTrace();	
						}
					} else {
						new AlertDialog.Builder(Proyectos_Cambios.this)
			              .setTitle("Informaci�n")
			              .setMessage("El nombre del proyecto no puede estar vac�o.\nEl proyecto debe tener un responsable asignado.")
			              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			                  public void onClick(DialogInterface dialog, int which) { 
			                  }
			               })
			               .show();
					}
				break;
			case R.id.proyectosCambiosCancelar:
				i = new Intent(this, Proyectos_List.class);
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
			Titulo = (TextView)findViewById(R.id.proyectosCambiosTitulo);
			Anio = (EditText)findViewById(R.id.proyectosCambiosAno);
			Nombre = (EditText)findViewById(R.id.proyectosCambiosNombre);
			guardar = (Button)findViewById(R.id.proyectosCambiosGuardar);
			cancelar = (Button)findViewById(R.id.proyectosCambiosCancelar);
			usuarios = (Spinner)findViewById(R.id.proyectosCambiosListadoUsuarios);
			Titulo.setText("EDICI�N DE PROYECTOS");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fall� la inserci�n", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "Proyecto editado exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// M�todo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this, Proyectos_List.class);
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
	  			proyecto = new Proyecto();
	  			proyecto.setProyectoID(b.getInt("ProyectoID"));
	  			proyecto.setUsuarioID(b.getInt("UsuarioID"));
	  			proyecto.setNombre(b.getString("Nombre"));
	  			proyecto.setAnio(b.getString("Anio"));
	  			
		 		posicionSpinner = b.getInt("posicionSpinner");
	  			
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
			Proyecto thisProyecto;
			String Respuesta = null;
		
			public void setProyecto(Proyecto proyecto){
				thisProyecto= proyecto;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(Proyectos_Cambios.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Agregando Proyecto");
	           progreso.setMessage("Verificando Nombre y A�o, Espere...");
	           progreso.setCancelable(false);
	           progreso.setMax(100);
	           progreso.setProgress(0);
	           progreso.show();

			}

			@Override
			protected Void doInBackground(Void... array) {
				thisProyecto.applyEdit();
				
				Respuesta = thisProyecto.Respuesta;
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

//region CLASE PARA LISTAR ASYNCRONAMENTE
	class listarUsuarios extends AsyncTask<Spinner, Void, ArrayAdapter<UsuarioList>>{
		Context contexto;
		Spinner list;
		InputStream is;
		Usuario[] usuarioNormal;
		ArrayList<UsuarioList> listaUsuarios2 = new ArrayList<UsuarioList>();
		ArrayList<UsuarioList> listaUsuarios = new ArrayList<UsuarioList>();
		
		public void setUsuarioList(ArrayList<UsuarioList> listaUsuarios){
			this.listaUsuarios2 = listaUsuarios;
		}
		
		public void cargarContenido(Context contexto){
			this.contexto = contexto;
		}
		
			@Override
			protected ArrayAdapter<UsuarioList> doInBackground(Spinner... params) {
				// TODO Auto-generated method stub
				list = params[0];
				String resultado = "fallo";
				UsuarioList usuario;
				
				//Creamos la conexion HTTP
				HttpClient cliente = new DefaultHttpClient();
				HttpGet peticionGet =  new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=1");
				
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
								objUsuarioL = new Usuario();
								objUsuarioL.setUsuarioID(objetoJSON.getInt("UsuarioID"));
							}
							
							usuario = new UsuarioList(objetoJSON.getInt("UsuarioID"),
													objetoJSON.getString("Loggin"));
							
							listaUsuarios.add(usuario);						
						}
						
					}catch(JSONException e){
						e.printStackTrace();
					}
				} else {
					usuario = new UsuarioList(0,
							"No hay ningun usuarioRegistrado");
	
					listaUsuarios.add(usuario);
				}
				
				ArrayAdapter<UsuarioList> adaptador = new ArrayAdapter<UsuarioList>(contexto,android.R.layout.simple_list_item_1,listaUsuarios);
				
				
				return adaptador;
		}

				@Override
				protected void onPostExecute(ArrayAdapter<UsuarioList> result) {
					// TODO Auto-generated method stub
					listaUsuarios2 = listaUsuarios;
					list.setAdapter(result);
					list.setSelection(posicionSpinner);
					objUsuarioL.UsuarioID = b.getInt("UsuarioID");
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								usuarioList = new UsuarioList();
								usuarioList =(UsuarioList) parentView.getAdapter().getItem(position);
								usuario = new Usuario();
								usuario.setUsuarioID(usuarioList.getUsuarioID());
							}
					    }

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub
							
						}
					});
				}

				@Override
				protected void onPreExecute() {

					
				}
				
			}
		//endregion

	
}
