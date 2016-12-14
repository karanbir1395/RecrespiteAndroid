        package com.recrespite.recrespite;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        /**
         * Created by Navpreet on 2016-10-18.
         * this class displays the details of the selected event. get request to event detail API
         */
        public class eventDetail extends AppCompatActivity {
        String title;
            ImageView image;
            String loc,dat,json;
            ImageLoader imageLoader;
            participantInfo infp ;
            TextView name,date,description,location,participant,end,cost;
            private static final String LOG_TAG = "ExampleApp";
            ProgressDialog mProgressDialog;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                imageLoader = new ImageLoader(this);
                setContentView(R.layout.activity_event_detail);
                infp=new participantInfo();
                image=(ImageView) findViewById(R.id.image);
                name=(TextView) findViewById(R.id.name);
                date=(TextView) findViewById(R.id.date);
                end=(TextView) findViewById(R.id.enddateText);
                cost=(TextView) findViewById(R.id.costText);
                description=(TextView) findViewById(R.id.description);
                location=(TextView) findViewById(R.id.location);
                participant=(TextView) findViewById(R.id.participant);
                Intent in = getIntent();
                title=in.getStringExtra("title");
                loc=in.getStringExtra("location");
                dat=in.getStringExtra("date");
                infp.setLocation(loc);
                description.setText(in.getStringExtra("description"));
                end.setText(in.getStringExtra("end"));

                participant.setText(in.getStringExtra("seats"));
                cost.setText(in.getStringExtra("cost"));

                Bitmap _bitmap = BitmapFactory.decodeByteArray(
                        getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
                if(_bitmap==null){
                    image.setImageResource(R.drawable.event);
                }
                else{
                image.setImageBitmap(_bitmap);}
               // Toast.makeText(this,in.getStringExtra("title"),Toast.LENGTH_SHORT).show();
                name.setText(title);
                infp.setEventName(title);
                date.setText(dat);
                location.setText(loc);

                //new loadevent().execute();
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

            public void RegisForm(View view) {
                Intent in = new Intent(this,selectParticipant.class);
                startActivity(in);
            }



        }
