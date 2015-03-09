package com.jonasSoftware.blueharvest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.String;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

//import java.util.HashMap;

/**
 * @author Brad Bass
 * @version 1
 */

public class DatabaseHandler extends SQLiteOpenHelper {
		
    public static List<String> _dataTables;
    // Database Version
    private static final int DATABASE_VERSION = 19;
 
    // Database Name
    private static final String DATABASE_NAME = "jonasScanner";
 
    // Labels table name
    private static final String TABLE_LABELS = "labels";
    private static final String TABLE_CHRG_DATA = "chrgData";
    private static final String TABLE_UPLOAD_DATA = "uploadData";
    private static final String TABLE_TRANSFER_DATA = "transferData";
    private static final String TABLE_RECEIVE_DATA = "receiveData";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_WHSE = "warehouse";
    private static final String TABLE_ITEM = "cost_item";
    private static final String TABLE_TYPE = "cost_type";
    private static final String TABLE_DEFAULTS = "defaults";
    private static final String TABLE_REPORT_DATA = "reportData";
    // --Commented out by Inspection (5/15/13 12:04 PM):public static String _tableName = TABLE_CHRG_DATA;
    private static String selectQuery;
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    //private static final String COLUMN_ID = "id";
    //private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INDEX = "record_index";
    private static final String COLUMN_TABLENAME = "table_name";
    private static final String COLUMN_RECORD = "record";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_UPC = "upc";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_KEY = "id";
    private static final String COLUMN_ACT_NAME = "account_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FROM = "from_email";
    private static final String COLUMN_TO = "to_email";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_WHSE = "warehouse";
    private static final String COLUMN_ITEM = "cost_item";
    private static final String COLUMN_TYPE = "cost_type";
    private static final String COLUMN_JOB_WO_NUM = "job_wo_num";
    private static final String COLUMN_SERIAL = "serial";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_FROM_WHSE = "from_whse";
    private static final String COLUMN_TO_WHSE = "to_whse";
    private static final String COLUMN_PO_NUM = "po_num";
    private static final String COLUMN_HOST = "email_host";
    private static final String COLUMN_PORT = "email_port";
    //
    static int _recordNum;
    static String _currentUpc;
    //static Boolean _existingRec = false;
    static Cursor _curCSV;
    //final SQLiteDatabase _dbr = this.getReadableDatabase();
    //final SQLiteDatabase _dbw = this.getWritableDatabase();
    // Login table name
    private static final String TABLE_LOGIN = "login";
    // Login Table Columns names
    //private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private String keyId;
    
