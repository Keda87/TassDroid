package co.id.keda87.tassdroid.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Adiyat Mubarak
 * Date: 3/8/14
 * Time: 11:36 AM
 */
public class SessionManager {

    private SharedPreferences preferences;
    private Editor editor;
    private Context context;

    private static final int PRIVATE_MODE = 0;
    private static final String PREFERENCES_NAME = "CREDENTIAL";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_KM = "ketua_kelas";

    public SessionManager(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createSession(String username, String password, String km) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_KM, km);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
        user.put(KEY_KM, preferences.getString(KEY_KM, null));

        return user;
    }


}