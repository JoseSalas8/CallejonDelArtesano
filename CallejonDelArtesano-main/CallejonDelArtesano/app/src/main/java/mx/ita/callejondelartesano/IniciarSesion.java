package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IniciarSesion extends AppCompatActivity {
    public Button button2;
    Button INGRESARGOOGLE;

    EditText edit_Email, edit_password;
    String [] arregloValores = new String[3];
    RequestQueue requestQueue;



    private GoogleSignInClient mGoogleSingInClient;
    private final static int RC_SIGN_IN = 123;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        button2 = (Button) findViewById(R.id.INGRESAR);
        edit_Email=(EditText)findViewById(R.id.correoLogin);
        edit_password=(EditText)findViewById(R.id.passwordLogin);

        INGRESARGOOGLE = findViewById(R.id.INGRESARGOOGLE);


        firebaseAuth = FirebaseAuth.getInstance();
     //   progressDialog = new ProgressDialog(Login.this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_Email.getText().toString();
                String pass = edit_password.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edit_Email.setError("Correo inválido");
                    edit_Email.setFocusable(true);
                } else{
                    LOGINUSUARIO(email,pass);
                }
            }
        });

      // crearsolicitud();

        //button.setOnClickListener(new View.OnClickListener() {

          //  @Override
            //public void onClick(View v) {
//                Toast toast = Toast.makeText( getApplicationContext(),editEmail.getText().toString()+" - "+editPass.getText().toString(), Toast.LENGTH_SHORT);
//                toast.show();
//                ArrancarInicioSesion();
//                BuscarUsuario("https://callejonwebservices.herokuapp.com/seleccionarUsuarios.php?email="+editEmail.getText().toString()+"&pass="+editPass.getText().toString());
              //  validarUsuario("https://callejonwebservices.herokuapp.com/procesoLogin.php");

          //  }
       // });
    }

    private void LOGINUSUARIO(String email, String pass) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            startActivity(new Intent(IniciarSesion.this, PantallaPrincipal.class));
                            assert user != null;
                            Toast.makeText(IniciarSesion.this, "Bienvenid@ de nuevo " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(IniciarSesion.this, "Algo ha salido mal"
                            , Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(IniciarSesion.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void crearsolicitud() {
       // GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      //          .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

       // mGoogleSingInClient= GoogleSignIn.getClient(this, gso);

    }

    private void singIn(){
        Intent signIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AutenticacionFirebase(account);
            } catch (ApiException e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void AutenticacionFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                String uid = user.getUid();
                                String correo = user.getEmail();
                                String nombre = user.getDisplayName();

                                HashMap<Object,String> DatosUsuario = new HashMap<>();

                                DatosUsuario.put("uid", uid);
                                DatosUsuario.put("correo",correo);
                                DatosUsuario.put("nombres",nombre);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference reference = database.getReference("Usuarios_de_app");

                                reference.child(uid).setValue(DatosUsuario);
                            }
                            startActivity(new Intent(IniciarSesion.this,PantallaPrincipal.class));
                    }
                        else{
                            Toast.makeText(IniciarSesion.this, "Error", Toast.LENGTH_SHORT).show();
                        }
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
                parametros.put("email",edit_Email.getText().toString());
                parametros.put("pass",edit_password.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void BuscarUsuario (String URL){
        edit_Email=(EditText)findViewById(R.id.correoLogin);
        edit_password=(EditText)findViewById(R.id.passwordLogin);

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        if(jsonObject.getString("Email").equals(edit_Email.getText().toString()) && jsonObject.getString("Pass").equals(edit_password.getText().toString())){
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