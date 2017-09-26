CursorObservableList is the library for create ObservableList using Android SqlLite cursor.

Usage (Pojo gets contact name and email from android phonebook. Don't forget request permissions ;) )

```
public class Pojo {
    @ColumnName(name = ContactsContract.CommonDataKinds.Email.DATA)
    String mName;
    @ColumnName(name = ContactsContract.Contacts.DISPLAY_NAME)
    String mEmail;
}
```

```
String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.toLowerCase().trim() + " ASC";

String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_ID,
        ContactsContract.CommonDataKinds.Email.DATA,
        ContactsContract.CommonDataKinds.Photo.CONTACT_ID};

String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";

ObservableList<CursorEmailContactWrapper> mCursorObservableList;

Cursor cursor = context
        .getContentResolver()
        .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);

mCursorObservableList = new CursorObservableList<>(PoJo.class, cursor);

```

if u'r using [Evant's BindingCollectionAdapter](https://github.com/evant/binding-collection-adapter)
and Android Data Binding library u can use this list in layout

```
<android.support.v7.widget.RecyclerView
    android:id="@+id/recycler"
    app:items="@{vm.mCursorObservableList}"
    app:layoutManager="@{LayoutManagers.linear()}"
    app:itemBinding="@{vm.binding}"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```