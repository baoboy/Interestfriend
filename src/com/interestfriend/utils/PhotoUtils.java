package com.interestfriend.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.interestfriend.activity.imagefactory.ImageFactoryActivity;

/**
 * * @description 图片工具�?
 * 
 * @author songbinbin
 * 
 */
public class PhotoUtils {
	// 图片在SD卡中的缓存路�?
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ File.separator
			+ "interestfriend" + File.separator + "Images" + File.separator;
	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 0;
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 1;
	// 裁剪照片的RequestCode
	public static final int INTENT_REQUEST_CODE_CROP = 2;
	// 滤镜图片的RequestCode
	public static final int INTENT_REQUEST_CODE_FLITER = 3;

	/**
	 * 通过手机相册获取图片
	 * 
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 通过手机照相获取图片
	 * 
	 * @param activity
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Activity activity) {
		FileUtils.createDirFile(IMAGE_PATH);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = IMAGE_PATH + UUID.randomUUID().toString() + "jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		return path;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            �?��裁剪的图片路�?
	 */
	public static void cropPhoto(Context context, Activity activity, String path) {
		Intent intent = new Intent(context, ImageFactoryActivity.class);
		if (path != null) {
			intent.putExtra("path", path);
			intent.putExtra(ImageFactoryActivity.TYPE,
					ImageFactoryActivity.CROP);
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
	}

	/**
	 * 滤镜图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            �?��滤镜的图片路�?
	 */
	public static void fliterPhoto(Context context, Activity activity,
			String path) {
		Intent intent = new Intent(context, ImageFactoryActivity.class);
		if (path != null) {
			intent.putExtra("path", path);
			intent.putExtra(ImageFactoryActivity.TYPE,
					ImageFactoryActivity.FLITER);
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER);
	}

	/**
	 * 删除图片缓存目录
	 */
	public static void deleteImageFile() {
		File dir = new File(IMAGE_PATH);
		if (dir.exists()) {
			FileUtils.delFolder(IMAGE_PATH);
		}
	}

	/**
	 * 从文件中获取图片
	 * 
	 * @param path
	 *            图片的路�?
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从Uri中获取图�?
	 * 
	 * @param cr
	 *            ContentResolver对象
	 * @param uri
	 *            图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 根据宽度和长度进行缩放图�?
	 * 
	 * @param path
	 *            图片的路�?
	 * @param w
	 *            宽度
	 * @param h
	 *            长度
	 * @return
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存�?
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽�?
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比�?
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长�?
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能�?过inSampleSize来进行缩放，其�?表明缩放的�?数，SDK中建议其值是2的指数�?
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图�?
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过�?
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽�?
	 * @param filePath
	 *            图片的路�?
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap CompressionPhoto(float screenWidth, String filePath,
			int ratio) {
		Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}
		return compressionBitmap;
	}

	/**
	 * 保存图片到SD�?
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileUtils.createDirFile(IMAGE_PATH);

		String fileName = UUID.randomUUID().toString() + ".jpg";
		String newFilePath = IMAGE_PATH + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
		} catch (FileNotFoundException e1) {
			return null;
		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	/**
	 * 滤镜效果--LOMO
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768
				/ height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = (int) (newR * v) >> 16;
					gi = (int) (newG * v) >> 16;
					bi = (int) (newB * v) >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}

	/**
	 * 根据文字获取图片
	 * 
	 * @param text
	 * @return
	 */

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距�?
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	/**
	 * 获取圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取颜色的圆角bitmap
	 * 
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getRoundBitmap(Context context, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
		int height = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4.0f, metrics));
		int round = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2.0f, metrics));
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0.0F, 0.0F, width, height), round,
				round, paint);
		return bitmap;
	}
}
