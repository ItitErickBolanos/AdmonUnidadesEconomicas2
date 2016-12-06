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

import com.objects.beans.Sector;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;
import com.objects.beans.listerBeans.SectorList;

public class SubSectores_Cambios extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
	EditText  Codigo,Nombre;
	TextView Titulo;
	Button  guardar,cancelar;
	Sector sector = null, objSector;
	SectorList sectorList;
	SubSector subsector;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	Spinner sectores;
	ArrayList<SectorList> listadoSubSectores = new ArrayList<SectorList>();
	int ID, posicionSpinner;
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subsectores__cambios);
		
		populateView();
		populateExtras();
		ID = subsector.getSubSectorID();
		
		try{
			
			listarSectores Sectores = new listarSectores();
			Sectores.cargarContenido(getApplicationContext());
			Sectores.setSectorList(listadoSubSectores);
			Sectores.execute(sectores);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
	
		Codigo.setText(subsector.getCodigo());
		Nombre.setText(subsector.getNombre());
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
			
			case R.id.subsectoresCambiosGuardar:
					int SubSectorID = subsector.getSubSectorID();
					subsector = new SubSector();
					subsector.setSubSectorID(SubSectorID);
					subsector.setCodigo(Codigo.getText().toString());
					subsector.setNombre(Nombre.getText().toString());
					subsector.SectorID = (sector != null) ? sector.SectorID : objSector.SectorID;
					if(Pattern.matches("[0-9]{3}", Codigo.getText().toString()) && !Nombre.getText().toString().equals("") && subsector.SectorID != 0){
						try{
							doLogginBackground doLoggin = new doLogginBackground();
							doLoggin.setSubSector(subsector);
							doLoggin.execute();
						}catch(Exception e){
							e.printStackTrace();	
						}
					} else {
						new AlertDialog.Builder(SubSectores_Cambios.this)
			              .setTitle("Informacion")
			              .setMessage("El codigo del subsector debe tener una longitud de 3 digitos y ser un numero.\nEl nombre del subsector no puede estar vacio.\nEl subsector debe estar vinculado a un sector.")
			              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			                  public void onClick(DialogInterface dialog, int which) { 
			                  }
			               })
			               .show();
					}
				break;
				
			case R.id.subsectoresCambiosCancelar:
				i = new Intent(this,SubSectores_List.class);
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
			Titulo = (TextView)findViewById(R.id.subsectoresCambiosTitulo);
			Codigo = (EditText)findViewById(R.id.subsectoresCambiosCodigo);
			Nombre = (EditText)findViewById(R.id.subsectoresCambiosNombre);
			guardar = (Button)findViewById(R.id.subsectoresCambiosGuardar);
			cancelar = (Button)findViewById(R.id.subsectoresCambiosCancelar);
			sectores = (Spinner)findViewById(R.id.subsectoresCambiosListado);
			Titulo.setText("EDICION DE SUBSECTORES");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fallo la insercion", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "Subsector editado exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this, SubSectores_List.class);
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
	  			subsector = new SubSector();
	 			subsector.setSubSectorID(b.getInt("SubSectorID"));
		 		subsector.setSectorID(b.getInt("SectorID"));
		 		subsector.setNombre(b.getString("Nombre"));
		 		subsector.setCodigo(b.getString("Codigo"));
	  			
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
			SubSector thisSector;
			String Respuesta = null;
		
			public void setSubSector(SubSector Subsector){
				thisSector= Subsector;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(SubSectores_Cambios.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Editando SubSector");
	           progreso.setMessage("Verificando Codigo y Nombre, Espere...");
	           progreso.setCancelable(false);
	           progreso.setMax(100);
	           progreso.setProgress(0);
	           progreso.show();

			}

			@Override
			protected Void doInBackground(Void... array) {
				thisSector.applyEdit();
				
				Respuesta = thisSector.Respuesta;
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
					list.setSelection(posicionSpinner);
					objSector.SectorID = b.getInt("SectorID");
					list.setOnItemSelectedListener(new OnItemSelectedListener() {

					    @Override
					    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					    	if (!parentView.getAdapter().isEmpty()){
					    		
								sectorList = new SectorList();
								sectorList =(SectorList) parentView.getAdapter().getItem(position);
								sector = new Sector();
								sector.setSectorID(sectorList.getSectorID());
							}
					    }

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub
							
						}
					});
					//list.setSelection(((ArrayAdapter)sectores.getAdapter()).getPosition(subsector.getSectorID()));
				}

				@Override
				protected void onPreExecute() {

					
				}
				
			}
		//endregion

	
}
