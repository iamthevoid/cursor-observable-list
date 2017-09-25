package iam.thevoid.cursorobservablelist;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import iam.thevoid.columnbinder.CursorObservableList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cursor query = getContentResolver().query(null, null, null, null, null);
        CursorObservableList<Pojo> list =
                new CursorObservableList<>(Pojo.class, query);
    }
}
