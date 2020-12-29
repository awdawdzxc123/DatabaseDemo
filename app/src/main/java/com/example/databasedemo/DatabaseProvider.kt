package com.example.databasedemo

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class DatabaseProvider : ContentProvider() {
    private val bookDir = 0
    private val bookItem = 1
    private val categoryDir = 2
    private val categoryItem = 3
    private val authority = "com.example.databasedemo.provider"
    private var dbHelper: MyDatabaseHelper? = null

    //懒加载技术
    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.apply {
            matcher.addURI(authority, "Book", bookDir)
            matcher.addURI(authority, "Book/#", bookItem)
            matcher.addURI(authority, "Category", categoryDir)
            matcher.addURI(authority, "Category/#", categoryItem)
        }
        matcher

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        val db = it.readableDatabase
        val deleteRows = when (uriMatcher.match(uri)) {
            bookDir -> {
                val bookID = uri.pathSegments[1]
                db.delete("Book", selection, selectionArgs)
            }
            bookItem -> {
                db.delete("Book", "id =?", selectionArgs)
            }
            categoryDir -> {

                db.delete("category", selection, selectionArgs)
            }
            categoryItem -> {
                val category = uri.pathSegments[1]
                db.delete("category", "id =?", selectionArgs)
            }
            else -> 0
        }
        deleteRows
    } ?: 0

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        bookDir -> "vnd.android.cursor.dir/vnd.dir/vnd.$authority.Book"
        bookItem -> "vnd.android.cursor.item/vnd.dir/vnd.$authority.Book"
        categoryDir -> "vnd.android.cursor.dir/vnd.dir/vnd.$authority.Category"
        categoryItem -> "vnd.android.cursor.item/vnd.dir/vnd.$authority.Category"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        val db = it.readableDatabase
        val uriMatcher = when (uriMatcher.match(uri)) {
            bookDir, bookItem -> {
                val newBookId = db.insert("Book", null, values)
                Uri.parse("content://$authority/book/$newBookId")
            }
            categoryDir, categoryItem -> {
                val newCategoryId = db.insert("Category", null, values)
                Uri.parse("content://$authority/Category/$newCategoryId")
            }
            else -> null
        }
        uriMatcher
    }

    override fun onCreate() = context?.let {
        dbHelper = MyDatabaseHelper(it, "BookStore.db", 3)
        true
    } ?: false

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?) = dbHelper?.let {
        val db = it.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            bookDir -> {
                db.query("Book", projection, selection, selectionArgs, null, null, sortOrder)
            }
            bookItem -> {
                val bookID = uri.pathSegments[1]
                db.query("Book", projection, "id = ?", arrayOf(bookID), null, null, sortOrder)
            }
            categoryDir -> {
                db.query("category", projection, selection, selectionArgs, null, null, sortOrder)
            }
            categoryItem -> {
                val categoryID = uri.pathSegments[1]
                db.query("category", projection, "id = ?", arrayOf(categoryID), null, null, sortOrder)
            }
            else -> null
        }
        cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?) = dbHelper?.let {
        val db = it.readableDatabase
        val updateRows = when (uriMatcher.match(uri)) {
            bookDir -> {
                val bookID = uri.pathSegments[1]
                db.update("Book", values, selection, selectionArgs)
            }
            bookItem -> {
                db.update("Book", values, "id =?", selectionArgs)
            }
            categoryDir -> {

                db.update("category", values, selection, selectionArgs)
            }
            categoryItem -> {
                val category = uri.pathSegments[1]
                db.update("category", values, "id =?", selectionArgs)
            }
            else -> 0
        }
        updateRows
    } ?: 0
}