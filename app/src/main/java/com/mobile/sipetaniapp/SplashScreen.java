package com.mobile.sipetaniapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.mobile.sipetaniapp.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;
    LinearLayout judul;
    ImageView logo;
    TextView deskripsi, developer;
    Animation frombottom, fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        judul = (LinearLayout)findViewById(R.id.judul);
        logo = (ImageView)findViewById(R.id.logo);
        deskripsi = (TextView)findViewById(R.id.deskripsi);
        developer = (TextView)findViewById(R.id.developer);
        frombottom = (Animation) AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = (Animation) AnimationUtils.loadAnimation(this,R.anim.fromtop);

        judul.setAnimation(fromtop);
        logo.setAnimation(fromtop);
        deskripsi.setAnimation(frombottom);
        developer.setAnimation(frombottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(splash);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
