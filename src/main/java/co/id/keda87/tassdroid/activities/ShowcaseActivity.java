package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import co.id.keda87.tassdroid.R;

/**
 * Created by Keda87 on 6/26/2014.
 */
public class ShowcaseActivity extends Activity {

    private FrameLayout overlay;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.overlay = new FrameLayout(getApplicationContext());
        this.view = getLayoutInflater().inflate(R.layout.showcase_menu, overlay, false);

        setContentView(this.overlay);
        this.overlay.addView(this.view);
    }

    @Override
    public void onBackPressed() {
        Log.d("FRAME", "Showcase back..");
    }

    public void closeShowcase(View view) {
        finish();
        Log.d("FRAME", "I got it..");
    }
}
