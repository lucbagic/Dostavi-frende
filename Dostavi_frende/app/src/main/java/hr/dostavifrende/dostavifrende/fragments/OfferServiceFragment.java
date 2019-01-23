package hr.dostavifrende.dostavifrende.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hr.dostavifrende.dostavifrende.MainActivity;
import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;
import hr.dostavifrende.dostavifrende.User;

public class OfferServiceFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference rootReference;
    AutoCompleteTextView textViewGrad;
    Button offerService;

    EditText editTextDatum,editTextPoruka;

    static final String[] GRADOVI = new String[]{
            "Bakar","Beli Manastir","Belišće","Benkovac","Biograd na Moru","Bjelovar","Buje","Buzet","Cres","Crikvenica","Čabar","Čakovec",
            "Čazma","Daruvar","Delnice","Donja Stubica","Donji Miholjac","Drniš","Dubrovnik","Duga Resa","Dugo Selo","Đakovo","Đurđevac",
            "Garešnica","Glina","Gospić","Grubišno Polje","Hrvatska Kostajnica","Hvar","Ilok","Imotski","Ivanec","Ivanić-Grad","Jastrebarsko",
            "Karlovac","Kastav","Kaštela","Klanjec","Knin","Komiža","Koprivnica","Korčula","Kraljevica","Krapina","Križevci","Krk","Kutina",
            "Kutjevo","Labin","Lepoglava","Lipik","Ludbreg","Makarska","Mali Lošinj","Metković","Mursko Središće","Našice","Nin","Nova Gradiška",
            "Novalja","Novi Marof","Novi Vinodolski","Novigrad","Novska","Obrovac","Ogulin","Omiš","Opatija","Opuzen","Orahovica","Oroslavje",
            "Osijek","Otočac","Otok","Ozalj","Pag","Pakrac","Pazin","Petrinja","Pleternica","Ploče","Poreč","Požega","Pregrada","Prelog","Pula",
            "Rab","Rijeka","Rovinj","Samobor","Senj","Sinj","Sisak","Skradin","Slatina","Slavonski Brod","Slunj","Solin","Split","Stari Grad",
            "Supetar","Sveta Nedelja","Sveti Ivan Zelina","Šibenik","Trilj","Trogir","Umag","Valpovo","Varaždin","Varaždinske Toplice",
            "Velika Gorica","Vinkovci","Virovitica","Vis","Vodice","Vodnjan","Vrbovec","Vrbovsko","Vrgorac","Vrlika","Vukovar","Zabok","Zadar",
            "Zagreb","Zaprešić","Zlatar","Županja"
    };

    Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_offer, container, false);

        editTextDatum = view.findViewById(R.id.editTextDatum);
        editTextPoruka = view.findViewById(R.id.editTextPoruka);

        textViewGrad = view.findViewById(R.id.textViewGrad);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, GRADOVI);
        textViewGrad.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        offerService = view.findViewById(R.id.buttonPonudi);
        offerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerService();
            }
        });


        return view;
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editTextDatum.setText(sdf.format(myCalendar.getTime()));
    }

    private String validateFields(){
        String errorMessage = "";
        if (textViewGrad.getText().toString().equals("") || editTextDatum.getText().toString().equals("")
                || editTextPoruka.getText().toString().equals("")){
            errorMessage = "Niste ispunili sva polja.";
        }else {
            errorMessage = "";
        }
        return errorMessage;
    }

    public void offerService(){
        String errorMessage = validateFields();
        if(errorMessage != ""){
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
        else {
            final String grad = textViewGrad.getText().toString();
            final String datum = editTextDatum.getText().toString();
            final String napomena = editTextPoruka.getText().toString();

            firebaseUser = auth.getCurrentUser();
            Offer newOfferInsertObj = new Offer(firebaseUser.getUid(), grad, napomena);
            rootReference.child("Offers").child(datum+" "+Calendar.getInstance().getTimeInMillis()).setValue(newOfferInsertObj)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Spremili ste uslugu.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
