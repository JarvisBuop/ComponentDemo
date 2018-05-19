package com.jarvisdong.uikit.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Toolkit {

    public static String getCacheDir(Context context, String subFolder) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        String fullPath;
        if (sdCardExist) {
            fullPath = Environment.getExternalStorageDirectory() + subFolder;//获取跟目录
        } else {
            fullPath = context.getApplicationContext().getFilesDir().toString() + subFolder;
        }
        File dldir = new File(fullPath);
        if (!dldir.exists())
            dldir.mkdirs();

        return fullPath;
    }

    /**
     * 将图片截取为圆角图片
     *
     * @param bitmap 原图片
     * @param ratio  截取比例，如果是8，则圆角半径是宽高的1/8，如果是2，则是圆形图片
     * @return 圆角矩形图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float ratio) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static String getAppName(Context context, int pID) {
        String processName = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            RunningAppProcessInfo info = (RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMobile(String number) {
        if (number != null) {
            String regexMobile = "^1([3|4|5|7|8])\\d{9}$";
            Pattern mobileP = Pattern.compile(regexMobile);
            Matcher m = mobileP.matcher(number);
            return m.find();
        }
        return false;

    }

    // 检测邮箱格式是否正确
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,5}");
        Matcher m = p.matcher(email);
        return m.find();
    }

    public static boolean isEngine(String engine) {
        Pattern p = Pattern.compile("(([0-9A-Z] ?)*[0-9A-Z]{1,})|(\\**[0-9A-Z]{4})$");
        Matcher m = p.matcher(engine);
        return m.find();
    }

    public static String getDeviceId(Context context) {
        try {
            Context mContext = context.getApplicationContext();

            final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, androidId;
            tmDevice = "" + tm.getDeviceId();
            // tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), tmDevice.hashCode());

            return deviceUuid.toString();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return "";
    }

    public static String getPhoneInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("model=");
        sb.append(Build.MODEL);
        sb.append(";sdk=");
        sb.append(Build.VERSION.SDK);
        sb.append(";release=");
        sb.append(Build.VERSION.RELEASE);
        return sb.toString();
    }

    // 计算两点距离
    private final static double EARTH_RADIUS = 6378.1370;

    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    // 计算方位角pab。
    public double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;

        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;

        // d = Math.round(d*10000);
        return d;
    }

    public static HashMap<String, String> parse(String content) {
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            StringTokenizer st = new StringTokenizer(content, ";");
            while (st != null && st.hasMoreTokens()) {
                String thisToken = st.nextToken();
                StringTokenizer st2 = new StringTokenizer(thisToken, "=");
                map.put(st2.nextToken(), st2.hasMoreTokens() ? st2.nextToken() : "");
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return map;
    }

    public static void setText(TextView tvText, float text) {
        tvText.setText(String.format((text < 100) ? "%.1f" : "%.0f", text));
    }

    public static void setText(TextView tvText, float text, String str) {
        tvText.setText(String.format((text < 100) ? "%.1f" : "%.0f", text) + str);
    }

    /**
     * 检测第三方APP是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static void startDial(Context context, String telephone) {
        if (TextUtils.isEmpty(telephone)) return;
        String uri = "tel:" + telephone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(uri));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void installApkFile(Context context, String filePath) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isDownloading(Context context, long downloadId) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = dm.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if (status == DownloadManager.STATUS_PAUSED
                    || status == DownloadManager.STATUS_PAUSED
                    || status == DownloadManager.STATUS_RUNNING) {
                return true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

}
