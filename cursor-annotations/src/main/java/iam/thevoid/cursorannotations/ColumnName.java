package iam.thevoid.cursorannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by iam on 23/09/2017.
 */

@Target(ElementType.FIELD) // on class level
@Retention(RetentionPolicy.SOURCE)
public @interface ColumnName {
    String name();
}
