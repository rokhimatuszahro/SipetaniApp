package com.mobile.sipetaniapp.Model;

public class DataModel {
    String no, nama, tgl, total, id_pemesanan;
    int iconprint;

    public DataModel(){}

    public DataModel(String no, String nama, String tgl, String total, String id_pemesanan, int iconprint){
        this.no = no;
        this.nama = nama;
        this.tgl = tgl;
        this.total = total;
        this.id_pemesanan = id_pemesanan;
        this.iconprint = iconprint;
    }

    public String getId_pemesanan()
    {
        return id_pemesanan;
    }

    public void setId_pemesanan(String id_pemesanan)
    {
        this.id_pemesanan = id_pemesanan;
    }

    public String getNo()
    {
        return no;
    }

    public void setNo(String no)
    {
        this.no = no;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getTgl()
    {
        return tgl;
    }

    public void setTgl(String tgl)
    {
        this.tgl = tgl;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public int getIconprint()
    {
        return iconprint;
    }

    public void setIconprint(int print)
    {
        this.iconprint = print;
    }
}
