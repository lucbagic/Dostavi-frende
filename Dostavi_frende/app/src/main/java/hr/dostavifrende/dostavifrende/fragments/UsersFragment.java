package hr.dostavifrende.dostavifrende.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.dostavifrende.dostavifrende.ChatMessage;
import hr.dostavifrende.dostavifrende.R;
import hr.dostavifrende.dostavifrende.core.fragments.User;
import hr.dostavifrende.dostavifrende.core.FragmentExtension;

public class UsersFragment extends Fragment implements FragmentExtension {
    private RecyclerView usersList;
    DatabaseReference rootReference;
    FirebaseUser user;
    FirebaseAuth auth;
    private FirebaseListAdapter<String> adapter;

    ArrayList<String> korisnici = new ArrayList<>();
    ArrayList<String> listaPrikaz = new ArrayList<>();

    //za slanje korisnika u novi fragment
    ArrayList<User> listaUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();

        if (user == null){
            UserUnknownFragment userUnknownFragment = new UserUnknownFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    userUnknownFragment).commit();
        }else {

            //Finalni prikaz korisnika
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

            Query messagesRoot = rootReference.child("Messages");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    korisnici.clear();
                    listaPrikaz.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ChatMessage message = ds.getValue(ChatMessage.class);
                        if (message.getMessageReceiver().equals(user.getUid()) || message.getMessageSender().equals(user.getUid())) {
                            String primatelj = message.getMessageReceiver();
                            if (!korisnici.contains(primatelj)) {
                                korisnici.add(primatelj);
                            }
                            String posiljatelj = message.getMessageSender();
                            if (!korisnici.contains(posiljatelj)) {
                                korisnici.add(posiljatelj);
                            }
                            korisnici.remove(user.getUid());
                        }

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

        }
        return view;
    }


    @Override
    public String getName() {
        return "Razgovori";
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_chat_black_24dp;
    }
}