package com.example.gymnastiqueapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymnastiqueapp.CreateGroupActivity;
import com.example.gymnastiqueapp.GroupActivity;
import com.example.gymnastiqueapp.GroupChatActivity;
import com.example.gymnastiqueapp.MainActivity;
import com.example.gymnastiqueapp.Models.Contacts;
import com.example.gymnastiqueapp.Models.Groups;
import com.example.gymnastiqueapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View groupFragmentView;
    private RecyclerView GroupRecyclerList;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_group = new ArrayList<>();
    private FloatingActionButton fab_new_group;

    private FirebaseAuth mAuth;
    private DatabaseReference GroupRef, RootRef, UserRef;
    private String currentUserName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitializeFields();
        //RetrieveAndDisplayGroups();
        RetrieveAllGroups();

        fab_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RequestNewGroup();
                Intent groupChatIntent = new Intent(getContext(), CreateGroupActivity.class);
                startActivity(groupChatIntent);
            }
        });



        return groupFragmentView;
    }

    private void RetrieveAllGroups() {
        FirebaseRecyclerOptions<Groups> options = new FirebaseRecyclerOptions.Builder<Groups>()
                .setQuery(GroupRef, Groups.class)
                .build();
        FirebaseRecyclerAdapter<Groups, GroupViewHolder> adapter =
                new FirebaseRecyclerAdapter<Groups, GroupViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull Groups model) {
                        holder.groupName.setText(model.getName());
                        holder.groupDescription.setText(model.getDescription());
                        holder.groupUser.setText(model.getUser());
                        holder.groupCreatedAt.setText(model.getDate());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "ok nama ah", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_display_layout, parent, false);
                        GroupViewHolder viewHolder = new GroupViewHolder(view);

                        return viewHolder;
                    }
                };
        GroupRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView groupName, groupDescription, groupUser, groupCreatedAt;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.group_display_name);
            groupDescription = itemView.findViewById(R.id.group_display_description);
            groupUser = itemView.findViewById(R.id.group_display_user);
            groupCreatedAt = itemView.findViewById(R.id.group_created_at);
        }
    }

    private String GetUserInfo(String userID) {
        UserRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentUserName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                currentUserName = null;
            }
        });

        return currentUserName;
    }

    private void RetrieveAndDisplayGroups() {
        GroupRef.addValueEventListener(new ValueEventListener() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle("Nom du groupe");

        final EditText groupNameField = new EditText(getContext());
        final EditText groupDescriptionField = new EditText(getContext());
        groupNameField.setHint("Nom");
        groupDescriptionField.setHint("Description");
        builder.setView(groupNameField);

        builder.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                String groupDescription = groupDescriptionField.getText().toString();
                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(getContext(), "Veuillez entrer le nom du group", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Le groupe "+groupName+" a été créé avec succès", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InitializeFields() {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
//        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_group);
//        list_view.setAdapter(arrayAdapter);
        GroupRecyclerList = (RecyclerView) groupFragmentView.findViewById(R.id.group_recycler_list);
        GroupRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        fab_new_group = (FloatingActionButton) groupFragmentView.findViewById(R.id.fab_add_group);

    }
}