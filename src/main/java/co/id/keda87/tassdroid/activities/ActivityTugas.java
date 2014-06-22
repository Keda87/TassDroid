package co.id.keda87.tassdroid.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.TugasPagerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 8:48 PM
 */
public class ActivityTugas extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private TugasPagerAdapter tugasPagerAdapter;
    private ActionBar actionBar;
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);

        //create instance
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.actionBar = getActionBar();
        this.tugasPagerAdapter = new TugasPagerAdapter(getSupportFragmentManager());
        this.tabs = getResources().getStringArray(R.array.judul_tab_tugas);

        this.viewPager.setAdapter(this.tugasPagerAdapter);
        this.actionBar.setHomeButtonEnabled(false);
        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //adding tabs
        for (String tab : this.tabs) {
            this.actionBar.addTab(this.actionBar.newTab().setText(tab).setTabListener(this));
        }

        //set pager action listener
        this.viewPager.setOnPageChangeListener(this);

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnTugas));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                switch (this.actionBar.getSelectedNavigationIndex()) {
                    case 0:
                        Log.d("REFRESH", "Muat ulang tab individu..");
                        break;
                    case 1:
                        Log.d("REFRESH", "Muat ulang tab kelompok..");
                        break;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        this.actionBar.setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
