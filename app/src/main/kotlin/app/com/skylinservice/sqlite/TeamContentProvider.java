package app.com.skylinservice.sqlite;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;

import app.com.skylinservice.manager.SkyLinDBManager;
import app.com.skylinservice.manager.UserIdManager;

public class TeamContentProvider extends ContentProvider {

    private SQLiteDatabase db;

    public TeamContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");

    }


    @Override
    public boolean onCreate() {

        DB d = new DB(this.getContext(), "teamstate.db", true);
        db = d.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String[] args = {String.valueOf(new UserIdManager().getUserId())};
        if (new UserIdManager().getUserId() == 0) {
            return db.query("labelnet", new String[]{selection, "_id"}, null, null, null, null, null);

        } else {
            return db.query("labelnet", new String[]{selection, "_id"}, "_id=?", args, null, null, null);

        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        try {
            String[] args = {String.valueOf(new UserIdManager().getUserId())};
            db.update("labelnet", values, "_id=?", args);
        } catch (Exception e) {

        } finally {
            AppUtils.launchApp(selection);
        }
        return 0;
    }
}
