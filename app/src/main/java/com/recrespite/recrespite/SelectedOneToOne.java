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
     * this class displays the pdf for selected one to one brochure in a webview
     */
    public class SelectedOneToOne extends AppCompatActivity {
        WebView web;
        String pdf;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_selected_one_to_one);

            web=(WebView) findViewById(R.id.web);


            web.getSettings().setJavaScriptEnabled(true);

            web.loadUrl("http://com.RecRespite.com/wp-content/uploads/2011/02/RR-YOUNG-ADULTS-3.pdf");

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
