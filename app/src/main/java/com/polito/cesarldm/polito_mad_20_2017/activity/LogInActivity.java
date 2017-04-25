package com.polito.cesarldm.polito_mad_20_2017.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.objects.Member;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="LogInActivity";
    Button regButton;
    Button logButton;
    EditText email;
    EditText password;
    private ProgressDialog prgDial;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebasedataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        prgDial= new ProgressDialog(this);
        prgDial.setMessage("Loading, plese wait");

        firebaseAuth=FirebaseAuth.getInstance();
        mFirebasedataBase=FirebaseDatabase.getInstance();
        myRef=mFirebasedataBase.getReference().child("Member");


        regButton=(Button) findViewById(R.id.RegButton);
        logButton=(Button) findViewById(R.id.LogButton);
        email=(EditText) findViewById(R.id.EDemail);
        password=(EditText) findViewById(R.id.EDpassword);
        regButton.setOnClickListener(this);
        logButton.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Signed In! :"+user.getEmail());
                    prgDial.dismiss();
                    gotoNextActivity();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Signed Out!");
                }
                // ...
            }
        };






    }

    @Override
    public void onClick(View v) {
        if(v ==regButton){
            registerUser();
        }
        if(v ==logButton){
            logInUser();
        }

    }

    private void logInUser() {
        String mail=email.getText().toString().trim();
        String pswd=password.getText().toString().trim();
        if(TextUtils.isEmpty(mail)){
            //empty mail
            Toast.makeText(this,"Please enter valid e-mail",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pswd)){
            //empty password
            Toast.makeText(this,"Please enter valid Password",Toast.LENGTH_LONG).show();
            return;
        }

        prgDial.show();
        firebaseAuth.signInWithEmailAndPassword(mail, pswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            toastMessage("Sign In Failed");
                            prgDial.dismiss();;
                        }else{

                        }

                        // ...
                    }
                });
    }

    private void registerUser() {
        String mail=email.getText().toString().trim();
        String pswd=password.getText().toString().trim();
        if(TextUtils.isEmpty(mail)){
            //empty mail
            Toast.makeText(this,"Please enter valid e-mail",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pswd)){
            //empty password
            Toast.makeText(this,"Please enter valid Password",Toast.LENGTH_LONG).show();
            return;
        }
        prgDial.setMessage("Registering, plese wait");
        prgDial.show();
        firebaseAuth.createUserWithEmailAndPassword(mail, pswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            toastMessage("Authentication Failed");
                            prgDial.dismiss();;
                        }else{
                            insertMember();


                        }

                        // ...
                    }
                });

    }

    private void insertMember() {
        FirebaseUser mUser=firebaseAuth.getCurrentUser();
        Member newMember=new Member(mUser.getUid(),mUser.getEmail());
        myRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(newMember);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void toastMessage(String st){
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();

    }

    public void gotoNextActivity(){
        finish();
        startActivity(new Intent(getApplicationContext(), GroupListActivity.class));
    }
}
