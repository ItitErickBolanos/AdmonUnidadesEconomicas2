package com.example.admonunidadeseconomicas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.objects.beans.Usuario;

import java.net.InetAddress;


public class MainActivity extends Activity implements OnClickListener{

//region PROPIEDADES DE LA CLASE
	EditText  tbUsuario,tbContrasena;
	TextView tvTitulo;
	Button  btnIngresar;
	Usuario usuario;
	ProgressDialog pd = null;
	Intent i;
	Bundle b;
//endregion
	
//region METODOS NATIVOS DE LA CLASE
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        populateView();
		btnIngresar.setOnClickListener(this);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_aplicacion, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.menuAppSalir:
				System.exit(0);
				break;
			case R.id.menuAppCerrarSesion:
				System.exit(0);
				break;
			case R.id.menuAppSistema:
				Toast.makeText(this, "Creado por; Legendary Software 'Innovation and Solutions' www.legendarysoftware.com.mx", Toast.LENGTH_LONG).show();
				break;
			case R.id.menuAppEstadoServidor:
				
				try{
					if (InetAddress.getByName("http://semestral.esy.es").isReachable(30)) {
						Toast.makeText(this, "Servidor en linea", Toast.LENGTH_LONG).show(); 
					}else { 
						Toast.makeText(this, "Servidor fuera de servicio", Toast.LENGTH_LONG).show();
					}
				}catch(Exception e){
					Log.i("no llego","Excepcion de error"); 
				}
			break;
		}

		return super.onOptionsItemSelected(item);
	}
//endregion

//region METODOS HECHOS PARA LA CLASE
    @Override
    public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btnIngresar:
			usuario = new Usuario();
				usuario.setLoggin(tbUsuario.getText().toString(),tbContrasena.getText().toString());
		
				try{
					
					doLogginBackground doLoggin = new doLogginBackground();
					doLoggin.setUser(usuario);
					doLoggin.execute();
					
				}catch(Exception e){
					
					e.printStackTrace();
					
				}
			break;
		}
		
	}
    
    public void populateView(){
		tvTitulo = (TextView)findViewById(R.id.tvTitulo);
		tbUsuario = (EditText)findViewById(R.id.tbUsuario);
		tbContrasena = (EditText)findViewById(R.id.tbContrasena);
		btnIngresar = (Button)findViewById(R.id.btnIngresar);
	}
  //metodo que avisa si hay un error
  	public void avisoError(){
  		Toast.makeText(getApplicationContext(), "Error de Autentificacion",Toast.LENGTH_LONG).show();
  	}

  	
  	// Metodo que pasa a la siguiente aplicacion eviando los parametros necesarios para mantener la session del usuario
  	public void sig(){
  		i = new Intent(this,MenuPrincpal.class);
  		b = new Bundle();
  			b.putInt("UsuarioID",usuario.getUsuarioID());
  			b.putString("Loggin",usuario.getLoggin());
  			b.putString("Password", usuario.getPassword());
  			b.putInt("PerfilID", usuario.getPerfilID());
  			b.putInt("DatosID",usuario.getDatosID());
  			b.putString("Email",usuario.getEmail());
  		i.putExtras(b);
  		startActivity(i);
  	}
    
//endregion
    
//region CLASE QUE IMPLEMENTA ASYNCTASK
  	class doLogginBackground extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progreso;
		Usuario thisUser;
	
		public void setUser(Usuario usuario){
			thisUser = usuario;
		}
	
		@Override
		protected void onPreExecute() {

           progreso = new ProgressDialog(MainActivity.this);
           progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
           progreso.setMessage("Autentificando, Espere...");
           progreso.setCancelable(false);
           progreso.setMax(100);
           progreso.setProgress(0);
           progreso.show();

		}

		@Override
		protected Void doInBackground(Void... array) {
			thisUser.Loggin();
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

			if(thisUser.UsuarioID != 0){
				progreso.dismiss();
				sig();
			}else{
				progreso.dismiss();
				avisoError();
			}
		}

  	}
//endregion

}
