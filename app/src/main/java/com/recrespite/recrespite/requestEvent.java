    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.Toast;

    import org.apache.http.HttpResponse;
    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpPut;
    import org.apache.http.entity.StringEntity;
    import org.apache.http.impl.client.HttpClientBuilder;
    import org.json.JSONArray;
    import org.json.JSONException;

    import java.io.BufferedInputStream;
    import java.io.BufferedReader;
    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.UnsupportedEncodingException;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class send the request to the admin requesting a new event for a particular region
     */
    public class requestEvent extends AppCompatActivity {
        Context context = this;
        EditText location, desc, notes;
        participantInfo info;
        String resultTrack;
        int count;
        StringEntity postString;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_request_event);
            info = new participantInfo();
            location = (EditText) findViewById(R.id.etLocation);
            desc = (EditText) findViewById(R.id.etDescription);
            notes = (EditText) findViewById(R.id.etNotes);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("Request new Event");
            builder1.setMessage("Recreational Respite's group programs are developed based on individual needs, abilities and interests of the participants. Therapeutic recreation focuses on inclusion, emphasizes participation and is goal driven.  If you wish to request a group, please write us here...");


            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();
            alert11.setCancelable(false);


        }

        public void submit(View view) {
    new requestedEventRegister().execute();
        }

        private class requestedEventRegister extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {


            }

            @Override
            protected String doInBackground(String... urls) {
                try {


                    HttpClient httpClient = HttpClientBuilder.create().build();


                    URL url = new URL("https://recrespite-3c13b.firebaseio.com/RequestEvent/.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        try {
                            ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            int i = in.read();
                            while (i != -1) {
                                bo.write(i);
                                i = in.read();
                            }
                            String res = bo.toString();
                            JSONArray a = new JSONArray(res);
                            count = a.length();

                        } catch (IOException e) {
                            return "";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        urlConnection.disconnect();
                    }

                    String postUrl = "https://recrespite-3c13b.firebaseio.com/RequestEvent/" + count + ".json";
                    try {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(context,String.valueOf(count),Toast.LENGTH_SHORT).show();
                                try {
                                    postString = new StringEntity("{\"Id\":\"" + (count-1)+ "\",\"email\":\"" + info.getEmail() + "\",\"eventDescription\":\"" + desc.getText().toString() + "\",\"notes\":\"" + notes.getText().toString() + "\",\"phone\":\"" + info.getPhone() + "\",\"requestLocation\":\"" + location.getText().toString() + "\",\"username\": \"" + info.getUsername() + "\"}");

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


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
                        }


                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                return resultTrack;
            }


            @Override
            protected void onPostExecute(String result1) {
                Toast.makeText(requestEvent.this,"your request has been successfully sent",Toast.LENGTH_SHORT).show();
                Intent in = new Intent(requestEvent.this,Home.class);
                startActivity(in);


            }
        }
    }
