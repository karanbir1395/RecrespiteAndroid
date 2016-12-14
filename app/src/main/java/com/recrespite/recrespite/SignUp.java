    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.os.Looper;
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

    import org.apache.http.HttpResponse;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.ResponseHandler;
    import org.apache.http.client.methods.HttpGet;
    import org.apache.http.client.methods.HttpPut;
    import org.apache.http.entity.StringEntity;
    import org.apache.http.impl.client.BasicResponseHandler;
    import org.apache.http.impl.client.DefaultHttpClient;
    import org.apache.http.message.BasicHeader;
    import org.apache.http.params.HttpConnectionParams;
    import org.apache.http.protocol.HTTP;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.io.InputStream;
    import java.io.UnsupportedEncodingException;
    import java.util.ArrayList;
    import java.util.List;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class displays the sign up form for users and adds their region and sends out request to
     * API to add new user
     */
    public class SignUp extends AppCompatActivity {

        EditText etUsername, etFirstname, etLastname, etEmailAddress, etPhone, etCity, etRegion;
    String profileType;
        participantInfo info;
        Context context=this;
    Spinner spinnerRegions;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            Intent in= getIntent();

            profileType=in.getStringExtra("profileType");
            spinnerRegions=(Spinner) findViewById(R.id.spinnerRegion);
            info= new participantInfo();
            new getRegions().execute();

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

        public void onClickNext(View view) {


            etUsername = (EditText) findViewById(R.id.username);
            etFirstname = (EditText) findViewById(R.id.etFirstName);
            etLastname = (EditText) findViewById(R.id.etLastName);
            etEmailAddress = (EditText) findViewById(R.id.email);
            etPhone = (EditText) findViewById(R.id.phone);
            etCity = (EditText) findViewById(R.id.city);



            final String username = etUsername.getText().toString().trim();
            final String firstname = etFirstname.getText().toString().trim();
            final String lastname = etLastname.getText().toString().trim();
            final String emailAddress = etEmailAddress.getText().toString().trim();
            final String phone = etPhone.getText().toString().trim();
            final String city = etCity.getText().toString().trim();


            //Checking validations
            if (username.equals("")) {
                etUsername.setError("Username is required");
                etUsername.setHint("Username");
            }

            else if(username.contains(" "))
            {
                etUsername.setError("No spaces allowed");

            }

            else if(!username.matches("[a-zA-Z.@_0-9]*"))
            {
                etUsername.setError("Only alphabets, numbers, . _ and @ symbols allowed");
            }

            else if(emailAddress.equals(""))
            {
                etEmailAddress.setError("Email is required");
                etEmailAddress.setHint("Email");
            }


            //Putting(sending) data to firebase database
            else {
                Thread t = new Thread() {

                    public void run() {
                        Looper.prepare(); //For Preparing Message Pool for the child Thread
                        HttpClient client = new DefaultHttpClient();
                        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                        HttpResponse response;
                        JSONObject json = new JSONObject();


                        final String url = "https://recrespite-3c13b.firebaseio.com/UserInformation/" + username + ".json";

                        HttpGet httpget = new HttpGet(url);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                     String SetServerString = null;
                        try {
                            SetServerString = client.execute(httpget, responseHandler);
                        }
                        catch (NullPointerException e){

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                       final String s= SetServerString;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                etUsername.setError("Username already exists");
    //Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    //stuff that updates ui


                            }
                        });



                           if((SetServerString.length()!=4)){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("username already exists, please enter a different username");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        etUsername.setError("Username already exists");

    //stuff that updates ui

                                                    }
                                                });

                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                alert11.setCancelable(false);


                            }
                        else{
                               try {

                                   HttpPut put = new HttpPut(url);
                                   json.put("firstname", firstname);
                                   json.put("lastname", lastname);
                                   json.put("username", username);
                                   json.put("email", emailAddress);
                                   json.put("phoneNumber", phone);
                                   json.put("city", city);
                                   json.put("region",spinnerRegions.getSelectedItem().toString());
                                   json.put("userType", profileType);

                                   info.setUsername(username);
                                   info.setEmail(emailAddress);
                                   info.setPhone(phone);
                                   info.setRegion(spinnerRegions.getSelectedItem().toString());

                                   info.setUserFirstName(firstname);
                                   info.setUserLastName(lastname);
                                   StringEntity se = new StringEntity(json.toString());
                                   se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                   put.setEntity(se);
                                   response = client.execute(put);
                                   if (response != null) {
                                       InputStream in = response.getEntity().getContent(); //Get the data in the entity

                                       intentParticipant(username);
                                   }
                               } catch (UnsupportedEncodingException e1) {
                                   e1.printStackTrace();
                               } catch (IOException e1) {
                                   e1.printStackTrace();
                               } catch (JSONException e1) {
                                   e1.printStackTrace();
                               }

                           }

                        /*Checking response */




                        Looper.loop(); //Loop in the message queue
                    }
                };

                t.start();
            }


        }

        public void intentParticipant(String username)
        {
            final String userVal=username;

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Do you want to add a participant in your Profile? Adding a participant helps you to register for our group events.");
            builder1.setCancelable(false);


            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(SignUp.this, AddParticipants.class);
                            intent.putExtra("passUsername", userVal);
                            startActivity(intent);



                }
            });
            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(SignUp.this,Home.class);
                    startActivity(in);
                }
            });




            AlertDialog alert11 = builder1.create();
            alert11.show();
            alert11.setCancelable(false);

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
                }

                catch (IOException e) {
                    e.printStackTrace();
                }  finally {

                }
                return null;
            }


            public void loadData(String json) throws JSONException {

                List<String> RegionsList = new ArrayList<String>();


                if (json == null) {
                    Log.v("TAG JSON", "Json was empty");
                }
                else{
                    // Log.v("TAG JSON", "Json was not empty");
                    try {

                        JSONArray jsonArray = new JSONArray(json);
                        // Log.v("LOG JSON ARRAY", jsonArray.getString(2));



                        for(int i=0; i<jsonArray.length(); i++) {
                            // jsonObject = jsonArray.getJSONObject(i);
                            RegionsList.add(jsonArray.getJSONObject(i).getString("name").toString().trim());

                        }
                        int inee = jsonArray.length();

                        Log.v("Array length", "length is: "+ inee);


                        ArrayAdapter<String> regions_Adapter = new ArrayAdapter<String>(
                                SignUp.this, android.R.layout.simple_spinner_item, RegionsList);
                        regions_Adapter.setDropDownViewResource( android.R.layout.select_dialog_singlechoice);

    // Spinner spinYear = (Spinner)findViewById(R.id.spin);
                      spinnerRegions.setAdapter(regions_Adapter);

                        //Log.d("LOG JSON", jsonObject.getString("name"));

                    } catch (Throwable t) {
                        Log.e("LOG JSON", "Could not parse malformed JSON: \"" + json + "\"");
                    }



                }


            }


        }
    }