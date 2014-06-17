package co.id.keda87.tassdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/11/2014.
 */
public class NilaiMentah implements Parcelable {

    public static final Creator<NilaiMentah> CREATOR = new Creator<NilaiMentah>() {
        @Override
        public NilaiMentah createFromParcel(Parcel source) {
            return new NilaiMentah(source);
        }

        @Override
        public NilaiMentah[] newArray(int size) {
            return new NilaiMentah[size];
        }
    };
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

    public NilaiMentah() {
    }

    public NilaiMentah(Parcel in) {
        this.kodeMk = in.readString();
        this.mataKuliah = in.readString();
        this.sks = in.readString();
        this.semester = in.readString();
        this.kelas = in.readString();
        this.kodeDosen = in.readString();
        this.kajian1 = in.readString();
        this.kajian2 = in.readString();
        this.kajian3 = in.readString();
        this.tugas = in.readString();
        this.praktikum = in.readString();
        this.nilaiIndex = in.readString();
        this.jumlahAlpha = in.readString();
        this.keterangan = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kodeMk);
        dest.writeString(this.mataKuliah);
        dest.writeString(this.sks);
        dest.writeString(this.semester);
        dest.writeString(this.kelas);
        dest.writeString(this.kodeDosen);
        dest.writeString(this.kajian1);
        dest.writeString(this.kajian2);
        dest.writeString(this.kajian3);
        dest.writeString(this.tugas);
        dest.writeString(this.praktikum);
        dest.writeString(this.nilaiIndex);
        dest.writeString(this.jumlahAlpha);
        dest.writeString(this.keterangan);
    }
}
