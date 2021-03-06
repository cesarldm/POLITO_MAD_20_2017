package com.polito.cesarldm.polito_mad_20_2017.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.adapters.CustomAdapter;
import com.polito.cesarldm.polito_mad_20_2017.objects.Group;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="GroupListActivity";
    ArrayList<String> groupids=new ArrayList<String>();
    ArrayList<Group> groupList= new ArrayList<Group>();
    ListView lvGroup;
    FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;

    Toolbar menu1;
    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        firebaseAuth=FirebaseAuth.getInstance();
        checkUserAuth();
        //String uId=mUser.getUid();
        menu1=(Toolbar)findViewById(R.id.toolbar3);
        menu1.setTitle("My Groups");
        mDatabase=FirebaseDatabase.getInstance();
        lvGroup=(ListView)findViewById(R.id.lvGROUPLIST);
        setSupportActionBar(menu1);
        if(firebaseAuth==null){
            goToLoginActivity();
        }
        //++++++++++++++
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Signed In! :"+user.getEmail());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Signed Out!");
                    goToLoginActivity();
                    finish();
                }
                // ...
            }
        };
       // DatabaseReference databaseOwnMember= mDatabase.getReference("Member/"+uId);
        DatabaseReference databaseRefToda = mDatabase.getReference();
        databaseRefToda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.child("Member").child(mUser.getUid()).child("groupList").getChildren();
                //clean arrays not to duplicate list
                groupids.clear();
                groupList.clear();
                if (children != null) {
                    for (DataSnapshot child : children) {
                        String newGroupId = child.getValue(String.class);
                        groupids.add(newGroupId);
                    }
                    Iterable<DataSnapshot> children2 = dataSnapshot.child("Group").getChildren();
                    ArrayList<Group>tempGroupList=new ArrayList<Group>();
                    if (children2 != null) {
                        for (DataSnapshot child : children2) {
                            Group newGroup = child.getValue(Group.class);
                            for(int i=0;i<groupids.size();i++){
                                if(newGroup.getId().equals(groupids.get(i))){
                                    tempGroupList.add(newGroup);
                                    groupList.add(newGroup);
                                }
                            }
                        }
                    }
                    //fill list view
                    CustomAdapter adapter = new CustomAdapter(GroupListActivity.this, tempGroupList);
                    lvGroup.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //display error message
            }

        });




        fab=(FloatingActionButton)findViewById(R.id.fabADDGROUP);
        fab.setOnClickListener(this);


    }
    @Override
    protected void onResume() {
        super.onResume();
        lvGroup.setOnItemClickListener(new OnGroupSelected());
    }


    private void checkUserAuth() {
        mUser=firebaseAuth.getCurrentUser();
        if(mUser==null){
            goToLoginActivity();

        }
        return;

    }

    public ArrayList<Group> getGroups(final ArrayList<String> groupids){
        final ArrayList<Group> tempGroupList=new ArrayList<Group>();
        DatabaseReference databaseGroupRef=mDatabase.getReference("Group");
        databaseGroupRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if (children != null) {
                    for (DataSnapshot child : children) {
                        Group newGroup = child.getValue(Group.class);
                        for(int i=0;i<groupids.size();i++){
                            if(newGroup.getId()==groupids.get(i)){
                                tempGroupList.add(newGroup);
                            }
                        }
                    }
                /*for (Group sortingList : groupList) {
                    groupNames.add(sortingList.getName());

                }
                */
                }
                //fill list view


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //display error message

            }
        });

        return tempGroupList;
    }

    private void goToLoginActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
    }
    private void goToAddSingleGroupActivity(){
        startActivity(new Intent(getApplicationContext(), AddSingleGroupActivity.class));

    }

    @Override
    public void onClick(View v) {
    if(v==fab){
        //Iniciar actividad de Jaen
       goToAddSingleGroupActivity();


    }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.settings){
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }
        if(item.getItemId()==R.id.settings){

        }
        return super.onOptionsItemSelected(item);
    }

    public void toastMessage(String st){
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();

    }

    class OnGroupSelected implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?>parent,View view,int position,long id){
            ViewGroup vg=(ViewGroup) view;
            String passId=groupList.get(position).getId();
            Intent groupIntent = new Intent(GroupListActivity.this, GroupActivity.class);
            groupIntent.putExtra("Id",passId);
            startActivity(groupIntent);



        }

    }
}

