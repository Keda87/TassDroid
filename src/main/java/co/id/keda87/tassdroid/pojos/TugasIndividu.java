package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/21/2014.
 */
public class TugasIndividu {

    public String no;

    @SerializedName("mata_kuliah")
    public String mataKuliah;

    @SerializedName("nama_tugas")
    public String namaTugas;

    public String deadline;
}
