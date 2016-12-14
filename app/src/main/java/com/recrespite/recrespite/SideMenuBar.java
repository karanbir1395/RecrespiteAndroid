    package com.recrespite.recrespite;

    import android.annotation.TargetApi;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Build;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AlertDialog;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;

    import java.util.ArrayList;

    /**
     * Created by Navpreet on 2016-10-17.
     * it populates the side menu drawer list view
     */
    public class SideMenuBar {
        ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
        public ListView mDrawerList;
        public DrawerLayout mDrawerLayout;
        Context context;
        String resultForEmail;
        View view1;
        public ArrayAdapter<String> mAdapter;
        public ActionBarDrawerToggle mDrawerToggle;
        public String mActivityTitle;
        UserSessionManager sessionManager;

        public void addDrawerItems(final Context context, DrawerLayout mDrawerLayout, ListView mDrawerList)


        {

            mNavItems.add(new NavItem("My Profile", R.mipmap.myprofile));
            mNavItems.add(new NavItem("1:1", R.mipmap.home));
            mNavItems.add(new NavItem("Groups", R.mipmap.events));
            mNavItems.add(new NavItem("Request Groups", R.mipmap.ques));
            mNavItems.add(new NavItem("Article Library", R.mipmap.library));
            mNavItems.add(new NavItem("Logout", R.mipmap.logout));

            this.mDrawerList = mDrawerList;
            //(ListView) findViewById(R.id.navList);
            DrawerListAdapter adapter = new DrawerListAdapter(context, mNavItems);
            mDrawerList.setAdapter(adapter);

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    if (position == 0) {
                        Intent intent = new Intent(context,Profile.class);
                        context.startActivity(intent);


                    }
                    if (position == 1) {
                        Intent intent = new Intent(context, oneToone.class);
                        context.startActivity(intent);
                    }
                    if (position == 2) {
                        Intent intent = new Intent(context,EventsRegionView.class);
                        context.startActivity(intent);
                    }
                    if (position == 3) {
                        Intent intent = new Intent(context, requestEvent.class);
                        context.startActivity(intent);
                    }

                if (position == 4) {
                    Intent intent = new Intent(context, LibraryCat.class);
                    context.startActivity(intent);
                }
                    if (position == 5) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Are you sure you want to logout?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sessionManager= new UserSessionManager(context);
                                        sessionManager.logoutUser();
                                    }
                                });

                        builder1.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               Intent in = new Intent(context,Home.class);
                                context.startActivity(in);
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }


                }

            });
        }
    }