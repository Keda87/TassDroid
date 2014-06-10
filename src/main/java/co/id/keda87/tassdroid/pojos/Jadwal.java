package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/6/2014.
 */
public class Jadwal {

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
}
