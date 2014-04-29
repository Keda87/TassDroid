package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.Fragment;
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
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.SliderListAdapter;
import co.id.keda87.tassdroid.fragment.FProfile;
import co.id.keda87.tassdroid.pojos.SliderItem;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get title each menu
        this.drawerTitle = this.title = getTitle();

        //load slider item title
        this.sliderMenuTitle = getResources().getStringArray(R.array.nav_drawer_items);

        //load slider item icon
        this.sliderMenuIcon = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerList = (ListView) findViewById(R.id.list_slider);
        this.sliderNav = new ArrayList<>();

        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[0], this.sliderMenuIcon.getResourceId(0, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[1], this.sliderMenuIcon.getResourceId(1, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[2], this.sliderMenuIcon.getResourceId(2, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[3], this.sliderMenuIcon.getResourceId(3, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[4], this.sliderMenuIcon.getResourceId(4, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[5], this.sliderMenuIcon.getResourceId(5, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[6], this.sliderMenuIcon.getResourceId(6, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[7], this.sliderMenuIcon.getResourceId(7, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[8], this.sliderMenuIcon.getResourceId(8, -1)));
        this.sliderNav.add(new SliderItem(this.sliderMenuTitle[9], this.sliderMenuIcon.getResourceId(9, -1)));

        //check if user is a class leader, will add into slider
        if (true) {
            this.sliderNav.add(new SliderItem(this.sliderMenuTitle[10], this.sliderMenuIcon.getResourceId(10, -1)));
        }

        this.sliderMenuIcon.recycle();

        //create instance slider list adapter
        this.sliderAdapter = new SliderListAdapter(getApplicationContext(), this.sliderNav);

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
            case R.id.app_setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = this.drawerLayout.isDrawerOpen(this.drawerList);
        menu.findItem(R.id.app_setting).setVisible(!drawerOpen);
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
     * @param position
     */
    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FProfile();
                break;
            default:
                break;
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
