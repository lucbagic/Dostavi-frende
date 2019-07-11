package hr.dostavifrende.dostavifrende.fragments;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.dostavifrende.dostavifrende.ChatMessage;
import hr.dostavifrende.dostavifrende.Message;
import hr.dostavifrende.dostavifrende.R;

public class ChatFragment extends Fragment {

    ArrayList<String> myArrayList = new ArrayList<>();

    ListView listaPoruka;
    EditText input;

    private FirebaseListAdapter<ChatMessage> adapter;
    DatabaseReference rootReference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        listaPoruka = view.findViewById(R.id.messages_view);
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
                Log.i("Slusam", "szu");
                rootReference
                        .child("Messages")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                               user.getDisplayName())
                        );

                input.setText("");
            }
        });

        ListView listOfMessages = view.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,
                R.layout.message, rootReference.child("Messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

        return view;
    }

}
