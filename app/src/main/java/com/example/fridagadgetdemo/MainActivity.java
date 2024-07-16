package com.example.fridagadgetdemo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fridagadgetdemo.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Frida_Test";

    // Used to load the 'fridagadgetdemo' library on application startup.
    static {
        System.loadLibrary("fridagadgetdemo");
    }

    private Context mContext;
    private ActivityMainBinding binding;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 100);
            }else {
                System.loadLibrary("frida");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                int result = Student.Add(1, 2);
                Toast.makeText(MainActivity.this, result + "", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: result:" + result);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[1]) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[1]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "获取存储权限成功", Toast.LENGTH_SHORT).show();
                System.loadLibrary("frida");
            } else {
                Toast.makeText(MainActivity.this, "获取存储权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}