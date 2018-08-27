package tw.com.bobolee.animal.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.util.HashMap;
import java.util.Map;

public class MySQLite extends SQLiteOpenHelper {
    //資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
    private static final int VERSION = 3;  //資料庫版本
    private static MySQLite sInstance;

    public static final String AnimalTable = "animal";
    public  static final String AnimalAreaNameColumn = "animal_area_name";
    public static final String AnimalKindColumn = "animal_kind";
    public static final String AnimalSexNameColumn = "animal_sex_name";
    public static final String AnimalBodyTypeNameColumn = "animal_bodytype_name";
    public static final String AnimalColourColumn = "animal_colour";
    public  static final String AnimalSterilizationNameColumn = "animal_sterilization_name";
    public  static final String AnimalBacterinNameColumn = "animal_bacterin_name";

    //我的最愛
    public  static final String FavoriteTable = "favorite";
    public  static final String FavoriteTempTable = "favorite_temp";
    public static final String FavoriteIdColumn = "favorite_id";
    public static final String FavoriteAnimalIdColumn = "favorite_animal_id";
    public static final String FavoriteAreaNameColumn = "favorite_area_name";
    public static final String FavoriteSexNameColumn = "favorite_sex_name";
    public static final String FavoriteAlbumFileColumn = "favorite_album_file";

    //搜尋條件記錄
    public static final String WhereConditionTable = "where_condition";
    public static final String WhereConditionIdColumn = "id";
    public static final String AreaWhereConditionColumn = "area";                     //地區
    public static final String AnimalKindWhereConditionColumn = "kind";               //種類
    public static final String AnimalColourWhereConditionColumn = "colour";           //毛色
    public static final String AnimalSexWhereConditionColumn = "sex";                 //性別
    public  static final String AnimalBodyTypeWhereConditionColumn = "body_type";     //體型
    public static final String AnimalSterilizationWhereConditionColumn = "sterilization";//結紮
    public  static final String AnimalBacterinWhereConditionColumn = "bacterin";        //狂犬疫苗

    //=========此區table為舊版本code,為了在舊換新的時候移除table,而保留table name======
    //Version 儲存上一次下載的全JsonData,供資料版本比對使用
    static final String VersionTable = "version";
    //Area 縣市基本資料
    static final String AreaTable = "area";
    //Shelter 收容所基本資料
    static final String ShelterTable = "shelter";
    //Sex 性別基本資料
    private static final String SexTable = "sex";
    //體型基本資料
    private static final String BodyTypeTable = "body_type";
    //年齡基本資料
    private static final String AgeTable = "age";
    //是、否，基本資料
    private static final String CheckboxTypeTable = "checkbox_type";
    //動物狀態基本資料
    private static final String StatusTable = "animal_state";
    //===================================================================================

    private static Map<String, String> TableHashMap = new HashMap<>();
    private static Map<String, String> ReserveTableHashMap = new HashMap<>();

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLite(context.getApplicationContext(), "inviteMoreDB");
        }
        return sInstance;
    }

    //建構子
    private MySQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        prepareDefaultData();
    }

    private MySQLite(Context context, String name) {
        this(context, name, null, VERSION);
        prepareDefaultData();
    }

    private void setCreateTableString() {
        String DATABASE_CREATE_TABLE="";
        TableHashMap.put(AnimalTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(VersionTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(AreaTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(ShelterTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(SexTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(BodyTypeTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(AgeTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(CheckboxTypeTable, DATABASE_CREATE_TABLE);
        TableHashMap.put(StatusTable, DATABASE_CREATE_TABLE);

        DATABASE_CREATE_TABLE =
                "CREATE TABLE " + FavoriteTable + "("
                        + FavoriteIdColumn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + FavoriteAnimalIdColumn + " TEXT,"
                        + FavoriteAreaNameColumn + " TEXT,"
                        + FavoriteSexNameColumn + " TEXT,"
                        + FavoriteAlbumFileColumn + " TEXT)";
        TableHashMap.put(FavoriteTable, DATABASE_CREATE_TABLE);

        DATABASE_CREATE_TABLE =
                "CREATE TABLE " + WhereConditionTable + "("
                        + WhereConditionIdColumn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + AreaWhereConditionColumn + " TEXT,"
                        + AnimalKindWhereConditionColumn + " TEXT,"
                        + AnimalColourWhereConditionColumn + " TEXT,"
                        + AnimalSexWhereConditionColumn + " TEXT,"
                        + AnimalBodyTypeWhereConditionColumn + " TEXT,"
                        + AnimalSterilizationWhereConditionColumn + " TEXT,"
                        + AnimalBacterinWhereConditionColumn + " TEXT)";
        TableHashMap.put(WhereConditionTable, DATABASE_CREATE_TABLE);

        DATABASE_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + FavoriteTempTable + "("
                        + FavoriteIdColumn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + FavoriteAnimalIdColumn + " TEXT,"
                        + FavoriteAreaNameColumn + " TEXT,"
                        + FavoriteSexNameColumn + " TEXT,"
                        + FavoriteAlbumFileColumn + " TEXT)";
        ReserveTableHashMap.put(FavoriteTempTable, DATABASE_CREATE_TABLE);
    }

    //安裝APP時執行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Map.Entry<String, String> entry : TableHashMap.entrySet()) {
            String value = entry.getValue();
            if (!value.equals("")) {
                db.execSQL(value);
            }
        }

        for (Map.Entry<String, String> entry : ReserveTableHashMap.entrySet()) {
            String value = entry.getValue();
            db.execSQL(value);
        }
    }

    //資料庫版本更新時執行一次
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //備份table清空
        for (Map.Entry<String, String> entry : ReserveTableHashMap.entrySet()) {
            String key = entry.getKey();
            db.execSQL("DELETE FROM " + key);
        }

        //我的最愛資料備份至temp
        db.execSQL("INSERT INTO "+FavoriteTempTable+"("+FavoriteAnimalIdColumn+") SELECT "+FavoriteAnimalIdColumn+" FROM "+FavoriteTable+" ORDER BY "+FavoriteIdColumn);

        //刪除所有table
        for (Map.Entry<String, String> entry : TableHashMap.entrySet()) {
            String key = entry.getKey();
            dropTable(db, key);
        }

        //建立所有table
        for (Map.Entry<String, String> entry : TableHashMap.entrySet()) {
            String value = entry.getValue();
            if (!value.equals("")) {
                db.execSQL(value);
            }
        }

        //從temp搬資料回到我的最愛
        db.execSQL("INSERT INTO "+FavoriteTable+"("+FavoriteAnimalIdColumn+") SELECT "+FavoriteAnimalIdColumn+" FROM "+FavoriteTempTable+" ORDER BY "+FavoriteIdColumn);
    }

    //準備需要使用到的Table Create 字串
    private void prepareDefaultData() {
        setCreateTableString();
    }

    private void dropTable(SQLiteDatabase db, String TableName) {
        if (TableHashMap.containsKey(TableName)) {
            //刪除舊有的資料表
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}