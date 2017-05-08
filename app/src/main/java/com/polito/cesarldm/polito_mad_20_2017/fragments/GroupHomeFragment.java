package com.polito.cesarldm.polito_mad_20_2017.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ListFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.activity.GroupActivity;
import com.polito.cesarldm.polito_mad_20_2017.activity.GroupListActivity;
import com.polito.cesarldm.polito_mad_20_2017.activity.LogInActivity;
import com.polito.cesarldm.polito_mad_20_2017.adapters.CustomAdapter;
import com.polito.cesarldm.polito_mad_20_2017.adapters.ExpenseArrayAdapter;
import com.polito.cesarldm.polito_mad_20_2017.objects.Expense;
import com.polito.cesarldm.polito_mad_20_2017.objects.Group;
import com.polito.cesarldm.polito_mad_20_2017.objects.Member;

import java.util.ArrayList;

/**
 * Created by CesarLdM on 6/5/17.
 */

public class GroupHomeFragment extends Fragment implements View.OnClickListener {
    private String TAG ="GroupHomeFragment";
    private String newId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    public String gId;
    private ArrayList<Expense> expenseList=new ArrayList<Expense>();
    private ArrayList<String>expenseId=new ArrayList<String>();
    ArrayList<String> tempExpenseList=new ArrayList<String>();
    boolean expenseAdded=false;

    View view;
    Button btnAddExp;
    ListView lvExpenses;
    EditText etName,etAmount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fragment_group_home,container, false);
        btnAddExp=(Button) view.findViewById(R.id.btn_fr_GAH_add_expense);
        lvExpenses=(ListView) view.findViewById(R.id.lv_fr_GAH_expense_list);
        etName=(EditText)view.findViewById(R.id.et_fr_GH_expense_name);
        etAmount=(EditText)view.findViewById(R.id.et_fr_GH_add_amount);
        btnAddExp.setOnClickListener(this);



        return view;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        mUser=firebaseAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
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

                }
                // ...
            }
        };
        final DatabaseReference databaseRefToda = mDatabase.getReference();
        databaseRefToda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateExpenseList(dataSnapshot);
                if(expenseAdded==true){
                    getUserExpenseList(dataSnapshot);
                    getGroupExpenseList(dataSnapshot);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //display error message
            }

        });



    }

    private void getGroupExpenseList(DataSnapshot dataSnapshot) {
        tempExpenseList.clear();
        Group newGroup=dataSnapshot.child("Group").child(gId).getValue(Group.class);
        if(newGroup.getExpenseList()==null){
            tempExpenseList.add(newId);
        }else {
            tempExpenseList = newGroup.getExpenseList();
            tempExpenseList.add(newId);
        }
        updateGroupInfo(tempExpenseList,gId);
        expenseAdded=false;


    }


    private void getUserExpenseList(DataSnapshot dataSnapshot) {
        tempExpenseList.clear();
            String userId=mUser.getUid();
            Member newMember= dataSnapshot.child("Member").child(userId).getValue(Member.class);
            if(newMember.getExpensesList()==null){
                tempExpenseList.add(newId);
            }else {
                tempExpenseList = newMember.getGroupList();
                tempExpenseList.add(newId);
            }
            updateUserInfo(tempExpenseList,userId);


    }
    public void updateUserInfo(ArrayList<String> updatedExpenseList, String userId){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Member/"+userId+"/expenseList");
        ref.setValue(updatedExpenseList);
    }
    public void updateGroupInfo(ArrayList<String> updatedExpenseList, String userId){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Group/"+userId+"/expenseList");
        ref.setValue(updatedExpenseList);
    }


    private void addExpense() {
        String name=etName.getText().toString().trim();
        double amount=Double.parseDouble(etAmount.getText().toString().trim());
        DatabaseReference expenseRef=mDatabase.getReference("Expense");
        newId=expenseRef.push().getKey().toString().trim();
        Expense newExpense=new Expense(name,amount,mUser.getEmail(),newId);
        expenseRef.child(newId).setValue(newExpense);
        expenseAdded=true;
    }

    private void populateExpenseList(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> children = dataSnapshot.child("Group").child(gId).child("expenseList").getChildren();
        //clean arrays not to duplicate list
        expenseId.clear();
        expenseList.clear();
        if (children != null) {
            for (DataSnapshot child : children) {
                String newExpenseId = child.getValue(String.class);
                expenseId.add(newExpenseId);
            }
            Iterable<DataSnapshot> children2 = dataSnapshot.child("Expense").getChildren();
            if (children2 != null) {
                for (DataSnapshot child : children2) {
                    Expense newExpense = child.getValue(Expense.class);
                    for(int i=0;i<expenseId.size();i++){
                        if(newExpense.getId().equals(expenseId.get(i))){
                            expenseList.add(newExpense);
                        }
                    }
                }
            }
            //fill list view
            ExpenseArrayAdapter adapter = new ExpenseArrayAdapter(this.getContext(), expenseList);
            lvExpenses.setAdapter(adapter);
        }
    }

    @Override
    public void onResume(){
        super.onResume();



    }

    public void toastMessage(String st){
        Toast.makeText(this.getContext(),st,Toast.LENGTH_SHORT).show();

    }
    private void goToLoginActivity() {
        startActivity(new Intent(this.getContext(), LogInActivity.class));
    }

    public void setGId(String st){
        this.gId=st;
    }

    @Override
    public void onClick(View v) {
        if (v==btnAddExp){
            addExpense();
        }
    }
}
