package com.example.gymnastiqueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText groupName, groupDescription;
    private Button createGroupBtn;
    private TextView myGroups;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, UserRef, GroupNameKeyRef;
    private String currentUserID, currentDate, currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        RootRef = FirebaseDatabase.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        InitializeFields();
        GetUserInfo();

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewGroup();
            }
        });
    }

    private void GetUserInfo() {
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentUserName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CreateNewGroup() {
        String group_name = groupName.getText().toString();
        String group_description = groupDescription.getText().toString();
        String messageKey = RootRef.push().getKey();
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd MMM yyyy");
        currentDate = currentDateFormat.format(calForDate.getTime());

        if (group_name.equals("")){
            Toast.makeText(CreateGroupActivity.this, "Vous devez donner un nom au groupe", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("user", currentUserName);
            profileMap.put("name", group_name);
            profileMap.put("description", group_description);
            profileMap.put("date", currentDate);

            RootRef.child("Group").child(group_name).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                MainActivity.redirectActivity(CreateGroupActivity.this, MainActivity.class);
                                //Toast.makeText(CreateGroupActivity.this, "Le profil est mis à jour", Toast.LENGTH_SHORT).show();
                                UserRef.child(currentUserID).child("Groups").setValue(group_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //Toast.makeText(CreateGroupActivity.this, "okok", Toast.LENGTH_SHORT).show();
                                            RootRef.child("Messages").child(group_name).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CreateGroupActivity.this, "Le groupe "+group_name+" a été créé avec succès", Toast.LENGTH_SHORT).show();
                                                        RootRef.child("Membres").child(currentUserID).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(CreateGroupActivity.this, "Le groupe "+group_name+" a été créé avec succès", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }else{
                                String message = task.getException().toString();
                                Toast.makeText(CreateGroupActivity.this, "Echècs : "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void InitializeFields() {
        groupName = (EditText) findViewById(R.id.group_name);
        groupDescription = (EditText) findViewById(R.id.group_description);
        createGroupBtn = (Button) findViewById(R.id.create_group_btn);
        myGroups = (TextView) findViewById(R.id.my_groups);
    }
}