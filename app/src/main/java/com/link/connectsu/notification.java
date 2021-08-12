package com.link.connectsu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class notification extends AppCompatActivity {

    ListView lvnotification;
    EditText tnotify;
    Button bnotify;


    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDB.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getNotifications();

        bnotify = (Button)findViewById(R.id.bnotify);
        tnotify = (EditText)findViewById(R.id.tnotify);

        //Push Notification
        bnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String notification = tnotify.getText().toString();
                if(notification.isEmpty()){
                    Toast.makeText(notification.this, "Enter notification first!", Toast.LENGTH_LONG).show();
                }else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());
                    mRef.child("ME18/Notification").child(String.valueOf(format)).setValue(notification).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(notification.this, "YOU ARE NOT A SU", Toast.LENGTH_SHORT).show();
                        }
                    });
                    tnotify.setText("");
                }
            }
        });
    }

    //Retrieve Notifications
    public void getNotifications(){
        lvnotification = (ListView) findViewById(R.id.lvnotification);

        Toast.makeText(this, "Retrieved", Toast.LENGTH_SHORT).show();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        lvnotification.setAdapter(adapter);

        mRef.child("ME18/Notification").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String data = snapshot.getValue(String.class);
                arrayList.add(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}