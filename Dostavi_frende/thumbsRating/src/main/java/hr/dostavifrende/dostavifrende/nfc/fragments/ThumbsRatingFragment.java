package hr.dostavifrende.dostavifrende.nfc.fragments;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.core.fragments.BaseFragment;
import hr.dostavifrende.dostavifrende.nfc.R;

public class ThumbsRatingFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmListener confirmListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_nfc_reader;
    }

    @Override
    public void init(View view) {
        Button nfcButton = view.findViewById(R.id.nfc_reader_button);
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
        confirmListener.potvrdiPrimitak();
    }
}
