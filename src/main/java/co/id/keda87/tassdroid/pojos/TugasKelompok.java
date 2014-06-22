package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/22/2014.
 */
public class TugasKelompok {

    @SerializedName("KD_MK")
    public String kodeMk;

    @SerializedName("TGL_BUAT")
    public String tanggalBuat;

    @SerializedName("NAMA_MK")
    public String namaMk;

    @SerializedName("NAMA_TUGAS")
    public String namaTugas;

    @SerializedName("TGL_KUMPUL")
    public String tanggalKumpul;

    @SerializedName("KD_TUGAS")
    public String kodeTugas;

    @SerializedName("KETERANGAN")
    public String keterangan;

    public String keterlambatan;

    public String ijin;

    @SerializedName("nim_kelompok")
    public String nimKelompok;

}
