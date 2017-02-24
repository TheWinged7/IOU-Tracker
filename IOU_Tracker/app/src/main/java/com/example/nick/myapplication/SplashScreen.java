package com.example.nick.myapplication;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


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
                        addDummyTask();
                    }
                }

        );


    }

 private void addDummyTask() //(int payed, int owed)
 {
   //  foo();
     TableLayout table = (TableLayout) findViewById(R.id.IOUTable);
     TableRow row = new TableRow(this);
     GridLayout grid = new GridLayout(this);
     TextView title = new TextView(this);
     CheckBox completed = new CheckBox(this);
     TextView payedBack = new TextView(this);
     TextView due = new TextView(this);

     row.setLayoutParams(new TableRow.LayoutParams(  TableRow.LayoutParams.MATCH_PARENT,
             TableRow.LayoutParams.WRAP_CONTENT)  );
     row.setDividerPadding(2);
     row.setBackgroundColor( getResources().getColor(R.color.light_grey) );


     TableRow.LayoutParams gridParams = new TableRow.LayoutParams();
     gridParams.height=TableRow.LayoutParams.WRAP_CONTENT;
     gridParams.width=TableRow.LayoutParams.WRAP_CONTENT;

     grid.setColumnCount(2);
     grid.setRowCount(2);
     grid.setPadding(0,2,2,2);
     grid.setLayoutParams(gridParams);


     TableRow.LayoutParams titleParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
             TableRow.LayoutParams.WRAP_CONTENT );

     title.setText("Title/Person");
     title.setTextAppearance(this, android.R.style.TextAppearance_Large);
     title.setLayoutParams(titleParams);
     title.setPadding(0,5,5,5);

     getResources().getDisplayMetrics();

     TableRow.LayoutParams compParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
             TableRow.LayoutParams.WRAP_CONTENT );


     completed.setLayoutParams(compParams);
     completed.setPadding(5,20,0,0);


     TableRow.LayoutParams payedParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
             TableRow.LayoutParams.WRAP_CONTENT );

     payedBack.setText("$Pay/Owe  (per%)");
    // title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
     payedBack.setLayoutParams(payedParams);
     payedBack.setPadding(0,10,0,0);


     /*
     payedBack.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
             TableRow.LayoutParams.WRAP_CONTENT ));
*/


     TableRow.LayoutParams dueParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
             TableRow.LayoutParams.WRAP_CONTENT );


     due.setText("<DATE>");
     due.setLayoutParams(dueParams);
     due.setPadding(5,10,0,0);


     row.addView(grid);
     grid.addView(title, 0);
     grid.addView(due, 1);
     grid.addView(payedBack, 2);
     grid.addView(completed, 3);



     table.addView(row, table.getChildCount() -1);


     table.invalidate();

     findViewById(R.id.tasksScroll).invalidate();

    // toasty(Integer.toString(table.getChildCount()), 2 );


 }

 private void foo()
 {
     Toast.makeText(getBaseContext(), "Button works", Toast.LENGTH_LONG).show();
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


