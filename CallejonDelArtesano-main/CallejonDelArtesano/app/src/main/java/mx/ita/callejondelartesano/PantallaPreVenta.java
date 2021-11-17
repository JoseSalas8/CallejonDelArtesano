package mx.ita.callejondelartesano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PantallaPreVenta extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    public Button button;
    String metodoDePago = "";

    ImageView imageview;
    TextView textPrecio;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pre_venta);

        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");

//        Toast toast = Toast.makeText(getApplicationContext(), incomingValue[0]+" - "+incomingValue[1] +" - "+incomingValue[2], Toast.LENGTH_SHORT);
//        toast.show();

        MostrarObjetos("http://callejonwebservices.herokuapp.com/seleccionarProductos.php?id="+incomingValue[1]);

        imageview = (ImageView)findViewById(R.id.imageView);

        Resources res = getResources();
        String mDrawableName = "img"+incomingValue[1];
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        imageview.setImageDrawable(drawable );

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrancarConfirmacionVenta();

            }
        });
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter =ArrayAdapter.createFromResource(this,R.array.metodo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    public void ArrancarConfirmacionVenta(){
        Intent incomingIntent = getIntent();
        String [] incomingValue = incomingIntent.getStringArrayExtra("array");
        incomingValue[2]=metodoDePago;

//        Toast toast = Toast.makeText(getApplicationContext(), incomingValue[0]+" - "+incomingValue[1] +" - "+incomingValue[2], Toast.LENGTH_SHORT);
//        toast.show();

        Intent intent = new Intent(this, PantallaConfirmarVenta.class);
        intent.putExtra("array", incomingValue);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        metodoDePago = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void MostrarObjetos (String URL){
        textPrecio = (TextView)findViewById(R.id.textView2);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        textPrecio.setText("$ "+jsonObject.getString("Precio"));

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