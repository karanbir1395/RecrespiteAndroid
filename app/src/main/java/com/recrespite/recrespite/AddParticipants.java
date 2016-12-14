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
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.RelativeLayout;
    import android.widget.Spinner;

    import org.apache.http.HttpResponse;
    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpPut;
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
     * this class adds Participants to the selected event and registers them for the event
     */
    public class AddParticipants extends AppCompatActivity {
    EditText firstname,lastname,age,program,notes;
        participantInfo info;
        Context context=this;
        String resultTrack,btnText,idOfParticipant;

        Button btnaddpart;
        int numOfParticipant;
        StringEntity postString;
        HashMap<String,String> participantage=new HashMap<>();
        HashMap<String,String> Beforeparticipantage=new HashMap<>();

        HashMap<String,String> participantInfo=new HashMap<>();
        HashMap<String,String> beforeparticipantInfo=new HashMap<>();
        Spinner genderSpinner,spinner;
        RelativeLayout add;

        @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_add_participants);
            spinner=(Spinner) findViewById(R.id.spinnerDiagnosis);

            btnaddpart=(Button) findViewById(R.id.buttonPart);
            genderSpinner = (Spinner) findViewById(R.id.spinnerGender);
            firstname = (EditText) findViewById(R.id.etFirstName);
            lastname = (EditText) findViewById(R.id.etLastName);
            age = (EditText) findViewById(R.id.etAge);
            program = (EditText) findViewById(R.id.etProgramInterest);
            notes = (EditText) findViewById(R.id.etNotes);
            info = new participantInfo();
            new getDiagnosis().execute();


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
        public void home(View view) {
    if(btnaddpart.getText().toString().equalsIgnoreCase("Add Participant")){

        if(firstname.getText().toString()==""){
            firstname.setError("first name is required");
        }
        if(age.getText().toString()==""){
           age.setError("age is required");
        }
        else {
            new getParticipantNum().execute();
        }
    }




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

        private class signUpParticipant extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
    btnText=btnaddpart.getText().toString();

            }

            @Override
            protected String doInBackground(String... urls) {
                try {



                    HttpClient httpClient = HttpClientBuilder.create().build();
                    if(btnText.equalsIgnoreCase("Add Participant")){
                        String postUrl = "https://recrespite-3c13b.firebaseio.com/UserInformation/"+info.getUsername()+"/participants/"+numOfParticipant+".json";
                    HttpPut put = new HttpPut(postUrl);
                    put.setHeader("Content-type", "application/json");
                    put.setEntity(postString);

                    HttpResponse response = httpClient.execute(put);
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();

                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                        Log.d("resultasdhuiahduia", String.valueOf(result));
                        resultTrack = String.valueOf(result);
                    }}


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


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Do you want to add a new participant?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if(numOfParticipant==5){
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                        builder1.setMessage("The maximum number of participant you can add in your profile are 5.");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent intent = new Intent(context, Home.class);
                                                        startActivity(intent);
                                                    }
                                                });


                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();

                                    }
                                    else{
                                    Intent intent = new Intent(context, AddParticipants.class);
                                    startActivity(intent);}
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(context, Home.class);
                                    startActivity(intent);

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


            }
        }


        public class getParticipantNum extends AsyncTask {


            @Override
            protected void onPreExecute() {

            }

            protected Object doInBackground(Object[] params) {

                try {
                    JSONParser jParser = new JSONParser(); // get JSON data fro
                    //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/UserInformation/" + info.getUsername() + "/participants/.json");

                    Log.d("array", String.valueOf(json1));

                    //loadData(json1);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                loadData(json1);
                            } catch (JSONException e) {

                                Log.e("JSON not processed", "Error processing JSON", e);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                return null;
            }


            public void loadData(String json) throws JSONException, UnsupportedEncodingException {
                //Toast.makeText(context,json.length()+" ",Toast.LENGTH_SHORT).show();
                if (json.contains("null")) {
                    numOfParticipant = 0;


                    postString = new StringEntity("{\"age\":\"" + age.getText().toString() + "\",\"diagnosis\":\"" + spinner.getSelectedItem().toString() + "\",\"firstname\": \"" + firstname.getText().toString() + "\",\"gender\": \"" + genderSpinner.getSelectedItem().toString() + "\",\"lastname\": \"" + lastname.getText().toString() + "\",\"notes\": \"" + notes.getText().toString() + "\",\"program\": \"" + program.getText().toString() + "\",\"username\": \"" + info.getUsername() + "\" }");


                    participantInfo.put("participant",firstname.getText().toString() + " " +lastname.getText().toString());
                    participantage.put(firstname.getText().toString(),age.getText().toString());


                    info.setParticipant(participantInfo);
                    info.setParticipantAge(participantage);


                    new signUpParticipant().execute();
                } else {
                    JSONArray array = new JSONArray(json);
                    for(int i=0;i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        if(object.getString("firstname").equals(firstname.getText().toString())){

                             String genderVal=object.getString("gender");
                            String spinnerVal=object.getString("diagnosis");
                            for(int j=0;j<genderSpinner.getCount();j++) {
                                if(genderSpinner.getItemAtPosition(j).toString().equalsIgnoreCase(genderVal)){
                                genderSpinner.setSelection(j);}
                            }
                            for(int j=0;j<spinner.getCount();j++) {
                                if(spinner.getItemAtPosition(j).toString().equalsIgnoreCase(spinnerVal)){
                                    spinner.setSelection(j);}
                            }
                        }
                        }

                    if (array.length() == 5) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("The maximum number of participant you can add in your profile are 5.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, Home.class);
                                        startActivity(intent);
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    } else {
                        numOfParticipant = array.length();

                        postString = new StringEntity("{\"age\":\"" + age.getText().toString() + "\",\"diagnosis\":\"" + spinner.getSelectedItem().toString() + "\",\"firstname\": \"" + firstname.getText().toString() + "\",\"gender\": \"" + genderSpinner.getSelectedItem().toString() + "\",\"lastname\": \"" + lastname.getText().toString() + "\",\"notes\": \"" + notes.getText().toString() + "\",\"program\": \"" + program.getText().toString() + "\",\"username\": \"" + info.getUsername() + "\" }");


                        participantInfo.put("participant",firstname.getText().toString() + " " +lastname.getText().toString());
                        participantage.put(firstname.getText().toString(),age.getText().toString());


                        info.setParticipant(participantInfo);
                        info.setParticipantAge(participantage);


                        new signUpParticipant().execute();


                    }


                }
            }}
        }

