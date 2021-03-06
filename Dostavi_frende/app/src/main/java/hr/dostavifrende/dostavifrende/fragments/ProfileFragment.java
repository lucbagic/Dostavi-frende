package hr.dostavifrende.dostavifrende.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import hr.dostavifrende.dostavifrende.R;

public class ProfileFragment extends Fragment {

    TextView textViewImePrezime, textViewEmail, textViewGodina;
    ImageView imageViewProfilna;
    FirebaseUser user;
    FirebaseAuth auth;
    StorageReference storageReference;
    String urlSlike;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        imageViewProfilna = view.findViewById(R.id.imageViewProfilna);
        textViewImePrezime = view.findViewById(R.id.textViewImePrezime);
        textViewGodina = view.findViewById(R.id.textViewGodina);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewEmail.setText(user.getEmail());

        getData();
        displayImage();
        return view;
    }

    public void displayImage(){
        if (user != null){
            storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    urlSlike = uri.toString().trim();
                    Picasso.with(getContext()).load(urlSlike).rotate(270).into(imageViewProfilna);
                }
            });
        }
    }

    public void getData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ime = dataSnapshot.child("ime").getValue().toString();
                String prezime = dataSnapshot.child("prezime").getValue().toString();
                Integer godinaRodenja = Integer.parseInt(dataSnapshot.child("godinaRodenja").getValue().toString());
                Integer godina = Calendar.getInstance().get(Calendar.YEAR) - godinaRodenja;
                textViewImePrezime.setText(ime+" "+prezime);
                textViewGodina.setText(godina.toString()+" "+"god");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
