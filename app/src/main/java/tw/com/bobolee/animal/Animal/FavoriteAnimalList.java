package tw.com.bobolee.animal.Animal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.GlobalClass.GlobalVariable;
import tw.com.bobolee.animal.Model.Animal;

public class FavoriteAnimalList extends AnimalListFragmentSample {
    private static final int LIMIT = 20;

    @Override
    public void getAnimalList(int Page) {
        ArrayList<Animal> list = new ArrayList<>();
        SQLiteDatabase db = GlobalVariable.getDB();
        String DataSqlStatement;
        DataSqlStatement = "SELECT " + MySQLite.FavoriteAnimalIdColumn + ","
                                        + MySQLite.FavoriteAreaNameColumn + ","
                                        + MySQLite.FavoriteSexNameColumn + ","
                                        + MySQLite.FavoriteAlbumFileColumn
                            +" FROM " + MySQLite.FavoriteTable
                            +" ORDER BY " + MySQLite.FavoriteIdColumn;

        Cursor cursor = db.rawQuery(DataSqlStatement
                +" LIMIT " + LIMIT
                +" OFFSET " + LIMIT * Page, null);

        String AnimalId;
        String AnimalAreaName;
        String AnimalAlbumFile;
        String AnimalSexName;
        while (cursor.moveToNext()) {
            AnimalId = cursor.getString(0);
            AnimalAreaName = cursor.getString(1);
            AnimalSexName = cursor.getString(2);
            AnimalAlbumFile = cursor.getString(3);
            try {
                list.add(new Animal(AnimalId,AnimalAreaName,AnimalSexName,AnimalAlbumFile));
            } catch (Throwable t) {
                Log.e("My App", "");
            }
        }
        cursor.close(); //關閉Cursor
        RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), list);
    }
}
