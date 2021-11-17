package mx.ita.callejondelartesano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button button;
    public Button buttonCrearCuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_CallejonDelArtesano);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrancarInicioSesion();
            }
        });

        buttonCrearCuenta = (Button) findViewById(R.id.button2);
        buttonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrancarCrearCuenta();
            }
        });

    }
    public void ArrancarInicioSesion(){
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
    }
    public void ArrancarCrearCuenta(){
        Intent intent = new Intent(this, PantallaCrearCuenta.class);
        startActivity(intent);
    }
}