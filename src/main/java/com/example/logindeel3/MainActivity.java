package com.example.logindeel3;

import static android.provider.CalendarContract.CalendarCache.URI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view)
    {
        System.out.println("Clicked");
        EditText username = (EditText) findViewById(R.id.etInlognaam);
        EditText password = (EditText) findViewById(R.id.etWachtwoord);
        String gebruiker = username.getText().toString();
        String ww = password.getText().toString();

        SQLiteDatabase gebruikersDB = this.openOrCreateDatabase("gebruikersDB", MODE_PRIVATE, null);

        File dbFile = getDatabasePath("gebruikersDB");
        if (dbFile.exists()) {
            Log.i("database", "bestaat");
            Log.i("Path to database ", dbFile.getPath().toString());
        } else {
            Log.i("database", "bestaat niet");
        }

        gebruikersDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + "gegevens "
                + "(inlognaam VARCHAR, wachtwoord VARCHAR);"
        );

        switch (view.getId()) {
            case R.id.registreren:
                Log.d("test", "registreren button geklikt");
                String sql = "INSERT or REPLACE INTO gegevens (inlognaam, wachtwoord) " +
                        " VALUES ('" + gebruiker + "', '" + ww + "');";
                gebruikersDB.execSQL(sql);
                break;
            case R.id.inloggen:
                Log.d("test", "inloggen button gekilkt");
                String table = "gegevens";
                String[] columnsToReturn = { "wachtwoord" };
                String selection = "inlognaam = ? ";
                String[] selectionArgs = { gebruiker } ;
                try {
                    Cursor cursor = gebruikersDB.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
                    cursor.moveToLast();
                    String column1 = cursor.getString(0);
                    if(column1.equals(ww)) {
                        Log.d(column1, "Inloggen button gekilkt");
                        Intent intent = new Intent(this, MainActivity3.class);
                        startActivity(intent);

                        //Intent browserIntent = new Intent("android.intent.action.VIEW", URI.parse("https://google.com"));

                    } else {
                        Log.d(column1, "inloggen mislukt");
                    }
                    cursor.close();
                    break;
                } catch  (android.database.CursorIndexOutOfBoundsException  e) {
                    Log.d(gebruiker, " bestaat waarschijnlijk niet ");
                }
        }
    }
}