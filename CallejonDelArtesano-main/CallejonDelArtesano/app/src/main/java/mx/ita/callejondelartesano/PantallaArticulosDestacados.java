package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class PantallaArticulosDestacados extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbarNav;
    Button button1, button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_articulos_destacados);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbarNav = findViewById(R.id.toolbar);

        setSupportActionBar(toolbarNav);
        navigationView.setNavigationItemSelectedListener(this);
//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarNav,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        button1= (Button)findViewById(R.id.buttonVer1);
        button2= (Button)findViewById(R.id.buttonVer2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vermas1();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vermas2();
            }
        });
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
            case R.id.nab_contacto:
                intent = new Intent(this, PantallaContactanos.class);
                intent.putExtra("array",incomingValue);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void Vermas1(){
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");


        Intent intent = new Intent(this, InformacionProducto.class);
        incomingValue[1]="2";

        intent.putExtra("array",incomingValue);
        startActivity(intent);
    }
    public void Vermas2(){
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");


        Intent intent = new Intent(this, InformacionProducto.class);
        incomingValue[1]="4";

        intent.putExtra("array",incomingValue);
        startActivity(intent);
    }
}