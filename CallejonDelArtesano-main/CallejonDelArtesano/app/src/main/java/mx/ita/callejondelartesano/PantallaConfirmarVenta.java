package mx.ita.callejondelartesano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PantallaConfirmarVenta extends AppCompatActivity {
    TextView textPrecio, textDetalles, textMetodoPago;
    ImageView imageview;
    Button button, button2;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_confirmar_venta);
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");

//        Toast toast = Toast.makeText(getApplicationContext(), incomingValue[0]+" - "+incomingValue[1] +" - "+incomingValue[2], Toast.LENGTH_SHORT);
//        toast.show();

        MostrarObjetos("http://callejonwebservices.herokuapp.com/seleccionarProductos.php?id="+incomingValue[1]);

        imageview = (ImageView)findViewById(R.id.imagePrincipal);


        textMetodoPago = (TextView)findViewById(R.id.textMetodoPago);
        textMetodoPago.setText("El método de pago seleccionado fue: "+incomingValue[2]);

        Resources res = getResources();
        String mDrawableName = "img"+incomingValue[1];
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        imageview.setImageDrawable(drawable );


        button = (Button)findViewById(R.id.button4);
        button2 = (Button)findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertarPedido("http://callejonwebservices.herokuapp.com/insertarPedido.php");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Abriraplicacion();
            }
        });
    }

    public void MostrarObjetos (String URL){
        textPrecio = (TextView)findViewById(R.id.textPrecio);
        textDetalles = (TextView)findViewById(R.id.textDetalles);
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        textPrecio.setText("$ "+jsonObject.getString("Precio"));
                        textDetalles.setText(jsonObject.getString("Detalles"));
                    } catch (JSONException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void InsertarPedido (String URL){
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
//                    ArrancarInicioSesion("123456789");
                    Toast toast = Toast.makeText(getApplicationContext(), "Se ha realizado el pedido", Toast.LENGTH_SHORT);
                    toast.show();
                    Abriraplicacion();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Se canceló el pedido", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("Cliente", incomingValue[0]);
                parametros.put("Producto", incomingValue[1]);
                parametros.put("Metodo", incomingValue[2]);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Abriraplicacion(){
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");


        Intent intent = new Intent(this, PantallaPrincipal.class);
        intent.putExtra("array",incomingValue);
        startActivity(intent);
    }
}