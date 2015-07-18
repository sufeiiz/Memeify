package nyc.c4q.scar.memer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufeizhao on 7/17/15.
 */
public class mySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB = "TemplateDB";
    public static final int VERSION = 1;
    private static mySQLiteOpenHelper INSTANCE;

    public static synchronized mySQLiteOpenHelper getInstance(Context context) {
        if(INSTANCE == null)
            INSTANCE = new mySQLiteOpenHelper(context.getApplicationContext());

        return INSTANCE;
    }

    public mySQLiteOpenHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insertData(List<String> urls) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ImageEntry.TABLE_NAME, null, null);

        for (String url : urls) {
            insertRow(url);
        }
    }

    private void insertRow(String url) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ImageEntry.COLUMN_NAME_URL, url);

        db.insertOrThrow(ImageEntry.TABLE_NAME, null, values);
    }

    public List<String> loadData() {
        String[] projection = {
                ImageEntry._ID,
                ImageEntry.COLUMN_NAME_URL};

        SQLiteDatabase db = getWritableDatabase();
        List<String> urls = new ArrayList<>();

        Cursor cursor = db.query(ImageEntry.TABLE_NAME,
                projection, "_ID < ?", new String[] {"20"}, null, null, ImageEntry._ID);
        while(cursor.moveToNext()) {
            urls.add(cursor.getString(cursor.getColumnIndex(ImageEntry.COLUMN_NAME_URL)));
        }

        cursor.close();
        return urls;
    }

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME      = "template";
        public static final String COLUMN_NAME_URL = "image_name";
    }

    //Will be: CREATE TABLE person (_id INTEGER PRIMARY KEY,name TEXT,age INTEGER,favorite_color TEXT )
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ImageEntry.TABLE_NAME + " (" +
            ImageEntry._ID + " INTEGER PRIMARY KEY," +
            ImageEntry.COLUMN_NAME_URL + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ImageEntry.TABLE_NAME;
}
