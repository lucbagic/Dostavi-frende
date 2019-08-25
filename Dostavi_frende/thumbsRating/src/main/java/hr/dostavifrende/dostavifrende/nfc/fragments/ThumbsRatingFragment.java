package hr.dostavifrende.dostavifrende.nfc.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.core.fragments.BaseFragment;
import hr.dostavifrende.dostavifrende.core.fragments.Deal;
import hr.dostavifrende.dostavifrende.core.fragments.User;
import hr.dostavifrende.dostavifrende.nfc.R;

public class ThumbsRatingFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmListener confirmListener;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference rootReference;
    Deal deal;
    String korisnik;
    Button btnThumbsUp;
    Button btnThumbsDown;
    int thumbsUp;
    int thumbsDown;

    @Override
    public int getLayout() {
        return R.layout.fragment_thumbs_rating;
    }

    @Override
    public void init(View view) {
        Button nfcButton = view.findViewById(R.id.nfc_reader_button);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        btnThumbsUp = view.findViewById(R.id.btnThumbsUp);
        btnThumbsDown = view.findViewById(R.id.btnThumbsDown);

        btnThumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsUp = 1;
                thumbsDown = 0;
                btnThumbsUp.setBackgroundResource(R.drawable.ic_thumb_up_positive_24dp);
                btnThumbsDown.setBackgroundResource(R.drawable.ic_thumb_down_black_24dp);
            }
        });

        btnThumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsUp = 0;
                thumbsDown = 1;
                btnThumbsUp.setBackgroundResource(R.drawable.ic_thumb_up_black_24dp);
                btnThumbsDown.setBackgroundResource(R.drawable.ic_thumb_down_red_24dp);
            }
        });

        nfcButton.setOnClickListener(this);
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
                        ocijeniKorisnikaThumbs();
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

    public void ocijeniKorisnikaThumbs(){
        final DatabaseReference referenca;
        referenca = rootReference.child("Users").child(korisnik);
        referenca.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                int tD = user.getThumbsDown();
                int tU = user.getThumbsUp();

                referenca.child("thumbsUp").setValue(tU+thumbsUp);
                referenca.child("thumbsDown").setValue(tD+thumbsDown);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
