package hr.dostavifrende.dostavifrende.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;

public class MyOffersFragment extends Fragment {
    private View view;
    private RecyclerView myOffersList;
    DatabaseReference rootReference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseRecyclerAdapter<Offer, OfferViewHolder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_offers, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        myOffersList = view.findViewById(R.id.recyclerViewMyOffers);
        myOffersList.setHasFixedSize(true);
        myOffersList.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>
                (Offer.class, R.layout.list_my_offers, OfferViewHolder.class, rootReference.child("Offers").orderByChild("korisnik").equalTo(user.getUid())) {
            @Override
            protected void populateViewHolder(final OfferViewHolder viewHolder, final Offer model, final int position) {
                viewHolder.setGrad(model.getGrad());
                viewHolder.setNapomena(model.getNapomena());
                viewHolder.setDatum(model.getDatum());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage("Jeste li sigurni da zelite obrisati uslugu?").setCancelable(false)
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firebaseRecyclerAdapter.getRef(position).removeValue();
                                        firebaseRecyclerAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog dialog = alertDialog.create();
                        dialog.setTitle("Brisanje usluge");
                        dialog.show();
                    }
                });


            }

        };


        myOffersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        View view;

        public OfferViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setNapomena(String napomena) {
            TextView textViewNapomena = view.findViewById(R.id.textViewNapomena);
            textViewNapomena.setText(napomena);
        }

        public void setGrad(String grad) {
            TextView textViewGrad = view.findViewById(R.id.textViewGrad);
            textViewGrad.setText(grad);
        }

        public void setDatum(String datum) {
            TextView textViewDatum = view.findViewById(R.id.textViewDatum);
            textViewDatum.setText(datum);

        }
    }


}