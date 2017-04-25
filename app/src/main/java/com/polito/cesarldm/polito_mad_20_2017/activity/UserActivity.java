package com.polito.cesarldm.polito_mad_20_2017.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.objects.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LogsAndroid";
    Button logOutbt,addnamebt;
    EditText editname;
    TextView textviewmail,textviewName,textviewSpent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;
    ProgressDialog prgDial;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_);

        prgDial= new ProgressDialog(this);
        addnamebt=(Button)findViewById(R.id.buttonSetUserName);
        logOutbt=(Button)findViewById(R.id.buttonUserLogOut);
        textviewmail=(TextView)findViewById(R.id.textViewMail);
        textviewName=(TextView)findViewById(R.id.textViewName);
        textviewSpent=(TextView)findViewById(R.id.textViewSpent);
        editname=(EditText)findViewById(R.id.editTextUserName);

        mFirebaseDataBase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDataBase.getReference();
        mAuth=FirebaseAuth.getInstance();
        String name=mAuth.getCurrentUser().getEmail();

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
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        logOutbt.setOnClickListener(this);
        addnamebt.setOnClickListener(this);

    }
    private void showData(DataSnapshot dataSnapshot){
    for(DataSnapshot ds : dataSnapshot.getChildren()) {
        final DatabaseReference userRef = mFirebaseDataBase.getReference("Member");
        userRef.orderByChild("id").equalTo(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Member newMember = dataSnapshot.getValue(Member.class);
                textviewName.setText(newMember.getName());
                textviewmail.setText(newMember.getEmail());
                textviewSpent.setText(Double.toString(newMember.getSpent()));

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


    }


    }

    @Override
    public void onClick(View v) {
        if(v ==logOutbt){
            logOutUser();
        }
        if(v==addnamebt){
            updateUserinfo();

        }


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUserinfo() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Member/"+mAuth.getCurrentUser().getUid()+"/name");
        ref.setValue(editname.getText().toString().trim());


        }







    private void logOutUser() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
    }
    public void toastMessage(String st){
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();

    }
}
