package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PantallaCrearCuenta extends AppCompatActivity {
    EditText editnombre, editemail, editpass;
    Button buttonAgregar;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_cuenta);

        editnombre= (EditText)findViewById(R.id.name_text);
        editemail= (EditText)findViewById(R.id.email_text);
        editpass= (EditText)findViewById(R.id.password_text);

        buttonAgregar=(Button)findViewById(R.id.buttonAgregarusuario);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText( getApplicationContext(),editnombre.getText().toString(), Toast.LENGTH_SHORT);
//                toast.show();
 //               ejecutarServicio("https://callejonwebservices.herokuapp.com/insertarUsuario.php");
                String email = editemail.getText().toString();
                String pass = editpass.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editemail.setError("Correo no válido");
                    editemail.setFocusable(true);
                }
                else {
                    REGISTRAR(email,pass);
                }
            }
        });
    }

    // Método para registrar usuario con correo
    private void REGISTRAR(String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String uid = user.getUid();
                            String email = editemail.getText().toString();
                            String pass = editpass.getText().toString();
                            String nombre = editnombre.getText().toString();

                            HashMap<Object, String> DatosUsuario = new HashMap<>();
                            DatosUsuario.put("uid", uid);
                            DatosUsuario.put("nombre",nombre);
                            DatosUsuario.put("email", email);
                            DatosUsuario.put("pass", pass);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("USUARIOS_APP");
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(PantallaCrearCuenta.this, "Se registró exitosamente"
                            , Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(PantallaCrearCuenta.this,PantallaPrincipal.class));
                        } else{
                            Toast.makeText(PantallaCrearCuenta.this, "Algo ha salido mal"
                            , Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PantallaCrearCuenta.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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