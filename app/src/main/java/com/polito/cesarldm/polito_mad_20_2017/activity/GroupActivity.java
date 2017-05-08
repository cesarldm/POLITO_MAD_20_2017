package com.polito.cesarldm.polito_mad_20_2017.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.fragments.GroupHomeFragment;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {
    private String gId;
    private FragmentManager mFragmentManager;
    private Bundle mSavedInstanceState;
    private GroupHomeFragment groupHomeFragment;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction fragmentTransaction=mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_GA_fragment_container,groupHomeFragment).commit();

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        gId = myIntent.getStringExtra("Id");
        mFragmentManager=getSupportFragmentManager();
       groupHomeFragment= new GroupHomeFragment();
        groupHomeFragment.setGId(gId);
        setContentView(R.layout.activity_group);




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
