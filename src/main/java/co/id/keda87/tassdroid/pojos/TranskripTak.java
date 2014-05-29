package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 5/28/2014.
 */
public class TranskripTak {

    public String no;

    public String nim;

    public String poin;

    public String semester;

    public String tahun;

    @SerializedName("parameter_penilaian")
    public String parameterPenilaian;

    @SerializedName("nama_jenis_kegiatan")
    public String namaJenisKegiatan;

    @SerializedName("nama_bagian")
    public String namaBagian;

}
