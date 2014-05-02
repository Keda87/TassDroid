package co.id.keda87.tassdroid.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Adiyat Mubarak
 * Date: 2/25/14
 * Time: 1:04 AM
 */
public class TassUtilities {

    private static final String BASE_API_URL = "http://tass.telkomuniversity.ac.id/telkomuniversity.php/api?key=";
    public static final String FONT_PATH_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String FONT_PATH_BOLD = "fonts/Roboto-Bold.ttf";
    private Context context;

    /**
     * Fungsi untuk melakukan instance external font
     *
     * @param ctx application context
     * @param type font type, 0 = LIGHT, 1 = BOLD
     * @return roboto typeface
     */
    public static Typeface getFontFace(Context ctx, int type) {
        switch (type) {
            case 0:
                return Typeface.createFromAsset(ctx.getAssets(), FONT_PATH_LIGHT);
            case 1:
                return Typeface.createFromAsset(ctx.getAssets(), FONT_PATH_BOLD);
            default:
                return Typeface.createFromAsset(ctx.getAssets(), FONT_PATH_LIGHT);
        }
    }

    /**
     * Fungsi untuk cek ketersediaan koneksi internet/wifi
     *
     * @param context : context aplikasi
     * @return true jika terhubung ke internet
     */
    public boolean isConnected(Context context) {
        this.context = context;

        ConnectivityManager conn = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo[] info = conn.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo ob : info) {
                    if (ob.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Fungsi untuk mengubah String menjadi md5 hash
     *
     * @param password : plain password mahasiswa di student portal
     * @return md5 hash result
     */
    public static String md5(String password) {
        StringBuffer sbuf = null;
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("KESALAHAN", e.getMessage());
        } catch (Exception e) {
            Log.e("KESALAHAN", e.getMessage());
        }

        md.update(password.getBytes());
        byte bytePwd[] = md.digest();

        if (sbuf == null) {
            sbuf = new StringBuffer();
            for (int i = 0; i < bytePwd.length; i++) {
                sbuf.append(Integer.toString((bytePwd[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        return sbuf.toString();
    }

    /**
     * Fungsi untuk generate angka random
     *
     * @return angka random
     */
    private static int randomGenerator() {
        int random = (int) (Math.random() * 10);
        int a = random == 0 ? random + 1 : random;
        return a;
    }

    /**
     * Fungsi untuk membuat key token
     *
     * @param rand : angka random yang digenerate oleh sistem
     * @return token dengan bentuk md5 hash
     */
    private static String generateToken(int rand) {
        int a = rand;
        String hasil = "";
        String c = "";

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yy HH");//format tanggal
        Date sekarang = new Date();//tanggal hari ini
        String now = sdf.format(sekarang);

        if (a > 0) {
            int b = a % 2;
            c = b == 0 ? "aIomd" : "0t54d";
            hasil = c + a + now;
            return md5(hasil);
        } else {
            return hasil = "inputanbukanangkatetapistring";
        }
    }

    /**
     * Fungsi untuk mengambil data JSON dari API
     * menggunakan method GET
     *
     * @param uri merupakan alamat API yang akan dipanggil
     * @return hasil JSON dari alamat API
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String doGetJson(String uri) {
        StringBuffer sb = null;
        String NEW_LINE = System.getProperty("line.separator");

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(NEW_LINE);
                }
            }
        } catch (IOException e) {
            Log.e("KESALAHAN", e.getMessage());
        } catch (Exception e) {
            Log.e("KESALAHAN", e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Fungsi untuk generate alamat API mentah
     *
     * @param nim      : nim mahasiswa di student portal
     * @param password : password mahasiswa di student portal
     * @param type     : tipe API yang akan dikeluarkan
     * @param random   : angka random yang digenerate aplikasi
     * @return hasil URL mentah dari API yang akan digunakan
     */
    private static String getTassApiUrl(String nim, String password, String type, int random) {
        return BASE_API_URL + md5(password) + "&nim=" + nim + "&type=" + type + "&a=" + random + "&hsl=" + generateToken(random);
    }

    /**
     * Fungsi untuk mengenerate alamat url API siap pakai
     *
     * @param nim      : nim mahasiswa di student portal
     * @param password : password mahasiswa di student portal
     * @param type     : tipe API yang akan dikeluarkan
     * @return hasil akhir dari alamat API yang siap digunakan
     */
    public static String uriBuilder(String nim, String password, String type) {
        int random = randomGenerator();

        switch (type) {
            case "login":
                return getTassApiUrl(nim, password, type, random);
            case "nm":
                return getTassApiUrl(nim, password, type, random);
            case "dftap":
                return getTassApiUrl(nim, password, type, random);
            case "biodata":
                return getTassApiUrl(nim, password, type, random);
            default:
                return getTassApiUrl(nim, password, type, random);
        }
    }

    public static void main(String[] args) {
//        nim adit
        String nim = "6301114139";
        String pwd = "asaibae";

//        nim percobaan
//        String nim = "6301124173";
//        String pwd = "240294";

        System.out.println(TassUtilities.uriBuilder(nim, pwd, "login"));
        System.out.println(TassUtilities.uriBuilder(nim, pwd, "nm"));
        System.out.println(TassUtilities.uriBuilder(nim, pwd, "dftap"));
        System.out.println(TassUtilities.uriBuilder(nim, pwd, "biodata"));
    }
}
