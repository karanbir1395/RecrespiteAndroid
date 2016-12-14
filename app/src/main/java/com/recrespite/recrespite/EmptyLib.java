    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.res.Configuration;
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
    import android.widget.ListView;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.HashMap;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class gives the list of all the upcoming events in a selected region.It populates the event
     * listView
     */
    public class EmptyLib extends AppCompatActivity {
        Context context = this;
        private DrawerLayout mDrawerLayout;
        SideMenuBar sideBar;
        HashMap<String,String> url= new HashMap<>();
        private ListView mDrawerList;
        private ArrayAdapter<String> listAdapter ;
        String region;
    String catalog_outdated;

        ListView listViewLib;

        ArrayList<HashMap<String, String>> arraylist= new ArrayList<>();
        AddEvents addEventsAdap;
        private static final String LOG_TAG = "ExampleApp";
        private String mActivityTitle;
        private ActionBarDrawerToggle mDrawerToggle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_empty_lib);
            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            listViewLib=(ListView) findViewById(R.id.libArtcles) ;
            getSupportActionBar().setHomeButtonEnabled(true);
            mActivityTitle = getTitle().toString();
            sideBar = new SideMenuBar();
            sideBar.addDrawerItems(this, mDrawerLayout, mDrawerList);
            setupDrawer();
            Intent in = getIntent();
            region=in.getStringExtra("region");

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
                    final String json1 = jParser.getJSONFromUrl("https://recrespite-3c13b.firebaseio.com/events/events.json");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                loadData(json1);
                            } catch (JSONException e) {

                                Log.e(LOG_TAG, "Error processing JSON", e);
                            } catch (ParseException e) {
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


            void loadData(String json) throws JSONException, ParseException {

               /* JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length() - 1; i++) {



                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    arraylist.add(i,jsonObject.getString("eventName"));
                   // url.put(jsonObject.getString("eventName"),jsonObject.getString("articlePDF"));


                }

                 listAdapter=new ArrayAdapter<String>(context,R.layout.lib_list_lay,arraylist);
            listViewLib.setAdapter(listAdapter);
              //  listAdapter= new ArrayAdapter<String>(context,R.layout.lib_list_lay,arraylist);
              //  listViewLib.setAdapter(listAdapter);
                listViewLib.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                      /*  Intent intent = new Intent(EmptyLib.this,SelectedLibraryItem.class);
                        String title=((TextView)view).getText().toString();
                        //Toast.makeText(Library.this,title,Toast.LENGTH_SHORT).show();
                        String pdf=url.get(((TextView) view).getText().toString());
                       intent.putExtra("title",((TextView) view).getText().toString());
                        intent.putExtra("url",pdf);
                        Log.d("url",pdf);
                        Toast.makeText(EmptyLib.this,((TextView) view).getText().toString()+pdf,Toast.LENGTH_SHORT).show();
                        startActivity(intent);*/

                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();




                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                   // String reg="["+jsonObject.getString("registration")+"]";
                  //  JSONArray registration= new JSONArray(reg);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = sdf.parse(jsonObject.getString("expiryDate"));
                    Date todayDate=new Date();
                    //String todyDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    if (strDate.after(new Date())) {
                        catalog_outdated = "1";
                    }
                    else{
                        catalog_outdated="0";
                    }
                    if(jsonObject.getString("region").equals(region)){
                       if(catalog_outdated.equals("1")){
                         //  if(registration.length()<=Integer.parseInt(jsonObject.getString("totalSeats"))){
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





                    arraylist.add(map);}}
                }//}

                if(arraylist.isEmpty()){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("There are no events in this region.");
                    builder1.setCancelable(false);


                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
    Intent in = new Intent(EmptyLib.this,EventsRegionView.class);
                            startActivity(in);
                        }
                    });





                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    alert11.setCancelable(false);
                }
                else{

                addEventsAdap = new AddEvents(context, arraylist);
                // Set the adapter to the ListView
                listViewLib.setAdapter(addEventsAdap);}
            }









        }


        protected void onPostExecute() {


        }

    }


