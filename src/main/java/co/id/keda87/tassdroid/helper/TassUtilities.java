package co.id.keda87.tassdroid.helper;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    static final String BASE_API_URL = "http://tass.telkomuniversity.ac.id/telkomuniversity.php/api?key=";

    /**
     * Fungsi untuk mengubah String menjadi hash dengan metode md5
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
     * Fungsi untuk mengeluarkan data json dari API
     *
     * @param apiUrl alamat API yang akan dibaca
     * @return json hasil keluaran dari API
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPlainJSON(String apiUrl) {
        StringBuilder builder = null;
        try {
            URL url = new URL(apiUrl);
            URLConnection urlc = url.openConnection();

            try (InputStreamReader inStream = new InputStreamReader(urlc.getInputStream());
                 BufferedReader buff = new BufferedReader(inStream)) {

                builder = new StringBuilder();
                while (true) {
                    String nextLine = buff.readLine();
                    String NL = System.getProperty("line.separator");
                    if (nextLine != null) {
                        builder.append(nextLine).append(NL);
                    } else {
                        break;
                    }
                }
            }

        } catch (MalformedURLException ex) {
            Log.e("KESALAHAN", ex.getMessage());
        } catch (IOException ex) {
            Log.e("KESALAHAN", ex.getMessage());
        }
        return builder.toString();
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
     * Fungsi API url generator, yang akan menghasilkan alamat url siap pakai
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
            default:
                return getTassApiUrl(nim, password, type, random);
        }
    }

    public static void main(String[] args) {
        String nim = "6301114139";
        String pwd = "asaibae";

        System.out.println(TassUtilities.uriBuilder(nim, pwd, "login"));
        System.out.println(TassUtilities.uriBuilder(nim, pwd, "nm"));
        System.out.println(TassUtilities.uriBuilder(nim, pwd, "dftap"));
    }
}
