package com.recrespite.recrespite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Navpreet on 2016-10-18.
 * this class edits the participant's information in userprofile
 */
public class EditParticipant extends AppCompatActivity {
    ListView listview;
    Context context=this;
    EditText firstname,lastname,age,program,notes;
    participantInfo info;
    String resultTrack,btnText,idOfParticipant;
    String selectedParticipant,selectedGender,SelectedDiagnosis,SelectedParticipantId;
    HashMap<String,String> participantage=new HashMap<>();
    HashMap<String,String> Beforeparticipantage=new HashMap<>();
StringEntity patchString;
    HashMap<String,String> participantInfo=new HashMap<>();
    ScrollView viewScroll;
    HashMap<String,String> beforeparticipantInfo=new HashMap<>();
    Spinner genderSpinner,spinner;
    ProgressBar progressBar;
    final List<String> list=new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_participant);
        info= new participantInfo();
        listview=(ListView) findViewById(R.id.participantList);
        for (int i=0;i<info.getParticipant().size();i++){
            list.add(info.getParticipant().get(i).get("participant"));}

        if(list.isEmpty()){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("There are no participants added in your profile,Do you want to add a participant?");
            builder1.setCancelable(false);


            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(EditParticipant.this,AddParticipants.class);
                    startActivity(in);
                }
            });
            builder1.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(EditParticipant.this,Profile.class);
                    startActivity(in);
                }
            });




            AlertDialog alert11 = builder1.create();
            alert11.show();
            alert11.setCancelable(false);}
        else {
            listAdapter = new ArrayAdapter<String>(context, R.layout.participantlist_layout, list);
            listview.setAdapter(listAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    selectedParticipant = listview.getItemAtPosition(position).toString();
                    Toast.makeText(context, selectedParticipant, Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.edit_participant_fragment);


                    SelectedParticipantId = String.valueOf(position);
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    spinner = (Spinner) findViewById(R.id.spinnerDiagnosis);
                    genderSpinner = (Spinner) findViewById(R.id.spinnerGender);
                    firstname = (EditText) findViewById(R.id.etFirstName);
                    lastname = (EditText) findViewById(R.id.etLastName);
                    age = (EditText) findViewById(R.id.etAge);
                    program = (EditText) findViewById(R.id.etProgramInterest);
                    notes = (EditText) findViewById(R.id.etNotes);
                    info = new participantInfo();
                    // set current progress
                    progressBar.setVisibility(View.VISIBLE);

                    new getDiagnosis().execute();
                    new getGenderAndDiagnosis().execute();
                    notes.setText(info.getNotes());
                    program.setText(info.getProgramOfInterest());


                    idOfParticipant = String.valueOf(position);

                    for (int i = 0; i < info.getParticipant().size(); i++) {

                        if ((selectedParticipant.equalsIgnoreCase(info.getParticipant().get(i).get("participant")))) {
                            String fname = info.getParticipant().get(i).get("participant");

                            firstname.setText(fname);
                            age.setText(info.getParticipantAge().get(i).get(firstname.getText().toString()));

                        }

                        // progressBar.setVisibility(View.INVISIBLE);
                    }


                }


            });
        }
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
    public void EditPart(View view) {



        try {
            patchString = new StringEntity("{\"age\":\""+age.getText().toString()+"\",\"diagnosis\":\""+spinner.getSelectedItem().toString()+"\",\"firstname\":\""+firstname.getText().toString()+"\",\"gender\":\""+genderSpinner.getSelectedItem().toString()+"\",\"lastname\":\""+lastname.getText().toString()+"\",\"notes\":\""+notes.getText().toString()+"\",\"program\":\""+program.getText().toString()+"\"}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new editParticipant().execute();
    }

    public class getDiagnosis extends AsyncTask {

        ArrayList<String> diagnosisArray = new ArrayList<String>();

        @Override
        protected void onPreExecute() {

        }

        protected Object doInBackground(Object[] params) {

            try {
                JSONParser jParser = new JSONParser(); // get JSON data fro
                //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/diagnosis/diagnosis.json");

                Log.d("array", String.valueOf(json1));

                //loadData(json1);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            loadData(json1);
                        } catch (JSONException e) {

                            Log.e("JSON not processed", "Error processing JSON", e);
                        }
                    }
                });
            }

            catch (IOException e) {
                e.printStackTrace();
            }  finally {

            }
            return null;
        }


        public void loadData(String json) throws JSONException {

            List<String> diagnosisList = new ArrayList<String>();


            if (json == null) {
                Log.v("TAG JSON", "Json was empty");
            }
            else{
                // Log.v("TAG JSON", "Json was not empty");
                try {

                    JSONArray jsonArray = new JSONArray(json);
                    // Log.v("LOG JSON ARRAY", jsonArray.getString(2));

                    JSONObject jsonObject = new JSONObject();

                    for(int i=0; i<jsonArray.length(); i++) {
                        // jsonObject = jsonArray.getJSONObject(i);
                        diagnosisList.add(jsonArray.getJSONObject(i).getString("name").toString().trim());

                    }
                    int inee = jsonArray.length();

                    Log.v("Array length", "length is: "+ inee);


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,diagnosisList);


                    spinner.setAdapter(adapter);

                    //Log.d("LOG JSON", jsonObject.getString("name"));

                } finally {

                }



            }


        }


    }
    public class getGenderAndDiagnosis extends AsyncTask {



        @Override
        protected void onPreExecute() {

        }

        protected Object doInBackground(Object[] params) {

            try {
                JSONParser jParser = new JSONParser(); // get JSON data fro
                //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/UserInformation/"+info.getUsername()+"/participants/"+SelectedParticipantId+".json");

                Log.d("array", String.valueOf(json1));

                //loadData(json1);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            loadData(json1);
                        } catch (JSONException e) {

                            Log.e("JSON not processed", "Error processing JSON", e);
                        }
                    }
                });
            }

            catch (IOException e) {
                e.printStackTrace();
            }  finally {

            }
            return null;
        }


        public void loadData(String json) throws JSONException {

           JSONObject ob= new JSONObject(json);
lastname.setText(ob.getString("lastname"));
            age.setText(ob.getString("age"));
            notes.setText(ob.getString("notes"));
            program.setText("program");

            for(int i=0;i<genderSpinner.getCount();i++){

                if(String.valueOf(genderSpinner.getItemAtPosition(i)).equalsIgnoreCase(ob.getString("gender"))){
                    genderSpinner.setSelection(i);
                }
            }
            for(int i=0;i<spinner.getCount();i++){
             //   Toast.makeText(EditParticipant.this,ob.getString("diagnosis"),Toast.LENGTH_SHORT).show();
                if(String.valueOf(spinner.getItemAtPosition(i)).equalsIgnoreCase(ob.getString("diagnosis"))){
                    spinner.setSelection(i);
                }
            }

            progressBar.setVisibility(View.INVISIBLE);
            }





    }



    private class editParticipant extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String patchUrl = "https://recrespite-3c13b.firebaseio.com/UserInformation/"+info.getUsername()+"/participants/"+SelectedParticipantId+".json";

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPatch patch = new HttpPatch(patchUrl);
                patch.setHeader("Content-type", "application/json");
                patch.setEntity(patchString);

                HttpResponse response = httpClient.execute(patch);
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                    Log.d("resultasdhuiahduia", String.valueOf(result));
                    resultTrack = String.valueOf(result);
                }
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultTrack;

        }


        @Override
        protected void onPostExecute(String result1) {


            beforeparticipantInfo.put("participant", firstname.getText().toString());
            Beforeparticipantage.put(firstname.getText().toString(), age.getText().toString());
            if(notes.getText().toString()!=null){
            info.setNotes(notes.getText().toString());}
            if(program.getText().toString()!=null) {
                info.setProgramOfInterest(program.getText().toString());
            }
            Toast.makeText(context,"Changes Saved",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(context,MyProfile.class);
            startActivity(intent);



        }
    }

}
