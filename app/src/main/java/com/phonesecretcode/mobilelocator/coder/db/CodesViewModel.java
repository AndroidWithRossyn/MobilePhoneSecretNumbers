package com.phonesecretcode.mobilelocator.coder.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.phonesecretcode.mobilelocator.coder.models.CodesModel;
import com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication;

import java.util.List;

public class CodesViewModel extends AndroidViewModel {

    CodesDao codesDao;

    public CodesViewModel(@NonNull Application application) {
        super(application);
        codesDao = MyApplication.getCodesDao();
    }

    public LiveData<List<CodesModel>> getAllData(String brand){
        return codesDao.getAllCodes(brand.toUpperCase());
    }

    public LiveData<List<CodesModel>> getFavouritesData(){
        return codesDao.getFavouriteCodes();
    }

}
