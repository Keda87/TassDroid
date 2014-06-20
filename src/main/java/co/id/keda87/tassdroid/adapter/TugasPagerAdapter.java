package co.id.keda87.tassdroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import co.id.keda87.tassdroid.fragment.FragmentTugasIndividu;
import co.id.keda87.tassdroid.fragment.FragmentTugasKelompok;

/**
 * Created by Keda87 on 6/20/2014.
 */
public class TugasPagerAdapter extends FragmentPagerAdapter {

    public TugasPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragmentTugasIndividu();
            case 1:
                return new FragmentTugasKelompok();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
