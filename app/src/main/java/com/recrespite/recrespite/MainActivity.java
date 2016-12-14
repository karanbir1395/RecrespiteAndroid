    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.text.Html;
    import android.util.Log;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.TextView;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.net.HttpURLConnection;
    import java.util.HashMap;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class is the first screen for the which is used for user login , sign up and about option
     * makes a get request to check if the user exists , if user exists the user is taken to the home
     * page otherwise user does not exist dialog box is displayes
     */
    public class MainActivity extends AppCompatActivity {
        TextView signUp, explore, about;
        EditText email;
        Context context = this;
        String urlEmail;
        UserSessionManager sessionManager;
        participantInfo info;
        private static final String LOG_TAG = "ExampleApp";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            signUp = (TextView) findViewById(R.id.signUp);
            //explore = (TextView) findViewById(R.id.explore);
            about = (TextView) findViewById(R.id.about);
            info=new participantInfo();
            sessionManager= new UserSessionManager(context);

            email = (EditText) findViewById(R.id.email);
            String signup = "<u>Sign Up</u>";
            String exploreApp = "<u>Explore the App</u>";
            String aboutUs = "<u>About Recreational Respite</u>";
            signUp.setText(Html.fromHtml(signup), TextView.BufferType.SPANNABLE);
            //explore.setText(Html.fromHtml(exploreApp), TextView.BufferType.SPANNABLE);
            about.setText(Html.fromHtml(aboutUs), TextView.BufferType.SPANNABLE);

            if(sessionManager.isUserLoggedIn()){
                urlEmail=sessionManager.getUserDetails().get("name");
              //  Toast.makeText(this,urlEmail,Toast.LENGTH_SHORT).show();
               new checkEmailExists().execute();

            }


        }

        public void signUp(View view) {


            Intent intent = new Intent(this,Categories.class);
            startActivity(intent);
        }

        public void logIn(View view) {


            urlEmail = email.getText().toString();
            sessionManager.createUserLoginSession(urlEmail);
            if (urlEmail.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("The username cannot be null please try again");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                builder.setCancelable(false);
            } else {
                new checkEmailExists().execute();
            }


        }

        public void recRes(View view) {


            Uri uri = Uri.parse("http://com.RecRespite.com/about-us/"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }


        public class checkEmailExists extends AsyncTask {
            @Override
            protected void onPreExecute() {

            }

            protected Object doInBackground(Object[] params) {
                HttpURLConnection conn = null;
                final StringBuilder json = new StringBuilder();
                try {
                    JSONParser jParser = new JSONParser(); // get JSON data fro
                   // final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/UserInformation/mannkara.json");
                    //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/UserInformation/"+urlEmail+".json");
                    Log.d("array", String.valueOf(json1));
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
               /* Toast.makeText(context,json,Toast.LENGTH_SHORT).show();
                    json="["+json+"]";
                JSONArray a= new JSONArray(json);
                JSONObject object = a.getJSONObject(0);
                Toast.makeText(context,object.getString("city"),Toast.LENGTH_SHORT).show();*/
    /*            for (int i = 0; i < json.length(); i++) {*/

                if(json.length()==4){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error");
                    builder.setMessage("The username does not exist");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();

                    dialog.show();
                    builder.setCancelable(false);
                }
              else {
                    json="["+json+"]";
                    JSONArray a= new JSONArray(json);
                    JSONObject object = a.getJSONObject(0);
                    info.setUsername(object.getString("username"));
                    info.setEmail(object.getString("email"));
                    info.setPhone(object.getString("phoneNumber"));
                    info.setUserFirstName(object.getString("firstname"));
                    info.setUserLastName(object.getString("lastname"));
                    info.setRegion(object.getString("region"));
                    JSONArray participant=new JSONArray(object.getString("participants"));

                    HashMap<String,String> participantage=new HashMap<>();

                    for (int i=0;i<participant.length();i++){
                        HashMap<String,String> participantInfo=new HashMap<>();
                        JSONObject p=participant.getJSONObject(i);
                        participantInfo.put("participant",p.getString("firstname"));
                        participantage.put(p.getString("firstname"),p.getString("age"));
                        info.setParticipant(participantInfo);
                        info.setParticipantAge(participantage);



                    }


                    //Toast.makeText(MainActivity.this,info.getParticipant().toString(),Toast.LENGTH_SHORT).show();



                    Intent intent=  new Intent(MainActivity.this,Home.class);
                    startActivity(intent);

                }



                }
                }
            }




