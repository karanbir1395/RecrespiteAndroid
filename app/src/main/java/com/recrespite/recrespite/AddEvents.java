    package com.recrespite.recrespite;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.drawable.BitmapDrawable;
    import android.os.AsyncTask;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.io.ByteArrayOutputStream;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.HashMap;

    /**
     * Created by Navpreet on 2016-10-18.
     * this class add events to the list view in the events library
     */
    public class AddEvents extends BaseAdapter {
        Context context;
        ImageLoader imageLoader;
    JSONObject o;
        LayoutInflater inflater;
        ArrayList<HashMap<String, String>> data;
    participantInfo info ;
        HashMap<String, String> resultp = new HashMap<String, String>();

        public AddEvents(Context context, ArrayList<HashMap<String, String>> eventlist) {
            this.context = context;
            imageLoader=new ImageLoader(context);
            data = eventlist;
            info=new participantInfo();
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            HashMap<String,String> d = new HashMap<>();

                     d=data.get(position);
            return d;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {

            final TextView date, title, location;
            final ImageView image;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View itemView = inflater.inflate(R.layout.home_list_view, parent, false);

            // Get the position
            resultp = data.get(position);
            date = (TextView) itemView.findViewById(R.id.eventDate);
            // Locate the TextViews in item.xml

           image = (ImageView) itemView.findViewById(R.id.eventImage);
            title = (TextView) itemView.findViewById(R.id.eventTitle);
            location = (TextView) itemView.findViewById(R.id.eventAddress);
            title.setText(resultp.get(Home.TITLE));
            title.setId(position);
            location.setText(resultp.get(Home.LOCATION));
            date.setText(resultp.get(Home.DATE));
            final String imageUrl= resultp.get(Home.IMAGE);

            String url =imageUrl.replace("www.dropbox.", "dl.dropboxusercontent.");
           new DownloadImageTask((ImageView) itemView.findViewById(R.id.eventImage)).execute(url);
           imageLoader.DisplayImage(url,image);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context,eventDetail.class);
                    intent.putExtra("title",title.getText().toString());
                    intent.putExtra("location",location.getText().toString());
                    intent.putExtra("date",date.getText().toString());
                    HashMap<String,String> g= new HashMap<String, String>();
                    g=getItem(title.getId());
                 //   String test=getItem(title.getId()).toString();

                    JSONArray a = null;



                    intent.putExtra("description",g.get("desc"));
                    intent.putExtra("end",g.get("end"));
                    intent.putExtra("seats",g.get("seats"));
                    intent.putExtra("cost",g.get("cost"));
                    info.setEventId(g.get("id"));


                    Bitmap _bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();// your bitmap

                        ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                        _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
                        intent.putExtra("byteArray", _bs.toByteArray());








                    context.startActivity(intent);
                }
            });

            return itemView;
        }
// method used to display image from dropbox.
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
                if(result==null){
                    bmImage.setImageResource(R.drawable.event);
                }
                else {
                    bmImage.setImageBitmap(result);
                }
            }
        }

    }