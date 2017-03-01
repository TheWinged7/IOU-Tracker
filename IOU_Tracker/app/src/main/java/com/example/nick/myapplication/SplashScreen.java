package com.example.nick.myapplication;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Button b =(Button)findViewById(R.id.testButton);


        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Random R = new Random();
                        int r = R.nextInt(100-10) +10;
                        addNewRow("John", 100, r , new Date (2017, 5, 20), false);

                    }
                }

        );

        addNewRow("Jane", 100, 100, new Date (2017, 1, 5), true);


    }

    private void  addNewRow(String person, int total, int payed, Date dueDate, boolean completed)
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
        View inflated = inf.inflate(R.layout.empty_row, row);


        TextView title = (TextView) inflated.findViewById(R.id.title);
        TextView owed = (TextView) inflated.findViewById(R.id.totalOwed);
        TextView due = (TextView) inflated.findViewById(R.id.dueDate);
        CheckBox payedFor = (CheckBox) inflated.findViewById(R.id.completed);
        ProgressBar progress = (ProgressBar) inflated.findViewById(R.id.progress);

        SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yy");
        String date = form.format(Date.parse(dueDate.toString() ));


        title.setText(person);
        owed.setText(Integer.toString(total));
        due.setText( date );
        payedFor.setChecked(completed);
      //  int percent =  (int)(((double)payed/(double)total) * 100);

        progress.setProgress(0);
        progress.setMax(total);
        progress.setProgress(payed);


         toasty( Integer.toString(progress.getProgress() ) ,2);

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

}


