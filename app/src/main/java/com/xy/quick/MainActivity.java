package com.xy.quick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "log";
    private static final float FLING_MIN_DISTANCE = 200;
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetector(this, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x1 = e1.getX();
            float x2 = e2.getX();
            float y1 = e1.getY();
            float y2 = e2.getY();
            float xDiff = x1 - x2;
            float yDiff = y1 - y2;

            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                if (xDiff > FLING_MIN_DISTANCE) {
                    // 左
                    openByPackageName("com.huya.hyoamobile");
                } else if (x2 - x1 > FLING_MIN_DISTANCE) {
                    // 右
                    openByPackageName("tv.danmaku.bili");
                }
            } else {
                if (yDiff > FLING_MIN_DISTANCE) {
                    // 上
                    openByPackageName("com.eg.android.AlipayGphone");
                } else if (y2 - y1 > FLING_MIN_DISTANCE) {
                    // 下
                    openByPackageName("enfc.metro");
                }
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        private void openByPackageName(String packageName) {
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "没有匹配" + packageName + "，请下载安装", Toast.LENGTH_SHORT).show();
            }
        }

        private void openByUrl(String url) {
            Uri data = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, data);
            //保证新启动的APP有单独的堆栈，如果希望新启动的APP和原有APP使用同一个堆栈则去掉该项
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivityForResult(intent, RESULT_OK);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "没有匹配" + url + "，请下载安装", Toast.LENGTH_SHORT).show();
            }
        }
    }
}