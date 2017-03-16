package com.example.nick.myapplication;


import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class SplashScreen extends AppCompatActivity {

    final DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Button b =(Button)findViewById(R.id.testButton);
        final Dialog d = new Dialog(this);


        b.setOnClickListener(
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
                    /*



                        addNewRow("John", 100, r , new Date (2017, 5, 20), false);
                    */
                    }
                }

        );

      //  addNewRow("Jane", 100, 100, new Date (2017, 1, 5), true);


    }

    private void  addNewRow(String person,  double total,  double payed,
                            Date dueDate,  boolean completed)
    {

        TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
        TableRow row = new TableRow(this);
        TableLayout.LayoutParams rowLayout=new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT);
        rowLayout.setMargins(0,5,0,5);

        row.setLayoutParams(rowLayout);
        row.setId( 100 + table.getChildCount() );




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


        d.setContentView(R.layout.incriment_popup);
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

                         /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            later replace pulling from/pushing to elements
                             to use the DB to get accurate numbers
                             !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

                    /*
                        ProgressBar progress = (ProgressBar) inflated.findViewById(R.id.progress);
                        CheckBox payedFor = (CheckBox) inflated.findViewById(R.id.completed);

                        boolean completed = payedFor.isChecked();
                        int payed = progress.getProgress();
                        int total = progress.getMax();

                        if (!completed  && payed < total)
                        {

                        */
                            /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            later add a popup to select the amount payed back
                            and then increment by that number / total from DB
                             !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/


                            /*
                            payed++;
                            progress.setProgress(payed);
                        }
                        if (payed>=total)
                        {
                            payedFor.setChecked(true);
                            payedFor.setClickable(false);
                            payBack.setEnabled(false);
                        }

                        */
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



       //  toasty( Integer.toString(progress.getProgress() ) ,2);

        table.addView(row, table.getChildCount()-1);

        row.invalidate();
        table.invalidate();
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



