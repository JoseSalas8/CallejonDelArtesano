package mx.ita.callejondelartesano;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

import java.util.HashMap;

public class activity_Login extends AppCompatActivity {

    EditText correoLogin,passwordLogin;
    Button INGRESAR, INGRESARGOOGLE;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;


    private FirebaseAuth firebaseAuth;
   // private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        correoLogin= findViewById(R.id.correoLogin);
        passwordLogin= findViewById(R.id.passwordLogin);
        INGRESAR = findViewById(R.id.INGRESAR);
        INGRESARGOOGLE = findViewById(R.id.INGRESARGOOGLE);
        firebaseAuth = FirebaseAuth.getInstance();

// Para crear la solicitud
        crearSolicitud();


        INGRESAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoLogin.getText().toString();
                String pass = passwordLogin.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    correoLogin.setError("Correo inválido");
                    correoLogin.setFocusable(true);
                } else{
                    LOGINUSUARIO(correo,pass);
                }
            }
        });

        INGRESARGOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



    }

    private void crearSolicitud() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    private void signIn(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
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

                                DatabaseReference reference = database.getReference("USUARIOS_APP");

                                reference.child(uid).setValue(DatosUsuario);
                            }
                            startActivity(new Intent(activity_Login.this,PantallaPrincipal.class));
                        }
                        else{
                            Toast.makeText(activity_Login.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void LOGINUSUARIO(String correo, String pass) {

    //    progressDialog.setCancelable(false);
      //  progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                          //  progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(activity_Login.this, PantallaPrincipal.class));
                            assert user != null;
                            Toast.makeText(activity_Login.this, "Bienvenid@ de nuevo " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                           // progressDialog.dismiss();
                            Toast.makeText(activity_Login.this, "Algo ha salido mal"
                            , Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  progressDialog.dismiss();
                Toast.makeText(activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  super.onSupportNavigateUp();
    }
}