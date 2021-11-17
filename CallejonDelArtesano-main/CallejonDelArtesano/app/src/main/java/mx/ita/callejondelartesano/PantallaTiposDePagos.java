package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class PantallaTiposDePagos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbarNav;
    public Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_tipos_de_pagos);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbarNav = findViewById(R.id.toolbar);

        setSupportActionBar(toolbarNav);
        navigationView.setNavigationItemSelectedListener(this);
//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarNav, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





        button = (Button) findViewById(R.id.buttonTarjetas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrancarTarjetas();


            }
        });
        button = (Button) findViewById(R.id.buttonQR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrancarQr();


            }
        });
    }
    public void ArrancarTarjetas(){
        Intent intent = new Intent(this, PantallaTajetas.class);
        startActivity(intent);
    }
    public void ArrancarQr(){
        Intent intent = new Intent(this, QR.class);
        startActivity(intent);
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

//    public void ArrancarPayPal(){
//        Intent intent = new Intent(this, InformacionProducto.class);
//        startActivity(intent);
//    }
//    public void ArrancarEfectivo(){
//        Intent intent = new Intent(this, InformacionProducto.class);
//        startActivity(intent);
//    }
}