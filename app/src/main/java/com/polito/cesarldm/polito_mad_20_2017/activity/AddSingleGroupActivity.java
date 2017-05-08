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
import java.util.Objects;


public class AddSingleGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddSingleGroupActivity";
    EditText budget, nameGroup;
    ListView lv;
    Button doneBtn;
    String saveName;
    double saveBudget;
    Group newGroup;

    MemberArrayAdapter adapter;
    ArrayList<Member> memberList = new ArrayList<Member>();
    ArrayList<Member>List =new ArrayList<Member>();
    ArrayList<String> selectedUsers= new ArrayList<String>();

    ArrayList<String> tempGroupList=new ArrayList<String>();

    String newId;
    private FirebaseUser fUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference newMemberRef;
    private DatabaseReference myRef;
    private boolean groupAded=false;
    private boolean memberAded=true;


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

        mAuth = FirebaseAuth.getInstance();
        fUser=mAuth.getCurrentUser();
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
        newMemberRef=mFirebaseDataBase.getReference("Member");
        newMemberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(memberAded==true) {
                   populateUserList(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef = mFirebaseDataBase.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(groupAded==true){
                    getUserGroupList(dataSnapshot);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               populateSelectedList(view);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == doneBtn) {
                if (budget.getText().toString().length() == 0) {
                    budget.setText("0");
            } if(nameGroup.getText().toString().length()==0 || nameGroup.getText().toString().equals(" ")){
                    toastMessage("Add nameGroup");
                } else {
                if (nameGroup.getText().toString().length() != 0) {
                   addGroup();
                }

            }
        }
    }

    public void populateSelectedList(View view){
        LinearLayout ll = (LinearLayout) view; // get the parent layout view
        CheckedTextView tv = (CheckedTextView) ll.findViewById(R.id.textViewUserMail);
        TextView usId=(TextView)ll.findViewById(R.id.textViewUserId);// get the child text view
        final String selectedItem = usId.getText().toString();
        tv.setCheckMarkDrawable(R.drawable.ic_check_box_white_24dp);
        if(selectedUsers.contains(selectedItem)){
            selectedUsers.remove(selectedItem);
            tv.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_white_24dp);
        } else selectedUsers.add(selectedItem);



    }

    public void populateUserList(DataSnapshot dataSnapshot) {
        List.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Member newMember = ds.getValue(Member.class);
            String debugA=newMember.getId();
            String debugB=mAuth.getCurrentUser().getUid();
            if(!debugA.equals(debugB)){
                List.add(newMember); //we don't want the logged in user appear in the list, will be added automatically
            }
        }

        MemberArrayAdapter adapter= new MemberArrayAdapter(AddSingleGroupActivity.this,List);
        lv.setAdapter(adapter);

        groupAded=false;

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

   public void getUserGroupList(DataSnapshot dataSnapshot) {
       int i;
       for(i=selectedUsers.size()-1; i>=0;i--){
           tempGroupList.clear();
            String userId=selectedUsers.get(i).trim();
           Member newMember=dataSnapshot.child("Member").child(userId).getValue(Member.class);
           if(newMember.getGroupList()==null){
               tempGroupList.add(newId);
           }else {
               tempGroupList = newMember.getGroupList();
               tempGroupList.add(newId);
           }
           updateUserInfo(tempGroupList,userId);
       }
       groupAded=false;
       selectedUsers.clear();

   }

   public void addGroup(){
       DatabaseReference groupRef=mFirebaseDataBase.getReference("Group");
       newId=groupRef.push().getKey().toString().trim();
       //Aqui se llama al on create
       showSelected();
       saveName = nameGroup.getText().toString().trim();
       double selectedBudget= Double.parseDouble(budget.getText().toString());
       selectedUsers.add(mAuth.getCurrentUser().getUid());//añadimos al usuario logeado
       Group newGroup=new Group(saveName,selectedBudget,newId,selectedUsers,fUser.getUid());
       groupRef.child(newId).setValue(newGroup);
       //aqui se vuelve a llamar al ondatachange
       groupAded=true;
       toastMessage("Group Added!");
   }


    public void updateUserInfo(ArrayList<String> updatedGroupList, String userId){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Member/"+userId+"/groupList");
        ref.setValue(updatedGroupList);
    }

    }


