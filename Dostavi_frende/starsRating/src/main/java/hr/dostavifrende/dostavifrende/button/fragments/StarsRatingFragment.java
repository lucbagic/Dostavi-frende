package hr.dostavifrende.dostavifrende.button.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import hr.dostavifrende.dostavifrende.button.R;
import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.core.fragments.BaseFragment;
import hr.dostavifrende.dostavifrende.core.fragments.Deal;
import hr.dostavifrende.dostavifrende.core.fragments.User;

public class StarsRatingFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmListener confirmListener;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference rootReference;
    Deal deal;
    String korisnik;
    RatingBar ratingBar;

    @Override
    public int getLayout() {
        return R.layout.fragment_confirm_button;
    }

    @Override
    public void init(View view) {
        Button confirm_button = view.findViewById(R.id.confirm_button);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        ratingBar = view.findViewById(R.id.ratingBar);

        confirm_button.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmListener){
            confirmListener = (ConfirmListener)context;
        }
    }

    @Override
    public void onClick(View view) {
        Query reference;
        reference = rootReference.child("Deals").child(user.getUid()).orderByChild("status").equalTo("aktivno");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        deal = ds.getValue(Deal.class);
                        String key = ds.getKey();
                        korisnik = deal.getKorisnik();
                        ocijeniKorisnikaStars();
                        rootReference.child("Deals").child(user.getUid()).child(key).child("status").setValue("Zavrseno");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        confirmListener.potvrdiPrimitak();
    }

    public void ocijeniKorisnikaStars(){
        final DatabaseReference referenca;
        referenca = rootReference.child("Users").child(korisnik);
        referenca.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   User user = dataSnapshot.getValue(User.class);

                   int zbroj = user.getZbroj();
                   int brojac = user.getBrojac();

                   referenca.child("zbroj").setValue(ratingBar.getRating()+zbroj);
                   referenca.child("brojac").setValue(brojac+1);

                   double ocjena = (double) (ratingBar.getRating()+zbroj)/(brojac+1);
                   referenca.child("ocjena").setValue(ocjena);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
