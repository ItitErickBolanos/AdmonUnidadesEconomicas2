package com.example.admonunidadeseconomicas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.objects.beans.Usuario;

import java.net.InetAddress;

public class MenuPrincpal extends ActionBarActivity implements OnItemClickListener {

//region PROPIEDADES DE CLASE
	Bundle b;
	Usuario usuario;
	Intent i;
	TextView headerText;
	ListView menu;
//endregion
		
//region ELEMENTOS DEL MENU DE ADMINISTRADOR
		String[] menuListaAdmin = {"Introduccion",
								   "Que es ERFCA?",
								   "Publicaciones",
								   "Eventos",
								   "Que es SISCA?",
							       "Proyectos",
							       "Empresas",
							       "Usuarios",
							       "SCIAN 2013",
							       "Convertidor de Unidades",
							       "Mapas"
							       };
//endregion
		
//region ELEMENTOS DEL MENU DE USUARIO GENERAL
		String[] menuListaUser = {"Introduccion",
								  "Que es ERFCA?",
								  "Publicaciones",
								  "Eventos",
								  "Que es SISCA?",
								  "Proyectos",
								  "Empresas",
								  "Convertidor de Unidades",
								  "Mapas"
								 };
//endregion
	
//region METODOS NATIVOS DE LA CLASE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_princpal);
		
		menu = (ListView) findViewById(R.id.menuList);
		headerText = (TextView) findViewById(R.id.headerSession);
		
		populateExtras();
		headerText.setText("Bienvenido: "+usuario.getLoggin());
		
		if(usuario.PerfilID == 1){
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(),
																	android.R.layout.simple_list_item_1,
																	menuListaAdmin);
			menu.setAdapter(listAdapter);
		}else{
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_list_item_1,
					menuListaUser);
			menu.setAdapter(listAdapter);
		}
		
		menu.setOnItemClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_princpal, menu);
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
	
//region METODO PARA RECUPERAR BUNDLE y SU CONTENIDO
	
	//metodo para recuperar el bundle de la actividad anterior.
		public void populateExtras(){
			b = getIntent().getExtras();
			
			usuario = new Usuario();
				usuario.setUsuarioID(b.getInt("UsuarioID"));
				usuario.setLoggin(b.getString("Loggin"));
				usuario.setPassword(b.getString("Password"));
				usuario.setPerfilID(b.getInt("PerfilID"));
				usuario.setDatosID(b.getInt("DatosID"));
				usuario.setEmail(b.getString("Email"));
		}
		
		//endregion
	
//region ENVIO DE DATOS A USUARIOS PROYECTOS y DEMAS
	
		//Metodo para enviar session a UsuariosABC
		/*public void sigUsuariosABC(){
			
			i = new Intent(this,Usuarios_ABC.class);
			b = new Bundle();
				b.putInt("UsuarioID", usuario.getUsuarioID());
				b.putString("Loggin", usuario.getLoggin());
				b.putString("Password", usuario.getPassword());
				b.putInt("PerfilID", usuario.getPerfilID());
				b.putInt("DatosID",usuario.getDatosID());
				b.putString("Email",usuario.getEmail());
				
			i.putExtras(b);
			startActivity(i);
			
		}*/
		
		//Metodo para enviar session a ProyectosABC
		public void sigProyectosABC(){
			i = new Intent(this,Proyectos_List.class);
			b = new Bundle();
				b.clear();
				b.putInt("UsuarioID",usuario.getUsuarioID());
				b.putString("Loggin",usuario.getLoggin());
				b.putString("Password", usuario.getPassword());
				b.putInt("PerfilID", usuario.getPerfilID());
				b.putInt("DatosID", usuario.getDatosID());
				b.putString("Email", usuario.getEmail());
			i.putExtras(b);
			startActivity(i);
		}
		
		//Metodo para enviar session a EmpresasABC
		public void sigEmpresasABC(){
			i = new Intent(this,Empresas_Mapa.class);
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
		
		//Metodo para enviar session a ScianABC
		public void sigScianABC(){
			i = new Intent(this,Scian_ABC.class);
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
		
//region MENU PRINCIPAL y MENU DE LISTVIEW
		
		//maneja los eventos de la ListView
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				if(parent.getCount()==11){
					
					switch(position){
					
					case 0:
						Toast.makeText(this, "Posicion 1", Toast.LENGTH_SHORT).show();
						break;
					
					case 1:
						Toast.makeText(this, "Posicion 2", Toast.LENGTH_SHORT).show();
						break;
					
					case 2:
						Toast.makeText(this, "Posicion 3", Toast.LENGTH_SHORT).show();
						break;
						
					case 3:
						Toast.makeText(this, "Posicion 4", Toast.LENGTH_SHORT).show();
						break;
						
					case 4:
						Toast.makeText(this, "Posicion 5", Toast.LENGTH_SHORT).show();
						break;
						
					case 5:
						sigProyectosABC();
						break;

					case 6:
						sigEmpresasABC();
						break;
						
					case 7:
						//Toast.makeText(this, "Posicion 8", Toast.LENGTH_SHORT).show();
						//sigUsuariosABC();
						break;
					
					case 8:
						//Toast.makeText(this, "Posicion 9", Toast.LENGTH_SHORT).show();
						sigScianABC();
						break;
					
					case 9:
						//Toast.makeText(this, "Posicion 10", Toast.LENGTH_SHORT).show();
						Toast.makeText(this, "Posicion 10", Toast.LENGTH_SHORT).show();
						break;
					case 10:
						//Toast.makeText(this, "Posicion 11", Toast.LENGTH_SHORT).show();
						Toast.makeText(this, "Posicion 11", Toast.LENGTH_SHORT).show();
						break;
					}

				}else{
					switch(position){
					
					case 0:
						Toast.makeText(this, "Posicion 1", Toast.LENGTH_SHORT).show();
							break;
				
					case 1:
						Toast.makeText(this, "Posicion 2", Toast.LENGTH_SHORT).show();
						break;
				
					case 2:
						Toast.makeText(this, "Posicion 3", Toast.LENGTH_SHORT).show();
						break;
					
					case 3:
						Toast.makeText(this, "Posicion 4", Toast.LENGTH_SHORT).show();
						break;
					
					case 4:
						Toast.makeText(this, "Posicion 5", Toast.LENGTH_SHORT).show();
						break;
					
					case 5:
						Toast.makeText(this, "Posicion 6", Toast.LENGTH_SHORT).show();
						break;

					case 6:
						Toast.makeText(this, "Posicion 7", Toast.LENGTH_SHORT).show();
						break;
					
					case 7:
						Toast.makeText(this, "Posicion 8", Toast.LENGTH_SHORT).show();
						break;
				
					case 8:
						Toast.makeText(this, "Posicion 9", Toast.LENGTH_SHORT).show();
						break;
				
					}
				}
				
				
			}
//endregion
	
}
