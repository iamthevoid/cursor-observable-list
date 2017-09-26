package iam.thevoid.columnbinder;

import android.database.Cursor;
import android.databinding.ListChangeRegistry;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by iam on 22/09/2017.
 */

public class CursorObservableList<T> implements ObservableList<T> {

    private final Class<T> typeParameterClass;

    private final T[] cache;

    private final ListChangeRegistry listeners = new ListChangeRegistry();
    private final Cursor mCursor;

    public CursorObservableList(Class<T> typeParameterClass, Cursor cursor) {
        this.typeParameterClass = typeParameterClass;
        mCursor = cursor;
        cache = initCache();
    }

    @Override
    public T get(int i) {
        if (mCursor == null) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(i)) {
            throw new IllegalStateException("couldn't move cursor to position " + i);
        }

        T item;

        if ((item = cache[i]) == null) {
            item = instance();
            setData(item, mCursor);
            cache[i] = item;
        }

        return item;
    }

    private void setData(T item, Cursor cursor) {
        ColumnBinder.bind(item, cursor);
    }

    public List<T> list() {
        if (mCursor == null || mCursor.isClosed()) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>(Arrays.asList(cache));
        for (int i = 0, l = size(); i < l; i++) {
            if (list.get(i) == null) {
                mCursor.moveToPosition(i);
                T item = instance();
                setData(item, mCursor);
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public int size() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> onListChangedCallback) {
        boolean first = listeners.isEmpty();
        listeners.add(onListChangedCallback);
        if (first) {
            listeners.notifyChanged(this);
        }
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> onListChangedCallback) {
        listeners.remove(onListChangedCallback);
    }

    private T instance()
    {
        try {
            return typeParameterClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private T[] initCache()
    {
        return (T[]) Array.newInstance(typeParameterClass, size());
    }

    @Override
    public boolean isEmpty() {
        return size() > 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < cache.length; i++) {
            if (o.equals(cache[i])) {
                return i;
            }
        }
        return -1;
    }

    public void close() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }

    // ================ UNUSED METHODS ==================

    @Override
    public int lastIndexOf(Object o) {
        throw new IllegalStateException("Invalid method: lastIndexOf");
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        throw new IllegalStateException("Invalid method: listIterator");
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        throw new IllegalStateException("Invalid method: listIterator");
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        throw new IllegalStateException("Invalid method: subList");
    }

    @Override
    public T set(int i, T t) {
        throw new IllegalStateException("Invalid method: set");
    }

    @Override
    public void add(int i, T t) {
        throw new IllegalStateException("Invalid method: add");
    }

    @Override
    public T remove(int i) {
        throw new IllegalStateException("Invalid method: remove");
    }

    @Override
    public boolean contains(Object o) {
        throw new IllegalStateException("Invalid method: contains");
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        throw new IllegalStateException("Invalid method: iterator");
    }

    @NonNull
    @Override
    public Object[] toArray() {
        throw new IllegalStateException("Invalid method: toArray");
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] t1s) {
        throw new IllegalStateException("Invalid method: toArray");
    }

    @Override
    public boolean add(T t) {
        throw new IllegalStateException("Invalid method: add");
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalStateException("Invalid method: remove");
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        throw new IllegalStateException("Invalid method: containsAll");
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        throw new IllegalStateException("Invalid method: addAll");
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        throw new IllegalStateException("Invalid method: addAll");
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        throw new IllegalStateException("Invalid method: removeAll");
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        throw new IllegalStateException("Invalid method: retainAll");
    }

    @Override
    public void clear() {
        throw new IllegalStateException("Invalid method: clear");
    }
}
