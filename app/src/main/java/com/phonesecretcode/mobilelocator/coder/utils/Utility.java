package com.phonesecretcode.mobilelocator.coder.utils;

import static com.phonesecretcode.mobilelocator.coder.singletonClass.AppOpenAds.currentActivity;
import static com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication.getCodesDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.phonesecretcode.mobilelocator.coder.BuildConfig;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.DialogExitBinding;
import com.phonesecretcode.mobilelocator.coder.models.CodesModel;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;
import com.phonesecretcode.mobilelocator.coder.models.MobileModels;
import com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication;
import com.phonesecretcode.mobilelocator.coder.ui.activity.TipsTricksActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utility {

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String PrivacyLink = "https://analogdreamsmedias.blogspot.com/p/privacy-policy.html";

    public static DevicesModel SELECTED_MODEL;

    public static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

   /* public static void readJsonFromRaw(Context context) {

        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.mobilecodes);
            Scanner scanner = new Scanner(inputStream);

            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                CodesModel model = new CodesModel(object.getInt("ID"),
                        object.getString("code"),
                        object.getString("codeDesc"),
                        object.getString("brand"));
                AsyncTask.execute(() -> {
                        getCodesDao().insertData(model);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public static List<String> getGenders() {
        List<String> list = new ArrayList<>();

        list.add(MALE);
        list.add(FEMALE);

        return list;
    }

    public static void setGradientShaderToTextView(TextView textView, int firstColor, int secondColor) {
        Paint paint = textView.getPaint();
        int[] colors = {firstColor, secondColor};
        float width = paint.measureText(textView.getText().toString());
        Shader textShader = new LinearGradient(0f, 1f, width, textView.getTextSize(), colors, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }

    public static void setGradientMainShaderToTextView(TextView textView) {
        Paint paint = textView.getPaint();
        int[] colors = new int[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colors = new int[]{textView.getContext().getColor(R.color.secondary), textView.getContext().getColor(R.color.primary)};
        }
        float width = paint.measureText(textView.getText().toString());
        Shader textShader = new LinearGradient(0f, 1f, width, textView.getTextSize(), colors, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }

    public static long convertToMilliseconds(String time) {
        int milliSeconds = 1000;
        if (time.contains("Sec")) return (long) Integer.parseInt(time.split(" ")[0]) * milliSeconds;
        return (long) Integer.parseInt(time.split(" ")[0]) * 60 * milliSeconds;
    }

    public static void nextActivity(Activity activity, Class<?> className) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            activity.startActivity(new Intent(currentActivity, className));
        });
    }
    public static void showRateApp() {
        ReviewManager reviewManager = ReviewManagerFactory.create(currentActivity);
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();

                Task <Void> flow = reviewManager.launchReviewFlow(currentActivity, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                });
            }
        });
    }

    public static void nextTTActivity(Activity activity, MobileModels models) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            activity.startActivity(new Intent(currentActivity, TipsTricksActivity.class).putExtra("model", models).putExtra("span", 1));
        });
    }

    public static void nextActivity(Activity activity, Class<?> className, String time) {
        MyApplication.getPreferences().setMilliSeconds(convertToMilliseconds(time));
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            activity.startActivity(new Intent(activity, className).putExtra("time", time));
        });
    }


    public static void nextFinishActivity(Activity activity, Class<?> className) {
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), activity, isLoaded -> {
            activity.startActivity(new Intent(activity, className).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            activity.finish();
        });
    }

    public static void finishActivity(Activity activity) {
        AdUtils.showBackPressAds(activity, Constants.adsResponseModel.getApp_open_ads().getAdx(), isLoaded -> {
            activity.finish();
        });
    }

    public static String createTime(int duration) {
        String time = "", minute = "", secs = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        if (min < 10) minute = "0" + min;
        else minute = "" + min;
        if (sec < 10) secs = "0" + sec;
        else secs = "" + sec;
        time = minute + ":" + secs;
        return time;
    }

    public static void copy(Context context, String str) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", str));
        Toast.makeText(context, "Text copied to clipboard.", Toast.LENGTH_SHORT).show();
    }


    public static void openMapsApp(Context context, String str) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + str));
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("openMapsApp: ", e.getLocalizedMessage());
            Toast.makeText(context, "Please install Google Map.", Toast.LENGTH_SHORT).show();
//            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")));
        }
    }

    public static boolean isCreditCardValid(String creditCardNumber) {
        String cleanedNumber = creditCardNumber.replaceAll("[ -]", "");
        StringBuilder reversedNumber = new StringBuilder(cleanedNumber).reverse();
        int sum = 0;
        boolean alternate = false;

        for (int i = 0; i < reversedNumber.length(); i++) {
            int digit = Character.getNumericValue(reversedNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    public static String checkCardType(String creditCardNumber) {
        String amexPattern = "^3[47][0-9]{13}$";
        String visaPattern = "^4[0-9]{12}(?:[0-9]{3})?$";
        String mastercardPattern = "^5[1-5][0-9]{14}$";
        String discoverPattern = "^6(?:011|5[0-9]{2})[0-9]{12}$";

        String cardType;
        if (creditCardNumber.matches(amexPattern)) {
            cardType = "American Express";
        } else if (creditCardNumber.matches(visaPattern)) {
            cardType = "Visa";
        } else if (creditCardNumber.matches(mastercardPattern)) {
            cardType = "Mastercard";
        } else if (creditCardNumber.matches(discoverPattern)) {
            cardType = "Discover";
        } else {
            cardType = "Unknown";
        }

        return cardType;
    }

    public static LiveData<List<String>> getAllVideosFromUri(Context context) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> videoPaths = new ArrayList<>();

        String[] projection = {MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATA};
        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " DESC";

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            while (cursor.moveToNext()) {
                String videoPath = cursor.getString(columnIndex);
                videoPaths.add(videoPath);
            }
            cursor.close();
            Log.e("getAllVideosFromUri: ", videoPaths.size() + " ");
            AsyncTask.execute(() -> data.postValue(videoPaths));
        }

        return data;
    }

    public static LiveData<List<String>> getAllVideosFromFolder(Context context) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> videoPaths = new ArrayList<>();

        // Define the columns you want to retrieve
        String[] projection = {MediaStore.Video.Media.DATA};

        // Define the selection query
        String folderPath = "/storage/emulated/0/Download/All Video Downloader";

        String selection = MediaStore.Video.Media.DATA + " like ?";
        String[] selectionArgs = new String[]{"%" + folderPath + "%"};

        // Query the media store for videos matching the folder path
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null) {

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            while (cursor.moveToNext()) {
                String videoPath = cursor.getString(columnIndex);
                videoPaths.add(videoPath);
            }

            cursor.close();
            AsyncTask.execute(() -> data.postValue(videoPaths));

        }

        return data;
    }

    public static LiveData<List<String>> getAllImagesFromFolder() {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        List<String> imagePaths = new ArrayList<>();

        String folderPath = "/storage/emulated/0/Download/"/* + directoryInstaShoryDirectorydownload_images*/;
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        String imagePath = file.getAbsolutePath();
                        imagePaths.add(imagePath);
                    }
                }
            }
        }

        String sfolder = "/storage/emulated/0"/* + SAVE_FOLDER_NAME*/;
        File files = new File(sfolder);
        if (files.exists() && files.isDirectory()) {
            File[] listFiles = files.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        String imagePath = file.getAbsolutePath();
                        imagePaths.add(imagePath);
                    }
                }
            }
        }
        AsyncTask.execute(() -> data.postValue(imagePaths));

        return data;
    }

    private static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp"));
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

