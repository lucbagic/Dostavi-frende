package hr.dostavifrende.dostavifrende;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail, editTextLozinka, editTextLozinka1, editTextIme, editTextPrezime, editTextGodinaRodenja;


    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference rootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextLozinka = findViewById(R.id.editTextLozinka);
        editTextLozinka1 = findViewById(R.id.editTextLozinka1);
        editTextIme = findViewById(R.id.editTextIme);
        editTextPrezime= findViewById(R.id.editTextPrezime);
        editTextGodinaRodenja = findViewById(R.id.editTextGodinaRodenja);


        auth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createUser(View v){
        if(editTextEmail.getText().toString().equals("") && editTextLozinka.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Niste ispunili sva polja", Toast.LENGTH_SHORT).show();
        }else {
            final String email = editTextEmail.getText().toString();
            final String lozinka = editTextLozinka.getText().toString();
            final String ime = editTextIme.getText().toString();
            final String prezime = editTextPrezime.getText().toString();
            final Integer godinaRodenja = Integer.valueOf(editTextGodinaRodenja.getText().toString());

            auth.createUserWithEmailAndPassword(email, lozinka)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseUser = auth.getCurrentUser();
                                User newUserInsertObj = new User(ime, prezime, lozinka, godinaRodenja);
                                rootReference.child("Users").child(firebaseUser.getUid()).setValue(newUserInsertObj)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Uspješno ste se registrirali", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(getApplicationContext(), "Korisnik nije kreiran", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
