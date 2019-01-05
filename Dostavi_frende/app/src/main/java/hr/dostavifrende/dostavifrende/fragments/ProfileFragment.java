package hr.dostavifrende.dostavifrende.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import hr.dostavifrende.dostavifrende.R;

public class ProfileFragment extends Fragment {

    TextView textViewImePrezime;
    ImageView imageViewProfilna;
    FirebaseUser user;
    FirebaseAuth auth;
    StorageReference storageReference;
    String urlSlike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");

        imageViewProfilna = view.findViewById(R.id.imageViewProfilna);

        textViewImePrezime = view.findViewById(R.id.textViewImePrezime);


        displayImage();
        return view;
    }

    public void displayImage(){
        Log.i("HEH", imageViewProfilna.toString());
        if (user != null){
            storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    urlSlike = uri.toString().trim();
                    Log.i("IMALIKRAJA", urlSlike+"o");
                    Picasso.with(getContext()).load(urlSlike).rotate(270).into(imageViewProfilna);
                }
            });
        }
    }

}
