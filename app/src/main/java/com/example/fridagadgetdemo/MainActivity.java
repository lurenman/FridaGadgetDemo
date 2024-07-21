package com.example.fridagadgetdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fridagadgetdemo.databinding.ActivityMainBinding;
import com.example.fridagadgetdemo.utils.AssetCopier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
                try {
                    String cpuApi = getCpuApi();
                    if (!TextUtils.isEmpty(cpuApi)) {
                        AssetCopier copier = new AssetCopier(mContext);
                        String filesDirPath = mContext.getFilesDir().getAbsolutePath();
                        Log.d(TAG, "filesDirPath: " + filesDirPath);
                        //主要copy到file目录，解决系统多开的问题
                        copier.copyAssetsFolder("frida", filesDirPath + File.separator + "frida");
                        String soPath = filesDirPath + File.separator + "frida" + File.separator + cpuApi + File.separator + "libfrida.so";
                        Log.d(TAG, "soPath: " + soPath);
                        System.load(soPath);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private String getCpuApi() {
        String abi = "";
        try {
            Process process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                // line 现在是CPU架构，如 "armeabi-v7a", "arm64-v8a", "x86", "x86_64" 等
                abi = line;
                Log.d(TAG, "CPU_ABI:" + line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return abi;
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
            File fileScriptsdir = new File(getFilesDir().getPath() + "/frida/scripts");
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