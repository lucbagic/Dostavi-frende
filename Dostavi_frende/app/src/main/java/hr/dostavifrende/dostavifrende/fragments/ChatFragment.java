package hr.dostavifrende.dostavifrende.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import hr.dostavifrende.dostavifrende.core.fragments.Deal;
import hr.dostavifrende.dostavifrende.R;
import hr.dostavifrende.dostavifrende.button.fragments.StarsRatingFragment;

public class ChatFragment extends Fragment {

    ArrayList<String> myArrayList = new ArrayList<>();

    EditText input;

    private FirebaseListAdapter<ChatMessage> adapter;
    DatabaseReference rootReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId = "";
    Button btnDogovoreno;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        userId = getArguments().getString("userId");
        btnDogovoreno = view.findViewById(R.id.buttonDogovoreno);

        ListView listOfMessages = view.findViewById(R.id.list_of_messages);
        Log.i("AJMO", userId);
        rootReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        if(auth != null){
            user = auth.getCurrentUser();
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);
        input = view.findViewById(R.id.input);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootReference
                        .child("Messages")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                user.getUid(),userId)
                        );
                input.setText("");
            }
        });

        btnDogovoreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query reference;
                reference = rootReference.child("Deals").child(user.getUid()).orderByChild("status").equalTo("aktivno");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            rootReference.child("Deals").child(user.getUid()).push().setValue(new Deal(userId, "aktivno"));
                        }
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Deal deal = ds.getValue(Deal.class);
                            if (!deal.getStatus().equals("aktivno")){
                                rootReference.child("Deals").child(user.getUid()).push().setValue(new Deal(userId, "aktivno"));
                            }else {
                                Toast.makeText(getContext(),"Dogovor već postoji!",Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,
                R.layout.message, rootReference.child("Messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                if (model.getMessageReceiver().equals(user.getUid()) && model.getMessageSender().equals(userId) ||
                        model.getMessageReceiver().equals(userId) && model.getMessageSender().equals(user.getUid())){
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageSender());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }

            }
        };

        listOfMessages.setAdapter(adapter);

        return view;
    }

}


