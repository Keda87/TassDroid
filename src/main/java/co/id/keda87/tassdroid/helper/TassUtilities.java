package co.id.keda87.tassdroid.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Adiyat Mubarak
 * Date: 2/25/14
 * Time: 1:04 AM
 */
public class TassUtilities {

    public static final String FONT_PATH_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String FONT_PATH_BOLD = "fonts/Roboto-Bold.ttf";
    public static final int[] colorsStripped = new int[]{0x30FF0000, 0x300000FF}; //konstanta warna stripped di listview
    private static final String BASE_API_URL = "http://tass.telkomuniversity.ac.id/telkomuniversity.php/api?key=";

    /**
     * Fungsi untuk merubah ke dalam bentuk Rupiah
     *
     * @param currency nilai currency yang akan diubah dengan tipe data String
     * @return hasil convert ke dalam bentuk rupiah
     */
    public static String toRupiah(String currency) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();

        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        return df.format(Double.parseDouble(currency));
    }

    /**
     * Fungsi untuk menampilkan Toast, dibuat fungsi ini karena
     * untuk menghindari pengulangan dalam pembuatan Toast.
     *
     * @param context : context pada activity / fragment
     * @param message : isi pesan yang akan ditampilkan
     * @param length  : durasi toast akan ditampilkan, 0 untuk SHORT dan 1 untuk LONG
     */
    public static void showToastMessage(Context context, int message, int length) {
        Toast.makeText(context, message, length == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    /**
     * Fungsi untuk melakukan instance external font
     *
     * @param ctx  application context
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
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        StringBuffer sbuf;
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("KESALAHAN", e.getMessage());
        }

        if (md != null) {
            md.update(password.getBytes());
        }
        byte bytePwd[] = md.digest();

        sbuf = new StringBuffer();
        for (byte aBytePwd : bytePwd) {
            sbuf.append(Integer.toString((aBytePwd & 0xff) + 0x100, 16).substring(1));
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
        return random == 0 ? random + 1 : random;
    }

    /**
     * Fungsi untuk membuat key token
     *
     * @param rand : angka random yang digenerate oleh sistem
     * @return token dengan bentuk md5 hash
     */
    private static String generateToken(int rand) {
        String hasil;
        String c;

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yy HH");//format tanggal
        Date sekarang = new Date();//tanggal hari ini
        String now = sdf.format(sekarang);

        if (rand > 0) {
            int b = rand % 2;
            c = b == 0 ? "aIomd" : "0t54d";
            hasil = c + rand + now;
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
        StringBuffer sb;
        String NEW_LINE = System.getProperty("line.separator");
        String hasil = null;

        //set request timeout
        HttpParams httpParams = new BasicHttpParams();
        int timeout = (int) (30 * DateUtils.SECOND_IN_MILLIS);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet(uri);
        request.setHeader("Content-type", "application/json");

        try {
            HttpResponse response = client.execute(request);
            Log.d("CONNECTION", "Kirim request ke server..");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(NEW_LINE);
                }
            }
            hasil = sb.toString();
        } catch (ClientProtocolException e) {
            Log.e("KESALAHAN CLIENT PROTOCOL", e.getMessage() == null ? "FAG Client Protocol Exception" : e.getMessage());
        } catch (IOException e) {
            Log.e("KESALAHAN IO", e.getMessage() == null ? "FAG IO Exception" : e.getMessage());
        } catch (Exception e) {
            Log.e("KESALAHAN", e.getMessage() == null ? "FAG ALL Exception" : e.getMessage());
        } finally {
            client.getConnectionManager().shutdown();
            Log.d("CONNECTION", "Koneksi ditutup..");
        }

        return hasil;
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

    private static String getTassParameteredApiUrl(String nim, String password, String type, String kodeMk, int random) {
        return BASE_API_URL + md5(password) + "&nim=" + nim + "&type=" + type + "&kdmk=" + kodeMk + "&a=" + random + "&hsl=" + generateToken(random);
    }

    /**
     * Fungsi untuk mengenerate alamat url API siap pakai
     *
     * @param nim      : nim mahasiswa di student portal
     * @param password : password mahasiswa di student portal
     * @param type     : tipe API yang akan dikeluarkan
     * @return hasil akhir dari alamat API yang siap digunakan
     */
    public static String uriBuilder(String nim, String password, String... type) {
        int random = randomGenerator();

        switch (type[0]) {
            case "login":
                return getTassApiUrl(nim, password, type[0], random);
            case "nm":
                return getTassApiUrl(nim, password, type[0], random);
            case "dftap":
                return getTassApiUrl(nim, password, type[0], random);
            case "biodata":
                return getTassApiUrl(nim, password, type[0], random);
            case "keuangan":
                return getTassApiUrl(nim, password, type[0], random);
            case "tak":
                return getTassApiUrl(nim, password, type[0], random);
            case "jadwal":
                return getTassApiUrl(nim, password, type[0], random);
            case "tgsi":
                return getTassApiUrl(nim, password, type[0], random);
            case "tgsk":
                return getTassApiUrl(nim, password, type[0], random);
            case "absensi":
                if (type.length == 2) {
                    return getTassParameteredApiUrl(nim, password, type[0], type[1], random);
                } else {
                    return getTassApiUrl(nim, password, type[0], random);
                }
            case "kalenderakademik":
                return getTassApiUrl(nim, password, type[0], random);
            default:
                return getTassApiUrl(nim, password, type[0], random);
        }
    }

    /**
     * Fungsi untuk merubah hari ke format bahasa indonesia
     *
     * @param day   : hari dalam format inggris
     * @param lang: convert dalam bentuk apa, en: merubah ke format english id: merubah ke format indonesia
     * @return hari : hasil keluaran String berupa hari dalam bahasa indonesia
     */
    public static String toDayID(String day, String lang) {
        String hari = null;
        switch (lang) {
            case "en":
                switch (day) {
                    case "MON":
                        hari = "MONDAY";
                        break;
                    case "TUE":
                        hari = "TUESDAY";
                        break;
                    case "WED":
                        hari = "WEDNESDAY";
                        break;
                    case "THU":
                        hari = "THURSDAY";
                        break;
                    case "FRI":
                        hari = "FRIDAY";
                        break;
                    case "SAT":
                        hari = "SATURDAY";
                        break;
                }
                break;
            case "id":
                switch (day) {
                    case "MON":
                        hari = "SENIN";
                        break;
                    case "TUE":
                        hari = "SELASA";
                        break;
                    case "WED":
                        hari = "RABU";
                        break;
                    case "THU":
                        hari = "KAMIS";
                        break;
                    case "FRI":
                        hari = "JUMAT";
                        break;
                    case "SAT":
                        hari = "SABTU";
                        break;
                }
                break;
        }
        return hari;
    }

    public static void main(String[] args) {
//        nim adit
//        String nim = "6301114139";
//        String pwd = "asaibae";

//        nim percobaan
        String nim = "6301124173";
        String pwd = "240294";

        System.out.println(TassUtilities.uriBuilder(nim, pwd, "kalenderakademik"));
    }
}
