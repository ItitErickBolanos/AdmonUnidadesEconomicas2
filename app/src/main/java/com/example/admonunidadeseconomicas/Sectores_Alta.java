package com.example.admonunidadeseconomicas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.objects.beans.Sector;
import com.objects.beans.Usuario;

import java.util.regex.Pattern;

public class Sectores_Alta extends ActionBarActivity implements OnClickListener {

	//region PROPIEDADES DE LA CLASE
	EditText  Codigo,Nombre;
	TextView Titulo;
	Button  guardar,cancelar;
	Sector sector;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
	//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sectores__alta);
		
		populateView();
		populateExtras();
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_aplicacion, menu);
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
			
			case R.id.sectoresAltaGuardar:
				if(Pattern.matches("[0-9]{2}", Codigo.getText().toString()) && !Nombre.getText().toString().equals("")){
					sector = new Sector();
					sector.setCodigo(Codigo.getText().toString());
					sector.setNombre(Nombre.getText().toString());
					try{
						doLogginBackground doLoggin = new doLogginBackground();
						doLoggin.setSector(sector);
						doLoggin.execute();
					}catch(Exception e){	
						e.printStackTrace();
					}
				} else {
					new AlertDialog.Builder(Sectores_Alta.this)
		              .setTitle("Informacion")
		              .setMessage("El codigo del sector debe tener una longitud de 2 digitos y ser un numero.\nEl nombre del sector no puede estar vacio.")
		              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int which) { 
		                  }
		               })
		               .show();
				}
					
				break;
			case R.id.sectoresAltaCancelar:
				i = new Intent(this,Sectores_List.class);
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
			Titulo = (TextView)findViewById(R.id.sectoresAltaTitulo);
			Codigo = (EditText)findViewById(R.id.sectoresAltaCodigo);
			Nombre = (EditText)findViewById(R.id.sectoresAltaNombre);
			guardar = (Button)findViewById(R.id.sectoresAltaGuardar);
			cancelar = (Button)findViewById(R.id.sectoresAltaCancelar);
			Titulo.setText("ALTA DE SECTORES");
		}
	  //metodo que avisa si hay un error
	  	public void avisoError(){
	  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
	  	}
	  	public void errorDeOperacion(){
	  		Toast.makeText(getApplicationContext(), "Fallo la insercion", Toast.LENGTH_SHORT).show();
	  	}
	  	public void altaOK(){
	  		Toast.makeText(getApplicationContext(), "Sector registrado exitosamente",Toast.LENGTH_SHORT).show();
	  	}
	  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
	  	public void sig(){
	  		i = new Intent(this,Sectores_List.class);
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
			Sector thisSector;
			String Respuesta = null;
		
			public void setSector(Sector sector){
				thisSector= sector;
			}
		
			@Override
			protected void onPreExecute() {

	           progreso = new ProgressDialog(Sectores_Alta.this);
	           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	           progreso.setTitle("Agregando Sector");
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

}
