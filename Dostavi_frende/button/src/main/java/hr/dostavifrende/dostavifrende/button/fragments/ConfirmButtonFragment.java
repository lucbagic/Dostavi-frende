package hr.dostavifrende.dostavifrende.button.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import hr.dostavifrende.dostavifrende.button.R;
import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.core.fragments.BaseFragment;

public class ConfirmButtonFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmListener confirmListener;
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference rootReference;

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
        Log.i("LUCKA", user.getUid());
        
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
        confirmListener.potvrdiPrimitak();
    }
}
