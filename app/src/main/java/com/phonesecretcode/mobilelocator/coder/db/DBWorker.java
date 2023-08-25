package com.phonesecretcode.mobilelocator.coder.db;

import static com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication.getCodesDao;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.models.CodesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

public class DBWorker extends Worker {

    public DBWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            Resources resources = getApplicationContext().getResources();
            InputStream inputStream = resources.openRawResource(R.raw.mobilecodes);
            Scanner scanner = new Scanner(inputStream);

            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                CodesModel model = new CodesModel(
                        object.getInt("ID"),
                        object.getString("code"),
                        object.getString("codeDesc"),
                        object.getString("brand"));
                getCodesDao().insertData(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("doWork: ", "work done");

        return Result.success();
    }
}
