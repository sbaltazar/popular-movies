package com.sbaltazar.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sbaltazar.popularmovies.data.dao.MovieDao;
import com.sbaltazar.popularmovies.data.entity.Movie;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Date;

@TypeConverters({MovieDatabase.Converters.class})
@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "movie_db")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public static class Converters {

        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }

        @TypeConverter
        public static Bitmap fromByteArray(byte[] data) {

            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            // WARNING: Get the size of image from config or save the size on DB
            Bitmap image = Bitmap.createBitmap(185, 278, Bitmap.Config.ARGB_8888);
            image.copyPixelsFromBuffer(byteBuffer);

            return image;
        }

        @TypeConverter
        public static byte[] bitmapToByteArray(Bitmap image) {

            int size = image.getRowBytes() * image.getHeight();

            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            image.copyPixelsToBuffer(byteBuffer);

            return byteBuffer.array();
        }
    }
}
