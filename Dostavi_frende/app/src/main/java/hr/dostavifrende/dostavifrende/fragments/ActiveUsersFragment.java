package hr.dostavifrende.dostavifrende.fragments;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import hr.dostavifrende.dostavifrende.Message;
import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;

public class ActiveUsersFragment extends Fragment {
    private View view;
    private RecyclerView activeUsersList;
    DatabaseReference rootReference;
    FirebaseUser user;
    FirebaseAuth auth;

   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users_active, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        activeUsersList = view.findViewById(R.id.recyclerViewActiveUsers);
        activeUsersList.setHasFixedSize(true);
        activeUsersList.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Offer, OfferViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>
                (Offer.class, R.layout.list_active_users, OfferViewHolder.class, rootReference.child("Offers")) {
            @Override
            protected void populateViewHolder(final OfferViewHolder viewHolder, final Offer model, int position) {
                viewHolder.setKorisnik(model.getImePrezime());

                DatabaseReference slikaKorisnika = rootReference.child("Users").child(model.korisnik.trim()).child("urlSlike");
                slikaKorisnika.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String slika = dataSnapshot.getValue().toString();
                        viewHolder.setImage(getContext(), slika);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog myDialog = new Dialog(getContext());
                        myDialog.setContentView(R.layout.costumpopup);
                        TextView textViewNapomena = myDialog.findViewById(R.id.textViewNapomena);
                        Button javiSe = myDialog.findViewById(R.id.buttonChat);
                        textViewNapomena.setText(model.getNapomena());
                        myDialog.show();
                        javiSe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Message newMessageInsertObj = new Message("Pozdrav!", user.getUid(), model.getKorisnik());


                                rootReference.child("Messages").child(user.getUid().concat("_").concat(model.korisnik)).push().setValue(newMessageInsertObj);

                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new ChatFragment()).commit();
                                myDialog.cancel();
                            }
                        });
                    }
                });
            }
        };
        activeUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        View view;
        public OfferViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }
        public void setKorisnik(String imePrezime){
            TextView textViewImePrezime = view.findViewById(R.id.textViewImePrezime);
            textViewImePrezime.setText(imePrezime);
        }
       public void setNapomena(String napomena){
           TextView textViewNapomena = view.findViewById(R.id.textViewNapomena);
           textViewNapomena.setText(napomena);
        }
        public void setImage(Context ctx, String image){
            ImageView imageViewSlika = view.findViewById(R.id.imageViewActiveUser);
            Picasso.with(ctx).load(image).into(imageViewSlika);
        }
    }




}