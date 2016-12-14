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
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;

    import org.apache.http.HttpResponse;
    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpPatch;
    import org.apache.http.entity.StringEntity;
    import org.apache.http.impl.client.HttpClientBuilder;
    import org.json.JSONArray;
    import org.json.JSONException;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.UnsupportedEncodingException;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class displays the registration form for evenets and send a put request to the API to add
     * participants name in the selected event. most of the user information is already populated before
     * hand
     */
    public class Registration extends AppCompatActivity {
        TextView partName, contactInfo, phone, email, age, program, location, needs, interest, goals, allergies;
        Spinner payment;
        participantInfo part;
        String resultTrack, participant;
        StringEntity postString;
        String regCount;
        Context context = this;
        private static final String LOG_TAG = "ExampleApp";
        participantInfo info;
        String user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_registration);
            part = new participantInfo();
            payment = (Spinner) findViewById(R.id.paymentVal);
            info = new participantInfo();
            final List<String> list = new ArrayList<String>();
            list.add("Cash");
            list.add("Cheque");
            needs = (EditText) findViewById(R.id.specialVal);
            interest = (EditText) findViewById(R.id.recInVal);
            goals = (EditText) findViewById(R.id.goalslVal);
            allergies = (EditText) findViewById(R.id.alergiesVal);
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            payment.setAdapter(adp1);
            location = (TextView) findViewById(R.id.progarmLocLay);
            location.setText(info.getLocation());
            program = (TextView) findViewById(R.id.progarmInVal);
            program.setText(info.getEventName());
            Intent in = getIntent();
            user = in.getStringExtra("username");
            age = (TextView) findViewById(R.id.ageVal);
            HashMap<String, String> ageMap = new HashMap<>();

            for (int i = 0; i < info.getParticipantAge().size(); i++) {
                ageMap = (info.getParticipantAge().get(i));

            }
            age.setText(ageMap.get(in.getStringExtra("part")));
            contactInfo = (TextView) findViewById(R.id.conatctVal);
            contactInfo.setText(part.getUsername());
            phone = (TextView) findViewById(R.id.PhoneVal);
            phone.setText(part.getPhone());
            email = (TextView) findViewById(R.id.emailVal);
            // Toast.makeText(this,part.getEmail(),Toast.LENGTH_SHORT).show();
            email.setText(part.getEmail());
            partName = (TextView) findViewById(R.id.nameVal);
            partName.setText(in.getStringExtra("part"));


        }

        public void reisterForEvent(View view) throws UnsupportedEncodingException {
            new loadevents().execute();
            postString = new StringEntity("{\"age\":\"" + age.getText().toString() + "\",\"allergies\":\"" + allergies.getText().toString() + "\",\"email\":\"" + email.getText().toString() + "\",\"emergencyPhone\":\"" + phone.getText().toString() + "\",\"expectationsAndGoals\":\"" + goals.getText().toString() + "\",\"location\":\"" + location.getText().toString() + "\",\"name\":\"" + partName.getText().toString() + "\",\"paymentType\":\"" + payment.getSelectedItem().toString() + "\",\"phone\":\"" + phone.getText().toString() + "\",\"programOfInterest\":\"" + program.getText().toString() + "\",\"recreationalInterest\":\"" + interest.getText().toString() + "\",\"specialNeeds\":\"" + needs.getText().toString() + "\",\"username\":\"" + user.toString() + "\"}");

            participant = partName.getText().toString();
            new register().execute();
        }


        private class register extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {


            }

            @Override
            protected String doInBackground(String... urls) {
                try {
                    String postUrl = "https://recrespite-3c13b.firebaseio.com/events/events/" + info.getEventId().toString().trim() + "/registration/"+regCount+".json";// put in your url

                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPatch patch = new HttpPatch(postUrl);
                    patch.setHeader("Content-type", "application/json");
                    patch.setEntity(postString);

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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to add a new participant?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(context, selectParticipant.class);
                                startActivity(intent);
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
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@com.RecRespite.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                //return true;
            }

            return super.onOptionsItemSelected(item);
        }


        public class loadevents extends AsyncTask {

            protected Object doInBackground(Object[] params) {

                try {
                    JSONParser jParser = new JSONParser(); // get JSON data from URL
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/events/events/" + info.getEventId().toString().trim() + "/registration.json");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                loadData(json1);
                            } catch (JSONException e) {

                                Log.e(LOG_TAG, "Error processing JSON", e);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                return null;
            }


            void loadData(String json) throws JSONException {

                JSONArray jsonArray = new JSONArray(json);

                regCount=String.valueOf(jsonArray.length());

            }
        }
    }