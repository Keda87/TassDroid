package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 7/2/2014.
 */
public class Absensi {

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
}
