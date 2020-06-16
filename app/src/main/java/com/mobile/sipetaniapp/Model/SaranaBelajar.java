package com.mobile.sipetaniapp.Model;

public class SaranaBelajar {
    private String judul;
    private String keterangan;
    private int imgSaranaBelajar;

    public SaranaBelajar(String judul, String keterangan, int imgSaranaBelajar) {
        this.judul = judul;
        this.keterangan = keterangan;
        this.imgSaranaBelajar = imgSaranaBelajar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getImgSaranaBelajar() {
        return imgSaranaBelajar;
    }

    public void setImgSaranaBelajar(int imgSaranaBelajar) {
        this.imgSaranaBelajar = imgSaranaBelajar;
    }
}