//    public static ShimmerDrawable getShimmer() {
//        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder().setDuration(1200) // how long the shimmering animation takes to do one full sweep
//                .setBaseAlpha(0.7f) //the alpha of the underlying children
//                .setHighlightAlpha(0.9f) // the shimmer alpha amount
//                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setShape(Shimmer.Shape.LINEAR).setAutoStart(true).build();
//        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
//        shimmerDrawable.setShimmer(shimmer);
//
//        return shimmerDrawable;
//    }


    public static String maskString(String input) {
        if (input == null || input.length() < 4) {
            return input;
        }

        StringBuilder maskedString = new StringBuilder(input.length());

        maskedString.append(input.substring(0, 2));
        for (int i = 2; i < input.length() - 2; i++) {
            maskedString.append("*");
        }


        maskedString.append(input.substring(input.length() - 2));

        return maskedString.toString();
    }

   /* public static void openCloseDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogExitBinding binding = DialogExitBinding.inflate(LayoutInflater.from(currentActivity));
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView, 1, null);

        binding.btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            activity.finishAffinity();
        });
        binding.btnRate.setOnClickListener(v -> {
            dialog.dismiss();
            final String appPackageName = activity.getPackageName();
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        binding.btnClose.setOnClickListener(v -> dialog.dismiss());

    }
*/
    public static void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_SUBJECT, currentActivity.getString(R.string.app_name) + " App Invitation");
        intent.putExtra(Intent.EXTRA_TEXT, currentActivity.getString(R.string.desc_1) + "\nDownload now to stay secure!" + "\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        intent.setType("text/plain");
        currentActivity.startActivity(intent);
    }

    public static void shareMessage(CodesModel model) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_SUBJECT, model.getCodeDesc());
        intent.putExtra(Intent.EXTRA_TEXT, model.getCode());
        intent.setType("text/plain");
        currentActivity.startActivity(intent);
    }

