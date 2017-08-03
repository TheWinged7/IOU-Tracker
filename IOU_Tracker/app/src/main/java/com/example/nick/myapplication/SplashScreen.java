package com.example.nick.myapplication;


import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SplashScreen extends AppCompatActivity {

    final DBHelper DB = new DBHelper(this);

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mDrawerList = (ListView)findViewById(R.id.navList);
        String[] osArray = { "NEW IOU", "Test All" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        Button addEntryButton =(Button)findViewById(R.id.newIOUButton);
        final Dialog d = new Dialog(this);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SplashScreen.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });

        load();

        addEntryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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

                                        addNewRow(title, total, 0 , date, false);

                                        d.dismiss();
                                    }
                                }



                        );

                        d.show();

                    }
                }

        );

      //  addNewRow("Jane", 100, 100, new Date (2017, 1, 5), true);


    }

    //for new entry from scratch
    private void  addNewRow(String person,  double total,  double payed,
                            Date dueDate,  boolean completed)
    {





        TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
        TableRow row = new TableRow(this);
        TableLayout.LayoutParams rowLayout=new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT);
        rowLayout.setMargins(0,5,0,5);

        row.setLayoutParams(rowLayout);

        long id;

            DB.insertRow(person, total, payed, dueDate, completed);
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
        payedFor.setChecked(completed);

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
            foo();
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
                        Button delete = (Button)d.findViewById(R.id.deleteButton);

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
                                        editRow(  Integer.toString(row.getId()), 0, dueDate);
                                        d.dismiss();
                                    }
                                }



                        );

                        delete.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toasty(Long.toString(ID), 1);
                                        new AlertDialog.Builder( v.getContext() )
                                                .setTitle("Confirm Delete")
                                                .setMessage("Are you sure you want to DELETE this IOU?")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //replace this later with proper delete call
                                                                toasty("delete confirmed", 1);

                                                                d.dismiss();
                                                            }
                                                        })
                                                .setNegativeButton(android.R.string.no,
                                                        new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //
                                                        toasty("delete canceled", 1);
                                                        //d.dismiss();
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


    private void editRow(String ID, double payed, Date date)
    {
        toasty ("edit triggered" ,1);
    }


    private void load ()
    {

        long [] ids =DB.getAllIDs();

        for (int i=0; i<ids.length; i++)
        {
            String[] row = DB.getRowByID(ids[i]);
            String title =row[1];
            double total, payed;
            total = Double.parseDouble(row[2]);
            payed = Double.parseDouble(row[3]);
            Date due =  new Date(row[4]);
            boolean completed = false;
            if (row[5] == "true")
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



