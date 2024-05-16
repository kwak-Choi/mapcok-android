package com.mapcok.ui.mypage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

class MyPageDao(requireContext: Context) {
    private val DATABASE_TABLE = "photo"
    private val NUM = "num"
    private val PHOTO_LOCATION = "photoLocation"
    private val PHOTO_DATE = "photoDate"
    private val PHOTO_SRC = "photoSrc"

    //DB선언부
    lateinit var helper: DBHelper
    lateinit var sqlDB: SQLiteDatabase
    private var mCtx: Context? = null

    //INSERT
    fun insertPhoto(dto:MyPagePhoto):Long{
        val args = ContentValues()
        args.put(PHOTO_LOCATION, dto.location)
        args.put(PHOTO_DATE, dto.date)
        args.put(PHOTO_SRC, dto.src)

        return sqlDB.insert(DATABASE_TABLE, null, args)
    }

    //SELECT
    fun selectAllPhotos(): MutableList<MyPagePhoto> {
        val list = mutableListOf<MyPagePhoto>()

        sqlDB.rawQuery("select * from $DATABASE_TABLE", null).use{
            while (it.moveToNext()) {
                list.add(
                    MyPagePhoto(
                        it.getInt(0),
                        it.getString(1),
                        it.getLong(2),
                        it.getString(3)
                    )
                )
            }
        }
        return list
    }


    fun selectPhotos(num: Int): MyPagePhoto {
        val cursor = sqlDB.query(
            DATABASE_TABLE,
            arrayOf(NUM, PHOTO_LOCATION, PHOTO_DATE, PHOTO_SRC),
            "$NUM = ?",
            arrayOf(num.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val location = cursor.getString(cursor.getColumnIndexOrThrow(PHOTO_LOCATION))
            val date = cursor.getLong(cursor.getColumnIndexOrThrow(PHOTO_DATE))
            val src = cursor.getString(cursor.getColumnIndexOrThrow(PHOTO_SRC))
            MyPagePhoto(num, location, date, src)
        } else {
            MyPagePhoto(-1, "", 0, "")
        }.also { cursor.close() }
    }


    @Throws(SQLException::class)
    fun open() {
        helper = DBHelper(mCtx!!)
        sqlDB = helper.writableDatabase
    }
    fun dbOpenHelper(context: Context) {
        this.mCtx = context
    }
    fun create() {
        //DB생성
        helper.onCreate(sqlDB)
    }
    fun upgrade(oldVersion: Int, newVersion: Int) {
        //DB version 변경
        helper.onUpgrade(sqlDB, 1, 2)
    }
    fun close() {
        //DB종료
        sqlDB.close()
    }

    inner class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_TABLE, null, 1) {
        var now = System.currentTimeMillis()
        override fun onCreate(p0: SQLiteDatabase) { //테이블 생성
            p0.execSQL("CREATE TABLE $DATABASE_TABLE ( $NUM INTEGER PRIMARY KEY AUTOINCREMENT, $PHOTO_LOCATION CHAR(200) , $PHOTO_DATE long, $PHOTO_SRC char(200));")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/apple');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/beach');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/bigben');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/bird');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/blueberries');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/city');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/cornflower');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/cute');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/dubai');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/duckling');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/fantasy');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/jellyfish');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/milkyway');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/mountain');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/mountains');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/nature');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/nuthatch');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/papenburg');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/rhododendron');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/sea');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/sky');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/thunderstorm');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/tree');")
            p0.execSQL("insert into $DATABASE_TABLE ( $PHOTO_LOCATION, $PHOTO_DATE, $PHOTO_SRC) values ('구미시 공단동', ${now}, '@drawable/woman');")
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { //테이블 삭제 후 생성
            p0!!.execSQL("DROP TABLE IF EXISTS  $DATABASE_TABLE")
            onCreate(p0)
        }
    }
}