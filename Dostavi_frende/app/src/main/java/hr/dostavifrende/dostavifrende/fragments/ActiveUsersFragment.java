package hr.dostavifrende.dostavifrende.fragments;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;

public class ActiveUsersFragment extends Fragment {
    private View view;
    private RecyclerView activeUsersList;
    FirebaseAuth auth;
    DatabaseReference rootReference;

   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users_active, container, false);

        auth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference().child("Offers");
        rootReference.keepSynced(true);

        activeUsersList = view.findViewById(R.id.recyclerViewActiveUsers);
        activeUsersList.setHasFixedSize(true);
        activeUsersList.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseRecyclerAdapter<Offer, OfferViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>
                (Offer.class, R.layout.list_active_users, OfferViewHolder.class, rootReference) {
            @Override
            protected void populateViewHolder(final OfferViewHolder viewHolder, final Offer model, int position) {

                viewHolder.setKorisnik(model.getImePrezime());
                viewHolder.setImage(getContext(), model.getUrlSlike());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Dialog myDialog = new Dialog(getContext());
                        myDialog.setContentView(R.layout.costumpopup);
                        //viewHolder.setNapomena(model.getNapomena());
                        TextView textViewNapomena = view.findViewById(R.id.textViewNapomena);
                        textViewNapomena.setText("oijfodseg");
                        myDialog.show();
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