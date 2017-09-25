package iam.thevoid.columnbinder;

import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iam on 25/09/2017.
 */

public class ColumnBinder {

    private static Map<String, Method> BINDINGS = new HashMap<>();

    public static void bind(Object object, Cursor cursor) {
        Class<?> cls = object.getClass();
        Method method = findBindingConstructorForClass(cls);
        try {
            method.invoke(getHandlerClass(object.getClass()).newInstance(), object, cursor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static Method findBindingConstructorForClass(Class cls) {
        Method method = BINDINGS.get(cls.getCanonicalName());
        if (method != null) {
            return method;
        }

        Class<?> bindingClass = null;

        try {
            bindingClass = getHandlerClass(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            method = bindingClass.getMethod("bind", cls, Cursor.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return method;
    }

    private static Class<?> getHandlerClass(Class cls) throws ClassNotFoundException {
        return cls.getClassLoader().loadClass(cls.getName() + "_CursorBinding");
    }
}
