package com.jarvisdong.uikit.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.AnimRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.jarvisdong.uikit.R;

import java.io.File;

/**
 * Created by JarvisDong on 2017/8/31.
 * OverView:
 */

public class IntentUtils {
    public static void startActivityByOptions(Context mContext, Class activity) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext,
                android.R.anim.fade_in, android.R.anim.fade_out);
        ActivityCompat.startActivity(mContext,
                new Intent(mContext, activity), compat.toBundle());
    }

    public static void startActivityByOptions(Context mContext, Class activity, @AnimRes int enterResId, @AnimRes int exitResId) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext,
                enterResId, exitResId);
        ActivityCompat.startActivity(mContext,
                new Intent(mContext, activity), compat.toBundle());
    }

    //===========================
    public static void startActivityByShare(Context mContext, View view, String mark, Intent intent) {
        String str = mark==null?mContext.getResources().getString(R.string.transitioName):mark;
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                        view, str);
        ActivityCompat.startActivity(mContext, intent, compat.toBundle());
    }

    public static void startActivityByShareForResult(Context mContext, View view, String mark, Intent intent, int requestCode) {
        String str = mark==null?mContext.getResources().getString(R.string.transitioName):mark;
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                        view, str);
        ActivityCompat.startActivityForResult((Activity) mContext, intent, requestCode, compat.toBundle());
    }

    public static void startActivityByShare(Context mContext, Intent intent, Pair<View, String>... mPairs) {
        if (mPairs == null) return;
        ActivityOptionsCompat compat = ActivityOptionsCompat
                .makeSceneTransitionAnimation((Activity) mContext, mPairs);
        ActivityCompat.startActivity(mContext, intent, compat.toBundle());
    }

    public static void startActivityByShareForResult(Context mContext, Intent intent, int requestCode, Pair<View, String>... mPairs) {
        if (mPairs == null) return;
//        Pair<View, String> imagePair = Pair.create(mImageView, getString(R.string.image));
//        Pair<View, String> textPair = Pair.create(mTextView, getString(R.string.name));
        ActivityOptionsCompat compat = ActivityOptionsCompat
                .makeSceneTransitionAnimation((Activity) mContext, mPairs);
        ActivityCompat.startActivityForResult((Activity) mContext, intent, requestCode, compat.toBundle());
    }

    public static void sendUpdateBroadcast(String screenShotFilePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(screenShotFilePath));
        intent.setData(uri);
        Utils.getApp().sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }
}
