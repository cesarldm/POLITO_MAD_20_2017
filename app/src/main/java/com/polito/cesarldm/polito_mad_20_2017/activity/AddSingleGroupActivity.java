package com.polito.cesarldm.polito_mad_20_2017.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.adapters.MemberArrayAdapter;
import com.polito.cesarldm.polito_mad_20_2017.objects.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class AddSingleGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddSingleGroupActivity";
    EditText budget, nameGroup;
    ListView lv;
    Button doneBtn;
    String saveName;
    double saveBudget;
    Group newGroup;
    String text1 = "Introduce a valid budget", text2 = "Introduce a valid name";
    int duration = Toast.LENGTH_SHORT;
    ArrayList<Member> memberList = new ArrayList<Member>();
    ArrayList<Member>List =new ArrayList<Member>();
    ArrayList<String> selectedUsers= new ArrayList<String>();
    String newId;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_group);

        nameGroup = (EditText) findViewById(R.id.name);
        budget = (EditText) findViewById(R.id.budget);
        doneBtn = (Button) findViewById(R.id.donebtn);
        doneBtn.setOnClickListener(this);
        lv=(ListView)findViewById(R.id.listViewUser);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDataBase.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                memberList=populateUserList(dataSnapshot);
                MemberArrayAdapter adapter= new MemberArrayAdapter(AddSingleGroupActivity.this,memberList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll = (LinearLayout) view; // get the parent layout view
                CheckedTextView tv = (CheckedTextView) ll.findViewById(R.id.textViewUserMail); // get the child text view
                final String selectedItem = tv.getText().toString();
                tv.setCheckMarkDrawable(R.drawable.ic_check_box_white_24dp);
                if(selectedUsers.contains(selectedItem)){
                    selectedUsers.remove(selectedItem);
                    tv.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_white_24dp);
                } else selectedUsers.add(selectedItem);

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == doneBtn) {
            saveName = nameGroup.getText().toString().trim();


                if (budget.getText().toString().length() == 0) {
                    toastMessage("Add Budget");


            } else if(nameGroup.getText().toString().length()==0){
                    toastMessage("Add nameGroup");

                } else {
                if ((budget.getText().toString().length() != 0) && (nameGroup.getText().toString().length() != 0)) {
                    DatabaseReference groupRef=mFirebaseDataBase.getReference("Group");
                    newId=groupRef.push().getKey();
                    updateUserInfo(newId);
                    showSelected();
                    double selectedBudget= Double.parseDouble(budget.getText().toString());

                    String selectedName=nameGroup.getText().toString().trim();
                    Group newGroup=new Group(selectedName,selectedBudget,newId,selectedUsers);
                    groupRef.child(newId).setValue(newGroup);
                    toastMessage("Group Added!");



                }

            }
        }

    }

    public ArrayList<Member> populateUserList(DataSnapshot dataSnapshot) {
        List.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
             DatabaseReference userRef = mFirebaseDataBase.getReference("Member");
            userRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Member newMember = dataSnapshot.getValue(Member.class);
                    List.add(newMember);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        return List;
    }

    public void toastMessage(String st){
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();

    }

    public void showSelected() {
        String items = "";
        for (String item : selectedUsers){
            items="-"+item+"\n";
            toastMessage("You Have selected:  "+"\n"+items);
        }

    }

    public void updateUserInfo(String newId){
        final String udtusrId=newId;
        int size=selectedUsers.size();
        final DatabaseReference updateMemberRef=mFirebaseDataBase.getReference("Member");

        do{
            String a=selectedUsers.get(size-1);
            updateMemberRef.orderByChild("email").equalTo(a).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Member updatedMember = dataSnapshot.getValue(Member.class);
                    updatedMember.addGroup(udtusrId);
                    updateMemberRef.setValue(updatedMember);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

                // ...
            });

            size--;


        }while(size!=0);

        toastMessage("exit loop do while");
    }

    }


