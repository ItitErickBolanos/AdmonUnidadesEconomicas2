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

public class Sectores_Cambios extends ActionBarActivity implements OnClickListener{


	//region PROPIEDADES DE LA CLASE
		EditText  Codigo,Nombre;
		TextView Titulo;
		Button  guardar,cancelar;
		Sector sector,respaldo;
		Usuario usuario;
		ProgressDialog pd = null;
		Intent i;
		Bundle bundleReceptor;
		int ID;
	//endregion
	
	
	//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sectores__cambios);
		
		
		populateView();
		populateExtras();
		ID = sector.getSectorID();
		respaldo = sector;
		
		try{
			
			cargandoUsuario loadingUser = new cargandoUsuario();
			loadingUser.setSectorID(ID);
			loadingUser.execute();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		guardar.setOnClickListener(this);
		cancelar.setOnClickListener(this);
		
		Codigo.setText(sector.getCodigo());
		Nombre.setText(sector.getNombre());
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
				
				case R.id.sectoresCambiosGuardar:
				if(Pattern.matches("[0-9]{2}", Codigo.getText().toString()) && !Nombre.getText().toString().equals("")){
					sector.setCodigo(Codigo.getText().toString());
					sector.setNombre(Nombre.getText().toString());
					Toast.makeText(getApplicationContext(),"ID:"+ID +"Nombre: "+Nombre.getText().toString()+" CODIGO:"+Codigo.getText().toString(), Toast.LENGTH_SHORT).show();
					
					try{
							
							doLogginBackground doLoggin = new doLogginBackground();
							doLoggin.setSector(sector.getSectorID(),sector.getNombre(),sector.getCodigo());
							doLoggin.execute();
							
						}catch(Exception e){
							
							e.printStackTrace();
							
						}
					} else {
						new AlertDialog.Builder(Sectores_Cambios.this)
			              .setTitle("Informacion")
			              .setMessage("El codigo del sector debe tener una longitud de 2 digitos y ser un numero.\nEl nombre del sector no puede estar vacio.")
			              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			                  public void onClick(DialogInterface dialog, int which) { 
			                  }
			               })
			               .show();
					}
					break;
				
				case R.id.sectoresCambiosCancelar:
					i = new Intent(this,Sectores_List.class);
					bundleReceptor = new Bundle();
					bundleReceptor.putInt("UsuarioID", usuario.getUsuarioID());
					bundleReceptor.putString("Loggin", usuario.getLoggin());
					bundleReceptor.putString("Password", usuario.getPassword());
					bundleReceptor.putInt("PerfilID", usuario.getPerfilID());
					bundleReceptor.putInt("DatosID",usuario.getDatosID());
					bundleReceptor.putString("Email",usuario.getEmail());
						
			  		i.putExtras(bundleReceptor);
					startActivity(i);
					break;
				
				}
				
			}
		    
		    public void populateView(){
				Titulo = (TextView)findViewById(R.id.sectoresCambiosTitulo);
				Codigo = (EditText)findViewById(R.id.sectoresCambiosCodigo);
				Nombre = (EditText)findViewById(R.id.sectoresCambiosNombre);
				guardar = (Button)findViewById(R.id.sectoresCambiosGuardar);
				cancelar = (Button)findViewById(R.id.sectoresCambiosCancelar);
				Titulo.setText("EDITAR SECTOR");
			}
		  //metodo que avisa si hay un error
		  	public void avisoError(){
		  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
		  	}
		  	public void errorDeOperacion(){
		  		Toast.makeText(getApplicationContext(), "Fallo la edicion", Toast.LENGTH_SHORT).show();
		  	}
		  	public void altaOK(){
		  		Toast.makeText(getApplicationContext(), "Sector editado exitosamente",Toast.LENGTH_SHORT).show();
		  	}
		  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
		  	public void sig(){
		  		i = new Intent(this,Sectores_List.class);
		  		altaOK();
		  		bundleReceptor = new Bundle();
		  			bundleReceptor.putInt("UsuarioID", usuario.getUsuarioID());
		  			bundleReceptor.putString("Loggin", usuario.getLoggin());
		  			bundleReceptor.putString("Password", usuario.getPassword());
		  			bundleReceptor.putInt("PerfilID", usuario.getPerfilID());
		  			bundleReceptor.putInt("DatosID",usuario.getDatosID());
		  			bundleReceptor.putString("Email",usuario.getEmail());
		  			
		  		i.putExtras(bundleReceptor);
		  		startActivity(i);
		  	}
		    
		//endregion
		 
	//region RECUPERANDO USUARIO ( IMPORTANTE PARA ENVIARLO A LA OTRA ACTIVIDAD)
			
	public void populateExtras(){
	 	bundleReceptor = getIntent().getExtras();
	 	if(!bundleReceptor.isEmpty()){
	 		sector = new Sector();
	 			
	 		sector.setSectorID(bundleReceptor.getInt("SectorID"));
	 		sector.setNombre(bundleReceptor.getString("Nombre"));
	 		sector.setCodigo(bundleReceptor.getString("Codigo"));
	 		
	 		usuario = new Usuario();
		
	  			usuario.setUsuarioID(bundleReceptor.getInt("UsuarioID"));
	  			usuario.setLoggin(bundleReceptor.getString("Loggin"));
	  			usuario.setPassword(bundleReceptor.getString("Password"));
	  			usuario.setPerfilID(bundleReceptor.getInt("PerfilID"));
	  			usuario.setDatosID(bundleReceptor.getInt("DatosID"));
	  			usuario.setEmail(bundleReceptor.getString("Email"));
	  		}
	  	}
	//endregion
	  	  	
	//region CLASE QUE IMPLEMENTA ASYNCTASK
		  	class doLogginBackground extends AsyncTask<Void, Void, Void> {

				private ProgressDialog progreso;
				Sector thisSector = new Sector();
				String Respuesta = null,Nombre=null,Codigo=null;;
				int ID = 0;
				
			
				public void setSector(int ID,String Nombre, String Codigo){
					this.Nombre = Nombre;
					this.Codigo = Codigo;
					this.ID = ID;
					thisSector.Codigo = Codigo;
					thisSector.Nombre = Nombre;
					thisSector.SectorID = ID;
				}
			
				@Override
				protected void onPreExecute() {

		           progreso = new ProgressDialog(Sectores_Cambios.this);
		           progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		           progreso.setTitle("Editando Sector");
		           progreso.setMessage("Verificando Codigo y Nombre, Espere...");
		           progreso.setCancelable(false);
		           progreso.setMax(100);
		           progreso.setProgress(0);
		           progreso.show();

				}

				@Override
				protected Void doInBackground(Void... array) {
					thisSector.applyEdit();
					
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
					sig();
						
				}

		  	}
		  	
		  	class cargandoUsuario extends AsyncTask<Void, Void, Void> {

				private ProgressDialog progreso;
				Sector thisSector = new Sector();
				String Respuesta = null;
				int ID;
			
				public void setSectorID(int ID){
					this.ID = ID;
				}
			
				@Override
				protected void onPreExecute() {

		           progreso = new ProgressDialog(Sectores_Cambios.this);
		           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		           progreso.setTitle("Editando Sector");
		           progreso.setMessage("Verificando Codigo y Nombre, Espere...");
		           progreso.setCancelable(false);
		           progreso.setMax(100);
		           progreso.setProgress(0);
		           progreso.show();

				}

				@Override
				protected Void doInBackground(Void... array) {
					thisSector.Load(ID);
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

					if(thisSector.SectorID>0){
						progreso.dismiss();
						
					}else{
						progreso.dismiss();
						errorDeOperacion();
					}
				}

		  	}
		  	
		  	
	//endregion

}
