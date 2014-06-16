package co.id.keda87.tassdroid.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Keda87 on 6/11/2014.
 */
public class NilaiMentah {
    public NilaiMentah(String kodeMk, String mataKuliah, String sks, String semester, String kelas, String kodeDosen, String kajian1, String kajian2, String kajian3, String tugas, String praktikum, String nilaiIndex, String jumlahAlpha, String keterangan) {
        this.kodeMk = kodeMk;
        this.mataKuliah = mataKuliah;
        this.sks = sks;
        this.semester = semester;
        this.kelas = kelas;
        this.kodeDosen = kodeDosen;
        this.kajian1 = kajian1;
        this.kajian2 = kajian2;
        this.kajian3 = kajian3;
        this.tugas = tugas;
        this.praktikum = praktikum;
        this.nilaiIndex = nilaiIndex;
        this.jumlahAlpha = jumlahAlpha;
        this.keterangan = keterangan;
    }

    @SerializedName("kodemk")
    public String kodeMk;

    @SerializedName("matakuliah")
    public String mataKuliah;

    public String sks;

    public String semester;

    public String kelas;

    @SerializedName("kodedosen")
    public String kodeDosen;

    public String kajian1;

    public String kajian2;

    public String kajian3;

    public String tugas;

    public String praktikum;

    @SerializedName("nilaiindex")
    public String nilaiIndex;

    @SerializedName("jumlahalpa")
    public String jumlahAlpha;

    public String keterangan;

}
