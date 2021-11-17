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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class IniciarSesion extends AppCompatActivity {
    public Button button;
    EditText editEmail, editPass;
    String [] arregloValores = new String[3];
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        button = (Button) findViewById(R.id.button2);
        editEmail=(EditText)findViewById(R.id.edit_email);
        editPass=(EditText)findViewById(R.id.edit_password);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText( getApplicationContext(),editEmail.getText().toString()+" - "+editPass.getText().toString(), Toast.LENGTH_SHORT);
//                toast.show();
//                ArrancarInicioSesion();
//                BuscarUsuario("https://callejonwebservices.herokuapp.com/seleccionarUsuarios.php?email="+editEmail.getText().toString()+"&pass="+editPass.getText().toString());
                validarUsuario("https://callejonwebservices.herokuapp.com/procesoLogin.php");

            }
        });
    }
    public void ArrancarInicioSesion(String id){
        Intent intent = new Intent(this, PantallaPrincipal.class);
        arregloValores[0] = id;
        intent.putExtra("array",arregloValores);
        startActivity(intent);
    }

    public void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    ArrancarInicioSesion(response);
//                    Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
//                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Correo o usuario incorrecto", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error con la conexión", Toast.LENGTH_SHORT);
                toast.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("email",editEmail.getText().toString());
                parametros.put("pass",editPass.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void BuscarUsuario (String URL){
        editEmail=(EditText)findViewById(R.id.edit_email);
        editPass=(EditText)findViewById(R.id.edit_password);

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        if(jsonObject.getString("Email").equals(editEmail.getText().toString()) && jsonObject.getString("Pass").equals(editPass.getText().toString())){
                            Toast toast = Toast.makeText(getApplicationContext(), "Bienvenido: "+jsonObject.getString("Nombre"), Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "El correo o la contraseña son erróneos", Toast.LENGTH_SHORT);
                            toast.show();
                        }

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
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}