    // Table create strings
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT)";
    
    private static final String CREATE_CHRGDATA_TABLE = "CREATE TABLE " + TABLE_CHRG_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_JOB_WO_NUM + " TEXT,"
            + COLUMN_ITEM + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_QUANTITY + " TEXT,"
            + COLUMN_SERIAL + " TEXT,"
            + COLUMN_COMMENT + " TEXT,"
            + COLUMN_DATE + " TEXT)";

    private static final String CREATE_UPLOADDATA_TABLE = "CREATE TABLE " + TABLE_UPLOAD_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_QUANTITY + " TEXT)";

    private static final String CREATE_TRANSFER_TABLE = "CREATE TABLE " + TABLE_TRANSFER_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_FROM_WHSE + " TEXT,"
            + COLUMN_TO_WHSE + " TEXT,"
            + COLUMN_QUANTITY + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_SERIAL + " TEXT)";

    private static final String CREATE_RECEIVE_TABLE = "CREATE TABLE " + TABLE_RECEIVE_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_PO_NUM + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_QUANTITY + " TEXT,"
            + COLUMN_SERIAL + " TEXT,"
            + COLUMN_COMMENT + " TEXT,"
            + COLUMN_DATE + " TEXT)";
    
    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
    		+ COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_ACT_NAME + " TEXT,"
    		+ COLUMN_PASSWORD + " TEXT,"
            + COLUMN_FROM + " TEXT,"
            + COLUMN_TO + " TEXT,"
    		+ COLUMN_SUBJECT + " TEXT,"
            + COLUMN_BODY + " TEXT,"
            + COLUMN_HOST + " TEXT,"
            + COLUMN_PORT + " TEXT)";

    private static final String CREATE_WHSE_TABLE = "CREATE TABLE " + TABLE_WHSE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT)";

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_ITEM + " TEXT)";

    private static final String CREATE_TYPE_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TYPE + " TEXT)";

    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_USERNAME + " TEXT UNIQUE,"
            + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";

    private static final String CREATE_DEFAULTS_TABLE = "CREATE TABLE " + TABLE_DEFAULTS + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_JOB_WO_NUM + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_ITEM + " TEXT)";

    private static final String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORT_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_INDEX + " TEXT,"
            + COLUMN_TABLENAME + " TEXT,"
            + COLUMN_RECORD + " TEXT)";

    private int returnVal;

    /**
     * 
     * @param context   context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /**
     * call execSQL to create the databases.
     * 
     * @param db 	SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables    	
        db.execSQL(CREATE_CATEGORIES_TABLE);        
        db.execSQL(CREATE_CHRGDATA_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_WHSE_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_TYPE_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_UPLOADDATA_TABLE);
        db.execSQL(CREATE_TRANSFER_TABLE);
        db.execSQL(CREATE_RECEIVE_TABLE);
        db.execSQL(CREATE_DEFAULTS_TABLE);
        db.execSQL(CREATE_REPORTS_TABLE);
        setDefaultLabel(db);
        //insertBlankRow();
    }
    
    /**
     * @param db    database
     * 
     */
    void setDefaultLabel(SQLiteDatabase db) {
    	// creates default label
    	//SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, "WHSE");
        db.insert(TABLE_LABELS, null, values);
        values.put(KEY_NAME, "TYPE");
        db.insert(TABLE_LABELS, null, values);
        values.put(KEY_NAME, "ITEM");
    	db.insert(TABLE_LABELS, null, values);
        //*
        ContentValues valuesWhse = new ContentValues();
        valuesWhse.put(COLUMN_WHSE, "");
        db.insert(TABLE_WHSE, null, valuesWhse);
        ContentValues valuesType = new ContentValues();
        valuesType.put(COLUMN_TYPE, "");
        db.insert(TABLE_TYPE, null, valuesType);
        ContentValues valuesItem = new ContentValues();
        valuesItem.put(COLUMN_ITEM, "");
        db.insert(TABLE_ITEM, null, valuesItem);
        //*/
    }
    
    /**
     * if version of database changes we need to update.
     * 
     * @param db			SQLite database
     * @param oldVersion	old version of database
     * @param newVersion	new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHRG_DATA);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHSE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPLOAD_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSFER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIVE_DATA);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFAULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_DATA);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Insert new label into labels table.
     * 
     * @param label     label
     */
    public void insertLabel(int table, String label){
        //
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, label);

        switch (table) {
            case 1:
                values.put(COLUMN_WHSE, label);
                assert db != null;
                db.insert(TABLE_WHSE, null, values);
                break;
            case 2:
                values.put(COLUMN_ITEM, label);
                assert db != null;
                db.insert(TABLE_ITEM, null, values);
                break;
            case 3:
                values.put(COLUMN_TYPE, label);
                assert db != null;
                db.insert(TABLE_TYPE, null, values);
        }

        if (db != null) {
            db.close(); // Closing database connection
        }
    }

    /*/
    private void insertBlankRow() {
        //insert a blank row into WHSE, ITEM and TYPE tables
        for (int i=1;i<4;i++) {
            insertLabel(i," ");
        }
    }//*/

    public List<String> checkTables()
    {
        String[] tables = {TABLE_CHRG_DATA, TABLE_RECEIVE_DATA, TABLE_TRANSFER_DATA, TABLE_UPLOAD_DATA};
        _dataTables = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        for (String table : tables) {
            //SQLiteDatabase db = this.getWritableDatabase();
            String sqlCmd = "SELECT COUNT(*) FROM " + table;
            assert db != null;
            Cursor cursor = db.rawQuery(sqlCmd, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                // do something
                _dataTables.add(table);
            }
        }
        if (db != null) {
            db.close();
        }
        return _dataTables;
    }

    /**
     * SettingsActivity
     *
     * @param _actName      email account name
     * @param _password     email account password
     * @param _from         email from address
     * @param _to           email list of to addresses [array]
     * @param _subject      email subject
     * @param _body         email body
     */
    public void saveToDb(String _actName, String _password, String _from, String _to,
                         String _subject, String _body, String _host, String _port){
        //
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACT_NAME, _actName);
        values.put(COLUMN_PASSWORD, _password);
        values.put(COLUMN_FROM, _from);
        values.put(COLUMN_TO, _to);
        values.put(COLUMN_SUBJECT, _subject);
        values.put(COLUMN_BODY, _body);
        values.put(COLUMN_HOST, _host);
        values.put(COLUMN_PORT, _port);

        // Insert row
        assert db != null;
        db.insert(TABLE_SETTINGS, null, values);

        db.close();
    }

    /**
     * ChargeActivity
     *
     * @param _whse     warehouse part is coming from
     * @param _wo       job or wo number
     * @param _costItem optional cost item
     * @param _costType Cost Type of part
     * @param _upc      the upc code either scanned in or manually entered
     * @param _quantity this is the quantity of the part
     * @param _serial   optional serial number
     * @param _comment  optional comment
     * @param _date     date
     */
    public void saveToDb(String _whse, String _wo, String _costItem,
                         String _costType, String _upc, String _quantity, String _serial,
                         String _comment, String _date){
    	// save fields to db with new fields
    	SQLiteDatabase db = this.getWritableDatabase();

        getKey(TABLE_CHRG_DATA);

        ContentValues values = new ContentValues();
    	values.put(COLUMN_WHSE, _whse);
        values.put(COLUMN_JOB_WO_NUM, _wo);
        values.put(COLUMN_ITEM, _costItem);
        values.put(COLUMN_TYPE, _costType);
        values.put(COLUMN_UPC, _upc);
        values.put(COLUMN_QUANTITY, _quantity);
        values.put(COLUMN_SERIAL, _serial);
        values.put(COLUMN_COMMENT, _comment);
        values.put(COLUMN_DATE, _date);
    	
    	// Insert row
        returnVal = db.update(TABLE_CHRG_DATA, values, "id = ? AND upc = "
                + _currentUpc, new String[]{keyId});
        if (returnVal == 0) {
            Long retVal = db.insert(TABLE_CHRG_DATA, null, values);
            returnVal = Integer.parseInt(retVal.toString());
        }
        saveToDb(TABLE_CHRG_DATA, returnVal, values.toString());

        db.close();
        keyId = null;
    }

    /**
     * Upload Activity
     *
     * @param whse      warehouse we are receiving to
     * @param upc       the upc code either scanned in or manually entered
     * @param quantity  this is the quantity of the part
     */
    public void saveToDb(String whse, String upc, String quantity) {
        //do stuff
        SQLiteDatabase db = this.getWritableDatabase();

        getKey(TABLE_UPLOAD_DATA);

        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);

        returnVal = db.update(TABLE_UPLOAD_DATA, values, "id = ? AND upc = "
                + _currentUpc, new String[]{keyId});
        if (returnVal == 0) {
            Long retVal = db.insert(TABLE_UPLOAD_DATA, null, values);
            returnVal = Integer.parseInt(retVal.toString());
        }
        saveToDb(TABLE_UPLOAD_DATA, returnVal, values.toString());

        db.close();
        keyId = null;
    }

    /**
     * TransferActivity
     *
     * @param fromWhse  This is the warehouse the part is comming from
     * @param toWhse    This is the warehouse the part is being transfered to
     * @param quantity  The quantity of the part being transfered
     * @param upc       The UPC code of the part being transfered
     * @param serial    Optional serial number
     */
    public void saveToDb(String fromWhse, String toWhse, String quantity, String upc,
                         String serial) {
        //do stuff
        SQLiteDatabase db = this.getWritableDatabase();

        getKey(TABLE_TRANSFER_DATA);

        // Insert row
        ContentValues values = new ContentValues();
        values.put(COLUMN_FROM_WHSE, fromWhse);
        values.put(COLUMN_TO_WHSE, toWhse);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SERIAL, serial);

        returnVal = db.update(TABLE_TRANSFER_DATA, values, "id = ? AND upc = "
                + _currentUpc, new String[]{keyId});
        if (returnVal == 0) {
            Long retVal = db.insert(TABLE_TRANSFER_DATA, null, values);
            returnVal = Integer.parseInt(retVal.toString());
        }
        saveToDb(TABLE_TRANSFER_DATA, returnVal, values.toString());

        db.close();
        keyId = null;
    }

    /**
     * ReceivePO Activity
     *
     * @param whse      this is the warehouse the part is coming from
     * @param quantity  this is the quantity of the part
     * @param upc       the upc code either scanned in or manually entered
     * @param serial    optional serial number
     * @param date      the transaction date
     * @param comment   option comment
     * @param po        optional PO number
     */
    public void saveToDb(String whse, String quantity, String upc, String serial, String date,
                         String comment, String po) {
        //do stuff
        SQLiteDatabase db = this.getWritableDatabase();

        getKey(TABLE_RECEIVE_DATA);

        // Insert row
        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_PO_NUM, po);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SERIAL, serial);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COMMENT, comment);

        returnVal = db.update(TABLE_RECEIVE_DATA, values, "id = ? AND upc = "
                + _currentUpc, new String[]{keyId});
        if (returnVal == 0) {
            Long retVal = db.insert(TABLE_RECEIVE_DATA, null, values);
            returnVal = Integer.parseInt(retVal.toString());
        }
        saveToDb(TABLE_RECEIVE_DATA, returnVal, values.toString());

        db.close();
        keyId = null;
    }

    public void saveToDb(String whse, String jobwo, String type, String item, Context context) {
        purgeData(TABLE_DEFAULTS);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_ITEM, item);
        values.put(COLUMN_JOB_WO_NUM, jobwo);

        try {
            db.insert(TABLE_DEFAULTS, null, values);
            Toast.makeText(context, "Defaults Saved", LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void saveToDb(String tableName, Integer index, String recordData) {
        // index is the keyId - use this later for editing/deleting records from report
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INDEX, index.toString());
        values.put(COLUMN_TABLENAME, tableName);
        values.put(COLUMN_RECORD, recordData);

        returnVal = db.update(TABLE_REPORT_DATA, values, "record_index = ? AND table_name = '"
                + tableName + "'", new String[]{index.toString()});
        if (returnVal == 0) {
            db.insert(TABLE_REPORT_DATA, null, values);
        }

        db.close();
    }

    ContentValues getValuesChrg(String whse, String wo, String costItem, String costType,
                                String upc, String quantity, String serial, String comment,
                                String date) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_JOB_WO_NUM, wo);
        values.put(COLUMN_ITEM, costItem);
        values.put(COLUMN_TYPE, costType);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SERIAL, serial);
        values.put(COLUMN_COMMENT, comment);
        values.put(COLUMN_DATE, date);
        return values;
    }

    ContentValues getValuesUpload(String whse, String upc, String quantity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        return values;
    }

    ContentValues getValuesTransfer(String fromWhse, String toWhse, String upc, String quantity, String serial) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FROM_WHSE, fromWhse);
        values.put(COLUMN_TO_WHSE, toWhse);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SERIAL, serial);
        return values;
    }

    ContentValues getValuesReceive(String whse, String po, String upc, String quantity, String serial, String date, String comment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_PO_NUM, po);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_SERIAL, serial);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COMMENT, comment);
        return values;
    }

    private void getKey(String dbName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(dbName, null, null, null, null, null, null);
        int rows = (int) DatabaseUtils.queryNumEntries(db, dbName);
        try {
            _recordNum = Integer.parseInt(keyId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (_recordNum >= rows) {_recordNum = _recordNum - 1;}
        _currentUpc = null;
        try {
            cursor.moveToPosition(_recordNum);
            _currentUpc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }

    public void getTableName(Integer keyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPORT_DATA, null, null, null, null, null, null);
        cursor.moveToPosition(keyId);
        String tableName = cursor.getString(cursor.getColumnIndex(COLUMN_TABLENAME));
        ReportActivity ra = new ReportActivity();
        ra.setTableName(tableName);
    }

    /**
     * populate all the fields in the settings activity
     */
    public void populateFields() {
    	
    	//List<String> settingsFields = new ArrayList<String>();
    	SQLiteDatabase db = this.getReadableDatabase();

        assert db != null;
        Cursor cursor = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
    	
    	while (cursor.moveToNext()) {
    		String _act_name = cursor.getString(cursor.getColumnIndex(COLUMN_ACT_NAME));
    		String _password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
    		String _from = cursor.getString(cursor.getColumnIndex(COLUMN_FROM));
    		String _to = cursor.getString(cursor.getColumnIndex(COLUMN_TO));
    		String _subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT));
    		String _body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY));
            String _host = cursor.getString(cursor.getColumnIndex(COLUMN_HOST));
            String _port = cursor.getString(cursor.getColumnIndex(COLUMN_PORT));

            SettingsActivity sa = new SettingsActivity();
    		
    		sa.setActName(_act_name);
    		sa.setPassword(_password);
    		sa.setFrom(_from);
    		sa.setTo(_to);
    		sa.setSubject(_subject);
    		sa.setBody(_body);
            sa.setHost(_host);
            sa.setPort(_port);
    	}
        db.close();
        cursor.close();
    }

    /**
     * populate the fields in the charge activity when using the navigation buttons
     */
    public void populateChrg(int recordNum) {

        // populate the fields using the cursor position
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;
        try {
            cursor = db.query(TABLE_CHRG_DATA, null, null, null, null, null, null);
            int rows = (int) DatabaseUtils.queryNumEntries(db, "chrgData");
            if (recordNum >= rows) {recordNum = recordNum - 1;}
            cursor.moveToPosition(recordNum);

            String _upc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
            String _date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String _wo = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_WO_NUM));
            String _whse = cursor.getString(cursor.getColumnIndex(COLUMN_WHSE));
            String _item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
            String _type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            String _qty = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
            String _serial = cursor.getString(cursor.getColumnIndex(COLUMN_SERIAL));
            String _comment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));

            ChargeActivity ca = new ChargeActivity();

            ca.setUPC(_upc);
            ca.setDate(_date);
            ca.setWO(_wo);
            ca.setWHSE(_whse);
            ca.setItem(_item);
            ca.setType(_type);
            ca.setQty(_qty);
            ca.setSerial(_serial);
            ca.setComment(_comment);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void populateUpload(int recordNum) {
        // populate the fields using the cursor position
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;
        try {
            cursor = db.query(TABLE_UPLOAD_DATA, null, null, null, null, null, null);
            cursor.moveToPosition(recordNum);

            String _upc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
            String _whse = cursor.getString(cursor.getColumnIndex(COLUMN_WHSE));
            String _qty = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));

            UploadActivity ua = new UploadActivity();

            ua.setUPC(_upc);
            ua.setWHSE(_whse);
            ua.setQty(_qty);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void populateTrans(int recordNum) {
        // populate the fields using the cursor position
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;
        try {
            cursor = db.query(TABLE_TRANSFER_DATA, null, null, null, null, null, null);
            cursor.moveToPosition(recordNum);

            String _upc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
            String _fromWhse = cursor.getString(cursor.getColumnIndex(COLUMN_FROM_WHSE));
            String _toWhse = cursor.getString(cursor.getColumnIndex(COLUMN_TO_WHSE));
            String _qty = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
            String _serial = cursor.getString(cursor.getColumnIndex(COLUMN_SERIAL));

            TransferActivity ta = new TransferActivity();

            ta.setFromWHSE(_fromWhse);
            ta.setToWhse(_toWhse);
            ta.setUPC(_upc);
            ta.setQty(_qty);
            ta.setSerial(_serial);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void populateReceive(int recordNum) {
        // populate the fields using the cursor position
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;
        try {
            cursor = db.query(TABLE_RECEIVE_DATA, null, null, null, null, null, null);
            cursor.moveToPosition(recordNum);

            String _upc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
            String _whse = cursor.getString(cursor.getColumnIndex(COLUMN_WHSE));
            String _qty = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
            String _serial = cursor.getString(cursor.getColumnIndex(COLUMN_SERIAL));
            String _date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String _comment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));
            String _po = cursor.getString(cursor.getColumnIndex(COLUMN_PO_NUM));

            ReceivePO rp = new ReceivePO();

            rp.setWHSE(_whse);
            rp.setUPC(_upc);
            rp.setQty(_qty);
            rp.setSerial(_serial);
            rp.setDate(_date);
            rp.setComment(_comment);
            rp.setPo(_po);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void populateReport() {
        SQLiteDatabase db = this.getReadableDatabase();
        ReportActivity ra = new ReportActivity();
        ContentValues values;
        Cursor cCursor = db.query(TABLE_CHRG_DATA, null, null, null, null, null, null);
        Cursor uCursor = db.query(TABLE_UPLOAD_DATA, null, null, null, null, null, null);
        Cursor tCursor = db.query(TABLE_TRANSFER_DATA, null, null, null, null, null, null);
        Cursor rCursor = db.query(TABLE_RECEIVE_DATA, null, null, null, null, null, null);
        List<String> recordData = new ArrayList<>();

        //chargeActivity
        recordData.add("*Charge Parts Records*");
        for (int c = 0; c <= cCursor.getCount() + 1; c++) {
            try {
                cCursor.moveToPosition(c);
                String _upc = cCursor.getString(cCursor.getColumnIndex(COLUMN_UPC));
                String _date = cCursor.getString(cCursor.getColumnIndex(COLUMN_DATE));
                String _wo = cCursor.getString(cCursor.getColumnIndex(COLUMN_JOB_WO_NUM));
                String _whse = cCursor.getString(cCursor.getColumnIndex(COLUMN_WHSE));
                String _item = cCursor.getString(cCursor.getColumnIndex(COLUMN_ITEM));
                String _type = cCursor.getString(cCursor.getColumnIndex(COLUMN_TYPE));
                String _qty = cCursor.getString(cCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = cCursor.getString(cCursor.getColumnIndex(COLUMN_SERIAL));
                String _comment = cCursor.getString(cCursor.getColumnIndex(COLUMN_COMMENT));

                values = getValuesChrg(_whse, _wo, _item, _type, _upc, _qty, _serial, _comment, _date);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //uploadActivity
        recordData.add("*Upload Parts Records*");
        for (int u = 0; u <= uCursor.getCount(); u++) {
            try {
                uCursor.moveToPosition(u);
                String _upc = uCursor.getString(uCursor.getColumnIndex(COLUMN_UPC));
                String _whse = uCursor.getString(uCursor.getColumnIndex(COLUMN_WHSE));
                String _qty = uCursor.getString(uCursor.getColumnIndex(COLUMN_QUANTITY));

                values = getValuesUpload(_whse, _upc, _qty);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //transferActivity
        recordData.add("*Transfer Parts Records*");
        for (int t = 0; t <= tCursor.getCount(); t++) {
            try {
                tCursor.moveToPosition(t);
                String _upc = tCursor.getString(tCursor.getColumnIndex(COLUMN_UPC));
                String _fromWhse = tCursor.getString(tCursor.getColumnIndex(COLUMN_FROM_WHSE));
                String _toWhse = tCursor.getString(tCursor.getColumnIndex(COLUMN_TO_WHSE));
                String _qty = tCursor.getString(tCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = tCursor.getString(tCursor.getColumnIndex(COLUMN_SERIAL));

                values = getValuesTransfer(_fromWhse, _toWhse, _upc, _qty, _serial);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //receiveActivity
        recordData.add("*Receive Parts Records*");
        for (int r = 0; r <= rCursor.getCount(); r++) {
            try {
                rCursor.moveToPosition(r);
                String _upc = rCursor.getString(rCursor.getColumnIndex(COLUMN_UPC));
                String _whse = rCursor.getString(rCursor.getColumnIndex(COLUMN_WHSE));
                String _qty = rCursor.getString(rCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = rCursor.getString(rCursor.getColumnIndex(COLUMN_SERIAL));
                String _date = rCursor.getString(rCursor.getColumnIndex(COLUMN_DATE));
                String _comment = rCursor.getString(rCursor.getColumnIndex(COLUMN_COMMENT));
                String _po = rCursor.getString(rCursor.getColumnIndex(COLUMN_PO_NUM));

                values = getValuesReceive(_whse, _po, _upc, _qty, _serial, _date, _comment);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ra.setRecordData(recordData);
        db.close();
        cCursor.close();
        uCursor.close();
    }

    void populateReportCharge() {
        SQLiteDatabase db = this.getReadableDatabase();
        ReportActivity ra = new ReportActivity();
        ContentValues values;
        Cursor cCursor = db.query(TABLE_CHRG_DATA, null, null, null, null, null, null);
        List<String> recordData = new ArrayList<>();

        for (int c = 0; c <= cCursor.getCount() + 1; c++) {
            try {
                cCursor.moveToPosition(c);
                String _upc = cCursor.getString(cCursor.getColumnIndex(COLUMN_UPC));
                String _date = cCursor.getString(cCursor.getColumnIndex(COLUMN_DATE));
                String _wo = cCursor.getString(cCursor.getColumnIndex(COLUMN_JOB_WO_NUM));
                String _whse = cCursor.getString(cCursor.getColumnIndex(COLUMN_WHSE));
                String _item = cCursor.getString(cCursor.getColumnIndex(COLUMN_ITEM));
                String _type = cCursor.getString(cCursor.getColumnIndex(COLUMN_TYPE));
                String _qty = cCursor.getString(cCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = cCursor.getString(cCursor.getColumnIndex(COLUMN_SERIAL));
                String _comment = cCursor.getString(cCursor.getColumnIndex(COLUMN_COMMENT));

                values = getValuesChrg(_whse, _wo, _item, _type, _upc, _qty, _serial, _comment, _date);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ra.setRecordData(recordData);
        db.close();
        cCursor.close();
    }

    void populateReportUpload() {
        SQLiteDatabase db = this.getReadableDatabase();
        ReportActivity ra = new ReportActivity();
        ContentValues values;
        Cursor uCursor = db.query(TABLE_UPLOAD_DATA, null, null, null, null, null, null);
        List<String> recordData = new ArrayList<>();

        for (int u = 0; u <= uCursor.getCount(); u++) {
            try {
                uCursor.moveToPosition(u);
                String _upc = uCursor.getString(uCursor.getColumnIndex(COLUMN_UPC));
                String _whse = uCursor.getString(uCursor.getColumnIndex(COLUMN_WHSE));
                String _qty = uCursor.getString(uCursor.getColumnIndex(COLUMN_QUANTITY));

                values = getValuesUpload(_whse, _upc, _qty);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ra.setRecordData(recordData);
        db.close();
        uCursor.close();
    }

    void populateReportTransfer() {
        SQLiteDatabase db = this.getReadableDatabase();
        ReportActivity ra = new ReportActivity();
        ContentValues values;
        Cursor tCursor = db.query(TABLE_TRANSFER_DATA, null, null, null, null, null, null);
        List<String> recordData = new ArrayList<>();

        for (int t = 0; t <= tCursor.getCount(); t++) {
            try {
                tCursor.moveToPosition(t);
                String _upc = tCursor.getString(tCursor.getColumnIndex(COLUMN_UPC));
                String _fromWhse = tCursor.getString(tCursor.getColumnIndex(COLUMN_FROM_WHSE));
                String _toWhse = tCursor.getString(tCursor.getColumnIndex(COLUMN_TO_WHSE));
                String _qty = tCursor.getString(tCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = tCursor.getString(tCursor.getColumnIndex(COLUMN_SERIAL));

                values = getValuesTransfer(_fromWhse, _toWhse, _upc, _qty, _serial);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ra.setRecordData(recordData);
        db.close();
        tCursor.close();
    }

    void populateReportReceive() {
        SQLiteDatabase db = this.getReadableDatabase();
        ReportActivity ra = new ReportActivity();
        ContentValues values;
        Cursor rCursor = db.query(TABLE_RECEIVE_DATA, null, null, null, null, null, null);
        List<String> recordData = new ArrayList<>();

        for (int r = 0; r <= rCursor.getCount(); r++) {
            try {
                rCursor.moveToPosition(r);
                String _upc = rCursor.getString(rCursor.getColumnIndex(COLUMN_UPC));
                String _whse = rCursor.getString(rCursor.getColumnIndex(COLUMN_WHSE));
                String _qty = rCursor.getString(rCursor.getColumnIndex(COLUMN_QUANTITY));
                String _serial = rCursor.getString(rCursor.getColumnIndex(COLUMN_SERIAL));
                String _date = rCursor.getString(rCursor.getColumnIndex(COLUMN_DATE));
                String _comment = rCursor.getString(rCursor.getColumnIndex(COLUMN_COMMENT));
                String _po = rCursor.getString(rCursor.getColumnIndex(COLUMN_PO_NUM));

                values = getValuesReceive(_whse, _po, _upc, _qty, _serial, _date, _comment);

                recordData.add(values.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ra.setRecordData(recordData);
        db.close();
        rCursor.close();
    }

    public void populateDefaults(int module) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;

        try {
            cursor = db.query(TABLE_DEFAULTS, null, null, null, null, null, null);
            cursor.moveToPosition(0);

            String _whse = cursor.getString(cursor.getColumnIndex(COLUMN_WHSE));
            String _type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            String _item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
            String _jobWo = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_WO_NUM));

            ConfigActivity ca = new ConfigActivity();
            ChargeActivity chrg = new ChargeActivity();
            UploadActivity ua = new UploadActivity();
            TransferActivity ta = new TransferActivity();
            ReceivePO rpo = new ReceivePO();

            switch (module) {
                case 1:
                    chrg.setWO(_jobWo);
                    chrg.setWHSE(_whse);
                    chrg.setType(_type);
                    chrg.setItem(_item);
                    break;
                case 2:
                    ua.setWHSE(_whse);
                    break;
                case 3:
                    ta.setFromWHSE(_whse);
                    break;
                case 4:
                    rpo.setWHSE(_whse);
                    break;
                case 5:
                    ca.setWHSE(_whse);
                    ca.setType(_type);
                    ca.setItem(_item);
                    ca.setWO(_jobWo);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Export the database to a csv file.
     * @param context   context
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void exportDb(Context context, String _filename, Integer tableNum) {
        //
    	SQLiteDatabase _db = this.getReadableDatabase();
        File exportDir = getDir(context);

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, _filename);
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            assert db != null;

            switch (tableNum) {
                case 1:
                    _curCSV = db.rawQuery("SELECT * FROM " + TABLE_CHRG_DATA, null);
                    while (_curCSV.moveToNext()) {
                        String arrStr[] = {_curCSV.getString(1), _curCSV.getString(2),
                                _curCSV.getString(3), _curCSV.getString(4), _curCSV.getString(5), _curCSV.getString(6),
                                _curCSV.getString(7), _curCSV.getString(8), _curCSV.getString(9)};
                        csvWrite.writeNext(arrStr);
                    }
                    break;
                case 2:
                    _curCSV = db.rawQuery("SELECT * FROM " + TABLE_UPLOAD_DATA, null);
                    while (_curCSV.moveToNext()) {
                        String arrStr[] = {_curCSV.getString(1), _curCSV.getString(2),
                                _curCSV.getString(3)};
                        csvWrite.writeNext(arrStr);
                    }
                    break;
                case 3:
                    _curCSV = db.rawQuery("SELECT * FROM " + TABLE_TRANSFER_DATA, null);
                    while (_curCSV.moveToNext()) {
                        String arrStr[] = {_curCSV.getString(1), _curCSV.getString(2),
                                _curCSV.getString(3), _curCSV.getString(4), _curCSV.getString(5)};
                        csvWrite.writeNext(arrStr);
                    }
                    break;
                case 4:
                    _curCSV = db.rawQuery("SELECT * FROM " + TABLE_RECEIVE_DATA, null);
                    while (_curCSV.moveToNext()) {
                        String arrStr[] = {_curCSV.getString(1), _curCSV.getString(2),
                                _curCSV.getString(3), _curCSV.getString(4), _curCSV.getString(5), _curCSV.getString(6),
                                _curCSV.getString(7)};
                        csvWrite.writeNext(arrStr);
                    }
                    break;
            }
            csvWrite.close();
            _curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("HomeActivity", sqlEx.getMessage(), sqlEx);
        }
        if (_db != null) {
            _db.close();
        }
    }
    
    /**
     * get the directory path to the upload file
     *
     * @param context   context
     * @return          exportDir
     */
    File getDir(Context context) {
    	File exportDir;
        /*
        if (getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            exportDir = new File(getExternalStorageDirectory(),"");
        } else {
            exportDir = context.getCacheDir();
        }//*/

        //test T-Op (var = condition ? valueIfTrue : valueIfFalse)
        exportDir = getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? new File(String.valueOf(getExternalStorageDirectory())) : context.getCacheDir();

        return exportDir;
    }
    
    /**
     * removes label from labels table
     * 
     * @param label     label
     */
    public void removeLabel(int table, String label) {
        //
    	SQLiteDatabase db = this.getWritableDatabase();

    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, label);

        switch (table) {
            case 1:
                assert db != null;
                db.delete(TABLE_WHSE, COLUMN_WHSE + "=?", new String[] {label});
                break;
            case 2:
                assert db != null;
                db.delete(TABLE_ITEM, COLUMN_ITEM + "=?", new String[] {label});
                break;
            case 3:
                assert db != null;
                db.delete(TABLE_TYPE, COLUMN_TYPE + "=?", new String[] {label});
        }

    	// removing row
    	//db.delete(TABLE_LABELS, KEY_NAME + "=?", new String[] {label});
        if (db != null) {
            db.close();
        }
    }

    /**
     * purge the charge data table.
     *
     */
    public void purgeData(String dbName) {
    	SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(dbName, null, null);
    	db.close();
    }

    /**
     * purge the settings table
     */
    public void purgeSettings() {
    	SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(TABLE_SETTINGS, null, null);
    	db.close();
    }
        
    /**
     * returns list of labels.
     * 
     * @return	returns list of labels.
     */
    public List<String> getAllLabels(String table){
        //
        List<String> labels = new ArrayList<>();

        switch (table) {
            case "1":
                selectQuery = "SELECT * FROM " + TABLE_WHSE;
                break;
            case "2":
                selectQuery = "SELECT * FROM " + TABLE_ITEM;
                break;
            case "3":
                selectQuery = "SELECT * FROM " + TABLE_TYPE;
                break;
            case "0":
                selectQuery = "SELECT * FROM " + TABLE_LABELS;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
        db.close();
 
        // returning labels
        return labels;
    }

    /**
     * Storing user details in database
     * */
    /*
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_USERNAME, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        assert db != null;
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
    //*/

    // bored...
    void allYourDataBaseAreBelongToUs(List<notSoRandom.BASES> bases) {
        String temp;
        List<String> _bases = new ArrayList<>();
        for (notSoRandom.BASES base : bases) {
            _bases.add(base != null ? base.toString() : null);
        }
        if (_bases.size()>1) // check if the number of bases is larger than 1
        {
            for (int x=0; x<_bases.size(); x++) // bubble sort outer loop
            {
                for (int i=0; i < _bases.size() - x - 1; i++) {
                    if (_bases.get(i).compareTo(_bases.get(i+1)) > 0)
                    {
                        temp = _bases.get(i);
                        _bases.set(i,_bases.get(i+1) );
                        _bases.set(i+1, temp);
                    }
                }
            }
        }
    }

    /**
     * Getting user data from database
     * */
    /*
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
    //*/

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        assert db != null;
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

    public void switchDB(int dbNum) {
        switch (dbNum) {
            case 1: populateChrg(_recordNum);
                break;
            case 2: populateUpload(_recordNum);
                break;
            case 3: populateTrans(_recordNum);
                break;
            case 4: populateReceive(_recordNum);
                break;
        }
    }

    public void moveToFirst(String dbName, int dbNum) {
        //go to the first record in the db
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.query(dbName, null, null, null, null, null, null);
            cursor.moveToFirst();
            _recordNum = cursor.getPosition();
            switchDB(dbNum);
            try {
                keyId = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    public void moveToLast(String dbName, int dbNum) {
        //go to last record in the db
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.query(dbName, null, null, null, null, null, null);
            cursor.moveToLast();
            _recordNum = cursor.getPosition();
            switchDB(dbNum);
            try {
                keyId = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    public void moveToNext(String dbName, Context context, int dbNum) {
        //go to the next record
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.query(dbName, null, null, null, null, null, null);
            cursor.moveToPosition(_recordNum);
            cursor.moveToNext();
            int currentPos = _recordNum;
            _recordNum = cursor.getPosition();
            //*
            if (currentPos == _recordNum) {
                makeText(context, context.getString(R.string.onLastRecord), LENGTH_LONG); //.show();
            }
            //*/
            switchDB(dbNum);
            try {
                keyId = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    /**
     * move cursor to the previous record
     *
     * @param dbName    name of the table
     * @param context   application context
     * @param dbNum     number of the table
     */
    public void moveToPrevious(String dbName, Context context, int dbNum) {
        //go to previous record
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            Cursor cursor = db.query(dbName, null, null, null, null, null, null);
            cursor.moveToPosition(_recordNum);
            cursor.moveToPrevious();
            int currentPos = _recordNum;
            _recordNum = cursor.getPosition();
            if (_recordNum == -1) {_recordNum = 0;}

            if (currentPos == _recordNum) {
                makeText(context, context.getString(R.string.onFirstRecord), LENGTH_LONG); //.show();
            }
            switchDB(dbNum);
            try {
                keyId = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    public void deleteAll(String dbName) {
        //delete all records in db
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            db.delete(dbName, null, null);
            //db.execSQL("DELETE FROM " + dbName);
            //purgeChrgData();
            db.execSQL("vacuum");
            db.close();
        }
    }

    public void deleteOne(String dbName) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            db.delete(dbName, COLUMN_KEY + "=?", new String[] {Integer.toString(_recordNum + 1)});
            //db.delete(dbName, COLUMN_KEY+"="+_recordNum, null);
            db.close();
        }
    }

    public void deleteOne(String tableName, Integer index) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            db.delete(tableName, COLUMN_KEY + "=?", new String[]{Integer.toString(index)});
            db.close();
        }
    }
}

class notSoRandom extends DatabaseHandler{

    public notSoRandom(Context context, List<BASES> bases) {
        super(context);
        List<BASES> bases1 = getSomeBase(bases);
        allYourDataBaseAreBelongToUs(bases1);
    }

    private List<BASES> getSomeBase(List<BASES> bases) {
        Random random = new Random();
        int base = random.nextInt(50)+1;
        for (int i = 0; i < base; i++) {
            //getBase
        }
        return bases;
    }

    public class BASES {
    }
}
