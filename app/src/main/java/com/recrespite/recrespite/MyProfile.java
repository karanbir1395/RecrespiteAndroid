    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.EditText;
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

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.UnsupportedEncodingException;
    import java.util.ArrayList;
    import java.util.List;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class displays the myprofile screen which has edit profile and edit participant option
     */
    public class MyProfile extends AppCompatActivity {
        EditText username,phone,firstname,lastname;
        participantInfo info;
        String selectedParticipant;
        private static final String LOG_TAG = "ExampleApp";
        Context context=this;
        final List<String> list=new ArrayList<String>();
        String resultTrack;
        int selection;
        Spinner spinnerRegions;
        StringEntity patchString;

        ArrayList<String> arraylist=new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_profile);
            info=new participantInfo();
            spinnerRegions=(Spinner) findViewById(R.id.spinnerRegion);
            username=(EditText) findViewById(R.id.usernameVal);
            phone=(EditText) findViewById(R.id.phoneVal);

            firstname=(EditText) findViewById(R.id.fNameVal);
            lastname=(EditText) findViewById(R.id.lastNameVal);
            username.setText(info.getUsername());
            phone.setText(info.getPhone());

            username.setEnabled(false);
            firstname.setText(info.getUserFirstName());
          //  Toast.makeText(this,list.toString(),Toast.LENGTH_SHORT).show();
            lastname.setText(info.getUserLastName());
            new getRegions().execute();
          // Toast.makeText(this,selection+" ",Toast.LENGTH_SHORT).show();



    }

        public void EditUser(View view) {

            try {
                info.setPhone(phone.getText().toString());
                info.setRegion(spinnerRegions.getSelectedItem().toString());
                info.setUserFirstName(firstname.getText().toString());
                info.setUserLastName(lastname.getText().toString());

                patchString= new StringEntity("{\"phoneNumber\":\""+phone.getText().toString()+"\",\"firstname\":\""+firstname.getText().toString()+"\",\"lastname\":\""+lastname.getText().toString()+"\",\"region\":\""+spinnerRegions.getSelectedItem().toString()+"\"}");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
           new editUser().execute();

          }

        private class editUser extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {


            }

            @Override
            protected String doInBackground(String... urls) {
                try {
                    String patchUrl = "https://recrespite-3c13b.firebaseio.com/UserInformation/"+info.getUsername()+".json";

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



                           Toast.makeText(context,"Changes Saved",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(context,Home.class);
                                startActivity(intent);



            }
        }

        public class getRegions extends AsyncTask {

            ArrayList<String> regionsArray = new ArrayList<String>();

            @Override
            protected void onPreExecute() {

            }

            protected Object doInBackground(Object[] params) {

                try {
                    JSONParser jParser = new JSONParser(); // get JSON data fro
                    //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/regions.json");

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
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                return null;
            }


            public void loadData(String json) throws JSONException {

                List<String> RegionsList = new ArrayList<String>();


                if (json == null) {
                    Log.v("TAG JSON", "Json was empty");
                } else {
                    // Log.v("TAG JSON", "Json was not empty");
                    try {

                        JSONArray jsonArray = new JSONArray(json);
                        // Log.v("LOG JSON ARRAY", jsonArray.getString(2));


                        for (int i = 0; i < jsonArray.length(); i++) {
                            // jsonObject = jsonArray.getJSONObject(i);



                            RegionsList.add(jsonArray.getJSONObject(i).getString("name").toString().trim());


                        }
                        int inee = jsonArray.length();

                        Log.v("Array length", "length is: " + inee);


                        ArrayAdapter<String> regions_Adapter = new ArrayAdapter<String>(
                                MyProfile.this, android.R.layout.simple_spinner_item, RegionsList);
                        regions_Adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

    // Spinner spinYear = (Spinner)findViewById(R.id.spin);
                        spinnerRegions.setAdapter(regions_Adapter);



              for(int i=0;i<spinnerRegions.getCount();i++) {
                  if (spinnerRegions.getItemAtPosition(i).equals(info.getRegion())) {
                      selection = i;
                      spinnerRegions.setSelection(i);
                  }
              }
                   //   spinnerRegions.setSelection(selection);

                        //Log.d("LOG JSON", jsonObject.getString("name"));

                    } catch (Throwable t) {
                        Log.e("LOG JSON", "Could not parse malformed JSON: \"" + json + "\"");
                    }


                }


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
        }