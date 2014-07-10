package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 7/11/2014.
 */
public class KalenderAkademik {

    @SerializedName("tahun_ajaran")
    public String tahunAjaran;

    public String semester;

    @SerializedName("tanggal_mulai")
    public String tanggalMulai;

    @SerializedName("tanggal_akhir")
    public String tanggalAkhir;

    public String status;

    @SerializedName("nama_kegiatan")
    public String namaKegiatan;
}
