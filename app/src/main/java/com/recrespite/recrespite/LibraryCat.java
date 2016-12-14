    package com.recrespite.recrespite;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class populates the listview for library categories
     * makes a get request to get library categories API
     */
    import android.content.Context;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.os.AsyncTask;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.TextView;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class LibraryCat extends AppCompatActivity {
        Context context = this;
        private DrawerLayout mDrawerLayout;
        SideMenuBar sideBar;
        HashMap<String,String> url= new HashMap<>();
        private ListView mDrawerList;
        private ArrayAdapter<String> listAdapter ;
        String region;

        ListView listViewLib;

        ArrayList<String> arraylist= new ArrayList<>();
        AddEvents addEventsAdap;
        private static final String LOG_TAG = "ExampleApp";
        private String mActivityTitle;
        private ActionBarDrawerToggle mDrawerToggle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_library_cat);
            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            listViewLib=(ListView) findViewById(R.id.libArtcles) ;
            getSupportActionBar().setHomeButtonEnabled(true);
            mActivityTitle = getTitle().toString();
            sideBar = new SideMenuBar();
            sideBar.addDrawerItems(this, mDrawerLayout, mDrawerList);
            setupDrawer();
          /* arraylist.add("Children and Youth");
            arraylist.add("Young Adults");
            arraylist.add("Community");
            arraylist.add("Seniors");
            arraylist.add("Mental Health");
            listAdapter= new ArrayAdapter<String>(this,
                    R.layout.lib_list_lay,arraylist);

            // Set the adapter to the ListView
            listViewLib.setAdapter(listAdapter);

            listViewLib.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(LibraryCat.this,Library.class);

                    intent.putExtra("category",((TextView)view).getText().toString());
                    startActivity(intent);
                }
            });



    */
            new loadevents().execute();
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

            // Activate the navigation drawer toggle
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        public class loadevents extends AsyncTask {

            protected Object doInBackground(Object[] params) {

                try {
                    JSONParser jParser = new JSONParser(); // get JSON data from URL
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/articles/articles.json");

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
                for (int i = 0; i < jsonArray.length(); i++) {

                    HashMap<String, String> map = new HashMap<String, String>();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

    if(arraylist.contains(jsonObject.getString("category"))){

    }
                    else{
                    arraylist.add(jsonObject.getString("category"));}
    //                url.put(jsonObject.getString("name"),jsonObject.getString("articlePDF"));


                }


                listAdapter= new ArrayAdapter<String>(context,R.layout.lib_list_lay,arraylist);
                listViewLib.setAdapter(listAdapter);
                listViewLib.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent intent = new Intent(LibraryCat.this,Library.class);

                        intent.putExtra("category",((TextView)view).getText().toString());
                        startActivity(intent);

                    }
                });
            }
        }


        protected void onPostExecute() {


        }
    }