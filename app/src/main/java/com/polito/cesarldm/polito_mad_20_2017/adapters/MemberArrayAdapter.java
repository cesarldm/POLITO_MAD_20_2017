package com.polito.cesarldm.polito_mad_20_2017.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.objects.Group;
import com.polito.cesarldm.polito_mad_20_2017.objects.Member;

import java.util.ArrayList;

/**
 * Created by CesarLdM on 23/4/17.
 */

public class MemberArrayAdapter extends ArrayAdapter<Member> {

    Context context;
    ArrayList<Member> memberList;


    public MemberArrayAdapter(Context context, ArrayList<Member> memberList){
        super(context, R.layout.user_list_item, memberList );
        this.context=context;
        this.memberList=memberList;



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int p =position;
        if (convertView==null){
            // TODO Auto-generated method stub
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.user_list_item, null);
        }
        //Otherwise
        MemberArrayAdapter.ViewHolder holder=new MemberArrayAdapter.ViewHolder();
        //initialite views
        holder.textview= (TextView) convertView.findViewById(R.id.textViewUserId);
        holder.textview2=(TextView) convertView.findViewById(R.id.textViewUserMail);
        //asign them data
        holder.textview.setText(memberList.get(position).getId());
        holder.textview2.setText(memberList.get(position).getEmail());

        return convertView;
    }

    public class ViewHolder {

        TextView textview;
        TextView textview2;
    }

}

