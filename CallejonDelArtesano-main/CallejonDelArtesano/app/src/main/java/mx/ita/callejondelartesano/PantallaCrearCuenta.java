package mx.ita.callejondelartesano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PantallaCrearCuenta extends AppCompatActivity {
    EditText editnombre, editemail, editpass;
    Button buttonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_cuenta);

        editnombre= (EditText)findViewById(R.id.name_text);
        editemail= (EditText)findViewById(R.id.email_text);
        editpass= (EditText)findViewById(R.id.password_text);

        buttonAgregar=(Button)findViewById(R.id.buttonAgregarusuario);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText( getApplicationContext(),editnombre.getText().toString(), Toast.LENGTH_SHORT);
//                toast.show();
                ejecutarServicio("https://callejonwebservices.herokuapp.com/insertarUsuario.php");
            }
        });
    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    ArrancarInicioSesion("123456789");
//                    Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
//                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "El correo electrónico ya existe", Toast.LENGTH_SHORT);
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
                parametros.put("nombre",editnombre.getText().toString());
                parametros.put("email",editemail.getText().toString());
                parametros.put("pass",editpass.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void ArrancarInicioSesion(String email){
        Intent intent = new Intent(this, PantallaPrincipal.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}