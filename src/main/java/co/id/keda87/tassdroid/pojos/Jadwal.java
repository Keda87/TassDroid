package co.id.keda87.tassdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/6/2014.
 */
public class Jadwal implements Parcelable {

    public static final Creator<Jadwal> CREATOR = new Creator<Jadwal>() {
        @Override
        public Jadwal createFromParcel(Parcel source) {
            return new Jadwal(source);
        }

        @Override
        public Jadwal[] newArray(int size) {
            return new Jadwal[size];
        }
    };
    public String hari;
    @SerializedName("kd_mk")
    public String kodeMk;
    @SerializedName("kd_kelas")
    public String kodeKelas;
    @SerializedName("kd_dosen")
    public String kodeDosen;
    @SerializedName("kd_ruang")
    public String kodeRuang;
    @SerializedName("waktu_mulai")
    public String waktuMulai;
    @SerializedName("waktu_selesai")
    public String waktuSelesai;
    @SerializedName("nama")
    public String namaDosen;
    @SerializedName("nama_matakuliah")
    public String mataKuliah;

    public Jadwal() {
    }

    public Jadwal(Parcel in) {
        this.hari = in.readString();
        this.kodeMk = in.readString();
        this.kodeKelas = in.readString();
        this.kodeDosen = in.readString();
        this.kodeRuang = in.readString();
        this.waktuMulai = in.readString();
        this.waktuSelesai = in.readString();
        this.namaDosen = in.readString();
        this.mataKuliah = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hari);
        dest.writeString(this.kodeMk);
        dest.writeString(this.kodeKelas);
        dest.writeString(this.kodeDosen);
        dest.writeString(this.kodeRuang);
        dest.writeString(this.waktuMulai);
        dest.writeString(this.waktuSelesai);
        dest.writeString(this.namaDosen);
        dest.writeString(this.mataKuliah);
    }
}
