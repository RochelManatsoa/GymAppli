package com.example.gymnastiqueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText RegisterEmail, RegisterPassword;
    private TextView AlreadyHaveAnAccount;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference RootRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeFields();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        RootRef = database.getReference();

        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.redirectActivity(RegisterActivity.this, LoginActivity.class);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String email = RegisterEmail.getText().toString();
        String pass = RegisterPassword.getText().toString();

        if(email.equals("") || pass.equals("")){
            Toast.makeText(this, "Veillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Création du profil");
            loadingBar.setMessage("Veillez patienter...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        RootRef.child("Users").child(currentUserID).setValue("");
                        MainActivity.redirectActivity(RegisterActivity.this, MainActivity.class);
                        Toast.makeText(RegisterActivity.this, "Succès", Toast.LENGTH_SHORT).show();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Echècs : "+ message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    private void initializeFields() {
        RegisterButton = (Button) findViewById(R.id.register_btn);
        RegisterEmail = (EditText) findViewById(R.id.register_email);
        RegisterPassword = (EditText) findViewById(R.id.register_password);
        AlreadyHaveAnAccount = (TextView) findViewById(R.id.already_have_account_link);
        loadingBar = new ProgressDialog(this);
    }
}