package co.id.keda87.tassdroid.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.SliderListAdapter;
import co.id.keda87.tassdroid.fragment.*;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.SliderItem;
import co.id.keda87.tassdroid.pojos.TugasIndividu;
import co.id.keda87.tassdroid.pojos.TugasKelompok;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

    private static final int TASK_NOTIFICATION_ID = 1;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.list_slider)
    ListView drawerList;
    SessionManager session;
    Gson gson;
    String username;
    String password;
    private NotificationManager notificationManager;
    private Notification notification;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] sliderMenuTitle;
    private TypedArray sliderMenuIcon;
    private List<SliderItem> sliderNav;
    private SliderListAdapter sliderAdapter;
    private SessionManager sessionManager;
    private SharedPreferences preferences;
    private Integer TUGAS_COUNT = 0;
    private boolean isChecked = false;

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(this.drawerList);
            Log.d("NAVIGATION", "Keadaan terbuka..");
        } else {
            Fragment fragment = new FragmentHome();
            this.drawerList.setItemChecked(0, true);
            this.drawerList.setSelection(0);
            this.setTitle(this.sliderMenuTitle[0]);
            this.drawerLayout.closeDrawer(this.drawerList);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            Log.d("NAVIGATION", "Keadaan fragment selain home");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!session.isLoggedIn()) {
            Log.d("SESSION", "Gak ada session disimpan");
            Intent i = new Intent(getApplicationContext(), MyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            Log.d("SESSION", "Masih ada session tersimpan");
            if (TassUtilities.isConnected(this)) {
                if (!isChecked) {
                    new CheckIndividu().execute();
                    new CheckKelompok().execute();
                    isChecked = true;
                }
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.inject(this);

        session = new SessionManager(MainMenuActivity.this);
        gson = new Gson();
        username = session.getUserDetails().get(SessionManager.KEY_USERNAME);
        password = session.getUserDetails().get(SessionManager.KEY_PASSWORD);

        //instance session manager
        this.sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> userSession = sessionManager.getUserDetails();

        //get title each menu
        this.drawerTitle = this.title = getTitle();

        //load slider item title
        this.sliderMenuTitle = getResources().getStringArray(R.array.nav_drawer_items);

        //load slider item icon
        this.sliderMenuIcon = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        //instance preferences
        this.preferences = getSharedPreferences("co.id.keda87.tassdroid", MODE_PRIVATE);
        this.session = new SessionManager(this);

        //display showcase at the first launch
        if (this.preferences.getBoolean("showcase", true)) {
            //launch showcase activity
            startActivity(new Intent(this, ShowcaseActivity.class));
            Log.d("SHOWCASE", "Aplikasi baru pertama kali jalan..");
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
        int PASSWORD_POSITION = this.sliderNav.size() == 6 ? 5 : 4;

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
        } else if (position == PASSWORD_POSITION) {
            startActivity(new Intent(getApplication(), ActivityGantiPassword.class));
            this.drawerLayout.closeDrawer(this.drawerList);
            Log.d("ACTIVITY", "Activity Ganti Password created");
        } else {
            Log.d("FRAGMENT", "No fragment created");
        }

        //check if fragment not null, and replace with
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

    private class CheckIndividu extends AsyncTask<String, Void, TugasIndividu[]> {
        @Override
        protected TugasIndividu[] doInBackground(String... params) {
            String URL_IND = TassUtilities.uriBuilder(username, password, "tgsi");
            Log.d("API INDIVIDU", URL_IND);

            TugasIndividu[] individu = null;
            try {
                individu = gson.fromJson(TassUtilities.doGetJson(URL_IND), TugasIndividu[].class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return individu;
        }

        @Override
        protected void onPostExecute(TugasIndividu[] tugasIndividus) {
            super.onPostExecute(tugasIndividus);
            if (tugasIndividus != null) {
                TUGAS_COUNT += tugasIndividus.length;
            }
        }
    }

    private class CheckKelompok extends AsyncTask<String, Void, TugasKelompok[]> {
        @Override
        protected TugasKelompok[] doInBackground(String... params) {
            String URL_KEL = TassUtilities.uriBuilder(username, password, "tgsk");
            Log.d("API KELOMPOK", URL_KEL);

            TugasKelompok[] kels = null;
            try {
                kels = gson.fromJson(TassUtilities.doGetJson(URL_KEL), TugasKelompok[].class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return kels;
        }

        @Override
        protected void onPostExecute(TugasKelompok[] kel) {
            super.onPostExecute(kel);
            if (kel != null) {
                TUGAS_COUNT += kel.length;
            }

            // display tugas if TUGAS_COUNT > 0
            if (TUGAS_COUNT > 0) {

                // create notification
                notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(getResources().getString(R.string.notif_title))
                        .setContentText(TUGAS_COUNT + " " + getResources().getString(R.string.notif_content))
                        .setTicker(getResources().getString(R.string.notif_thicker))
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setSmallIcon(R.drawable.ic_tugas)
                        .setAutoCancel(true)
                        .getNotification(); // use .build instead .getNotification for api 16 or above

                // displaying notificaton
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(TASK_NOTIFICATION_ID, notification);

                // set TUGAS_COUNT to 0
                TUGAS_COUNT = 0;
            }
        }
    }
}
