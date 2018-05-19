package com.jarvisdong.uikit.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

public class Version implements Comparable<Version> {
    private String version;

    public final String get() {
        return this.version;
    }

    public Version(String version) {
        if (version == null) {
            throw new IllegalArgumentException("Version can not be null");
        } else if (!version.matches("[0-9]+(\\.[0-9]+)*")) {
            throw new IllegalArgumentException("Invalid version format");
        } else {
            this.version = version;
        }
    }

    public int compareTo(Version that) {
        if (that == null) {
            return 1;
        } else {
            String[] thisParts = this.get().split("\\.");
            String[] thatParts = that.get().split("\\.");
            int length = Math.max(thisParts.length, thatParts.length);

            for (int i = 0; i < length; ++i) {
                int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
                int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
                if (thisPart < thatPart) {
                    return -1;
                }

                if (thisPart > thatPart) {
                    return 1;
                }
            }

            return 0;
        }
    }

    public boolean equals(Object that) {
        return this == that ? true : (that == null ? false : (this.getClass() != that.getClass() ? false : this.compareTo((Version) that) == 0));
    }

    public static String getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();

        String appVersion;
        try {
            PackageInfo e = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion = e.versionName;
        } catch (NameNotFoundException var4) {
            appVersion = "";
        }

        return appVersion;
    }

    public static int getAppVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();

        int appVersionCode = 0;
        try {
            PackageInfo e = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionCode = e.versionCode;
        } catch (NameNotFoundException var4) {
            appVersionCode = 0;
        }

        return appVersionCode;
    }

    public static int compareVersion(String left, String right) {
        Version leftVer = new Version(left);
        Version rightVer = new Version(right);
        return leftVer.compareTo(rightVer);
    }

    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;

        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException var4) {
            applicationInfo = null;
        }

        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }


    public static String getCertificateSHA1Fingerprint(Context context) {
//获取包管理器
        PackageManager pm = context.getPackageManager();
//获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
//在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        String packageName = context.getPackageName();
//返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
//获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
//将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
//证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
//X509证书，X.509是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
//加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
//获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
//字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    //这里是将获取到得编码进行16进制转换
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(":");
        }
        return str.toString();
    }

    //获取元数据;
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
        }
        return metaValue;// xxx
    }
}
