package com.recrespite.recrespite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Navpreet on 2016-10-18.
 * adds the user categories for user sign up selection
 */
public class Categories extends AppCompatActivity {
    ListView categoreis;
    ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@com.RecRespite.com" });
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            intent.putExtra(Intent.EXTRA_TEXT, "mail body");
            startActivity(Intent.createChooser(intent, ""));
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void parent(View v) {
        AlertDialog.Builder b1 = new AlertDialog.Builder(this);




        final LayoutInflater inflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view=inflater.inflate(R.layout.dialog, null) ;

        categoreis=(ListView)view.findViewById(R.id.listView);
        list.add("Developmental");
        list.add("Intellectual");
        list.add("Mental Health");
        list.add("Physical");
        list.add("Chronic Illness");
        list.add("Neurological and Brain Injury");
        list.add("Mobility Impairment");
        listAdapter=new ArrayAdapter<String>(this, R.layout.dialog_list_layout,list);
        categoreis.setAdapter(listAdapter);
                                b1.setView(view);


        b1.show();
    }

    public void signUp(View view) {
        Intent intent= new Intent(this,SignUp.class);
        intent.putExtra("profileType",view.getContentDescription().toString());
        startActivity(intent);
    }
}
