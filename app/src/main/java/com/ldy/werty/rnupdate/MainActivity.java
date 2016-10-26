package com.ldy.werty.rnupdate;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.babytree.platform.reactnative.update.patch.BsdiffPatch;
import com.ldy.werty.rnupdate.util.FileUtil;
import com.ldy.werty.rnupdate.util.Md5Util;

import java.io.File;

/**
 *
 * 参考：ReactNative中文网推出的代码热更新服务
 * https://github.com/reactnativecn/react-native-pushy
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "react_patch"
            + File.separator;

    public static final String ASSETS_BUNDLE = "react/" + "index.jsbundle";
    public static final String ASSETS_PATCH = "react/" + "jsbundle.patch";

    public static final String PATH_BUNDLE_NEW = PATH_SD + "index.jsbundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.haha);

        Animation animation = new TranslateAnimation(
                0, Animation.RELATIVE_TO_SELF,
                1, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_SELF,
                1, Animation.RELATIVE_TO_PARENT);
        animation.setDuration(200);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.start();
        imageView.setAnimation(animation);
    }

    public void syncUpdate(View view) {
        FileUtil.makeFolders(PATH_SD);
        boolean isPatchSuccess = patchApplyBundle();
        Toast.makeText(this, "RnUpdate->" + isPatchSuccess, Toast.LENGTH_SHORT).show();
    }

    public void asyncUpdate(View view) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                FileUtil.makeFolders(PATH_SD);
                return patchApplyBundle();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Toast.makeText(MainActivity.this, "RnUpdate->" + aBoolean, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    /**
     * patch文件与老版本bundle生成新的bundle
     *
     * @return 新的 bundle ()
     */
    public boolean patchApplyBundle() {
        try {
            byte[] origin = FileUtil.readerStreamByte(FileUtil.openAssets(this, ASSETS_BUNDLE));
            byte[] patch = FileUtil.readerStreamByte(FileUtil.openAssets(this, ASSETS_PATCH));
            if (origin == null || patch == null) {
                return false;
            }
            long start = System.currentTimeMillis();
            byte[] bytes = BsdiffPatch.bsdiffPatch(origin, patch);
            Log.i("RnUpdate", "patchApplyBundle-->" + (System.currentTimeMillis() - start));
            boolean isPatchSuccess = FileUtil.writerStreamByte(PATH_BUNDLE_NEW, bytes);
            Log.i("RnUpdate", "patchApplyBundle md5=[" + Md5Util.md5(new File(PATH_BUNDLE_NEW)) + "]");
            return isPatchSuccess;
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i("RnUpdate", "patchApplyBundle e[" + e + "]");
        }
        return false;
    }
}
