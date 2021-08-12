package com.link.connectsu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class lecture_status extends AppCompatActivity {

    FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    DatabaseReference  mRef = mDB.getReference();


    TextView lectureid;
    TextView lecturestatus;
    Button assignment;
    Button notification;
    Button bupdate;
    RadioGroup rGroup;

    Spinner spinner;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerData;

    private Object ValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_status);

        Toast.makeText(this, "Retrieved", Toast.LENGTH_SHORT).show();


        lectureid = (TextView)findViewById(R.id.lectureid);
        lecturestatus = (TextView)findViewById(R.id.lecturestatus);
        assignment = (Button) findViewById(R.id.assignment);
        notification = (Button) findViewById(R.id.notificaiton);
        spinner = (Spinner) findViewById(R.id.sassign);
        rGroup = (RadioGroup) findViewById(R.id.radioGroup);
        bupdate =(Button) findViewById(R.id.updateStatus);

        //firebaseUser.getEmail().toString();
        //Toast.makeText(lecture_status.this, firebaseUser.getEmail().toString(), Toast.LENGTH_LONG).show();

        //Getting Lecture Status
        getLectureId_and_Status();

        //Getting spinner data
        retrieve_spinnerData();

        //Selecting Radio

        //Updating Lecture Status
        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting lecture status
                int radio_checkedId = rGroup.getCheckedRadioButtonId();
                 String status="";
                 switch (radio_checkedId){
                     case R.id.radioButton1 : status = "Running";
                     break;
                     case R.id.radioButton2 : status = "Delayed";
                     break;
                     case R.id.radioButton3 : status = "Cancelled";
                     break;
                     default: status = "Running";
                 }
                 Toast.makeText(lecture_status.this, status, Toast.LENGTH_SHORT).show();
                 //Getting lecture name
                String lecture_Id = spinner.getSelectedItem().toString();
                Toast.makeText(lecture_status.this, lecture_Id, Toast.LENGTH_SHORT).show();
                update_lectureId_and_status(lecture_Id,status);
            }
        });

        //Moving to Sections
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lecture_status.this,assignment.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lecture_status.this,notification.class);
                startActivity(intent);
            }
        });

    }
    //Retrieve Data for Spinner
    public void retrieve_spinnerData(){
        spinnerData = new ArrayList<>();
        adapter = new ArrayAdapter<>(lecture_status.this, android.R.layout.simple_spinner_dropdown_item,spinnerData);
        spinner.setAdapter(adapter);
        mRef.child("ME18/Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    spinnerData.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Update Lecture Status
    public void update_lectureId_and_status(String lectureId, String status){
        mRef.child("ME18/Lecture/CurrentLecture/LectureID").setValue(lectureId).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(lecture_status.this, "YOU ARE NOT A SU", Toast.LENGTH_SHORT).show();
            }
        });
        mRef.child("ME18/Lecture/CurrentLecture/Status").setValue(status);
    }
    //Get LectureId and Status
    public void getLectureId_and_Status(){
        mRef.child("ME18/Lecture/CurrentLecture/LectureID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue(String.class);
                lectureid.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child("ME18/Lecture/CurrentLecture/Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue(String.class);
                lecturestatus.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}