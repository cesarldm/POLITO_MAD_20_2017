package com.polito.cesarldm.polito_mad_20_2017.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.polito.cesarldm.polito_mad_20_2017.R;
import com.polito.cesarldm.polito_mad_20_2017.fragments.GroupHomeFragment;
import com.polito.cesarldm.polito_mad_20_2017.objects.Expense;
import com.polito.cesarldm.polito_mad_20_2017.objects.Member;

import java.util.ArrayList;

/**
 * Created by CesarLdM on 8/5/17.
 */

public class ExpenseArrayAdapter extends ArrayAdapter<Expense> {
    Context context;
    ArrayList<Expense> expenseList;


    public ExpenseArrayAdapter(Context context, ArrayList<Expense> expenseList){
        super(context, R.layout.expense_list_item, expenseList );
        this.context=context;
        this.expenseList=expenseList;



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int p =position;
        if (convertView==null){
            // TODO Auto-generated method stub
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.expense_list_item, null);
        }
        //Otherwise
        ExpenseArrayAdapter.ViewHolder holder=new ExpenseArrayAdapter.ViewHolder();
        //initialite views
        holder.expenseName= (TextView) convertView.findViewById(R.id.tv_expense_name);
        holder.expenseUser=(TextView) convertView.findViewById(R.id.tv_user_name);
        holder.expenseDate=(TextView) convertView.findViewById(R.id.tv_expense_date);
        //asign them data
        holder.expenseName.setText(expenseList.get(position).getName());
        holder.expenseUser.setText(expenseList.get(position).getWho());
        holder.expenseDate.setText(expenseList.get(position).getDate());


        return convertView;
    }

    public class ViewHolder {

        TextView expenseName;
        TextView expenseUser;
        TextView expenseDate;
    }


}
