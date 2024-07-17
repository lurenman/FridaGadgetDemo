package com.example.fridagadgetdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fridagadgetdemo.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Frida_Test";

    // Used to load the 'fridagadgetdemo' library on application startup.
    static {
        System.loadLibrary("fridagadgetdemo");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyFileFromAssetsToFilesDir("frida-hook-student.js", "frida-hook-student.js");
                copyFileFromAssetsToFilesDir("frida-hook-androidid.js", "frida-hook-androidid.js");
                //加载fridagadget so 之后就会加载hook脚本
                System.loadLibrary("frida");
            }
        }).start();
    }

    public void copyFileFromAssetsToFilesDir(String assetFileName, String targetFileName) {
        InputStream in = null;
        OutputStream out = null;
        try {
            // 获取 AssetManager
            AssetManager assetManager = getAssets();

            // 打开输入流以读取 assets 中的文件
            in = assetManager.open(assetFileName);

            // 获取 fileScriptsdir 目录，并创建输出流
            File fileScriptsdir = new File(getFilesDir().getPath() + "/scripts");
            if (!fileScriptsdir.exists()) {
                fileScriptsdir.mkdir();
            }
            File file = new File(fileScriptsdir, targetFileName);
            out = new FileOutputStream(file);

            // 复制文件
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            // 刷新输出流（如果需要）
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}