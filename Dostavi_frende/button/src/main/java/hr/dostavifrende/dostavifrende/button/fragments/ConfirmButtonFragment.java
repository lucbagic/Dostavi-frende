package hr.dostavifrende.dostavifrende.button.fragments;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import hr.dostavifrende.dostavifrende.button.R;
import hr.dostavifrende.dostavifrende.core.ConfirmListener;
import hr.dostavifrende.dostavifrende.core.fragments.BaseFragment;

public class ConfirmButtonFragment extends BaseFragment implements View.OnClickListener {

    private ConfirmListener confirmListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_confirm_button;
    }

    @Override
    public void init(View view) {
        Button confirm_button = view.findViewById(R.id.confirm_button);
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
