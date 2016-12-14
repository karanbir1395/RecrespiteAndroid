    package com.recrespite.recrespite;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class populate the home screen which contains navigation for teh app
     */
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.os.AsyncTask;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.TextView;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class Home extends AppCompatActivity {
        private ListView mDrawerList;
        Context context = this;
        Bitmap mIcon_val;
        private DrawerLayout mDrawerLayout;
        private ArrayAdapter<String> mAdapter;
        SideMenuBar sideBar;
        private ImageLoader imgLoader;
        ListView eventsListView;
        UserSessionManager sessionManager;
        ProgressDialog mProgressDialog;
        AddEvents addEventsAdap;
        ArrayList<ImageView> articleImages = new ArrayList<>();
        ArrayList<TextView> articleTitle = new ArrayList<>();
        private static final String LOG_TAG = "ExampleApp";
        ArrayList<HashMap<String, String>> arraylist;

        private ActionBarDrawerToggle mDrawerToggle;
        private String mActivityTitle;
        static String DATE = "date";
        static String IMAGE = "image";
        static String COST="cost";
        static String ENDTIME="end";
        static String DESC="desc";
        static String STARTTIME="start";
        static String SEATS="seats";


        static String LOCATION = "location";
        static String TITLE = "title";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
           // imgLoader = new ImageLoader(this);
    sessionManager= new UserSessionManager(context);

    new loadevents().execute();






        }

        public void myProfile(View view) {
    Intent in = new Intent(context,Profile.class);

            startActivity(in);
        }


        public void OpenEvents(View view) {
          Intent in = new Intent(context,EventsRegionView.class);
        startActivity(in);}

        public void reqEvent(View view) {
            Intent in = new Intent(context,requestEvent.class);
            startActivity(in);
        }


        public void openLib(View view) {
            Intent in = new Intent(context,LibraryCat.class);
            startActivity(in);  }


        public void logout(View view) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Are you sure you want to logout?");
           builder1.setCancelable(false);


            builder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sessionManager = new UserSessionManager(context);
                            sessionManager.logoutUser();
                        }
                    });

            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(context,Home.class);
                    context.startActivity(in);
                }
            });



            AlertDialog alert11 = builder1.create();
            alert11.show();   }

        public void OneToOne(View view) {
            Intent intent=new Intent(context,oneToone.class);
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
        public class loadevents extends AsyncTask {

                protected Object doInBackground(Object[] params) {
                    arraylist = new ArrayList<HashMap<String, String>>();
                    try {
                        JSONParser jParser = new JSONParser(); // get JSON data from URL
                        final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/events/events.json");

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
                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        HashMap<String, String> map = new HashMap<String, String>();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("id",jsonObject.getString("Id"));
                        map.put("title", jsonObject.getString("eventName"));
                        map.put("date", jsonObject.getString("date"));
                        map.put("location", jsonObject.getString("location"));
                        map.put("image", jsonObject.getString("eventImage"));
                        map.put("cost", jsonObject.getString("cost"));
                        map.put("end", jsonObject.getString("endTime"));
                        map.put("desc", jsonObject.getString("eventDescription"));
                        map.put("start", jsonObject.getString("startTime"));
                        map.put("seats", jsonObject.getString("totalSeats"));





                        arraylist.add(map);
                    }

                    addEventsAdap = new AddEvents(context, arraylist);
                    // Set the adapter to the ListView
                   // eventsListView.setAdapter(addEventsAdap);
                }


                protected void onPostExecute() {

                }
            }


        }


