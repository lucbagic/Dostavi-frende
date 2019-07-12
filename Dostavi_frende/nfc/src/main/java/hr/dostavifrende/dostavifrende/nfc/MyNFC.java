package hr.dostavifrende.dostavifrende.nfc;

import android.app.Fragment;

import hr.dostavifrende.dostavifrende.core.Modul;
import hr.dostavifrende.dostavifrende.core.PotvrdaListener;

public class MyNFC extends Fragment implements Modul {
    private PotvrdaListener listener;
    @Override
    public void setPotvrdaListener(PotvrdaListener listener) {
        this.listener = listener;
    }


    private void nfcReceived(){
        listener.potvrdiPrimitak();
    }
}
