package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.SliderListAdapter;
import co.id.keda87.tassdroid.fragment.*;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.pojos.SliderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Adiyat Mubarak
 * Date: 3/9/14
 * Time: 2:55 PM
 */
public class MainMenuActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] sliderMenuTitle;
    private TypedArray sliderMenuIcon;
    private List<SliderItem> sliderNav;
    private SliderListAdapter sliderAdapter;
    private SessionManager sessionManager;
    private SharedPreferences preferences;

    @Override
    public void onBackPressed() {
        Log.d("TOMBOL KEMBALI", "Tombol kembali ditekan, tapi gk bisa keluar :P");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        //instance session manager
        this.sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> userSession = sessionManager.getUserDetails();

        //get title each menu
        this.drawerTitle = this.title = getTitle();

        //load slider item title
        this.sliderMenuTitle = getResources().getStringArray(R.array.nav_drawer_items);

        //load slider item icon
        this.sliderMenuIcon = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        //instance widget
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerList = (ListView) findViewById(R.id.list_slider);

        //instance preferences
        this.preferences = getSharedPreferences("co.id.keda87.tassdroid", MODE_PRIVATE);

        //display showcase at the first launch
        if (this.preferences.getBoolean("showcase", true)) {
            //launch showcase activity
            startActivity(new Intent(this, ShowcaseActivity.class));

            //set showcase preferences to false, so the showcase does not shown anymore
            this.preferences.edit().putBoolean("showcase", false).commit();
        }

        this.sliderNav = new ArrayList<>();
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[0], this.sliderMenuIcon.getResourceId(0, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[1], this.sliderMenuIcon.getResourceId(1, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[2], this.sliderMenuIcon.getResourceId(2, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[3], this.sliderMenuIcon.getResourceId(3, -1)));
        //check if user is a class leader, will add into slider
        if (userSession.get(SessionManager.KEY_KM).equals("1")) {
            this.sliderNav.add(new SliderItem(this.sliderMenuTitle[4], this.sliderMenuIcon.getResourceId(4, -1)));
        }
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[5], this.sliderMenuIcon.getResourceId(5, -1)));

        this.sliderMenuIcon.recycle();

        //create instance slider list adapter
        this.sliderAdapter = new SliderListAdapter(this, this.sliderNav);

        //set adapter into list drawer
        this.drawerList.setAdapter(this.sliderAdapter);

        //enable home button actionbar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
        };
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        if (savedInstanceState == null) {
            this.displayView(0);
        }
        this.drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.app_item_logout:
                sessionManager.destroySession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = this.drawerLayout.isDrawerOpen(this.drawerList);
        menu.findItem(R.id.app_item_logout).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(this.title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Method to display fragment depend on ListItem position
     *
     * @param position display fragment view depend on user select
     */
    private void displayView(int position) {
        Fragment fragment = null;

        //list menu position depend on access level for user or KM
        int TAK_POSITION = this.sliderNav.size() == 6 ? 3 : -1;
        int BAP_POSITION = this.sliderNav.size() == 6 ? 4 : 3;
        int SETTINGS_POSITION = this.sliderNav.size() == 6 ? 5 : 4;

        if (position == 0) {
            fragment = new FragmentHome();
            Log.d("FRAGMENT", "Fragment home created");
        } else if (position == 1) {
            fragment = new FragmentKalender();
            Log.d("FRAGMENT", "Fragment academic calendar created");
        } else if (position == 2) {
            fragment = new FragmentKeuangan();
            Log.d("FRAGMENT", "Fragment financial status created");
        } else if (position == BAP_POSITION) {
            if (BAP_POSITION == 4) {
                fragment = new FragmentBap();
                Log.d("FRAGMENT", "Fragment BAP mode KM");
            } else {
                fragment = new FragmentTak();
                Log.d("FRAGMENT", "Fragment TAK mode biasa");
            }
        } else if (position == TAK_POSITION) {
            fragment = new FragmentTak();
            Log.d("FRAGMENT", "Fragment TAK mode KM");
        } else if (position == SETTINGS_POSITION) {
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_LONG).show();
            Log.d("FRAGMENT", "Fragment Settings created");
        } else {
            Log.d("FRAGMENT", "No fragment created");
        }

        //check if fragment  not null, and replace with
        //new fragment instance from switch above
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            this.drawerList.setItemChecked(position, true);
            this.drawerList.setSelection(position);
            this.setTitle(this.sliderMenuTitle[position]);
            this.drawerLayout.closeDrawer(this.drawerList);
        } else {
            Log.e("KESALAHAN", "Gagal membuat fragment");
        }
    }
}
