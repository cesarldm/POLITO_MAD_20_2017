package com.polito.cesarldm.polito_mad_20_2017.adapters;

import com.polito.cesarldm.polito_mad_20_2017.*;
import com.polito.cesarldm.polito_mad_20_2017.objects.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CesarLdM on 3/4/17.
 */

public class CustomAdapter extends ArrayAdapter<Group> {

    Context context;
    ArrayList<Group> groups;


    public CustomAdapter(Context context, ArrayList<Group> grupos){
        super(context, R.layout.list_item, grupos);
        this.context=context;
        this.groups=grupos;



    }

    //INNER CLASS

    /*public Group getObject(int position){
        Group g= groups.get(position);
        return g;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int p =position;
        if (convertView==null){
            // TODO Auto-generated method stub
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_item, null);
        }
        //Otherwise
        ViewHolder holder=new ViewHolder();
        //initialite views
        holder.textview= (TextView) convertView.findViewById(R.id.editText);
        holder.textview2=(TextView) convertView.findViewById(R.id.textView2);
        holder.imageView=(ImageView) convertView.findViewById(R.id.imageButton);
        //asign them data
        holder.textview.setText(groups.get(position).getName());
        Double a =groups.get(position).getBudget();
        holder.textview2.setText(a.toString()+"€");


        /*holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view,groups.get(p).getName(),Snackbar.LENGTH_LONG).show();
            }

        });*/



        return convertView;
    }

    public class ViewHolder {

        TextView textview;
        TextView textview2;
        ImageView imageView;
    }


}
