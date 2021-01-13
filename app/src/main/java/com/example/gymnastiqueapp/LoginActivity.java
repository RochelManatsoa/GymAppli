package com.example.gymnastiqueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button LoginButton, PhoneLoginButton;
    private TextView NeedNewAccountLink, ForgetPasswordLink, LoginUsing;
    private EditText LoginEmail, LoginPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFields();
        mAuth = FirebaseAuth.getInstance();

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.redirectActivity(LoginActivity.this, RegisterActivity.class);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });
    }

    private void AllowUserToLogin() {

        String email = LoginEmail.getText().toString();
        String pass = LoginPassword.getText().toString();

        if(email.equals("") || pass.equals("")){
            Toast.makeText(this, "Veillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Identification");
            loadingBar.setMessage("Veuiller patienter...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        MainActivity.redirectActivity(LoginActivity.this, MainActivity.class);
                        Toast.makeText(LoginActivity.this, "Succès", Toast.LENGTH_SHORT).show();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Echècs : "+ message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    private void initializeFields() {
        LoginButton = (Button) findViewById(R.id.login_btn);
        PhoneLoginButton = (Button) findViewById(R.id.phone_login_btn);
        LoginEmail = (EditText) findViewById(R.id.login_email);
        LoginPassword = (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        LoginUsing = (TextView) findViewById(R.id.login_using);
        loadingBar = new ProgressDialog(this);
    }
}