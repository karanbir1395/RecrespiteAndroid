package com.recrespite.recrespite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Navpreet on 2016-10-17.
 * side menu bar layout contaning text and images and inflating the sid menu bar list
 */

    public class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.menu, null);
            } else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);

            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(position).mTitle);

            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
