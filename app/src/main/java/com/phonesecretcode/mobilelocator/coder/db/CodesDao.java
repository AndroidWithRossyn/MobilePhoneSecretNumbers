package com.phonesecretcode.mobilelocator.coder.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.phonesecretcode.mobilelocator.coder.models.CodesModel;

import java.util.List;

@Dao
public interface CodesDao {

    @Insert(entity = CodesModel.class, onConflict = OnConflictStrategy.REPLACE)
    void insertData(CodesModel model);

    @Update
    void updateData(CodesModel model);

    @Query("SELECT * FROM codes WHERE UPPER(brand) = :brand GROUP BY ID ORDER BY ID ASC")
    LiveData<List<CodesModel>> getAllCodes(String brand);

    @Query("SELECT * FROM codes WHERE isFavourite = 1 GROUP BY ID ORDER BY ID ASC")
    LiveData<List<CodesModel>> getFavouriteCodes();

}
