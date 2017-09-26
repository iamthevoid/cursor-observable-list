package iam.thevoid.cursorobservablelist;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;

import iam.thevoid.columnbinder.CursorObservableList;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class MainActivity extends AppCompatActivity {

    String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.toLowerCase().trim() + " ASC";

    String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID};

    String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(READ_CONTACTS, WRITE_CONTACTS)
                .compose(rxPermissions.ensure(READ_CONTACTS, WRITE_CONTACTS))
                .filter(gained -> gained)
                .subscribe(granted -> {
                    if (granted) {
                        Cursor cursor = getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
                        CursorObservableList<Pojo> list =
                                new CursorObservableList<>(Pojo.class, cursor);
                        list.list();
                    }
                });

    }
}
