package tw.com.bobolee.animal.GlobalClass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tw.com.bobolee.animal.Database.MySQLite;

/**
 * Created by bohuilee on 2016/10/4.
 */

public class GlobalVariable {
    private static SQLiteDatabase mDB;
    private static ArrayList<String> AreaList = new ArrayList<>();
    private static ArrayList<String> AnimalColourList = new ArrayList<>();
    private static ArrayList<String> AnimalKindList = new ArrayList<>();
    private static ArrayList<String> AnimalSexList = new ArrayList<>();
    private static ArrayList<String> AnimalBodyTypeList = new ArrayList<>();
    private static ArrayList<String> AnimalSterilizationList = new ArrayList<>();
    private static ArrayList<String> AnimalBacterinList = new ArrayList<>();

    public static void initialize(){
        mDB = null;
        AreaList = new ArrayList<>();
        AnimalColourList = new ArrayList<>();
        AnimalKindList = new ArrayList<>();
        AnimalSexList = new ArrayList<>();
        AnimalBodyTypeList = new ArrayList<>();
        AnimalSterilizationList = new ArrayList<>();
        AnimalBacterinList = new ArrayList<>();
    }

    public void setDB(Context context){
        MySQLite SQLite;
        SQLite = MySQLite.getInstance(context);
        mDB = SQLite.getWritableDatabase();
    }

    public static void setDB(SQLiteDatabase db){
        mDB = db;
    }

    public static SQLiteDatabase getDB(){return mDB;}

    public static void addAreaList(String Element) {
        AreaList.add(Element);
    }

    public static void addAnimalColourList(String Element) {
        AnimalColourList.add(Element);
    }

    public static void addAnimalKindList(String Element) {
        AnimalKindList.add(Element);
    }

    public static void addAnimalSexList(String Element) {
        AnimalSexList.add(Element);
    }

    public static void addAnimalBodyTypeList(String Element) {
        AnimalBodyTypeList.add(Element);
    }

    public static void addAnimalSterilizationList(String Element) {
        AnimalSterilizationList.add(Element);
    }

    public static void addAnimalBacterinList(String Element) {
        AnimalBacterinList.add(Element);
    }

    public static ArrayList<String> getAreaList() {
        return AreaList;
    }

    public static ArrayList<String> getAnimalColourList() {
        return AnimalColourList;
    }

    public static ArrayList<String> getAnimalKindList() {
        return AnimalKindList;
    }

    public static ArrayList<String> getAnimalSexList() {
        return AnimalSexList;
    }

    public static ArrayList<String> getAnimalBodyTypeList() {
        return AnimalBodyTypeList;
    }

    public static ArrayList<String> getAnimalSterilizationList() {
        return AnimalSterilizationList;
    }

    public static ArrayList<String> getAnimalBacterinList() {
        return AnimalBacterinList;
    }
}
