package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.id.keda87.tassdroid.R;

/**
 * Created by Keda87 on 7/30/2014.
 */
public class ShowcaseItemActivity extends Activity {

    private SharedPreferences preferences;
    private FrameLayout overlay;
    private View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overlay = new FrameLayout(this);
        this.view = getLayoutInflater().inflate(R.layout.showcase_item, overlay, false);
        this.preferences = getSharedPreferences("co.id.keda87.tassdroid", MODE_PRIVATE);
        this.overlay.addView(this.view);

        setContentView(R.layout.showcase_item);
        ButterKnife.inject(this);
    }

    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.buttonShowAbsen)
    void closeShowcase() {
        this.preferences.edit().putBoolean("showcase_absen", false).commit();
        finish();
    }
}