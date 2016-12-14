    package com.recrespite.recrespite;

    import android.content.Intent;
    import android.net.Uri;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Toast;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class displays teh one to one program brocures
     */
    public class oneToone extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_one_toone);
        }


        public void youngAdults(View view) {

            Uri uri = Uri.parse("http://recrespite.com/wp-content/uploads/2011/02/RR-YOUNG-ADULTS-3.pdf"); // missing 'http://' will cause crashed
           if(uri!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
            else{
               Toast.makeText(this,"The pdf is not available right now,Please try again later.",Toast.LENGTH_SHORT).show();
           }
        }

        public void childrenOnclick(View view) {


            Uri uri = Uri.parse("http://recrespite.com/wp-content/uploads/2011/02/RR-CHILDREN-AND-YOUTH-full-page.pdf"); // missing 'http://' will cause crashed
           if(uri!=null){

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
           else{
               Toast.makeText(this,"The pdf is not available right now,Please try again later.",Toast.LENGTH_SHORT).show();
           }
        }

        public void adultsOnclick(View view) {

            Uri uri = Uri.parse("http://recrespite.com/wp-content/uploads/2011/02/RR-ADULTS-AND-OLDER-ADULTS-full-page.pdf"); // missing 'http://' will cause crashed
            if(uri!=null){

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
            else{
                Toast.makeText(this,"The pdf is not available right now,Please try again later.",Toast.LENGTH_SHORT).show();
            }
        }

        public void mentalOnclick(View view) {
            Uri uri = Uri.parse("http://recrespite.com/wp-content/uploads/2011/02/RR-MENTAL-HEALTH-full-page.pdf"); // missing 'http://' will cause crashed

           if(uri!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
           else{
               Toast.makeText(this,"The pdf is not available right now,Please try again later.",Toast.LENGTH_SHORT).show();
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
