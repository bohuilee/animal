package tw.com.bobolee.animal.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tw.com.bobolee.animal.GlobalClass.GlobalVariable;
import tw.com.bobolee.animal.Database.MySQLite;

/**
 * Created by bohuilee on 2015/12/18.
 */
public class Animal {
    private transient  SQLiteDatabase mdb;
    private String AnimalId;
    private String AnimalSubId;
    private String AnimalAreaPkId;
    private String AnimalAreaName;
    private String AnimalShelterPkId;
    private String AnimalPlace;
    private String AnimalKind;
    private String AnimalSex;
    private String AnimalSexName;
    private String AnimalBodyType;
    private String AnimalBodyTypeName;
    private String AnimalColour;
    private String AnimalAge;
    private String AnimalAgeName;
    private String AnimalSterilization;
    private String AnimalSterilizationName;
    private String AnimalBacterin;
    private String AnimalBacterinName;
    private String AnimalFoundPlace;
    private String AnimalStatus;
    private String AnimalStatusName;
    private String AnimalRemark;
    private String AnimalOpenDate;
    private String AnimalClosedDate;
    private String AnimalUpdate;
    private String AnimalCreateTime;
    private String AnimalShelterName;
    private String AnimalAlbumFile;

    public Animal() {
        mdb = GlobalVariable.getDB();
    }

    public Animal(String animalId,String animalAreaName,String animalSexName,String animalAlbumFile) {
        mdb = GlobalVariable.getDB();
        AnimalId = animalId;
        AnimalAreaName = animalAreaName;
        AnimalSexName = animalSexName;
        AnimalAlbumFile = animalAlbumFile;
    }

    public void setAnimalId(String animalId) {
        AnimalId = animalId;
    }

    public void setAnimalSubId(String animalSubId) {
        AnimalSubId = animalSubId;
    }

    public void setAnimalAreaPkId(String animalAreaPkId) {
        AnimalAreaPkId = animalAreaPkId;
    }

    public void setAnimalAreaName(String animalAreaName) {
        AnimalAreaName = animalAreaName;
    }

    public void setAnimalShelterPkId(String animalShelterPkId) {
        AnimalShelterPkId = animalShelterPkId;
    }

    public void setAnimalPlace(String animalPlace) {
        AnimalPlace = animalPlace;
    }

    public void setAnimalKind(String animalKind) {
        AnimalKind = animalKind;
    }

    public void setAnimalSex(String animalSex) {
        AnimalSex = animalSex;
    }

    public void setAnimalSexName(String animalSexName) {
        AnimalSexName = animalSexName;
    }

    public void setAnimalBodyType(String animalBodyType) {
        AnimalBodyType = animalBodyType;
    }

    public void setAnimalBodyTypeName(String animalBodyTypeName) {
        AnimalBodyTypeName = animalBodyTypeName;
    }

    public void setAnimalColour(String animalColour) {
        AnimalColour = animalColour;
    }

    public void setAnimalAge(String animalAge) {
        AnimalAge = animalAge;
    }

    public void setAnimalAgeName(String animalAgeName) {
        AnimalAgeName = animalAgeName;
    }

    public void setAnimalSterilization(String animalSterilization) {
        AnimalSterilization = animalSterilization;
    }

    public void setAnimalSterilizationName(String animalSterilizationName) {
        AnimalSterilizationName = animalSterilizationName;
    }

    public void setAnimalBacterin(String animalBacterin) {
        AnimalBacterin = animalBacterin;
    }

    public void setAnimalBacterinName(String animalBacterinName) {
        AnimalBacterinName = animalBacterinName;
    }

    public void setAnimalFoundPlace(String animalFoundPlace) {
        AnimalFoundPlace = animalFoundPlace;
    }

    public void setAnimalStatus(String animalStatus) {
        AnimalStatus = animalStatus;
    }

    public void setAnimalStatusName(String animalStatusName) {
        AnimalStatusName = animalStatusName;
    }

    public void setAnimalRemark(String animalRemark) {
        AnimalRemark = animalRemark;
    }

