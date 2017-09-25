package iam.thevoid.cursorobservablelist;

import android.provider.ContactsContract;

import iam.thevoid.cursorannotations.ColumnName;


/**
 * Created by iam on 25/09/2017.
 */

public class Pojo {
    @ColumnName(name = ContactsContract.CommonDataKinds.Email.DATA)
    String mName;
    @ColumnName(name = ContactsContract.Contacts.DISPLAY_NAME)
    String mEmail;
}
