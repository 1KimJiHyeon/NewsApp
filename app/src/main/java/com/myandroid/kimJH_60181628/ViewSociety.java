package com.myandroid.kimJH_60181628;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSociety extends AppCompatActivity {
    SQLiteDatabase db;
    DbHelperSociety dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_society);

        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DbHelperSociety(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + dbHelper.TABLE_NAME + " where " + dbHelper.C_ID + "=" + id, null);
        TextView title = (TextView) findViewById(R.id.title);
        TextView date = (TextView) findViewById(R.id.date);
        TextView summary = (TextView) findViewById(R.id.summary);
        TextView source = (TextView) findViewById(R.id.source);
        ImageView img=(ImageView)findViewById(R.id.showImage);
        byte[] bit=null;


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                date.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DATE)));
                summary.setText(cursor.getString(cursor.getColumnIndex(dbHelper.SUMMARY)));
                source.setText(cursor.getString(cursor.getColumnIndex(dbHelper.SOURCE)));
                bit =cursor.getBlob(cursor.getColumnIndex(dbHelper.IMAGE));
            }
            Bitmap bitmap= BitmapFactory.decodeByteArray(bit,0,bit.length);
            img.setImageBitmap(bitmap);
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, ListSociety.class);
        startActivity(setIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong(getString(R.string.rowID));

        switch (item.getItemId()) {
            case R.id.action_edit:

                Intent openEditNote = new Intent(ViewSociety.this, EditSociety.class);
                openEditNote.putExtra(getString(R.string.intent_row_id), id);
                startActivity(openEditNote);
                return true;

            case R.id.action_discard:
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSociety.this);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                long id = getIntent().getExtras().getLong(getString(R.string.rodID));
                                db.delete(dbHelper.TABLE_NAME, dbHelper.C_ID + "=" + id, null);
                                db.close();
                                Intent openMainActivity = new Intent(ViewSociety.this, ListSociety.class);
                                startActivity(openMainActivity);

                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)    //Do nothing on no
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
