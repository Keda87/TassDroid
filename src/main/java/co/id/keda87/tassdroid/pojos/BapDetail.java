package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 7/20/2014.
 */
public class BapDetail {

    public String kelas;

    public String dosen;

    @SerializedName("nama_dosen")
    public String namaDosen;

    public String mk;

    @SerializedName("nama_matakuliah")
    public String namaMataKuliah;

    @SerializedName("tahun_ajaran")
    public String tahunAjaran;

    public String semester;

    public String pertemuan;

    public String hari;

    public String tanggal;

    @SerializedName("waktu_mulai")
    public String waktuMulai;

    @SerializedName("waktu_selesai")
    public String waktuSelesai;

    @SerializedName("status_approve_mk")
    public String statusApproveMk;

}
