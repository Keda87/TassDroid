package co.id.keda87.tassdroid.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import co.id.keda87.tassdroid.activities.MyActivity;

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

    /**
     * Fungsi untuk menyimpan session ke preferences
     * ketika user berhasil login ke aplikasi
     *
     * @param username : username student portal
     * @param password : password student portal
     * @param km       : status KM pada keluaran JSON
     */
    public void createSession(String username, String password, String km) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_KM, km);

        editor.commit();
    }

    /**
     * Fungsi untuk menghapus session pada preferences
     * ketika user logout dari aplikasi dan kembali ke
     * login activity
     */
    public void destroySession() {
        editor.clear();
        editor.commit();

        //move to Login Activity
        Intent intent = new Intent(context, MyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//closing all activities
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//add new flag to start new activity

        context.startActivity(intent);
    }

    /**
     * Fungsi untuk membaca detail data pengguna pada aplikasi
     * yang di tampung pada HashMap dan diambil dari preferences
     *
     * @return HashMap data pengguna
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));
        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
        user.put(KEY_KM, preferences.getString(KEY_KM, null));

        return user;
    }

    /**
     * Fungsi untuk mengecek apakah status login user
     * bernilai benar atau tidak
     *
     * @return true jika user telah berhasil login sebelumnya
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    /**
     * Fungsi untuk mengecek apakah user telah login, jika tidak
     * maka akan dikembalikan ke login activity
     */
    public void loginCheck() {
        if (!this.isLoggedIn()) {

            //move to Login Activity
            Intent intent = new Intent(context, MyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//closing all activities
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//add new flag to start new activity

            context.startActivity(intent);
        }
    }


}