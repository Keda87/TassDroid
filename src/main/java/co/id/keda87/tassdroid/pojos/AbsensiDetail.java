package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 7/9/2014.
 */
public class AbsensiDetail {

    @SerializedName("kd_kelas")
    public String kodeKelas;

    @SerializedName("kd_dosen")
    public String kodeDosen;

    @SerializedName("nama_dosen")
    public String namaDosen;

    @SerializedName("kd_mk")
    public String kodeMataKuliah;

    @SerializedName("nama_mk")
    public String namaMataKuliah;

    @SerializedName("tahun_ajaran")
    public String tahunAjaran;

    public String semester;

    public String pertemuan;

    @SerializedName("status_kehadiran")
    public String statusKehadiran;

    @SerializedName("status_hadir")
    public String statusHadir;

}
