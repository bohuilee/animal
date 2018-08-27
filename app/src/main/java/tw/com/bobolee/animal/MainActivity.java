package tw.com.bobolee.animal;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import java.util.List;

import tw.com.bobolee.animal.About.AboutAppFragmentSample;
import tw.com.bobolee.animal.Animal.ExpiringAnimalList;
import tw.com.bobolee.animal.Animal.FavoriteAnimalList;
import tw.com.bobolee.animal.Animal.NewAnimalList;
import tw.com.bobolee.animal.Area.AreaRegionList;
import tw.com.bobolee.animal.GlobalClass.FragmentUtils;
import tw.com.bobolee.animal.GoogleAnalytics.GoogleAnalytics;
import tw.com.bobolee.animal.Info.UseInfoFragmentSample;
import tw.com.bobolee.animal.Search.SearchConditionFragmentSample;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;
    private NavigationView navigationView;
    public MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
    public ViewPager viewPager;
    private TabLayout tabLayout;
    public static final NewAnimalList mNewAnimalList = new NewAnimalList();
    public static final ExpiringAnimalList mExpiringAnimalList = new ExpiringAnimalList();
    public static final FavoriteAnimalList mFavoriteAnimalList = new FavoriteAnimalList();
    private static final String[] FragmentList = {"AreaRegionListFragment", "SearchConditionFragment", "UseInfo", "AboutApp"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Tracker mTracker;

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);

        //GA
        GoogleAnalytics();

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setElevation(0);
        ViewCompat.setElevation(toolbar,0);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //mAppBarLayout.setElevation(0);
        ViewCompat.setElevation(mAppBarLayout,0);

        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageSelected(int pos) {
                switch (pos){
                    case 0:
                        mTracker.setScreenName("tw.com.bobolee.animal.Animal.NewAnimalList");
                        break;
                    case 1:
                        mTracker.setScreenName("tw.com.bobolee.animal.Animal.ExpiringAnimalList");
                        break;
                    case 2:
                        mTracker.setScreenName("tw.com.bobolee.animal.Animal.FavoriteAnimalList");
                        break;
                }
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        };
        viewPager.setOnPageChangeListener(mPageChangeListener);
        //Three tabs in TabLayout
        mAdapter.addFragment(mNewAnimalList, getResources().getString(R.string.NewAnimal));
        mAdapter.addFragment(mExpiringAnimalList, getResources().getString(R.string.ExpiringAnimal));
        mAdapter.addFragment(mFavoriteAnimalList, getResources().getString(R.string.FavoriteAnimal));
        viewPager.setAdapter(mAdapter);


        //Set TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(12);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondary), ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //close DrawerLayout if DrawerLayout opened
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                //Double click back button to close APP
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.AskForExit, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    //The FragmentStatePagerAdapter of the first three Fragments in TabLayout
    static class MyAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment Fragment, String Title) {
            mFragments.add(Fragment);
            mFragmentTitles.add(Title);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int CurrentId = item.getItemId();

        removePastFragment();

        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (CurrentId == R.id.nav_HomePage) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Home")
                    .build());
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            navigationView.setCheckedItem(CurrentId);
        } else if (CurrentId == R.id.nav_Search) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Search")
                    .build());
            //搜尋頁面
            transaction.replace(R.id.ViewPagerRelativeLayout, new SearchConditionFragmentSample(), "SearchConditionFragment");
            navigationView.setCheckedItem(CurrentId);
        }
        transaction.commit();

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int CurrentId = item.getItemId();

        removePastFragment();

        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (CurrentId == R.id.nav_HomePage) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Home")
                    .build());
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        } else if (CurrentId == R.id.nav_Area) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Area")
                    .build());
            //收容所資訊
            transaction.replace(R.id.ViewPagerRelativeLayout, new AreaRegionList(), "AreaRegionListFragment");
        } else if (CurrentId == R.id.nav_Search) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Search")
                    .build());
            //搜尋頁面
            transaction.replace(R.id.ViewPagerRelativeLayout, new SearchConditionFragmentSample(), "SearchConditionFragment");
        } else if (CurrentId == R.id.nav_use_info) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("UseInfo")
                    .build());
            //使用說明
            transaction.replace(R.id.ViewPagerRelativeLayout, new UseInfoFragmentSample(), "UseInfo");
        } else if (CurrentId == R.id.nav_about) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("About")
                    .build());
            //關於App
            transaction.replace(R.id.ViewPagerRelativeLayout, new AboutAppFragmentSample(), "AboutApp");
        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void removePastFragment() {
        FragmentUtils.sDisableFragmentAnimations = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            String name = getSupportFragmentManager().getBackStackEntryAt(i).getName();
            getSupportFragmentManager().popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment Frag = getSupportFragmentManager().findFragmentByTag(name);
            if (Frag != null) {
                transaction.remove(Frag);
            }
        }

        for (String name : FragmentList) {
            Fragment Frag = getSupportFragmentManager().findFragmentByTag(name);
            if (Frag != null) {
                transaction.remove(Frag);
            }
        }
        transaction.commit();
        FragmentUtils.sDisableFragmentAnimations = false;
    }

    private void GoogleAnalytics(){
        GoogleAnalytics application = (GoogleAnalytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("tw.com.bobolee.animal.Animal.NewAnimalList");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}

