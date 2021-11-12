package com.yodosmart.bsdiffbspatch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 在根目录下放了两个apk, old and new apk
 */
public class BsdiffBspatchActivity extends Activity {
    String path = Environment.getExternalStorageDirectory().getPath();
    final String oldpath = path + "/1test/" + "old.apk";
    final String newpath = path + "/1test/" + "new.apk";
    final String patchPath = path + "/1test/" + "test.patch";
    final String patchApk = path + "/1test/" + "merged.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsdiff_bspatch);
        if (addPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }


    public void generatediff(View view) {
        //性能测试
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = DiffPatchUtil.diff(oldpath,newpath,patchPath);
                Log.i("wangweijun", "diff result="+result);

            }
        });
        thread.start();
    }

    public void patch(View view) {
        //性能测试
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = DiffPatchUtil.patch(oldpath,patchApk,patchPath);
                Log.i("wangweijun", "patch result="+result);
            }
        });
        thread.start();
    }

    public void installApk(View view) {
        // adb install -t merged.apk
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(patchApk)),
                "application/vnd.android.package-archive");
        startActivity(intent);*/
    }

    private boolean addPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
