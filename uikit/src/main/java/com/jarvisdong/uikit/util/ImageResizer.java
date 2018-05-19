package com.jarvisdong.uikit.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ImageResizer {
    public final static String IMAGE_CACHE_DIR = "jd/images";//压缩使用到,


    /**
     * decodeResource
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true的话，在decode时将会返回null,通过此设置可以去查询一个bitmap的属性，比如bitmap的长与宽，而不占用内存大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //获取合适的采样率;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     *
     */
    public static Bitmap decodeSampleBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        //获取合适的采样率;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    /**
     * decodeFile
     *
     * @param filePath
     * @return
     */
    public static String compressImage(String filePath) {
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true的话，在decode时将会返回null,通过此设置可以去查询一个bitmap的属性，比如bitmap的长与宽，而不占用内存大小
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 1080.0f;
        float maxWidth = 800.0f;
        float imgRatio = actualWidth * 1.0f / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height 设置为bitmap宽高比
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // 获取图像压缩比
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        // 当存储Pixel的内存空间在系统内存不足时可以被回收，
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));

        // 检查图像是否旋转
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename(IMAGE_CACHE_DIR);//获取文件输出路径,无后缀文件;
        try {
            out = new FileOutputStream(filename + getFileLastSuffix(filePath));
            // write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename + getFileLastSuffix(filePath);
    }

    //有点后缀;
    public static String getFileLastSuffix(String targetName) {
        return targetName.substring(targetName.lastIndexOf(".")).toLowerCase();
    }

    //无点后缀;
    public static String getFileLastSuffixOffset(String targetName) {
        return targetName.substring(targetName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 构建随机文件名;
     *
     * @param directory
     * @return
     */
    public static String getFilename(String directory) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis());
        return uriSting;
    }

    // TODO 压缩比;
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            while (halfHeight / inSampleSize >= reqHeight
                    && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        LogUtils.e("insampleSize:" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 删除指定路劲的文件;
     */
    public static boolean deleteFilePath(String deletePath) {
        File file = new File(deletePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * ************************** BGA image resize and compress ***************************
     *
     */

    /**
     * 从资源获取 Bitmap
     * bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
     */
    @Nullable
    public static Bitmap getScaledImageFromResource(@NonNull Context context, int resId, int maxWidth, int maxHeight, float minWidth, float minHeight) {
        LoadBitmapPair<Throwable> result;
        do {
            result = getImageFromResource(context, resId, maxWidth, maxHeight);
            if (result != null && (result.first != null)) {
                break;
            }
            maxWidth /= 2;
            maxHeight /= 2;
        }
        while (result != null && result.second instanceof OutOfMemoryError && maxWidth > minWidth && maxHeight > minHeight);

        if (result == null) {
            return null;
        } else {
            return result.first;
        }
    }

    /**
     * 从资源获取Bitmap
     * 最大宽高为 maxWidth maxHeight
     */
    public static LoadBitmapPair<Throwable> getImageFromResource(@NonNull Context context, int resId, int maxWidth, int maxHeight) {
        LoadBitmapPair<Throwable> result;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap.Config preferredConfig = Bitmap.Config.RGB_565;
        InputStream is = null;

        try {
            if (maxWidth == 0 && maxHeight == 0) {
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                result = new LoadBitmapPair<>(BitmapFactory.decodeStream(is, null, decodeOptions), null);
                is.close();
            } else {
                // If we have to resize this image, first get the natural
                // bounds.
                decodeOptions.inJustDecodeBounds = true;
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                BitmapFactory.decodeStream(is, null, decodeOptions);
                is.reset();
                is.close();

                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;

                // Then compute the dimensions we would ideally like to decode to.
                int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
                int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;
                // doesn't
                // support it?
                // decodeOptions.inPreferQualityOverSpeed =
                // PREFER_QUALITY_OVER_SPEED;
                decodeOptions.inSampleSize = calculateInSampleSize(decodeOptions, desiredWidth, desiredHeight);
                decodeOptions.inPreferredConfig = preferredConfig;
                is = context.getResources().openRawResource(resId);
                Bitmap tempBitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
                is.close();
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                    result = new LoadBitmapPair<>(Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true), null);
                    tempBitmap.recycle();
                } else {
                    result = new LoadBitmapPair<>(tempBitmap, null);
                }
            }
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            result = new LoadBitmapPair(null, exception);
        } catch (Exception exception) {
            exception.printStackTrace();
            result = new LoadBitmapPair(null, exception);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static class LoadBitmapPair<S extends Throwable> extends Pair<Bitmap, S> {
        LoadBitmapPair(@Nullable Bitmap first, @Nullable S second) {
            super(first, second);
        }
    }

    public static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
}
