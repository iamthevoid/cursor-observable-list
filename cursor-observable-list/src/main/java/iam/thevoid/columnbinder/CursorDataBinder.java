package iam.thevoid.columnbinder;

import android.database.Cursor;

/**
 * Created by iam on 22/09/2017.
 */

public abstract class CursorDataBinder<T> {

    public abstract void bind(T object, Cursor cursor);
}
