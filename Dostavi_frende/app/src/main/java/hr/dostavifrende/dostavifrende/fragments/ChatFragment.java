package hr.dostavifrende.dostavifrende.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hr.dostavifrende.dostavifrende.Message;
import hr.dostavifrende.dostavifrende.R;

public class ChatFragment extends Fragment {

    ArrayList<String> myArrayList = new ArrayList<>();

    ListView listaPoruka;


    DatabaseReference rootReference;
    private FirebaseListAdapter<Message> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listaPoruka = view.findViewById(R.id.messages_view);
        rootReference = FirebaseDatabase.getInstance().getReference();


        return view;
    }

}
