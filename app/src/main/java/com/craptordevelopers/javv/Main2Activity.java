package com.craptordevelopers.javv;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_add_a_photo_white_18dp,
            R.drawable.ic_mms_white_48pt_3x,
            R.drawable.ic_question_answer_white_18dp
    };

    private Toolbar toolbar;
    private TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    protected int permissionSecond , permissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        permissionCheck= ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        permissionSecond=ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main2Activity.this , MapsActivity.class);
                startActivity(intent);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>1 && grantResults[0]== PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CameraFragment())
                    .commitAllowingStateLoss();
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
        if (id == R.id.action_new_chat) {
            return true;
        }
        if (id == R.id.action_account) {
            Intent intent = new Intent(Main2Activity.this , ChooserActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

//            setUpTabActions(position);

            switch (position) {
                case 0:
//                    if (permissionCheck== PackageManager.PERMISSION_GRANTED && permissionSecond==PackageManager.PERMISSION_GRANTED) {
//                        getWindow().getDecorView().setSystemUiVisibility(
//                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                        CameraFragment camera_fragment = new CameraFragment();
//                        return camera_fragment;
////                        getSupportFragmentManager().beginTransaction().add(R.id.container, new CameraFragment()).commitAllowingStateLoss();
//                    }else
//                    {
//                        ActivityCompat.requestPermissions(Main2Activity.this,new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                        onResume();
//                    }
                    Camera2Fragment camera_fragment = new Camera2Fragment();
                        return camera_fragment;

                case 1:
                    StoriesFragment tab_stories = new StoriesFragment();
                    return tab_stories;
                case 2:
                    ChatListFragment tab_chat_list = new ChatListFragment();
                    return tab_chat_list;
                default:
                    ChatListFragment tab1 = new ChatListFragment();
                    return tab1;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return null;
                case 1:
                    return null;
                case 2:
                    return null;
            }
            return null;
        }
    }
}
