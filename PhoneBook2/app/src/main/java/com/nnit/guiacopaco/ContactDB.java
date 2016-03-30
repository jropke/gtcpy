package com.nnit.guiacopaco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// ContactDb class that encapsulates the database functions of this application
public class ContactDB
{

    private ContactDB manager;
    // define constants
    public static final String KEY_ROWID = "ContactID";
    public static final String KEY_Nombre= "Nombre";
    public static final String KEY_Dep = "Dep";
    public static final String KEY_Ciudad = "Ciudad";
    public static final String KEY_Telefono = "Telefono";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "PhonebookDB";
    private static final String DATABASE_TABLE = "Contacts";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table Contacts (ContactID integer primary key, Nombre text not null, Dep text not null, Ciudad text not null, Telefono text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ContactDB(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creates the db if it does not exist
        Context mContext;
        @Override

        public void onCreate(SQLiteDatabase db)
        {

            db.execSQL(DATABASE_CREATE);



            // Ejecutar fichero precarga de datos
      //      InputStream is = null;
        //    try {
       //         is = this.mContext.getAssets().open("");
       //         if (is != null) {
        //            db.beginTransaction();
       //             BufferedReader reader = new BufferedReader(
        //                    new InputStreamReader(is));
        //            String line = reader.readLine();
        //            while (!TextUtils.isEmpty(line)) {
        //                db.execSQL(line);
        //                line = reader.readLine();
         //           }
         //           db.setTransactionSuccessful();
        //        }
        //    } catch (Exception ex) {
                // Muestra log
        //    } finally {
       //         db.endTransaction();
        //        if (is != null) {
        //            try {
        //                is.close();
        //            } catch (IOException e) {
        //                // Muestra log
        //            }
        //        }
        //    }

         //   db.execSQL(DATABASE_CREATE);
           // db.beginTransaction();
            //try {
              //  ContentValues values = new ContentValues();
               // for (int i = 0; i < KEY_FirstName.length(); i++) {
                 //   values.put("FirstName", KEY_FirstName);
                   // values.put("LastName", KEY_LastName);
                 //   values.put("emailID", KEY_emailID);
                 //   values.put("Mobile01", KEY_Mobile01);
                  //  db.insert("Contacts", null, values);
               // }
               // db.setTransactionSuccessful();
            //} finally {
             //   db.endTransaction();
           // }

        }

        // called when the db needs upgrading
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }

    }

  //open db
    public ContactDB open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close db
    public void close() 
    {
        DBHelper.close();
    }

    //add a record
    public long insertContacts(int ContactID, String Nombre, String Dep, String Ciudad, String Telefono)

    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, ContactID);
        initialValues.put(KEY_Nombre, Nombre);
        initialValues.put(KEY_Dep, Dep);
        initialValues.put(KEY_Ciudad, Ciudad);
        initialValues.put(KEY_Telefono, Telefono);
        return db.insert(DATABASE_TABLE, null, initialValues);



    }


  //delete a record
    public boolean deleteContacts(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //get all record
    public Cursor getAllContacts() 
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_Nombre,KEY_Dep,KEY_Ciudad,KEY_Telefono}, null, null, null, null, null);
    }

    //get a record
    public Cursor getContacts(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_Nombre,KEY_Dep, KEY_Ciudad,KEY_Telefono},
                        KEY_ROWID + "=" + rowId, 
                        null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //record update
    public boolean updateContacts(long rowId, String FirstName, String LastName, String emailID, String Mobile01) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_Nombre, FirstName);
        args.put(KEY_Dep, LastName);
        args.put(KEY_Ciudad, emailID);
        args.put(KEY_Telefono, Mobile01);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }

}
