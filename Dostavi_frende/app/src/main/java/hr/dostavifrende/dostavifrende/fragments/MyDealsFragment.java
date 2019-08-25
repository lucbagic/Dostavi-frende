package hr.dostavifrende.dostavifrende.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.dostavifrende.dostavifrende.Deal;
import hr.dostavifrende.dostavifrende.R;

public class MyDealsFragment extends Fragment {

    private View view;
    private RecyclerView myDealList;
    DatabaseReference rootReference;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseRecyclerAdapter<Deal, MyDealsFragment.DealViewHolder> firebaseRecyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_deals, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        myDealList = view.findViewById(R.id.recyclerViewMyDeals);
        myDealList.setHasFixedSize(true);
        myDealList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Deal, DealViewHolder>
                (Deal.class, R.layout.list_my_deals, DealViewHolder.class, rootReference.child("Deals").child(user.getUid()).orderByChild("status")) {
            @Override
            protected void populateViewHolder(final DealViewHolder viewHolder, final Deal model, final int position) {
                viewHolder.setKorisnik(model.getKorisnik());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setDatum(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getDealTime()));

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage("Jeste li sigurni da zelite obrisati dogovor?").setCancelable(false)
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
                        dialog.setTitle("Brisanje dogovora");
                        dialog.show();
                    }
                });
            }
        };


        myDealList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class DealViewHolder extends RecyclerView.ViewHolder {
        View view;

        public DealViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setKorisnik(String korisnik) {
            TextView textViewGrad = view.findViewById(R.id.textViewKorisnik);
            textViewGrad.setText(korisnik);
        }

        public void setStatus(String status) {
            TextView textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewStatus.setText(status);
        }
        public void setDatum(CharSequence datum) {
            TextView textViewDatum = view.findViewById(R.id.textViewDatum);
            textViewDatum.setText(datum);
        }
    }
}

