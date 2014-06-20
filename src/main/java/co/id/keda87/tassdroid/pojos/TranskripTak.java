package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by Keda87 on 5/28/2014.
 */
public class TranskripTak implements Comparable<TranskripTak> {

    public String no;

    public String nim;

    public String poin;

    public String semester;

    public String tahun;
    public static Comparator<TranskripTak> TahunComparator = new Comparator<TranskripTak>() {
        @Override
        public int compare(TranskripTak lhs, TranskripTak rhs) {
            return lhs.tahun.compareTo(rhs.tahun);
        }
    };
    @SerializedName("parameter_penilaian")
    public String parameterPenilaian;
    @SerializedName("nama_jenis_kegiatan")
    public String namaJenisKegiatan;
    @SerializedName("nama_bagian")
    public String namaBagian;

    @Override
    public int compareTo(TranskripTak another) {
        return 0;
    }
}
