package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mobile.sipetaniapp.Until.ServerAPI;
import com.mobile.sipetaniapp.Until.ServerAPI;

public class print extends AppCompatActivity {

    WebView webView;
    Button cetak;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        webView = (WebView)findViewById(R.id.webview);
        cetak = (Button)findViewById(R.id.cetak);
        pd = new ProgressDialog(print.this);

        Intent intent = getIntent();
        final String id_pemesanan = intent.getStringExtra("id_pemesanan");

        pd.setMessage("Load Tiket...");
        pd.setCancelable(false);
        pd.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url){
                pd.cancel();
                cetak.setVisibility(View.VISIBLE);
                webView = view;
            }
        });
        String htmlDocument = ServerAPI.URL_PRINT+id_pemesanan;
        webView.loadUrl(htmlDocument);
        cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPrintJob(webView);
            }
        });
    }

    private void createWebPrintJob(WebView webView){
        Intent intent = getIntent();
        final String id_pemesanan = intent.getStringExtra("id_pemesanan");
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name)+"Tiket"+id_pemesanan;
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);
        PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }
}
