package com.example.fridagadgetdemo.utils;
import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class AssetCopier {
    private Context context;
    private AssetManager assetManager;

    public AssetCopier(Context context) {
        this.context = context;
        this.assetManager = context.getAssets();
    }

    public void copyAssetsFolder(String fromAssetPath, String toPath) throws IOException {
        String[] files = assetManager.list(fromAssetPath);
        if (files != null && files.length > 0) {
            File dir = new File(toPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            for (String file : files) {
                if (file.contains(".")) {
                    // It's a file, copy it
                    copyAssetFile(fromAssetPath + File.separator + file, toPath + File.separator + file);
                } else {
                    // It's a directory, recursively copy its contents
                    copyAssetsFolder(fromAssetPath + File.separator + file, toPath + File.separator + file);
                }
            }
        }
    }

    private void copyAssetFile(String fromAssetPath, String toPath) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            File outFile = new File(toPath);
            out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
