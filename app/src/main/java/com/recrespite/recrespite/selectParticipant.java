    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Spinner;

    import java.util.ArrayList;
    import java.util.List;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class populates the drop down list for all the participants in user profile
     * if it is empty user is asked if he wants to add a new participant
     */
    public class selectParticipant extends AppCompatActivity {
    Spinner spin;
        Context context=this;
        final List<String> list=new ArrayList<String>();
        private static final String LOG_TAG = "ExampleApp";
        participantInfo info;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_participant);
            info = new participantInfo();
            for (int i = 0; i < info.getParticipant().size(); i++) {
                list.add(info.getParticipant().get(i).get("participant"));
            }
            if (list.isEmpty()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("There are no participants in your profile, to register for an event you have to add at least one participant. Do you want to add a participant?");
                builder1.setCancelable(false);


                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent(selectParticipant.this, AddParticipants.class);
                        startActivity(in);
                    }
                });
                builder1.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent(selectParticipant.this, Home.class);
                        startActivity(in);
                    }
                });


                AlertDialog alert11 = builder1.create();
                alert11.show();
                alert11.setCancelable(false);
            }

            else {
                spin = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(selectParticipant.this, R.layout.dialog_list_layout, list);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adp1);
            }


        }

        public void conti(View view) {

            String user=spin.getSelectedItem().toString();

            Intent intent= new Intent(this,Registration.class);
            intent.putExtra("part",user);


            intent.putExtra("username","mannkara");
            startActivity(intent);
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

    }