/*
    public static void setAppleCodesData() {

        List<CodesModel> list = new ArrayList<>();

        // iPhone
        list.add(new CodesModel("*67", "Hide Caller ID", "Apple"));
        list.add(new CodesModel("*225#", "Check Postpaid Cellular Balance", "Apple"));
        list.add(new CodesModel("*777#", "Check Prepaid Cellular Balance", "Apple"));
        list.add(new CodesModel("*43#", "Enable Call Waiting Status", "Apple"));
        list.add(new CodesModel("#43#", "Disable Call Waiting Status", "Apple"));
        list.add(new CodesModel("*#06#", "Check Your iPhone's IMEI number", "Apple"));
        list.add(new CodesModel("#61#", "Check Missed Calls", "Apple"));
        list.add(new CodesModel("*#07#", "Check Legal and Regulatory Details", "Apple"));
        list.add(new CodesModel("*#30#", "Check Call Line Presentation", "Apple"));
        list.add(new CodesModel("*3001#12345#*", "Check Your iPhone's Signal", "Apple"));
        list.add(new CodesModel("*5005*25371#", "Check whether the alert system is working or not", "Apple"));
        list.add(new CodesModel("*5005*25370#", "To disable the alert system", "Apple"));




    }
*/

    public static List<DevicesModel> getDevices() {
        List<DevicesModel> list = new ArrayList<>();

        // OS
        list.add(new DevicesModel(1, "iOS", 0));
        list.add(new DevicesModel(2, "Android", 0));

        // Brands
        list.add(new DevicesModel(3, "Apple", 1));
        list.add(new DevicesModel(4, "Samsung", 2));
        list.add(new DevicesModel(5, "Huawei", 2));
//        list.add(new DevicesModel(6, "Xiaomi", 2));
        list.add(new DevicesModel(7, "OnePlus", 2));
        list.add(new DevicesModel(8, "Oppo", 2));
        list.add(new DevicesModel(9, "Vivo", 2));
        list.add(new DevicesModel(10, "LG", 2));
        list.add(new DevicesModel(11, "Sony", 2));
//        list.add(new DevicesModel(12, "Google", 2));
        list.add(new DevicesModel(13, "Motorola", 2));
        list.add(new DevicesModel(14, "Nokia", 2));
        list.add(new DevicesModel(15, "HTC", 2));
        list.add(new DevicesModel(16, "Lenovo", 2));
        list.add(new DevicesModel(17, "Asus", 2));
        list.add(new DevicesModel(18, "BlackBerry", 2));
//        list.add(new DevicesModel(19, "Honor", 2));
//        list.add(new DevicesModel(20, "Meizu", 2));
//        list.add(new DevicesModel(21, "ZTE", 2));
//        list.add(new DevicesModel(128, "Xperia", 2));
//        list.add(new DevicesModel(208, "Poco", 2));
        list.add(new DevicesModel(304, "Realme", 2));

        // Samsung Models
        list.add(new DevicesModel(22, "Galaxy S", 4));
        list.add(new DevicesModel(23, "Galaxy Note", 4));
        list.add(new DevicesModel(24, "Galaxy A", 4));
        list.add(new DevicesModel(25, "Galaxy M", 4));
        list.add(new DevicesModel(26, "Galaxy Z", 4));
        list.add(new DevicesModel(27, "Galaxy F", 4));
        list.add(new DevicesModel(28, "Galaxy J", 4));
        list.add(new DevicesModel(29, "Galaxy C", 4));
        list.add(new DevicesModel(30, "Galaxy ON", 4));

        // Samsung S Models
        list.add(new DevicesModel(31, "Samsung Galaxy S21", 22));
        list.add(new DevicesModel(32, "Samsung Galaxy S20", 22));
        list.add(new DevicesModel(33, "Samsung Galaxy S10", 22));
        list.add(new DevicesModel(34, "Samsung Galaxy S9", 22));
        list.add(new DevicesModel(35, "Samsung Galaxy S8", 22));
        list.add(new DevicesModel(36, "Samsung Galaxy S7", 22));
        list.add(new DevicesModel(37, "Samsung Galaxy S6", 22));
        list.add(new DevicesModel(38, "Samsung Galaxy S5", 22));
        list.add(new DevicesModel(39, "Samsung Galaxy S4", 22));
        list.add(new DevicesModel(40, "Samsung Galaxy S3", 22));

        // HTC Models
        list.add(new DevicesModel(41, "HTC One", 15));
        list.add(new DevicesModel(42, "HTC Desire", 15));
        list.add(new DevicesModel(43, "HTC U ", 15));
        list.add(new DevicesModel(44, "HTC Sensation", 15));
        list.add(new DevicesModel(45, "HTC Evo", 15));
        list.add(new DevicesModel(46, "HTC Wildfire", 15));
        list.add(new DevicesModel(47, "HTC Butterfly", 15));
        list.add(new DevicesModel(48, "HTC Explorer", 15));

        // HTC One Models
        list.add(new DevicesModel(49, "HTC One (M7)", 41));
        list.add(new DevicesModel(50, "HTC One M8", 41));
        list.add(new DevicesModel(51, "HTC One M9", 41));
        list.add(new DevicesModel(52, "HTC One A9", 41));
        list.add(new DevicesModel(53, "HTC One M10", 41));
        list.add(new DevicesModel(54, "HTC One X", 41));
        list.add(new DevicesModel(55, "HTC One S", 41));
        list.add(new DevicesModel(56, "HTC One V", 41));
        list.add(new DevicesModel(57, "HTC One Mini", 41));
        list.add(new DevicesModel(58, "HTC One E9", 41));

        // HTC Desire Models
        list.add(new DevicesModel(59, "HTC Desire 816", 42));
        list.add(new DevicesModel(60, "HTC Desire 820", 42));
        list.add(new DevicesModel(61, "HTC Desire 626", 42));
        list.add(new DevicesModel(62, "HTC Desire 530", 42));
        list.add(new DevicesModel(63, "HTC Desire 10 Pro", 42));
        list.add(new DevicesModel(64, "HTC Desire 12", 42));
        list.add(new DevicesModel(65, "HTC Desire Eye", 42));
        list.add(new DevicesModel(66, "HTC Desire HD", 42));
        list.add(new DevicesModel(67, "HTC Desire S", 42));
        list.add(new DevicesModel(68, "HTC Desire C", 42));

        // HTC U Models
        list.add(new DevicesModel(69, "HTC U11", 43));
        list.add(new DevicesModel(70, "HTC U11+", 43));
        list.add(new DevicesModel(71, "HTC U12+", 43));
        list.add(new DevicesModel(72, "HTC U Ultra", 43));
        list.add(new DevicesModel(73, "HTC U Play", 43));
        list.add(new DevicesModel(74, "HTC U19e", 43));
        list.add(new DevicesModel(75, "HTC U20 5G", 43));
        list.add(new DevicesModel(76, "HTC U11 Life", 43));
        list.add(new DevicesModel(77, "HTC U11 Eyes", 43));
        list.add(new DevicesModel(78, "HTC U11 Eyes", 43));

        // HTC U Models
        list.add(new DevicesModel(79, "HTC Sensation", 44));
        list.add(new DevicesModel(80, "HTC Sensation XE", 44));
        list.add(new DevicesModel(82, "HTC Sensation XL", 44));
        list.add(new DevicesModel(83, "HTC Sensation 4G", 44));

        // HTC Evo Models
        list.add(new DevicesModel(84, "HTC EVO 4G", 45));
        list.add(new DevicesModel(85, "HTC EVO 3D", 45));
        list.add(new DevicesModel(86, "HTC EVO Shift 4G", 45));
        list.add(new DevicesModel(87, "HTC EVO Design 4G", 45));

        // HTC Wildfire Models
        list.add(new DevicesModel(88, "HTC Wildfire S", 46));
        list.add(new DevicesModel(89, "HTC Wildfire X", 46));
        list.add(new DevicesModel(90, "HTC Wildfire E1", 46));
        list.add(new DevicesModel(91, "HTC Wildfire E3", 46));

        // HTC Butterfly Models
        list.add(new DevicesModel(92, "HTC Butterfly", 47));
        list.add(new DevicesModel(93, "HTC Butterfly S", 47));
        list.add(new DevicesModel(94, "HTC Butterfly 2", 47));
        list.add(new DevicesModel(95, "HTC Butterfly 3", 47));

        // ASUS Models
        list.add(new DevicesModel(96, "ZenFone", 17));
        list.add(new DevicesModel(97, "ROG", 17));
        list.add(new DevicesModel(98, "ZenFone Max", 17));
        list.add(new DevicesModel(99, "ZenFone Lite", 17));
        list.add(new DevicesModel(100, "ZenFone Selfie", 17));
        list.add(new DevicesModel(101, "ZenFone Live", 17));
        list.add(new DevicesModel(102, "PadFone", 17));

        // ASUS Zenfone Models
        list.add(new DevicesModel(103, "ZenFone 8", 96));
        list.add(new DevicesModel(104, "ZenFone 7", 96));
        list.add(new DevicesModel(105, "ZenFone 6", 96));
        list.add(new DevicesModel(106, "ZenFone 5", 96));
        list.add(new DevicesModel(107, "ZenFone 4", 96));
        list.add(new DevicesModel(108, "ZenFone 3", 96));
        list.add(new DevicesModel(109, "ZenFone 2", 96));
        list.add(new DevicesModel(110, "ZenFone", 96));

        // ASUS ROG Models
        list.add(new DevicesModel(111, "ROG Phone 5", 97));
        list.add(new DevicesModel(112, "ROG Phone 3", 97));
        list.add(new DevicesModel(113, "ROG Phone 2", 97));
        list.add(new DevicesModel(124, "ROG Phone", 97));

        //  ZenFone Max Models
        list.add(new DevicesModel(115, "ZenFone Max Pro M3", 98));
        list.add(new DevicesModel(116, "ZenFone Max Pro M2", 98));
        list.add(new DevicesModel(117, "ZenFone Max Pro M1", 98));
        list.add(new DevicesModel(118, "ZenFone Max M2", 98));
        list.add(new DevicesModel(119, "ZenFone Max M1", 98));

        // ZenFone Lite Models
        list.add(new DevicesModel(120, "ZenFone Lite L1", 99));
        list.add(new DevicesModel(121, "ZenFone Lite L2", 99));

        // ZenFone Selfie Models
        list.add(new DevicesModel(122, "Asus ZenFone Selfie", 100));
        list.add(new DevicesModel(123, "Asus ZenFone 4 Selfie", 100));

        // Asus ZenFone Live
        list.add(new DevicesModel(124, "Asus ZenFone Live L2", 101));
        list.add(new DevicesModel(125, "Asus ZenFone Live", 101));

        // PadFone Models
        list.add(new DevicesModel(126, "PadFone X", 102));
        list.add(new DevicesModel(127, "PadFone S", 102));

        // Xperia Models
        list.add(new DevicesModel(129, "Xperia 1", 128));
        list.add(new DevicesModel(130, "Xperia 5", 128));
        list.add(new DevicesModel(131, "Xperia 10", 128));
        list.add(new DevicesModel(132, "Xperia L", 128));
        list.add(new DevicesModel(133, "Xperia XZ", 128));
        list.add(new DevicesModel(134, "Xperia XA", 128));
        list.add(new DevicesModel(135, "Xperia Compact", 128));
        list.add(new DevicesModel(136, "Xperia M", 128));
        list.add(new DevicesModel(137, "Xperia Z", 128));

        // Xperia 1 Model
        list.add(new DevicesModel(138, "Xperia 1 III", 129));
        list.add(new DevicesModel(139, "Xperia 1 II", 129));
        list.add(new DevicesModel(140, "Xperia 1", 129));

        // Xperia 5 Model
        list.add(new DevicesModel(141, "Xperia 5 III", 130));
        list.add(new DevicesModel(142, "Xperia 5 II", 130));
        list.add(new DevicesModel(143, "Xperia 5", 130));

        // Xperia 10 Model
        list.add(new DevicesModel(144, "Xperia 10 III", 131));
        list.add(new DevicesModel(145, "Xperia 10 II", 131));
        list.add(new DevicesModel(146, "Xperia 10", 131));

        // Xperia L Model
        list.add(new DevicesModel(147, "Xperia L4", 132));
        list.add(new DevicesModel(148, "Xperia L3", 132));

        // Xperia XZ Model
        list.add(new DevicesModel(149, "Xperia XZ3", 133));
        list.add(new DevicesModel(150, "Xperia XZ2", 133));
        list.add(new DevicesModel(151, "Xperia XZ1", 133));

        // Xperia XA Model
        list.add(new DevicesModel(152, "Xperia XA2", 134));
        list.add(new DevicesModel(153, "Xperia XA1", 134));

        // Xperia Compact Model
        list.add(new DevicesModel(154, "Xperia XZ2 Compact", 135));
        list.add(new DevicesModel(155, "Xperia XZ1 Compact", 135));

        // Xperia M Model
        list.add(new DevicesModel(156, "Xperia M5", 136));
        list.add(new DevicesModel(157, "Xperia M4 Aqua", 136));

        // Xperia Z Model
        list.add(new DevicesModel(158, "Xperia Z5", 137));
        list.add(new DevicesModel(159, "Xperia Z3", 137));
        list.add(new DevicesModel(160, "Xperia Z1", 137));

        // Xiaomi Model
        list.add(new DevicesModel(161, "MI 12", 6));
        list.add(new DevicesModel(162, "Redmi", 6));
        list.add(new DevicesModel(163, "Mi MIX", 6));
        list.add(new DevicesModel(164, "Mi A", 6));
        list.add(new DevicesModel(165, "Black Shark", 6));
        list.add(new DevicesModel(166, "Mi Note", 6));
        list.add(new DevicesModel(167, "Mi Max", 6));

        // Mi
        list.add(new DevicesModel(168, "Mi 12", 161));
        list.add(new DevicesModel(169, "Mi 11", 161));
        list.add(new DevicesModel(170, "Mi 10", 161));
        list.add(new DevicesModel(171, "Mi 9", 161));
        list.add(new DevicesModel(172, "Mi 8", 161));
        list.add(new DevicesModel(173, "Mi 6", 161));
        list.add(new DevicesModel(174, "Mi 5", 161));
        list.add(new DevicesModel(175, "Mi 4", 161));
        list.add(new DevicesModel(176, "Mi 3", 161));
        list.add(new DevicesModel(177, "Mi 2", 161));

        // Redmi
        list.add(new DevicesModel(178, "Redmi Note 11", 162));
        list.add(new DevicesModel(179, "Redmi Note 10", 162));
        list.add(new DevicesModel(180, "Redmi Note 9", 162));
        list.add(new DevicesModel(181, "Redmi Note 8", 162));
        list.add(new DevicesModel(182, "Redmi Note 7", 162));
        list.add(new DevicesModel(183, "Redmi 10", 162));
        list.add(new DevicesModel(184, "Redmi 9", 162));
        list.add(new DevicesModel(185, "Redmi 8", 162));
        list.add(new DevicesModel(186, "Redmi 7", 162));
        list.add(new DevicesModel(187, "Redmi 6", 162));

        // Mi MIX
        list.add(new DevicesModel(188, "Mi MIX 4", 163));
        list.add(new DevicesModel(189, "Mi MIX 3", 163));
        list.add(new DevicesModel(190, "Mi MIX 2", 163));
        list.add(new DevicesModel(191, "Mi MIX", 163));

        // Mi A
        list.add(new DevicesModel(192, "Mi A4", 164));
        list.add(new DevicesModel(193, "Mi A3", 164));
        list.add(new DevicesModel(194, "Mi A2", 164));
        list.add(new DevicesModel(195, "Mi A1", 164));

        // Black Shark
        list.add(new DevicesModel(196, "Black Shark 4", 165));
        list.add(new DevicesModel(197, "Black Shark 3", 165));
        list.add(new DevicesModel(198, "Black Shark 2", 165));
        list.add(new DevicesModel(199, "Black Shark", 165));

        // Mi Note
        list.add(new DevicesModel(200, "Mi Note 11", 166));
        list.add(new DevicesModel(201, "Mi Note 10", 166));
        list.add(new DevicesModel(202, "Mi Note 3", 166));
        list.add(new DevicesModel(203, "Mi Note 2", 166));

        // Mi Max
        list.add(new DevicesModel(204, "Mi Max 4", 167));
        list.add(new DevicesModel(205, "Mi Max 3", 167));
        list.add(new DevicesModel(206, "Mi Max 2", 167));
        list.add(new DevicesModel(207, "Mi Max", 167));

        // Poco
        list.add(new DevicesModel(209, "Poco F", 208));
        list.add(new DevicesModel(210, "Poco X", 208));
        list.add(new DevicesModel(211, "Poco M", 208));
        list.add(new DevicesModel(212, "Poco C", 208));

        // Poco F
        list.add(new DevicesModel(213, "Poco F4", 209));
        list.add(new DevicesModel(214, "Poco F3", 209));

        // Poco X
        list.add(new DevicesModel(215, "Poco X3", 210));
        list.add(new DevicesModel(216, "POCO X3 Pro", 210));
        list.add(new DevicesModel(217, "POCO X2", 210));
        list.add(new DevicesModel(218, "Poco X5 Pro", 210));
        list.add(new DevicesModel(219, "Poco X4", 210));

        // Poco M
        list.add(new DevicesModel(220, "Poco M4", 211));
        list.add(new DevicesModel(221, "Poco M3 Pro", 211));

        // Poco C
        list.add(new DevicesModel(222, "Poco C4", 212));
        list.add(new DevicesModel(223, "Poco C3", 212));

        // Apple
        list.add(new DevicesModel(224, "iPhone 14", 3));
        list.add(new DevicesModel(225, "iPhone 13", 3));
        list.add(new DevicesModel(226, "iPhone 12", 3));
        list.add(new DevicesModel(227, "iPhone SE", 3));
        list.add(new DevicesModel(228, "iPhone 11", 3));
        list.add(new DevicesModel(229, "iPhone XS", 3));
        list.add(new DevicesModel(230, "iPhone XR", 3));
        list.add(new DevicesModel(231, "iPhone X", 3));
        list.add(new DevicesModel(232, "iPhone 8", 3));
        list.add(new DevicesModel(233, "iPhone 7", 3));
        list.add(new DevicesModel(234, "iPhone 6", 3));
        list.add(new DevicesModel(235, "iPhone 5", 3));
        list.add(new DevicesModel(236, "iPhone 4", 3));
        list.add(new DevicesModel(237, "iPhone 3", 3));
        list.add(new DevicesModel(238, "iPhone", 3));

        // iPhone 14
        list.add(new DevicesModel(239, "iPhone 14", 224));
        list.add(new DevicesModel(240, "iPhone 14 Plus", 224));
        list.add(new DevicesModel(241, "iPhone 14 Pro", 224));
        list.add(new DevicesModel(242, "iPhone 14 Pro Max", 224));

        // Iphone 13
        list.add(new DevicesModel(243, "iPhone 13", 225));
        list.add(new DevicesModel(244, "iPhone 13 Mini", 225));
        list.add(new DevicesModel(245, "iPhone 13 Pro", 225));
        list.add(new DevicesModel(246, "iPhone 13 Pro Max", 225));

        // Iphone 12
        list.add(new DevicesModel(247, "iPhone 12", 226));
        list.add(new DevicesModel(248, "iPhone 12 Mini", 226));
        list.add(new DevicesModel(249, "iPhone 12 Pro", 226));
        list.add(new DevicesModel(250, "iPhone 12 Pro Max", 226));

        // Iphone 11
        list.add(new DevicesModel(251, "iPhone 11", 228));
        list.add(new DevicesModel(252, "iPhone 11 Pro", 228));
        list.add(new DevicesModel(253, "iPhone 11 Pro Max", 228));

        // Iphone Xs
        list.add(new DevicesModel(254, "iPhone XS", 229));
        list.add(new DevicesModel(255, "iPhone XS Max", 229));

        // Iphone 8
        list.add(new DevicesModel(256, "iPhone 8", 232));
        list.add(new DevicesModel(257, "iPhone 8 Plus", 232));

        // Iphone 7
        list.add(new DevicesModel(258, "iPhone 7", 233));
        list.add(new DevicesModel(259, "iPhone 7 Plus", 233));

        // Iphone 6
        list.add(new DevicesModel(260, "iPhone 6", 234));
        list.add(new DevicesModel(261, "iPhone 6s", 234));
        list.add(new DevicesModel(262, "iPhone 6 Plus", 234));

        // Iphone 5
        list.add(new DevicesModel(263, "iPhone 5", 235));
        list.add(new DevicesModel(264, "iPhone 5c", 235));
        list.add(new DevicesModel(265, "iPhone 5s", 235));

        // Iphone 4
        list.add(new DevicesModel(266, "iPhone 4", 236));
        list.add(new DevicesModel(267, "iPhone 4s", 236));

        // Iphone 3
        list.add(new DevicesModel(268, "iPhone 3G", 237));
        list.add(new DevicesModel(269, "iPhone 3Gs", 237));

        // Iphone SE
        list.add(new DevicesModel(270, "iPhone SE (1st generation)", 227));
        list.add(new DevicesModel(271, "iPhone SE (2nd generation)", 227));

        // Oneplus
        list.add(new DevicesModel(272, "OnePlus 9", 7));
        list.add(new DevicesModel(273, "OnePlus 8", 7));
        list.add(new DevicesModel(274, "OnePlus 7", 7));
        list.add(new DevicesModel(275, "OnePlus 6", 7));
        list.add(new DevicesModel(276, "OnePlus 5", 7));
        list.add(new DevicesModel(277, "OnePlus Nord", 7));
        list.add(new DevicesModel(278, "OnePlus X", 7));
        list.add(new DevicesModel(279, "OnePlus 3", 7));
        list.add(new DevicesModel(280, "OnePlus 2 & 1", 7));

        // Oneplus 9
        list.add(new DevicesModel(281, "OnePlus 9", 272));
        list.add(new DevicesModel(282, "OnePlus 9 Pro", 272));
        list.add(new DevicesModel(283, "OnePlus 9R", 272));
        list.add(new DevicesModel(284, "OnePlus 9RT", 272));

        // Oneplus 8
        list.add(new DevicesModel(285, "OnePlus 8", 273));
        list.add(new DevicesModel(286, "OnePlus 8 Pro", 273));
        list.add(new DevicesModel(287, "OnePlus 8T", 273));

        // OnePlus 7
        list.add(new DevicesModel(288, "OnePlus 7", 274));
        list.add(new DevicesModel(289, "OnePlus 7 Pro", 274));
        list.add(new DevicesModel(290, "OnePlus 7T", 274));
        list.add(new DevicesModel(291, "OnePlus 7T Pro", 274));

        // OnePlus 6
        list.add(new DevicesModel(292, "OnePlus 6", 275));
        list.add(new DevicesModel(293, "OnePlus 6T", 275));

        // OnePlus 5
        list.add(new DevicesModel(294, "OnePlus 5", 276));
        list.add(new DevicesModel(295, "OnePlus 5T", 276));

        // OnePlus Nord
        list.add(new DevicesModel(296, "OnePlus Nord", 277));
        list.add(new DevicesModel(297, "OnePlus Nord 2", 277));
        list.add(new DevicesModel(298, "OnePlus Nord CE 5G", 277));

        // OnePlus X
        list.add(new DevicesModel(299, "OnePlus X", 278));

        // OnePlus 3
        list.add(new DevicesModel(300, "OnePlus 3T", 279));
        list.add(new DevicesModel(301, "OnePlus 3", 279));

        // OnePlus 2 & 1
        list.add(new DevicesModel(302, "OnePlus 2", 280));
        list.add(new DevicesModel(303, "OnePlus 1", 280));

        // Realme
        list.add(new DevicesModel(305, "Realme GT", 304));
        list.add(new DevicesModel(306, "Realme X", 304));
        list.add(new DevicesModel(307, "Realme Narzo", 304));
        list.add(new DevicesModel(308, "Realme C", 304));
        list.add(new DevicesModel(309, "Realme V", 304));
        list.add(new DevicesModel(310, "Realme U", 304));
        list.add(new DevicesModel(311, "Realme 8", 304));
        list.add(new DevicesModel(312, "Realme 7", 304));
        list.add(new DevicesModel(313, "Realme 6", 304));
        list.add(new DevicesModel(314, "Realme 5", 304));
        list.add(new DevicesModel(315, "Realme 3", 304));
        list.add(new DevicesModel(316, "Realme 2", 304));
        list.add(new DevicesModel(317, "Realme 1", 304));

        // Realme GT
        list.add(new DevicesModel(318, "Realme GT 5G", 305));

        // Realme X
        list.add(new DevicesModel(319, "Realme X7 Pro", 306));
        list.add(new DevicesModel(320, "Realme X7", 306));
        list.add(new DevicesModel(321, "Realme X50 Pro 5G", 306));
        list.add(new DevicesModel(322, "Realme X50 5G", 306));
        list.add(new DevicesModel(323, "Realme XT", 306));
        list.add(new DevicesModel(324, "Realme X2 Pro", 306));
        list.add(new DevicesModel(325, "Realme X2", 306));
        list.add(new DevicesModel(326, "Realme X", 306));

        // Realme Narzo
        list.add(new DevicesModel(327, "Realme Narzo 30 Pro 5G", 307));
        list.add(new DevicesModel(328, "Realme Narzo 30A", 307));
        list.add(new DevicesModel(329, "Realme Narzo 20 Pro", 307));
        list.add(new DevicesModel(330, "Realme Narzo 20A", 307));
        list.add(new DevicesModel(331, "Realme Narzo 10A", 307));

        // Realme C
        list.add(new DevicesModel(332, "Realme C25s", 308));
        list.add(new DevicesModel(333, "Realme C25", 308));
        list.add(new DevicesModel(334, "Realme C21", 308));
        list.add(new DevicesModel(335, "Realme C20", 308));

        // Realme V
        list.add(new DevicesModel(336, "Realme V15", 309));
        list.add(new DevicesModel(337, "Realme V11", 309));
        list.add(new DevicesModel(338, "Realme V5", 309));

        // Realme U
        list.add(new DevicesModel(339, "Realme U1", 310));

        // Realme 8
        list.add(new DevicesModel(340, "Realme 8 Pro", 310));
        list.add(new DevicesModel(341, "Realme 8", 310));
        list.add(new DevicesModel(342, "Realme 8i", 310));
        list.add(new DevicesModel(343, "Realme 8 5G", 310));

        // Realme 7
        list.add(new DevicesModel(344, "Realme 7 Pro", 311));
        list.add(new DevicesModel(345, "Realme 7", 311));

        // Realme 6
        list.add(new DevicesModel(346, "Realme 6 Pro", 312));
        list.add(new DevicesModel(347, "Realme 6", 312));
        list.add(new DevicesModel(348, "Realme 6i", 312));
        list.add(new DevicesModel(349, "Realme 6S", 312));

        // Realme 5
        list.add(new DevicesModel(350, "Realme 5 Pro", 313));
        list.add(new DevicesModel(351, "Realme 5", 313));
        list.add(new DevicesModel(352, "Realme 5i", 313));

        // Realme 3
        list.add(new DevicesModel(353, "Realme 3 Pro", 314));
        list.add(new DevicesModel(354, "Realme 3", 314));

        // Realme 2
        list.add(new DevicesModel(355, "Realme 2 Pro", 315));
        list.add(new DevicesModel(356, "Realme 2", 315));

        // Realme 1
        list.add(new DevicesModel(357, "Realme 1", 316));

        // Oppo
        list.add(new DevicesModel(358, "Oppo Find", 8));
        list.add(new DevicesModel(359, "Oppo Reno", 8));
        list.add(new DevicesModel(360, "Oppo A", 8));
        list.add(new DevicesModel(361, "Oppo F", 8));
        list.add(new DevicesModel(362, "Oppo K", 8));
        list.add(new DevicesModel(363, "Oppo R", 8));
        list.add(new DevicesModel(364, "Oppo N", 8));
        list.add(new DevicesModel(365, "Oppo Ace", 8));
        list.add(new DevicesModel(366, "Oppo Joy", 8));
        list.add(new DevicesModel(367, "Oppo Neo", 8));

        // Oppo Find
        list.add(new DevicesModel(368, "Find X3 Pro", 358));
        list.add(new DevicesModel(369, "Find X2 Pro", 358));
        list.add(new DevicesModel(370, "Find X2", 358));
        list.add(new DevicesModel(371, "Find X", 358));

        // Oppo Reno
        list.add(new DevicesModel(372, "Reno 6 Pro+", 359));
        list.add(new DevicesModel(373, "Reno 6 Pro+", 359));
        list.add(new DevicesModel(374, "Reno 6", 359));
        list.add(new DevicesModel(375, "Reno 5 Pro+", 359));
        list.add(new DevicesModel(376, "Reno 5 Pro", 359));
        list.add(new DevicesModel(377, "Reno 5", 359));
        list.add(new DevicesModel(378, "Reno 4 Pro", 359));
        list.add(new DevicesModel(379, "Reno 4", 359));
        list.add(new DevicesModel(380, "Reno 3 Pro", 359));
        list.add(new DevicesModel(381, "Reno 3", 359));

        // Oppo A
        list.add(new DevicesModel(382, "A94", 360));
        list.add(new DevicesModel(383, "A93", 360));
        list.add(new DevicesModel(384, "A92", 360));
        list.add(new DevicesModel(385, "A91", 360));
        list.add(new DevicesModel(386, "A15", 360));
        list.add(new DevicesModel(387, "A12", 360));
        list.add(new DevicesModel(388, "A11k", 360));

        // Oppo F
        list.add(new DevicesModel(389, "F19 Pro+", 361));
        list.add(new DevicesModel(390, "F19 Pro", 361));
        list.add(new DevicesModel(391, "F19", 361));
        list.add(new DevicesModel(392, "F17 Pro", 361));
        list.add(new DevicesModel(393, "F17", 361));
        list.add(new DevicesModel(394, "F15", 361));

        // Oppo K
        list.add(new DevicesModel(395, "K9", 362));
        list.add(new DevicesModel(396, "K7x", 362));
        list.add(new DevicesModel(397, "K7", 362));

        // Oppo R
        list.add(new DevicesModel(398, "R15 Pro", 363));
        list.add(new DevicesModel(399, "R11s Plus", 363));
        list.add(new DevicesModel(400, "R9s Plus", 363));
        list.add(new DevicesModel(401, "R7 Plus", 363));
        list.add(new DevicesModel(402, "R5", 363));

        // Oppo N
        list.add(new DevicesModel(403, "N3", 364));
        list.add(new DevicesModel(404, "N1", 364));
        list.add(new DevicesModel(405, "N1 mini", 364));

        // Oppo Ace
        list.add(new DevicesModel(406, "Ace2", 365));
        list.add(new DevicesModel(407, "Ace2 EVA Limited Edition", 365));

        // Oppo Joy
        list.add(new DevicesModel(408, "Joy", 366));
        list.add(new DevicesModel(409, "Joy Plus", 366));

        // Oppo Neo
        list.add(new DevicesModel(410, "Neo 7", 367));
        list.add(new DevicesModel(411, "Neo 5", 367));

        // Vivo
        list.add(new DevicesModel(412, "Vivo X", 9));
        list.add(new DevicesModel(413, "Vivo V", 9));
        list.add(new DevicesModel(414, "Vivo Y", 9));
        list.add(new DevicesModel(415, "Vivo S", 9));
        list.add(new DevicesModel(416, "Vivo iQOO", 9));
        list.add(new DevicesModel(417, "Vivo Z", 9));
        list.add(new DevicesModel(418, "Vivo U", 9));
        list.add(new DevicesModel(419, "Vivo Nex", 9));
        list.add(new DevicesModel(420, "Vivo Y20", 9));
        list.add(new DevicesModel(421, "Vivo Y17", 9));

        // Vivo X
        list.add(new DevicesModel(422, "X70 Pro+", 412));
        list.add(new DevicesModel(423, "X70 Pro", 412));
        list.add(new DevicesModel(424, "X70", 412));
        list.add(new DevicesModel(425, "X60 Pro+", 412));
        list.add(new DevicesModel(426, "X60 Pro", 412));
        list.add(new DevicesModel(427, "X60", 412));
        list.add(new DevicesModel(428, "X51 5G", 412));
        list.add(new DevicesModel(429, "X50 Pro+", 412));
        list.add(new DevicesModel(430, "X50 Pro", 412));
        list.add(new DevicesModel(431, "X50", 412));

        // Vivo V
        list.add(new DevicesModel(432, "V21 Pro", 413));
        list.add(new DevicesModel(433, "V21e", 413));
        list.add(new DevicesModel(434, "V21", 413));
        list.add(new DevicesModel(435, "V20 Pro", 413));
        list.add(new DevicesModel(436, "V20 SE", 413));
        list.add(new DevicesModel(437, "V20", 413));
        list.add(new DevicesModel(438, "V19", 413));
        list.add(new DevicesModel(439, "V17 Pro", 413));
        list.add(new DevicesModel(440, "V15 Pro", 413));
        list.add(new DevicesModel(441, "V11 Pro", 413));

        // Vivo Y
        list.add(new DevicesModel(442, "Y72 5G", 414));
        list.add(new DevicesModel(443, "Y70", 414));
        list.add(new DevicesModel(444, "Y31s", 414));
        list.add(new DevicesModel(445, "Y31", 414));
        list.add(new DevicesModel(446, "Y20G", 414));
        list.add(new DevicesModel(447, "Y20s", 414));
        list.add(new DevicesModel(448, "Y20", 414));
        list.add(new DevicesModel(449, "Y19", 414));
        list.add(new DevicesModel(450, "Y12s", 414));
        list.add(new DevicesModel(451, "Y12", 414));

        // Vivo S
        list.add(new DevicesModel(452, "S10 Pro", 415));
        list.add(new DevicesModel(453, "S10", 415));
        list.add(new DevicesModel(454, "S9", 415));
        list.add(new DevicesModel(455, "S7t", 415));
        list.add(new DevicesModel(456, "S7e", 415));
        list.add(new DevicesModel(457, "S7", 415));
        list.add(new DevicesModel(458, "S6", 415));
        list.add(new DevicesModel(459, "S5", 415));
        list.add(new DevicesModel(460, "S1 Pro", 415));
        list.add(new DevicesModel(461, "S1", 415));

        // Vivo iQOO
        list.add(new DevicesModel(462, "iQOO 8 Pro", 416));
        list.add(new DevicesModel(463, "iQOO 8", 416));
        list.add(new DevicesModel(464, "iQOO 7 Legend", 416));
        list.add(new DevicesModel(465, "iQOO 7", 416));
        list.add(new DevicesModel(466, "iQOO Neo 5", 416));
        list.add(new DevicesModel(467, "iQOO 5 Pro", 416));
        list.add(new DevicesModel(468, "iQOO 5", 416));
        list.add(new DevicesModel(469, "iQOO 3", 416));
        list.add(new DevicesModel(470, "iQOO Z3", 416));
        list.add(new DevicesModel(471, "iQOO Z1x", 416));

        // Vivo Z
        list.add(new DevicesModel(472, "Z6", 417));
        list.add(new DevicesModel(473, "Z5X", 417));
        list.add(new DevicesModel(474, "Z5i", 417));
        list.add(new DevicesModel(475, "Z5", 417));
        list.add(new DevicesModel(476, "Z3x", 417));
        list.add(new DevicesModel(477, "Z3", 417));
        list.add(new DevicesModel(478, "Z1x", 417));
        list.add(new DevicesModel(479, "Z1 Pro", 417));
        list.add(new DevicesModel(480, "Z1 Lite", 417));
        list.add(new DevicesModel(481, "Z1", 417));

        // Vivo U
        list.add(new DevicesModel(482, "U20", 418));
        list.add(new DevicesModel(483, "U10", 418));
        list.add(new DevicesModel(484, "U1", 418));

        // Vivo Nex
        list.add(new DevicesModel(485, "Nex 3S", 419));
        list.add(new DevicesModel(486, "Nex 3", 419));
        list.add(new DevicesModel(487, "Nex Dual Display Edition", 419));
        list.add(new DevicesModel(488, "Nex S", 419));
        list.add(new DevicesModel(489, "Nex", 419));

        // Vivo Y20
        list.add(new DevicesModel(490, "Y20G", 420));
        list.add(new DevicesModel(491, "Y20s", 420));
        list.add(new DevicesModel(492, "Y20", 420));

        // Vivo Y17
        list.add(new DevicesModel(493, "Y17", 421));

        // Huawei
        list.add(new DevicesModel(494, "Huawei P", 5));
        list.add(new DevicesModel(495, "Huawei Mate", 5));
        list.add(new DevicesModel(496, "Huawei Nova", 5));
        list.add(new DevicesModel(497, "Huawei Y", 5));
        list.add(new DevicesModel(498, "Huawei Enjoy", 5));
        list.add(new DevicesModel(499, "Huawei G", 5));
        list.add(new DevicesModel(500, "Huawei X", 5));
        list.add(new DevicesModel(501, "Huawei Maimang", 5));
        list.add(new DevicesModel(502, "Huawei Ascend", 5));

        // Huawei P
        list.add(new DevicesModel(503, "P50 Pro", 494));
        list.add(new DevicesModel(504, "P50", 494));
        list.add(new DevicesModel(505, "P40 Pro", 494));
        list.add(new DevicesModel(506, "P40", 494));
        list.add(new DevicesModel(507, "P30 Pro", 494));
        list.add(new DevicesModel(508, "P30", 494));
        list.add(new DevicesModel(509, "P20 Pro", 494));
        list.add(new DevicesModel(510, "P20", 494));
        list.add(new DevicesModel(511, "P10 Plus", 494));
        list.add(new DevicesModel(512, "P10", 494));

        // Huawei Mate
        list.add(new DevicesModel(513, "Mate 40 Pro", 495));
        list.add(new DevicesModel(514, "Mate 40", 495));
        list.add(new DevicesModel(515, "Mate 30 Pro", 495));
        list.add(new DevicesModel(516, "Mate 30", 495));
        list.add(new DevicesModel(517, "Mate 20 Pro", 495));
        list.add(new DevicesModel(518, "Mate 20", 495));
        list.add(new DevicesModel(519, "Mate 10 Pro", 495));
        list.add(new DevicesModel(520, "Mate 10", 495));

        // Huawei Nova
        list.add(new DevicesModel(521, "Nova 9", 496));
        list.add(new DevicesModel(522, "Nova 8 Pro", 496));
        list.add(new DevicesModel(523, "Nova 8", 496));
        list.add(new DevicesModel(524, "Nova 7 Pro", 496));
        list.add(new DevicesModel(525, "Nova 7", 496));
        list.add(new DevicesModel(526, "Nova 6", 496));
        list.add(new DevicesModel(527, "Nova 5 Pro", 496));
        list.add(new DevicesModel(528, "Nova 5", 496));

        // Huawei Y
        list.add(new DevicesModel(529, "Y9s", 497));
        list.add(new DevicesModel(530, "Y9 Prime", 497));
        list.add(new DevicesModel(531, "Y7a", 497));
        list.add(new DevicesModel(532, "Y6s", 497));
        list.add(new DevicesModel(533, "Y5p", 497));

        // Huawei Enjoy
        list.add(new DevicesModel(534, "Enjoy 20 Pro", 498));
        list.add(new DevicesModel(535, "Enjoy 10 Plus", 498));
        list.add(new DevicesModel(536, "Enjoy 9e", 498));

        // Huawei G
        list.add(new DevicesModel(537, "G9 Plus", 499));
        list.add(new DevicesModel(538, "G8", 499));

        // Huawei X
        list.add(new DevicesModel(539, "X2", 500));

        // Huawei Maimang
        list.add(new DevicesModel(540, "Maimang 9", 501));
        list.add(new DevicesModel(541, "Maimang 8", 501));

        // Huawei Ascend
        list.add(new DevicesModel(542, "Ascend P7", 502));
        list.add(new DevicesModel(543, "Ascend Mate 7", 502));
        list.add(new DevicesModel(544, "Ascend G7", 502));

        // LG
        list.add(new DevicesModel(545, "LG G", 10));
        list.add(new DevicesModel(546, "LG V", 10));
        list.add(new DevicesModel(547, "LG Velvet", 10));
        list.add(new DevicesModel(548, "LG Q", 10));
        list.add(new DevicesModel(549, "LG K", 10));
        list.add(new DevicesModel(550, "LG W", 10));
        list.add(new DevicesModel(551, "LG Stylo", 10));
        list.add(new DevicesModel(552, "LG X", 10));
        list.add(new DevicesModel(553, "LG Nexus", 10));
        list.add(new DevicesModel(554, "LG Optimus", 10));

        // LG G
        list.add(new DevicesModel(555, "G8 ThinQ", 545));
        list.add(new DevicesModel(556, "G7 ThinQ", 545));
        list.add(new DevicesModel(557, "G6", 545));
        list.add(new DevicesModel(558, "G5", 545));
        list.add(new DevicesModel(559, "G4", 545));
        list.add(new DevicesModel(560, "G3", 545));
        list.add(new DevicesModel(561, "G2", 545));

        // LG V
        list.add(new DevicesModel(562, "V60 ThinQ", 546));
        list.add(new DevicesModel(563, "V50 ThinQ", 546));
        list.add(new DevicesModel(564, "V40 ThinQ", 546));
        list.add(new DevicesModel(565, "V30", 546));
        list.add(new DevicesModel(566, "V20", 546));
        list.add(new DevicesModel(567, "V10", 546));

        // LG Velvet
        list.add(new DevicesModel(568, "Velvet 5G", 547));
        list.add(new DevicesModel(569, "Velvet", 547));

        // LG Q
        list.add(new DevicesModel(570, "Q70", 548));
        list.add(new DevicesModel(571, "Q60", 548));
        list.add(new DevicesModel(572, "Q Stylus", 548));
        list.add(new DevicesModel(573, "Q7", 548));
        list.add(new DevicesModel(574, "Q6", 548));

        // LG K
        list.add(new DevicesModel(575, "K92 5G", 549));
        list.add(new DevicesModel(576, "K61", 549));
        list.add(new DevicesModel(577, "K51S", 549));
        list.add(new DevicesModel(578, "K40S", 549));
        list.add(new DevicesModel(579, "K30", 549));

        // LG W
        list.add(new DevicesModel(580, "W41 Pro", 550));
        list.add(new DevicesModel(581, "W31 Plus", 550));
        list.add(new DevicesModel(582, "W31", 550));

        // LG Stylo
        list.add(new DevicesModel(583, "Stylo 6", 551));
        list.add(new DevicesModel(584, "Stylo 5", 551));
        list.add(new DevicesModel(585, "Stylo 4", 551));

        // LG X
        list.add(new DevicesModel(586, "X Power", 552));
        list.add(new DevicesModel(587, "X Mach", 552));
        list.add(new DevicesModel(588, "X Screen", 552));

        // LG Nexus
        list.add(new DevicesModel(589, "Nexus 5X", 553));
        list.add(new DevicesModel(590, "Nexus 5", 553));
        list.add(new DevicesModel(591, "Nexus 4", 553));

        // LG Optimus
        list.add(new DevicesModel(592, "Optimus G Pro", 554));
        list.add(new DevicesModel(593, "Optimus G", 554));
        list.add(new DevicesModel(594, "Optimus L9", 554));
        list.add(new DevicesModel(595, "Optimus 3D", 554));

        /*
        list.add(new DevicesModel(596, "", 554));
        list.add(new DevicesModel(597, "", 554));
        list.add(new DevicesModel(598, "", 554));
        list.add(new DevicesModel(599, "", 554));

        list.add(new DevicesModel(600, "", 494));
        list.add(new DevicesModel(601, "", 494));
        list.add(new DevicesModel(602, "", 494));
        list.add(new DevicesModel(603, "", 494));
        list.add(new DevicesModel(604, "", 494));
        list.add(new DevicesModel(605, "", 494));
        list.add(new DevicesModel(606, "", 494));
        list.add(new DevicesModel(607, "", 494));
        list.add(new DevicesModel(608, "", 494));
        list.add(new DevicesModel(609, "", 494));

        list.add(new DevicesModel(610, "", 494));
        list.add(new DevicesModel(611, "", 494));
        list.add(new DevicesModel(612, "", 494));
        list.add(new DevicesModel(613, "", 494));
        list.add(new DevicesModel(614, "", 494));
        list.add(new DevicesModel(615, "", 494));
        list.add(new DevicesModel(616, "", 494));
        list.add(new DevicesModel(617, "", 494));
        list.add(new DevicesModel(618, "", 494));
        list.add(new DevicesModel(619, "", 494));

        list.add(new DevicesModel(620, "", 494));
        list.add(new DevicesModel(621, "", 494));
        list.add(new DevicesModel(622, "", 494));
        list.add(new DevicesModel(623, "", 494));
        list.add(new DevicesModel(624, "", 494));
        list.add(new DevicesModel(625, "", 494));
        list.add(new DevicesModel(626, "", 494));
        list.add(new DevicesModel(627, "", 494));
        list.add(new DevicesModel(628, "", 494));
        list.add(new DevicesModel(629, "", 494));

        list.add(new DevicesModel(630, "", 494));
        list.add(new DevicesModel(631, "", 494));
        list.add(new DevicesModel(632, "", 494));
        list.add(new DevicesModel(633, "", 494));
        list.add(new DevicesModel(634, "", 494));
        list.add(new DevicesModel(635, "", 494));
        list.add(new DevicesModel(636, "", 494));
        list.add(new DevicesModel(637, "", 494));
        list.add(new DevicesModel(638, "", 494));
        list.add(new DevicesModel(639, "", 494));

        list.add(new DevicesModel(640, "", 494));
        list.add(new DevicesModel(641, "", 494));
        list.add(new DevicesModel(642, "", 494));
        list.add(new DevicesModel(643, "", 494));
        list.add(new DevicesModel(644, "", 494));
        list.add(new DevicesModel(645, "", 494));
        list.add(new DevicesModel(646, "", 494));
        list.add(new DevicesModel(647, "", 494));
        list.add(new DevicesModel(648, "", 494));
        list.add(new DevicesModel(649, "", 494));

        list.add(new DevicesModel(650, "", 494));
        list.add(new DevicesModel(651, "", 494));
        list.add(new DevicesModel(652, "", 494));
        list.add(new DevicesModel(653, "", 494));
        list.add(new DevicesModel(654, "", 494));
        list.add(new DevicesModel(655, "", 494));
        list.add(new DevicesModel(656, "", 494));
        list.add(new DevicesModel(657, "", 494));
        list.add(new DevicesModel(658, "", 494));
        list.add(new DevicesModel(659, "", 494));

        list.add(new DevicesModel(660, "", 494));
        list.add(new DevicesModel(661, "", 494));
        list.add(new DevicesModel(662, "", 494));
        list.add(new DevicesModel(663, "", 494));
        list.add(new DevicesModel(664, "", 494));
        list.add(new DevicesModel(665, "", 494));
        list.add(new DevicesModel(666, "", 494));
        list.add(new DevicesModel(667, "", 494));
        list.add(new DevicesModel(668, "", 494));
        list.add(new DevicesModel(669, "", 494));

        list.add(new DevicesModel(670, "", 494));
        list.add(new DevicesModel(671, "", 494));
        list.add(new DevicesModel(672, "", 494));
        list.add(new DevicesModel(673, "", 494));
        list.add(new DevicesModel(674, "", 494));
        list.add(new DevicesModel(675, "", 494));
        list.add(new DevicesModel(676, "", 494));
        list.add(new DevicesModel(677, "", 494));
        list.add(new DevicesModel(678, "", 494));
        list.add(new DevicesModel(679, "", 494));
*/


        return list;
    }

    public static List<MobileModels> getTipsTricksData() {
        List<MobileModels> list = new ArrayList<>();

        list.add(new MobileModels(-1, -2, currentActivity.getString(R.string.mobile_tips), null, 0));
        list.add(new MobileModels(0, -2, currentActivity.getString(R.string.mobile_tricks), null, 0));

        list.add(new MobileModels(1, -1, currentActivity.getString(R.string.tips_title1), currentActivity.getString(R.string.tips_desc1), R.drawable.globe));
        list.add(new MobileModels(2, -1, currentActivity.getString(R.string.tips_title2), currentActivity.getString(R.string.tips_desc2), R.drawable.performance));
        list.add(new MobileModels(3, -1, currentActivity.getString(R.string.tips_title3), currentActivity.getString(R.string.tips_desc3), R.drawable.tips_and_tricky));
        list.add(new MobileModels(4, -1, currentActivity.getString(R.string.tips_title4), currentActivity.getString(R.string.tips_desc4), R.drawable.shield));
        list.add(new MobileModels(5, -1, currentActivity.getString(R.string.tips_title5), currentActivity.getString(R.string.tips_desc5), R.drawable.gift_box));

        list.add(new MobileModels(6, 1, "Internet Speed", currentActivity.getString(R.string.internet_speed), R.drawable.ic_internet));
        list.add(new MobileModels(7, 1, "Mobile Data", currentActivity.getString(R.string.mobile_data), R.drawable.ic_data));
        list.add(new MobileModels(8, 1, "Data Server", currentActivity.getString(R.string.data_server), R.drawable.ic_server_data));

        list.add(new MobileModels(9, 2, "Performance", currentActivity.getString(R.string.performance), R.drawable.ic_performance));
        list.add(new MobileModels(10, 2, "Storage", currentActivity.getString(R.string.storage), R.drawable.ic_sd_storage));
        list.add(new MobileModels(11, 2, "Battery", currentActivity.getString(R.string.battery), R.drawable.ic_battery));
        list.add(new MobileModels(12, 2, "Battery Saver", currentActivity.getString(R.string.battery_saver), R.drawable.ic_battery_saver));
        list.add(new MobileModels(13, 2, "Free Storage", currentActivity.getString(R.string.free_storage), R.drawable.ic_free_storage));
        list.add(new MobileModels(14, 2, "System", currentActivity.getString(R.string.system), R.drawable.ic_system));
        list.add(new MobileModels(15, 2, "Display", currentActivity.getString(R.string.display), R.drawable.ic_display));

        list.add(new MobileModels(16, 3, "Profile", currentActivity.getString(R.string.profile), R.drawable.ic_profile));
        list.add(new MobileModels(17, 3, "Notes", currentActivity.getString(R.string.notes), R.drawable.ic_notes));
        list.add(new MobileModels(18, 3, "Gesture", currentActivity.getString(R.string.gesture), R.drawable.ic_gesture));
        list.add(new MobileModels(19, 3, "OK Google", currentActivity.getString(R.string.ok_google), R.drawable.ic_google));
        list.add(new MobileModels(20, 3, "Dictionary", currentActivity.getString(R.string.dictionary), R.drawable.ic_dictionary));
        list.add(new MobileModels(21, 3, "Find your Device", currentActivity.getString(R.string.find_your_device), R.drawable.ic_device_ssd));
        list.add(new MobileModels(22, 3, "Tips & Tricks", currentActivity.getString(R.string.tips_and_tricks), R.drawable.ic_info_tips));
        list.add(new MobileModels(23, 3, "Remote Access", currentActivity.getString(R.string.remote_access), R.drawable.ic_remote_fill));
        list.add(new MobileModels(24, 3, "Voice Search", currentActivity.getString(R.string.voice_search), R.drawable.ic_mic));
        list.add(new MobileModels(25, 3, "Missed Call Responses", currentActivity.getString(R.string.missed_call_responses), R.drawable.ic_phone_miss));
        list.add(new MobileModels(26, 3, "Languages", currentActivity.getString(R.string.languages), R.drawable.ic_languages));
        list.add(new MobileModels(27, 3, "Picture Mode", currentActivity.getString(R.string.picture_mode), R.drawable.ic_pip));

        list.add(new MobileModels(28, 4, "Privacy", currentActivity.getString(R.string.privacy), R.drawable.ic_privacy));

        list.add(new MobileModels(29, 5, "Booster", currentActivity.getString(R.string.booster), R.drawable.ic_booster));
        list.add(new MobileModels(30, 5, "OTG (On-The-Go)", currentActivity.getString(R.string.otg), R.drawable.ic_otg));
        list.add(new MobileModels(31, 5, "Miscellaneous", currentActivity.getString(R.string.miscellaneous), R.drawable.ic_miscellaneous));
        list.add(new MobileModels(32, 5, "Reset", currentActivity.getString(R.string.reset), R.drawable.ic_reset));
        list.add(new MobileModels(33, 5, "Notification Center", currentActivity.getString(R.string.notification_center), R.drawable.ic_notification));

        list.add(new MobileModels(34, 0, currentActivity.getString(R.string.tricks_title1), currentActivity.getString(R.string.tricks_desc1), R.drawable.tips_and_tricky));
        list.add(new MobileModels(35, 0, currentActivity.getString(R.string.tricks_title2), currentActivity.getString(R.string.tricks_desc2), R.drawable.file_folder));
        list.add(new MobileModels(36, 0, currentActivity.getString(R.string.tricks_title3), currentActivity.getString(R.string.tricks_desc3), R.drawable.battery_optimize));
        list.add(new MobileModels(37, 0, currentActivity.getString(R.string.tricks_title4), currentActivity.getString(R.string.tricks_desc4), R.drawable.wireless_connectivity));
        list.add(new MobileModels(38, 0, currentActivity.getString(R.string.tricks_title5), currentActivity.getString(R.string.tricks_desc5), R.drawable.screen_and_display));
        list.add(new MobileModels(39, 0, currentActivity.getString(R.string.tricks_title6), currentActivity.getString(R.string.tricks_desc6), R.drawable.accessibility));
        list.add(new MobileModels(40, 0, currentActivity.getString(R.string.tricks_title7), currentActivity.getString(R.string.tricks_desc7), R.drawable.game_controller));
        list.add(new MobileModels(41, 0, currentActivity.getString(R.string.tricks_title8), currentActivity.getString(R.string.tricks_desc8), R.drawable.globe_one));

        list.add(new MobileModels(42, 34, "Unknown Facts", currentActivity.getString(R.string.unknown_facts), R.drawable.ic_unknown_facts));
        list.add(new MobileModels(43, 34, "Unlock Pattern", currentActivity.getString(R.string.unlock_pattern), R.drawable.ic_unlock_patterns));
        list.add(new MobileModels(44, 34, "Break Pattern", currentActivity.getString(R.string.break_pattern), R.drawable.ic_break_pattern));
        list.add(new MobileModels(45, 34, "Hard Factory Reset", currentActivity.getString(R.string.hard_reset), R.drawable.ic_reset));

        list.add(new MobileModels(46, 35, "Recover Files", currentActivity.getString(R.string.recover_files), R.drawable.ic_recover));
        list.add(new MobileModels(47, 35, "Delete Notification", currentActivity.getString(R.string.delete_notification), R.drawable.ic_delete));

        list.add(new MobileModels(48, 36, "Improve Battery", currentActivity.getString(R.string.improve_battery), R.drawable.ic_battery));

        list.add(new MobileModels(49, 37, "WiFi Hotspot", currentActivity.getString(R.string.wifi_hotspot), R.drawable.ic_wifi));
        list.add(new MobileModels(50, 37, "Show WiFi Hotspot", currentActivity.getString(R.string.show_wifi), R.drawable.ic_hotspot));
        list.add(new MobileModels(51, 37, "Block Bluetooth", currentActivity.getString(R.string.block_bt), R.drawable.ic_bluetooth));

        list.add(new MobileModels(52, 38, "Screen Magnifier", currentActivity.getString(R.string.screen_magnifier), R.drawable.ic_magnifier));
        list.add(new MobileModels(53, 38, "Take Screenshot", currentActivity.getString(R.string.take_screenshot), R.drawable.ic_screenshot));
        list.add(new MobileModels(54, 38, "Record Screen", currentActivity.getString(R.string.record_screen), R.drawable.ic_record));
        list.add(new MobileModels(55, 38, "Cast Screen", currentActivity.getString(R.string.cast_screen), R.drawable.ic_cast));

        list.add(new MobileModels(56, 39, "Control your Laptop", currentActivity.getString(R.string.control_laptop), R.drawable.ic_laptop));
        list.add(new MobileModels(57, 39, "Control your Android", currentActivity.getString(R.string.control_android), R.drawable.ic_android));

        list.add(new MobileModels(58, 40, "Improve Gaming Performance", currentActivity.getString(R.string.gaming_performance), R.drawable.ic_game_control));

        list.add(new MobileModels(59, 41, "Share internet without Hotspot", currentActivity.getString(R.string.share_internet), R.drawable.ic_share_net));

        return list;
    }

}

/*0*/
/*1*/
/*2*/
/*3*/
/*4*/
/*5*/
/*6*/
/*7*/
/*8*/
/*9*/