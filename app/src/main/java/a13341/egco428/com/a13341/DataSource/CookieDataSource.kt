package a13341.egco428.com.a13341.DataSource

import a13341.egco428.com.a13341.Helper.MySQLiteHelper
import a13341.egco428.com.a13341.Model.Cookie
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class CookieDataSource(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: MySQLiteHelper
    private val allColumns = arrayOf<String>(MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_MESSAGE,MySQLiteHelper.COLUMN_TYPE)

    val allComments: List<Cookie>
        get() {
            val comments = ArrayList<Cookie>()

            val cursor = database!!.query(MySQLiteHelper.TABLE_COOKIES, allColumns, null, null, null, null, null)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val comment = cursorToComment(cursor)
                comments.add(comment)
                cursor.moveToNext()
            }
            cursor.close()
            return comments
        }

    init {
        dbHelper = MySQLiteHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.getWritableDatabase()
    }

    fun close() {
        dbHelper.close()
    }

    fun createComment(comment: String,type: String): Cookie {
        val values = ContentValues()
        values.put(MySQLiteHelper.COLUMN_MESSAGE, comment)
        values.put(MySQLiteHelper.COLUMN_TYPE, type)
        val insertId = database!!.insert(MySQLiteHelper.TABLE_COOKIES, null,
                values)
        val cursor = database!!.query(MySQLiteHelper.TABLE_COOKIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null)
        cursor.moveToFirst()
        val newComment = cursorToComment(cursor)
        cursor.close()
        return newComment
    }

    fun deleteComment(comment: Cookie) {
        val id = comment.id
        println("Comment deleted with id: " + id)
        database!!.delete(MySQLiteHelper.TABLE_COOKIES, MySQLiteHelper.COLUMN_ID + " = " + id, null)
    }

    private fun cursorToComment(cursor: Cursor): Cookie {
        val comment = Cookie()
        comment.id = cursor.getLong(0)
        comment.message = cursor.getString(1)
        comment.type = cursor.getString(2)
        return comment
    }
}