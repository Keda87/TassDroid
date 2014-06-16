package co.id.keda87.tassdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/11/2014.
 */
public class NilaiMentah implements Parcelable {

    @SerializedName("kodemk")
    public String kodeMk;

    @SerializedName("matakuliah")
    public String mataKuliah;

    public String sks;

    public String semester;

    public String kelas;

    @SerializedName("kodedosen")
    public String kodeDosen;

    public String kajian1;

    public String kajian2;

    public String kajian3;

    public String tugas;

    public String praktikum;

    @SerializedName("nilaiindex")
    public String nilaiIndex;

    @SerializedName("jumlahalpa")
    public String jumlahAlpha;

    public String keterangan;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
