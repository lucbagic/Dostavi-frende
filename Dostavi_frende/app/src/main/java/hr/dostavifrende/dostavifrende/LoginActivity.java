package hr.dostavifrende.dostavifrende;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextLozinka;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextLozinka = findViewById(R.id.editTextLozinka);

        auth = FirebaseAuth.getInstance();
    }

    public void loginUser(View v){
        if (editTextEmail.getText().toString().equals("") && editTextLozinka.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Prazna polja nisu dozvoljena", Toast.LENGTH_SHORT).show();
        }else {
            auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextLozinka.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.i("OOOOO", "HMMMM");
                                Toast.makeText(getApplicationContext(), "Uspješna prijava", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "Neuspješna prijava", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
