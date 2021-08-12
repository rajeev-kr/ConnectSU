package com.link.connectsu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class assignment extends AppCompatActivity {

    ListView lvassignment;
    Button basssign;
    Button btimeselect;
    EditText tasssign;
    TextView tv_duedate;
    Spinner sasssign;
    //For assignment
    ArrayList<String> assignment_retrival_arr_list = new ArrayList<>();
    ArrayAdapter<String> assignment_retrival_adapter;
    //For spinner
    ArrayAdapter<String> adapterSpinner;
    ArrayList<String> spinnerData;
    //Assignment_timestamp;
    String assignment_timestamp;

    FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDB.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        lvassignment = (ListView) findViewById(R.id.lvassignment);
        basssign = (Button) findViewById(R.id.bassign);
        tasssign = (EditText)findViewById(R.id.tassign);
        sasssign = (Spinner) findViewById(R.id.sassign);
        btimeselect = (Button) findViewById(R.id.btimeselect);
        tv_duedate = (TextView) findViewById(R.id.tv_duedate);

        Toast.makeText(this, "Retrieved", Toast.LENGTH_SHORT).show();

        //Retrieve Assignments
        get_Asssignments();
        //Get spinner data
        retrieve_spinnerData();
        //Assignment due date selector
        btimeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignment_timestamp="";
                tv_duedate.setText("");
                selectAssignmentduedate();
            }

        });
        //Push Assignments
        basssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String assignment = tasssign.getText().toString();
                String lecture_id = sasssign.getSelectedItem().toString();
                String aText = lecture_id + '$' + assignment;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                String  aTime = tv_duedate.getText().toString() + "-" + format;
                if(assignment.equals("") || aTime.equals("")) {
                    Toast.makeText(assignment.this, "Enter Assignment First!", Toast.LENGTH_LONG).show();
                }
                else {
                    mRef.child("ME18/Assignment").child(aTime).setValue(aText).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(assignment.this, "YOU ARE NOT A SU", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                tasssign.setText("");
                tv_duedate.setText("");
            }
        });
    }
    //Get spinner data
    public void retrieve_spinnerData(){
        spinnerData = new ArrayList<>();
        adapterSpinner = new ArrayAdapter<>(assignment.this, android.R.layout.simple_spinner_dropdown_item,spinnerData);
        sasssign.setAdapter(adapterSpinner);
        mRef.child("ME18/Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    spinnerData.add(item.getValue().toString());
                }
                adapterSpinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Get Assignment due date
    public  void selectAssignmentduedate(){
        //----
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                assignment_timestamp += hourOfDay + " : " +  minute ;
                tv_duedate.setText(assignment_timestamp);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();

        //----
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                assignment_timestamp += date + "-" + month + "-" + year + " ";
                tv_duedate.setText(assignment_timestamp);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }
    //Get Assignment data
    public void get_Asssignments(){
        assignment_retrival_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assignment_retrival_arr_list);
        lvassignment.setAdapter(assignment_retrival_adapter);
        mRef.child("ME18/Assignment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String timestamp = snapshot.getKey();
                String data = snapshot.getValue(String.class);
                StringBuffer sb = new StringBuffer(timestamp);
                sb.delete(timestamp.length() - 20, timestamp.length());
                timestamp = sb.toString();

                data = data.replace('$', '|');

                assignment_retrival_arr_list.add(timestamp+" | "+data);
                assignment_retrival_adapter.notifyDataSetChanged();
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