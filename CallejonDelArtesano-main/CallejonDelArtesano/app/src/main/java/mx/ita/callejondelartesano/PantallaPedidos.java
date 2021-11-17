package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PantallaPedidos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbarNav;
    RequestQueue requestQueue;
    TextView textDetalles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pedidos);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbarNav = findViewById(R.id.toolbar);

        setSupportActionBar(toolbarNav);
        navigationView.setNavigationItemSelectedListener(this);
//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarNav,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");

        MostrarPedidos("http://callejonwebservices.herokuapp.com/seleccionarPedidos.php?id="+incomingValue[0]);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");
        switch (item.getItemId()){
            case R.id.nav_carrito:
                Intent intent = new Intent(this, PantallaCarrito.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
            case R.id.nav_home:
                intent = new Intent(this, PantallaPrincipal.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
            case R.id.nab_nosotrs:
                intent = new Intent(this, PantallaSobreNosotros.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
            case R.id.nav_pedidos:
                intent = new Intent(this, PantallaPedidos.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
            case R.id.nav_promociones:
                intent = new Intent(this, PantallaArticulosDestacados.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
            case R.id.nav_formas_pago:
                intent = new Intent(this, PantallaTiposDePagos.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void MostrarPedidos(String URL){
        textDetalles = (TextView)findViewById(R.id.textviewPedidios);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
//                Toast toast = Toast.makeText(getApplicationContext(), response.length(), Toast.LENGTH_SHORT);
//                toast.show();
                String cadena = "";
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        cadena += "Nombre del producto:  "+ jsonObject.getString("Nombre Producto")+"\n";
                        cadena += "El id es: "+ jsonObject.getString("idPedido")+"\n";
                        cadena += "Quien lo encargó:  "+ jsonObject.getString("Nombre usuario")+"\n";
                        cadena += "Método de pago:  "+ jsonObject.getString("Metodo Pago")+"\n";

                        if(i < response.length()-1){
                            cadena += "------------------------------------------------------------- \n";
                        }
                        textDetalles.setText(cadena);

                    } catch (JSONException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error con la conexión", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}