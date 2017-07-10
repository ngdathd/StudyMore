package com.ngdat.studymore.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.ngdat.studymore.R;
import com.ngdat.studymore.common.Constants;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils implements Constants {
    public static Bitmap getLargeBitmap(Resources resources, int idImg) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, idImg, options);

        int scale = 2;
        int widthImg = options.outWidth;
        int heightImg = options.outHeight;

        if (WIDTH_SCREEN >= widthImg && HEIGHT_SCREEN >= heightImg) {
            options.inSampleSize = 1;
        } else {
            while ((widthImg / scale) >= WIDTH_SCREEN && (heightImg / scale) >= HEIGHT_SCREEN) {
                scale += 2;
            }
            options.inSampleSize = scale;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, idImg, options);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isNotEmpty(EditText editText) {
        if (0 < editText.getText().toString().trim().length()) {
            return true;
        } else {
            editText.requestFocus();
            editText.setError("Vui lòng điền thông tin!");
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "[a-zA-Z0-9._-]+@[a-z]+(\\.+[a-z]+)+";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getLinkAvatar(String userId, int width, int height) {
        return "https://graph.facebook.com/" + userId + "/picture?height=" + height + "&width=" + width;
    }

    public static int setImgBackground(int t) {
        switch (t % 6) {
            case 0: {
                return R.drawable.img_bg0;
            }
            case 1: {
                return R.drawable.img_bg1;
            }
            case 2: {
                return R.drawable.img_bg2;
            }
            case 3: {
                return R.drawable.img_bg3;
            }
            case 4: {
                return R.drawable.img_bg4;
            }
            case 5: {
                return R.drawable.img_bg5;
            }
            default:
                return 0;
        }
    }

    public static int setImgAvatar(int t) {
        switch (t % 6) {
            case 0: {
                return R.drawable.img_ic0;
            }
            case 1: {
                return R.drawable.img_ic1;
            }
            case 2: {
                return R.drawable.img_ic2;
            }
            case 3: {
                return R.drawable.img_ic3;
            }
            case 4: {
                return R.drawable.img_ic4;
            }
            case 5: {
                return R.drawable.img_ic5;
            }
            default:
                return 0;
        }
    }

    public static boolean checkToday(int day, int year) {
        Calendar c = Calendar.getInstance();
        return day == c.get(Calendar.DAY_OF_YEAR)
                && year == c.get(Calendar.YEAR);
    }

    public static boolean checkYesterday(int day, int year) {
        Calendar c = Calendar.getInstance();
        if (year == c.get(Calendar.YEAR)) {
            if (day == c.get(Calendar.DAY_OF_YEAR) - 1) {
                return true;
            }
        }
        if (year == c.get(Calendar.YEAR) - 1) {
            if (year % 4 == 0) {
                if (day == 366) {
                    return true;
                }
            } else {
                if (day == 365) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkLastWeek(int day, int year) {
        Calendar c = Calendar.getInstance();
        if (year == c.get(Calendar.YEAR)) {
            if (day < c.get(Calendar.DAY_OF_YEAR) && day >= c.get(Calendar.DAY_OF_YEAR) - 7) {
                return true;
            }
        }
        if (year == c.get(Calendar.YEAR) - 1) {
            if (year % 4 == 0) {
                switch (day) {
                    case 366:
                    case 365:
                    case 364:
                    case 363:
                    case 362:
                    case 361:
                    case 360: {
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            } else {
                switch (day) {
                    case 365:
                    case 364:
                    case 363:
                    case 362:
                    case 361:
                    case 360:
                    case 359: {
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}