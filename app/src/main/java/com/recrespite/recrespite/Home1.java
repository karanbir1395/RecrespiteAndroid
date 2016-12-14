    package com.recrespite.recrespite;

    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.drawable.BitmapDrawable;
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
    import java.io.InputStream;
    import java.net.MalformedURLException;
    import java.util.ArrayList;
    import java.util.HashMap;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class is version 2 for home screen
     * no longer in use
     */
    public class Home1 extends AppCompatActivity {
        private ListView mDrawerList;
        Context context = this;
        Bitmap mIcon_val;
        private DrawerLayout mDrawerLayout;
        private ArrayAdapter<String> mAdapter;
        SideMenuBar sideBar;
        private ImageLoader imgLoader;
        ListView eventsListView;
        ProgressDialog mProgressDialog;
        AddEvents addEventsAdap;
        participantInfo info;
        TextView upcoming;
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
            setContentView(R.layout.activity_home1);
            imgLoader = new ImageLoader(this);
            info= new participantInfo();
            upcoming=(TextView) findViewById(R.id.upcomingText);







            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            eventsListView=(ListView) findViewById(R.id.eventsList);
            articleImages.add((ImageView) findViewById(R.id.image1));
            articleImages.add((ImageView) findViewById(R.id.image2));
            articleImages.add((ImageView) findViewById(R.id.image3));
            articleImages.add((ImageView) findViewById(R.id.image4));
            articleImages.add((ImageView) findViewById(R.id.image5));
            articleImages.add((ImageView) findViewById(R.id.image6));
            articleTitle.add((TextView) findViewById(R.id.image1Text));
            articleTitle.add((TextView) findViewById(R.id.image2Text));
            articleTitle.add((TextView) findViewById(R.id.image3Text));
            articleTitle.add((TextView) findViewById(R.id.image4Text));
            articleTitle.add((TextView) findViewById(R.id.image5Text));
            articleTitle.add((TextView) findViewById(R.id.image6Text));
            getSupportActionBar().setHomeButtonEnabled(true);
            mActivityTitle = getTitle().toString();
            sideBar = new SideMenuBar();
            sideBar.addDrawerItems(this, mDrawerLayout, mDrawerList);
            setupDrawer();
            new loadevents().execute();
            new loadarticles().execute();



        }


        private void setupDrawer() {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getSupportActionBar().setTitle("Navigation");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    getSupportActionBar().setTitle(mActivityTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
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
                return true;
            }

            // Activate the navigation drawer toggle
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        public void category(View view) {

            Intent in = new Intent(Home1.this,SelectedLibraryItem.class);
            in.putExtra("url",view.getContentDescription().toString());
            startActivity(in);

        }

        public class loadarticles extends AsyncTask {

            protected Object doInBackground(Object[] params) {


                JSONParser jParser = null; // get JSON data fro
                try {
                    jParser = new JSONParser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //  JSONObject jsonObject=jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/userlog/hallen.json");
                final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/articles/articles.json");
                Log.d("array", String.valueOf(json1));
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            loadData(json1);
                        } catch (JSONException e) {

                            Log.e(LOG_TAG, "Error processing JSON", e);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                return null;
            }
        }


        void loadData(String json) throws JSONException, MalformedURLException {
            mProgressDialog = new ProgressDialog(Home1.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please wait");

            //Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
    //String js=json.substring(0,12)+"["+json.substring(12,json.length()-1)+"]}";
            // Toast.makeText(this,json.substring(0,12)+"["+json.substring(12,json.length()-1)+"]}",Toast.LENGTH_LONG).show();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length() - 1; i++) {
                // HashMap<String, String> map = new HashMap<String, String>();
                // HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                articleTitle.get(i).setText(jsonObj1.getString("name"));
                String url1 = jsonObj1.getString("articleImage");
                String url = url1.replace("www.dropbox.", "dl.dropboxusercontent.");
                articleImages.get(i).setContentDescription(jsonObj1.getString("articlePDF"));
                //   imgLoader.DisplayImage(url, articleImages.get(i));



                new DownloadImageTask(articleImages.get(i)).execute(url);


            }


        }


        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {

                    InputStream in = new java.net.URL(urldisplay).openStream();

                    mIcon11 = BitmapFactory.decodeStream(in);
                    // in.reset();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                // loadBitmap(bmImage.getId(),bmImage);
                BitmapDrawable ob = new BitmapDrawable(getResources(), result);

                bmImage.setBackgroundDrawable(ob);

                mProgressDialog.dismiss();
            }
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
                    if(jsonObject.getString("region").equalsIgnoreCase(info.getRegion())) {
                        map.put("id", jsonObject.getString("Id"));
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
                    }}
                if(arraylist.isEmpty()){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("There are no upcoming events in your region, To see all events organized by us, please click on okay, or navigate to side menu bar and click on Events.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent= new Intent(context,EmptyLib.class);
                                    startActivity(intent);
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    upcoming.setText("There are no upcoming events in your region, To see all events organized by us ,please go to Events on the side menu bar of the app.");

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    //

                }


                addEventsAdap = new AddEvents(context, arraylist);
                // Set the adapter to the ListView
                eventsListView.setAdapter(addEventsAdap);
            }


            protected void onPostExecute() {

            }
        }


    }
