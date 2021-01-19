package com.example.gymnastiqueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private RecyclerView groupRecyclerList;
    private ArrayList<String> list_of_group = new ArrayList<>();
    private DatabaseReference GroupsRef, RootRef;
    private FloatingActionButton fab_new_group;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        GroupsRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        mToolbar =  (Toolbar) findViewById(R.id.find_group_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Joindre un goupe");
    }

    @Override
    protected void onStart() {
        super.onStart();

        InitializeFields();
        RetrieveAndDisplayGroups();
        RetrieveUsersGroups();

        fab_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(GroupActivity.this, "okay", Toast.LENGTH_SHORT).show();
                RequestNewGroup();
            }
        });

    }

    private void RetrieveUsersGroups() {

    }

    private void InitializeFields() {
        //groupRecyclerList = (RecyclerView) findViewById(R.id.find_group_recycler_list);
        //groupRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_group);
        listView.setAdapter(arrayAdapter);
        fab_new_group = (FloatingActionButton) findViewById(R.id.fab_add_group);
    }


    private void RetrieveAndDisplayGroups() {
        GroupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_group.clear();
                list_of_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Nom du groupe");

        final EditText groupNameField = new EditText(this);
        groupNameField.setHint("ex: Toulouse Gym");
        builder.setView(groupNameField);

        builder.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(GroupActivity.this, "Veuillez entrer le nom du group", Toast.LENGTH_SHORT).show();
                }else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(String groupName) {
        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(GroupActivity.this, "Le groupe "+groupName+" a été créé avec succès", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}