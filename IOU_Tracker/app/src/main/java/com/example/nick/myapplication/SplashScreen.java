package com.example.nick.myapplication;


import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.os.Handler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class SplashScreen extends AppCompatActivity {

    final DBHelper DB = new DBHelper(this);

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_splash_screen);

        mDrawerList = (ListView)findViewById(R.id.navList);
        String[] navDrawArray = { getResources().getString(R.string.addIOUButton), "Test Functions", "Test Buttons/Fields" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawArray);
        mDrawerList.setAdapter(mAdapter);




        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // toasty("Button: " + mAdapter.getItem(position) , 1);

                mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_splash_screen);
                switch (position)
                {
                    case 0: //NEW IOU
                        newRow();
                        mDrawerLayout.closeDrawers();
                        break;

                    case 1: //Test All
                        mDrawerLayout.closeDrawers();
                        testFunctions();
                        break;

                    case 2:
                        mDrawerLayout.closeDrawers();
                        toasty("test coming soon\u2122", 0);
                        break;

                    default:
                        toasty("OOPS! item not found", 0);
                        break;
                }

            }
        });

        load();

      //  addNewRow("Jane", 100, 100, new Date (2017, 1, 5), true);


    }


    /*
    !!++!!++!!++!!++!!++!!++!!++!!++!!++
    FOR TESTING ONLY
    */

    private void testButtons()
    {

    }

    private void testFunctions()
    {
        addNewRow("foobar", 12.34, 1.23, new Date(2017, 5, 1));

        //gotta do tests for bobby tables
        addNewRow("Bobby\"); DROP TABLE IOUs;--", 12.34, 1.23, new Date(2017, 5, 1));
        addNewRow("Bobby\'); DROP TABLE IOUs;--", 12.34, 1.23, new Date(2017, 5, 1));

        (new Handler()).postDelayed(new Runnable()
        {
            @Override
            public void run() {

                ArrayList<String> IDs = getRowsByName("foobar");
                toasty("Edited entry \"foobar\"", 0);
                for (String S: IDs) {
                     editRow(S, 34.56, 12.30,new Date(2017, 5, 1) );
                }
            }
        }, 2000);

        (new Handler()).postDelayed(new Runnable()
        {
            @Override
            public void run() {
                ArrayList<String> IDs = getRowsByName("foobar");

                for (String S: IDs) {
                    deleteRow(Integer.parseInt(S));
                }

                IDs = getRowsByName("Bobby\"); DROP TABLE IOUs;--");
                deleteRow((Integer.parseInt(IDs.get(IDs.size()-1) ) ) );
                IDs = getRowsByName("Bobby\'); DROP TABLE IOUs;--");
                deleteRow((Integer.parseInt(IDs.get(IDs.size()-1) ) ) );
            }
        }, 5000);



    }

    /*
        !!++!!++!!++!!++!!++!!++!!++!!++!!++
     */

    private void newRow()
    {
        final Dialog d = new Dialog(this);;

        WindowManager.LayoutParams dLayout = new WindowManager.LayoutParams();
        dLayout.copyFrom(d.getWindow().getAttributes());
        dLayout.width = WindowManager.LayoutParams.MATCH_PARENT;
        dLayout.height = WindowManager.LayoutParams.MATCH_PARENT;


        d.setContentView(R.layout.add_popup);
        d.setTitle("Add new IOU");
        d.getWindow().setAttributes(dLayout);

        Button cancel = (Button)d.findViewById(R.id.cancelButton);
        Button confirm = (Button)d.findViewById(R.id.submitButton);
        final EditText TITLE = (EditText) d.findViewById(R.id.titleBox);
        final EditText TOTAL = (EditText) d.findViewById(R.id.owedBox);
        final DatePicker DATE = (DatePicker) d.findViewById(R.id.datePicker) ;


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.dismiss();
                    }
                }



        );

        confirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String title = TITLE.getText().toString();
                        double total = Double.parseDouble( TOTAL.getText().toString() )  ;

                        final DecimalFormat df = new DecimalFormat("$#0.00");



                        Date date = new Date(DATE.getYear() - 1900,
                                DATE.getMonth(), DATE.getDayOfMonth());

                        addNewRow(title, total, 0 , date);

                        d.dismiss();
                    }
                }



        );

        d.show();
    }

    //for new entry from scratch
    private void  addNewRow(String person,  double total,  double payed,
                            Date dueDate)
    {





        TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
        TableRow row = new TableRow(this);
        TableLayout.LayoutParams rowLayout=new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT);
        
        if (table.getChildCount() <=1){
            rowLayout.setMargins(0,20,0,5);

        }
        else{
            rowLayout.setMargins(0,5,0,5);
        }

        row.setLayoutParams(rowLayout);

        long id;

            DB.insertRow(person, total, payed, dueDate, false);
            id  = DB.getLastRowID();


        table.addView(row, table.getChildCount()-1);

        if (id!=-1) {
            row.setId((int)id);
           // toasty(Integer.toString(row.getId()), 1);
        }
        else
        {
            return;
        }


        LayoutInflater inf = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View inflated = inf.inflate(R.layout.empty_row, row);


        TextView title = (TextView) inflated.findViewById(R.id.title);
        TextView owed = (TextView) inflated.findViewById(R.id.totalOwed);
        TextView due = (TextView) inflated.findViewById(R.id.dueDate);
        CheckBox payedFor = (CheckBox) inflated.findViewById(R.id.completed);
        ProgressBar progress = (ProgressBar) inflated.findViewById(R.id.progress);
        final Button payBack = (Button) inflated.findViewById(R.id.payButton);


        final Dialog d = new Dialog(this);

        WindowManager.LayoutParams dLayout = new WindowManager.LayoutParams();
        dLayout.copyFrom(d.getWindow().getAttributes());
        dLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;


        d.setContentView(R.layout.edit_popup);
        d.setTitle("Pay back Amount:");
        d.getWindow().setAttributes(dLayout);


        payBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.show();


                        Button cancel = (Button)d.findViewById(R.id.cancelButton);
                        Button confirm = (Button)d.findViewById(R.id.submitButton);

                        cancel.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        d.dismiss();
                                    }
                                }



                        );

                        confirm.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        d.dismiss();
                                    }
                                }



                        );


                    }
                }



        );

        SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yy");
        String date = form.format(Date.parse(dueDate.toString() ));


        title.setText(person);
        owed.setText(Double.toString(total));
        due.setText( date );
        payedFor.setChecked(false);

        progress.setProgress(0);
        progress.setMax(100);



        row.invalidate();
        table.invalidate();
    }


    //for existing entry loaded from DB
    private void  addNewRow(String person, double total, double payed,
                            final Date dueDate, boolean completed, final long ID)
    {

        TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
        final TableRow row = new TableRow(this);
        TableLayout.LayoutParams rowLayout=new TableLayout.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT);

        if (table.getChildCount() <=1){
          //  foo();
            rowLayout.setMargins(0,20,0,5);

        }
        else{
            rowLayout.setMargins(0,5,0,5);
        }

            row.setLayoutParams(rowLayout);

        table.addView(row, table.getChildCount()-1);


        row.setId((int)ID);
       // toasty(Integer.toString(row.getId()), 1);




        LayoutInflater inf = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View inflated = inf.inflate(R.layout.empty_row, row);


        TextView title = (TextView) inflated.findViewById(R.id.title);
        TextView owed = (TextView) inflated.findViewById(R.id.totalOwed);
        TextView due = (TextView) inflated.findViewById(R.id.dueDate);
        CheckBox payedFor = (CheckBox) inflated.findViewById(R.id.completed);
        ProgressBar progress = (ProgressBar) inflated.findViewById(R.id.progress);
        final Button payBack = (Button) inflated.findViewById(R.id.payButton);


        final Dialog d = new Dialog(this);

        WindowManager.LayoutParams dLayout = new WindowManager.LayoutParams();
        dLayout.copyFrom(d.getWindow().getAttributes());
        dLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;


        d.setContentView(R.layout.edit_popup);
        d.setTitle("Pay back Amount:");
        d.getWindow().setAttributes(dLayout);


        payBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.show();


                        Button cancel = (Button)d.findViewById(R.id.cancelButton);
                        Button confirm = (Button)d.findViewById(R.id.submitButton);
                        final Button delete = (Button)d.findViewById(R.id.deleteButton);

                        cancel.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        d.dismiss();
                                    }
                                }



                        );

                        confirm.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       // editRow(  Integer.toString(row.getId()),0 , 0, dueDate);
                                        d.dismiss();
                                    }
                                }



                        );

                        delete.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new AlertDialog.Builder( v.getContext() )
                                                .setTitle("Confirm Delete")
                                                .setMessage("Are you sure you want to DELETE this IOU?")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //replace this later with proper delete call
                                                                deleteRow(ID);

                                                                d.dismiss();
                                                            }
                                                        })
                                                .setNegativeButton(android.R.string.no,
                                                        new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();



                                    }
                                }

                        );

                    }
                }



        );

        SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yy");
        String date = form.format(Date.parse(dueDate.toString() ));


        title.setText(person);
        owed.setText(Double.toString(total));
        due.setText( date );
        payedFor.setChecked(completed);

        progress.setProgress(0);
        progress.setMax(100);



        row.invalidate();
        table.invalidate();
    }


    private boolean editRow(String ID, double total, double payed, Date date)
    {
        boolean completed = false;
        if (payed>= total)
        {
            completed = true;
        }

        if (DB.editRow(Long.parseLong(ID), total, payed, date, completed)) {
            TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
            TableRow row = (TableRow) findViewById(Integer.parseInt(ID));
            TextView t = (TextView)((GridLayout) row.getChildAt(0)).getChildAt(2);
            t.setText(Double.toString(total));
            row.invalidate();
            table.invalidate();

            return true;
        }

        return false;
    }

    private void deleteRow(long ID)
    {
        String title = DB.getRowByID(ID).get(1);
        DB.deleteRow( ID);

        TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
        table.removeView( (TableRow)findViewById((int)ID));
        table.invalidate();

    }

    private  ArrayList<String> getRowsByName(String name)
    {
        ArrayList<String> IDs = DB.getIDsByTitle(name);

        if (IDs.size() >=0)
        {
            return IDs;
        }
        else
        {
            return  null;
        }
    }

    private void load ()
    {

        long [] ids =DB.getAllIDs();

        for (int i=0; i<ids.length; i++)
        {
            ArrayList<String> row = DB.getRowByID(ids[i]);
            String title =row.get(1);
            double total, payed;
            total = Double.parseDouble(row.get(2));
            payed = Double.parseDouble(row.get(3));
            Date due =  new Date(row.get(4));
            boolean completed = false;
            if (row.get(5) == "true")
            {
                completed= true;
            }

            addNewRow(title, total, payed, due, completed, ids[i]);
        }

    }

    private void foo()
    {
        toasty ("button works",1);
    }

    private void toasty(String s, int leng)
    {
        switch (leng)
        {
            case 1: {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                break;
            }
            case 2:
            {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                break;
            }

            default: {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        DB.close();
        super.onDestroy();

    }
}



