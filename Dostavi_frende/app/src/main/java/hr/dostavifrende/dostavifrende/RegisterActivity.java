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

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextLozinka;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextLozinka = findViewById(R.id.editTextLozinka);

        auth = FirebaseAuth.getInstance();

    }

    public void createUser(View v){
        if(editTextEmail.getText().toString().equals("") && editTextLozinka.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sva polja", Toast.LENGTH_SHORT).show();
        }else {
            final String email = editTextEmail.getText().toString();
            final String loz = editTextLozinka.getText().toString();

            auth.createUserWithEmailAndPassword(email, loz)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Uspješno ste se registrirali", Toast.LENGTH_SHORT).show();
                                finish();

                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "Korisnik nije kreiran", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
