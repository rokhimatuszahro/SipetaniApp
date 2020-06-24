package com.mobile.sipetaniapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SaranaFragment extends Fragment implements SaranaBelajarAdapter.OnSaranaBelajarClickListener {
    public RecyclerView rv;
    public SaranaBelajarAdapter saranaBelajarAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public List<SaranaBelajar> listSaranaBelajar = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_sarana, container, false);

        rv = (RecyclerView) rootview.findViewById(R.id.rvSaranaBelajar);

        listSaranaBelajar.add(new SaranaBelajar("KOLEKSI 500 JENIS FLORA DAN FAUNA", "Taman Botani Sukorambi memiliki 500 koleksi flora dan\n" +
                "fauna yang tersebar di beberapa sudut taman.\n" + "Tidak sedikit tanaman yang ada tergolong langkah.\n" +
                "Demikian pula ada beberapa tanaman yang berasal dari\n" + "luar Jawa, atau luar negeri.", R.drawable.bunga));
        listSaranaBelajar.add(new SaranaBelajar("ANEKA KOLEKSI HEWAN DILINDUNGI", "Ada tiga jenis hewan dilindungi di Taman Botani\n" +
                "Sukorambi, yaitu kijang, rusa, dan burung merak.\n" + "Ada 4 kijang, 12 rusa dan 4 burug merak.\n" +
                "Lokasi hewan tersebut berada di taman yang cukup luas,\n" + "sehingga mudah dilihat oleh para pengunjung.", R.drawable.rusa));
        listSaranaBelajar.add(new SaranaBelajar("ANEKA KOLEKSI HEWAN HIAS", "Taman Botani Sukorambi memiliki koleksi hewan hias yang\n" +
                "beragam. Seperti landak, marmut, aneka jenis burung\n" +
                "seperti burung gagak, love bird dan lainnya. Begitu juga\n" +
                "dengan koleksi ikan yang cukup banyak, mulai dari ikan\n" +
                "koi, nila, gurame, lele dan patin.", R.drawable.kelinci));
        listSaranaBelajar.add(new SaranaBelajar("PEMBENIHAN IKAN KOI DAN LOBSTER", "Taman Botani Sukorambi tidak hanya menyenangkan\n" +
                "untuk keluarga. Kini di taman ini juga menjadi lokasi\n" +
                "pembenihan dan pembesaran ikan koi dan lobster air\n" +
                "tawar. Ada sekitar 9 kolam untuk pembudidayaan.", R.drawable.lopster));
        listSaranaBelajar.add(new SaranaBelajar("BUNNY AND FRIENDS VILLAGE", "Ada ratusan koleksi kelinci di Taman Botani Sukorambi\n" +
                "yang dipelihara dan dirawat di tempat khusus. Yakni\n" +
                "di dekat kantor pengelola. Hewan lucu itu berada dalam\n" +
                "satu kandang dan bisa dipelajari perilakunya oleh\n" + "masyarakat luas.", R.drawable.buny));
        listSaranaBelajar.add(new SaranaBelajar("FLYING FOX", "Di Taman Botani Sukorambi terdapat wahana yang bisa\n" +
                "Anda rasakan untuk memacu adrenalin. Yaitu Flying Fox\n" +
                "Jungle dan Flying Fox Tombro. Dengan dilengkapi\n" +
                "berbagai alat pengaman, wahana ini layak dicoba bagi\n" +
                "Anda yang menyukai petualangan ekstrim.", R.drawable.flayingfox));
        listSaranaBelajar.add(new SaranaBelajar("KOLAM RENANG", "Fasilitas kolam renang di Taman Botani Sukorambi ini\n" +
                "memiliki 5 kolam renang, antara lain : Kolam Renang\n" +
                "Dewasa, Kolam Renang Remaja, Kolam Renang Anak,\n" +
                "Kolam Renang Pelangi 1 dan Kolam Renang Pelangi 2.", R.drawable.kolam));
        listSaranaBelajar.add(new SaranaBelajar("ANEKA PERMAINAN", "Taman rekreasi yang berada di daerah Sukorambi ini\n" +
                "juga menjadi pilihan tepat untuk outbond. Di sini,\n" +
                "pengunjung bisa request permainan yang ingin\n" + "dilakukan bersama kelompok masing-masing.", R.drawable.permainan));
        listSaranaBelajar.add(new SaranaBelajar("PONDOK BACA", "Pondok baca ini menyediakan berbagai koleksi bacaan.\n" +
                "Mulai dari buku tentang agama, ilmu pengetahuan,\n" +
                "tanaman dan buku lainnya. Suasana yang nyaman dan\n" +
                "sunyi membuat masyarakat yang membaca bisa fokus.\n" +
                "Bahkan, ada dua ruangan untuk memanjakan, dilantai\n" + "dasar dan atas.", R.drawable.pondokbaca));
        listSaranaBelajar.add(new SaranaBelajar("RUMAH POHON", "Rumah Pohon menjadi salah satu wahana di Taman Botani\n" +
                "Sukorambi yang pantang dilewatkan. Dengan tinggi\n" +
                "sekitar 5 meter dari permukaan tanah, pengunjung bisa\n" +
                "melihat panorama Taman Botani Sukorambi\n" + "dari ketinggian.", R.drawable.rumahpohon));
        listSaranaBelajar.add(new SaranaBelajar("MUSLIMAH PRIVATE AREA", "Pengunjung bebas meminjam MPA. Setiap jam dikenakan\n" +
                "Rp 100.000.Di dalam MPA terdapat kolam renang dengan\n" +
                "kedalaman bervariasi, musala,kamar mandi dengan\n" +
                "fasilitas air panas, dan tempat meeting.Pengunjung yang\n" +
                "hendak meminjam MPA harus menandatangani sejumlah\n" + "pernyataan kepada pengelola.", R.drawable.kolam2));
        listSaranaBelajar.add(new SaranaBelajar("PERMAINAN AIR", "Di Wahana permainan air. Di bagian bawah, terdapat\n" +
                "danau buatan dengan sebuah pulau mungil di tengahnya.\n" +
                "Bagi yang ingin merasakan sensasi mendayung perahu,\n" +
                "pengunjung bisa mencoba wahana ini. Di danau buatan ini,\n" +
                "terdapat dua buah perahu yang bisa disewa oleh\n" + "pengunjung.", R.drawable.permainanair));
        listSaranaBelajar.add(new SaranaBelajar("BERKUDA", "Pengunjung bisa berkeliling Taman Botani Sukorambi \n" +
                "dengan menunggang kuda yang sudah disediakan\n" +
                "pengelola, dengan didampingi oleh pawang kuda. Untuk sekali\n" +
                "menunggang kuda, pengunjung hanya dikenakan\n" + "tarif Rp 10 ribu.", R.drawable.kuda));

        saranaBelajarAdapter = new SaranaBelajarAdapter(listSaranaBelajar);
        saranaBelajarAdapter.setListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setHasFixedSize(true);
        rv.setAdapter(saranaBelajarAdapter);
        rv.setLayoutManager(layoutManager);

        return rootview;
    }

    @Override
    public void onClick(View view, int position) {
        SaranaBelajar saranabelajar = listSaranaBelajar.get(position);
        Toast.makeText(getActivity(), saranabelajar.getJudul(), Toast.LENGTH_LONG).show();
    }
}
