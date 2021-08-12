package com.link.connectsu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailId;
    private EditText passWord;
    private Button signIn;
    private Button signUp;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting Info
        emailId = (EditText)findViewById(R.id.emailid);
        passWord = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.signin);
        signUp = (Button)findViewById(R.id.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        //Checks if user is already Logged in Or Not !!!
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            finish();
            Intent intent = new Intent(MainActivity.this,lecture_status.class);
            startActivity(intent);
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_process((emailId.getText().toString()), passWord.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_process(emailId.getText().toString(), passWord.getText().toString());
            }
        });
    }

    //SignIn
    private void signin_process(String email, String pass){

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,lecture_status.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"LogIn Failed !",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(MainActivity.this,lecture_status.class);
                    //startActivity(intent);
                }

            }
        });
    }
    //SignUp
    private void signup_process(String email, String pass){
        //Toast.makeText(MainActivity.this, email, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, pass, Toast.LENGTH_SHORT).show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registration Successful ! Please Verify ", Toast.LENGTH_LONG).show();
                                emailId.setText("");
                                passWord.setText("");
                            }
                            else {
                                Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}