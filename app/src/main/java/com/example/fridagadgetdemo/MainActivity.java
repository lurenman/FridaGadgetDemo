package com.example.fridagadgetdemo;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fridagadgetdemo.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Frida_Test";

    // Used to load the 'fridagadgetdemo' library on application startup.
    static {
        System.loadLibrary("fridagadgetdemo");
        System.loadLibrary("frida");
    }

    private Context mContext;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAndroidId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d(TAG, "onClick androidId: " + androidId);
                Toast.makeText(mContext, "androidId:" + androidId, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = Student.Add(1, 1);
                Log.d(TAG, "onClick add result:" + result);
                Toast.makeText(MainActivity.this, result + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}