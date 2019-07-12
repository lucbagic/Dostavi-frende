package hr.dostavifrende.dostavifrende.fragments;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.dostavifrende.dostavifrende.ChatMessage;
import hr.dostavifrende.dostavifrende.Offer;
import hr.dostavifrende.dostavifrende.R;
import hr.dostavifrende.dostavifrende.User;

public class UsersFragment extends Fragment {
    private RecyclerView usersList;
    DatabaseReference rootReference;
    FirebaseUser user;
    FirebaseAuth auth;
    private FirebaseListAdapter<String> adapter;

    ArrayList<String> korisnici = new ArrayList<>();
    ArrayList<String> listaPrikaz = new ArrayList<>();
    ArrayList<User> listaUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();

        final ListView listaKorisnika = view.findViewById(R.id.list);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listaPrikaz);
        listaKorisnika.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userId = listaUsers.get(i).id;
                Bundle b = new Bundle();
                b.putString("userId", userId);
                ChatFragment f = new ChatFragment();
                f.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       f).commit();
            }
        });

        Query messagesRoot = rootReference.child("Messages").orderByChild("messageSender").equalTo(user.getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String primatelj = ds.child("messageReceiver").getValue().toString();
                    korisnici.add(primatelj);
                }
                for (int i = 0; i < korisnici.size(); i++) {
                    final DatabaseReference imeKorisnika = rootReference.child("Users").child(korisnici.get(i));
                    imeKorisnika.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String ime = dataSnapshot.child("ime").getValue().toString();
                            listaPrikaz.add(ime);
                            listaUsers.add(new User(ime, dataSnapshot.getKey()));
                            arrayAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                listaKorisnika.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        messagesRoot.addListenerForSingleValueEvent(eventListener);
        return view;
    }


}