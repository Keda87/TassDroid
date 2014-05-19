package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 5/15/2014.
 */
public class StatusKeuangan {

    public String no;

    public String semester;

    @SerializedName("tahun_ajaran")
    public String tahunAjaran;

    @SerializedName("nama_tarif")
    public String namaTarif;

    @SerializedName("tanggal_bayar")
    public String tanggalBayar;

    @SerializedName("jml_bayar")
    public String jumlahBayar;

    public String status;

    @SerializedName("no_kuitansi")
    public String nomorKuitansi;
}
