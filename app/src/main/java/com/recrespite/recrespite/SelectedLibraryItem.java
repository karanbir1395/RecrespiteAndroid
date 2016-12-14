    package com.recrespite.recrespite;

    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.webkit.WebView;
    import android.webkit.WebViewClient;
    /**
     * Created by Navpreet on 2016-10-18.
     * this class displays the PDF for selected article in a web view. pdf location is passed to
     * the activity using an intent
     */
    public class SelectedLibraryItem extends AppCompatActivity {
        WebView web;
    String pdf;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_selected_library_item);
           web=(WebView) findViewById(R.id.web);
            Intent in = getIntent();
            pdf=in.getStringExtra("url");
          //  Toast.makeText(this,pdf,Toast.LENGTH_SHORT).show();



            web.setWebViewClient(new myWebClient());
            web.loadUrl(pdf);

           // web.loadUrl( "https://firebasestorage.googleapis.com/v0/b/recrespite-3c13b.appspot.com/o/PDF%20-%20Articles%2FA%20day%20of%20Lego-%20Visual%20Aids.pdf?alt=media&token=240311d2-a8d7-416c-8570-7fc82917e509");

          //  web.loadUrl(pdf);

        }
        public class myWebClient extends WebViewClient
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                view.loadUrl(url);
                return true;

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
