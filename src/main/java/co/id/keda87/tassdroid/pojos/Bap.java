package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/26/2014.
 */
public class Bap {

    public String no;

    @SerializedName("kode_matakuliah")
    public String kodeMataKuliah;

    @SerializedName("mata_kuliah")
    public String mataKuliah;

    public String kelas;

    public String dosen;

    @SerializedName("jumlah_pertemuan")
    public String jumlahPertemuan;

    @SerializedName("jumlah_pertemuan_approval")
    public String jumlahPertemuanApproval;

    @SerializedName("jumlah_pertemuan_belum_approve")
    public String jumlahPertemuanBelumApprove;
}
