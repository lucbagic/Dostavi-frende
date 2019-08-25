package hr.dostavifrende.dostavifrende;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import hr.dostavifrende.dostavifrende.button.fragments.StarsRatingFragment;
import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.fragments.ActiveUsersFragment;
import hr.dostavifrende.dostavifrende.fragments.UserUnknownFragment;
import hr.dostavifrende.dostavifrende.nfc.fragments.ThumbsRatingFragment;
import hr.dostavifrende.dostavifrende.util.BottomNavigationManager;

public class MainActivity extends AppCompatActivity implements ConfirmListener {
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference rootReference;
    Uri imageUri;
    String urlSlike;
    ProgressDialog progressDialog;
    ImageView imageViewProfilna;

    TextView textView;

    EditText editTextDatumOd, editTextDatumDo, editTextVrijemeOd, editTextVrijemeDo, editTextPoruka;

    private BottomNavigationManager bnm;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if(auth != null){
            user = auth.getCurrentUser();
        }
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");
        rootReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        initBottomNavigationManager();

        //Izmjena fragmenata
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ActiveUsersFragment()).commit();
    }

    private void initBottomNavigationManager() {

        bnm = BottomNavigationManager.newInstnace();
        bnm.setBottomNavigationDependencies(
                this,
                bottomNavigationView,
                R.id.dynamic_group);
        bnm.startMainModule();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.button_reader_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StarsRatingFragment()).commit();
                return true;
            case R.id.nfc_reader_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ThumbsRatingFragment()).commit();
                return true;
            default:
                return false;

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        /*case R.id.nav_active_users:
                            selectedFragment = new ActiveUsersFragment();
                            break;
                        case R.id.nav_offer:
                            selectedFragment = new OfferServiceFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new UsersFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            selectedFragment = isUserLoggedIn(selectedFragment);
                            break;*/
                        default: BottomNavigationManager.newInstnace().selectNavigationItem(item);

                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                      //      selectedFragment).commit();
                    return true;
                }
            };

    private android.support.v4.app.Fragment isUserLoggedIn(android.support.v4.app.Fragment returnFragment){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            returnFragment = new UserUnknownFragment();
        }
        return returnFragment;
    }

    public void openRegister(View v){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void openLogin(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void signOut(View v){
        auth.signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new UserUnknownFragment()).commit();
    }


    public void openGallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageViewProfilna = findViewById(R.id.imageViewProfilna);
            imageViewProfilna.setImageURI(imageUri);
            StorageReference myRef = storageReference.child(user.getUid());
            progressDialog.setMessage("Učitavanje u tijeku.");
            progressDialog.show();
            myRef.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Uspješno učitana slika.", Toast.LENGTH_SHORT).show();
                                storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        urlSlike = uri.toString().trim();
                                        rootReference.child("Users").child(user.getUid()).child("urlSlike").setValue(urlSlike);
                                    }
                                });
                            }else {
                                Toast.makeText(getApplicationContext(), "Neuspješno učitavanje.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    public void potvrdiPrimitak() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Potvrda");
        alertDialog.setMessage("Usluga je uspjesno dostavljena");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        /*final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.costumpopup);
        TextView textViewNapomena = myDialog.findViewById(R.id.textViewNapomena);
        textViewNapomena.setText("Bla bla car");
        myDialog.show();*/
    }
}