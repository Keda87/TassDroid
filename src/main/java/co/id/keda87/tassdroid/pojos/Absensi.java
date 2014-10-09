package co.id.keda87.tassdroid.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 7/2/2014.
 */
public class Absensi implements Parcelable {

    @SerializedName("kd_kelas")
    public String kodeKelas;

    @SerializedName("kd_dosen")
    public String kodeDosen;

    @SerializedName("nama_dosen")
    public String namaDosen;

    @SerializedName("kd_mk")
    public String kodeMk;

    @SerializedName("nama_mk")
    public String namaMk;

    @SerializedName("tahun_ajaran")
    public String tahunAjaran;

    public String semester;

    @SerializedName("jml_pertemuan")
    public String jumlahPertemuan;

    @SerializedName("jml_hadir")
    public String jumlahHadir;

    @SerializedName("prosen_hadir")
    public String prosenHadir;

    public Absensi() {
    }

    public Absensi(Parcel in) {
        this.kodeKelas = in.readString();
        this.kodeDosen = in.readString();
        this.namaDosen = in.readString();
        this.kodeMk = in.readString();
        this.namaMk = in.readString();
        this.tahunAjaran = in.readString();
        this.semester = in.readString();
        this.jumlahPertemuan = in.readString();
        this.jumlahHadir = in.readString();
        this.prosenHadir = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kodeKelas);
        dest.writeString(this.kodeDosen);
        dest.writeString(this.namaDosen);
        dest.writeString(this.kodeMk);
        dest.writeString(this.namaMk);
        dest.writeString(this.tahunAjaran);
        dest.writeString(this.semester);
        dest.writeString(this.jumlahPertemuan);
        dest.writeString(this.jumlahHadir);
        dest.writeString(this.prosenHadir);
    }

    public static final Creator<Absensi> CREATOR = new Creator<Absensi>() {
        @Override
        public Absensi createFromParcel(Parcel source) {
            return new Absensi(source);
        }

        @Override
        public Absensi[] newArray(int size) {
            return new Absensi[size];
        }
    };
}