    public void setAnimalOpenDate(String animalOpenDate) {
        AnimalOpenDate = animalOpenDate;
    }

    public void setAnimalClosedDate(String animalClosedDate) {
        AnimalClosedDate = animalClosedDate;
    }

    public void setAnimalUpdate(String animalUpdate) {
        AnimalUpdate = animalUpdate;
    }

    public void setAnimalCreateTime(String animalCreateTime) {
        AnimalCreateTime = animalCreateTime;
    }

    public void setAnimalShelterName(String animalShelterName) {
        AnimalShelterName = animalShelterName;
    }

    public void setAnimalAlbumFile(String animalAlbumFile) {
        AnimalAlbumFile = animalAlbumFile;
    }

    public SQLiteDatabase getMdb() {

        return mdb;
    }

    public String getAnimalId() {
        return AnimalId;
    }

    public String getAnimalSubId() {
        return AnimalSubId;
    }

    public String getAnimalAreaPkId() {
        return AnimalAreaPkId;
    }

    public String getAnimalAreaName() {
        return AnimalAreaName;
    }

    public String getAnimalShelterPkId() {
        return AnimalShelterPkId;
    }

    public String getAnimalPlace() {
        return AnimalPlace;
    }

    public String getAnimalKind() {
        return AnimalKind;
    }

    public String getAnimalSex() {
        return AnimalSex;
    }

    public String getAnimalSexName() {
        return AnimalSexName;
    }

    public String getAnimalBodyType() {
        return AnimalBodyType;
    }

    public String getAnimalBodyTypeName() {
        return AnimalBodyTypeName;
    }

    public String getAnimalColour() {
        return AnimalColour;
    }

    public String getAnimalAge() {
        return AnimalAge;
    }

    public String getAnimalAgeName() {
        return AnimalAgeName;
    }

    public String getAnimalSterilization() {
        return AnimalSterilization;
    }

    public String getAnimalSterilizationName() {
        return AnimalSterilizationName;
    }

    public String getAnimalBacterin() {
        return AnimalBacterin;
    }

    public String getAnimalBacterinName() {
        return AnimalBacterinName;
    }

    public String getAnimalFoundPlace() {
        return AnimalFoundPlace;
    }

    public String getAnimalStatus() {
        return AnimalStatus;
    }

    public String getAnimalStatusName() {
        return AnimalStatusName;
    }

    public String getAnimalRemark() {
        return AnimalRemark;
    }

    public String getAnimalOpenDate() {
        return AnimalOpenDate;
    }

    public String getAnimalClosedDate() {
        return AnimalClosedDate;
    }

    public String getAnimalUpdate() {
        return AnimalUpdate;
    }

    public String getAnimalCreateTime() {
        return AnimalCreateTime;
    }

    public String getAnimalShelterName() {
        return AnimalShelterName;
    }

    public String getAnimalAlbumFile() {
        return AnimalAlbumFile;
    }

    public boolean isFavorite(){
        Cursor cursor;
        cursor = mdb.rawQuery("SELECT " + MySQLite.FavoriteAnimalIdColumn
                + " FROM " + MySQLite.FavoriteTable
                + " WHERE " + MySQLite.FavoriteAnimalIdColumn + " = '" +AnimalId+ "'",null);
        int RowCnt = cursor.getCount();
        cursor.close();
        return (RowCnt > 0);
    }

    public void removeFavorite(){
        mdb.delete(MySQLite.FavoriteTable, MySQLite.FavoriteAnimalIdColumn + "='" + AnimalId + "'", null);
    }

    public void addFavorite(){
        ContentValues values = new ContentValues();
        values.put(MySQLite.FavoriteAnimalIdColumn,AnimalId);
        values.put(MySQLite.FavoriteAreaNameColumn,AnimalAreaName);
        values.put(MySQLite.FavoriteAlbumFileColumn,AnimalAlbumFile);
        mdb.insert(MySQLite.FavoriteTable,null,values);
    }

}
