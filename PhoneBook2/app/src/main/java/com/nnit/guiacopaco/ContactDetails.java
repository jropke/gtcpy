package com.nnit.guiacopaco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nnit.guiacopaco.data.PhoneBookItem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ContactDetails extends Activity {

	private TextView firstName;
	private TextView lastName;
	private TextView emailAddress;
	private TextView phoneNumber;
	private Long rowID;
	ContactDB PhonebookDB = new ContactDB(this);
	private Cursor CursorList;
	public static final String TARGET_INITIALS = "com.nnit.phonebook.TARGET_INITIALS";
	private static final int CONTACT_SAVE_INTENT_REQUEST = 1;

	private PhoneBookItem pbItem = null;

	private Uri contactUri = null;

	private Resources resources = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_contact_details_e);

		resources = getResources();

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_detail_2);

		PhonebookDB.open();
		try
		{
			firstName=(TextView)findViewById(R.id.txtFirstName);
			lastName=(TextView)findViewById(R.id.txtLastName);
			emailAddress=(TextView)findViewById(R.id.txtEmailID);
			phoneNumber=(TextView)findViewById(R.id.txtPhone);

			String position=getIntent().getStringExtra("posit");
			rowID=Long.parseLong(position);
			CursorList=PhonebookDB.getContacts(rowID);
			firstName.setText(CursorList.getString(1)+" ");
			lastName.setText(CursorList.getString(2));
			emailAddress.setText(CursorList.getString(3));
			phoneNumber.setText(CursorList.getString(4));
		}
		catch(Exception e)
		{
			Log.e("Phonebook_TAG","Got an error while displaying the contact",e);
		}


		ImageButton llamar_bar = (ImageButton) findViewById(R.id.llamar_bar);
		llamar_bar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(ContactDetails.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(resources.getString(R.string.info_call))
						.setPositiveButton(resources.getString(R.string.lable_okbtn), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.DIAL");
								intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
								startActivity(intent);
								dialog.dismiss();
							}
						})
						.setNegativeButton(resources.getString(R.string.lable_cancelbtn), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.show();

			}

		});

		ImageButton txtPhn = (ImageButton) findViewById(R.id.txtPhn);
		txtPhn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(ContactDetails.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(resources.getString(R.string.info_call))
						.setPositiveButton(resources.getString(R.string.lable_okbtn), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.DIAL");
								intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
								startActivity(intent);
								dialog.dismiss();
							}
						})
						.setNegativeButton(resources.getString(R.string.lable_cancelbtn), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.show();

			}

		});


////////////agregar comienzo

		ImageButton guardar1 = (ImageButton) findViewById(R.id.guardar1);
		guardar1.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(ContactDetails.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(resources.getString(R.string.info_addcontact))
						.setPositiveButton(resources.getString(R.string.lable_okbtn), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String firstNAme = firstName.getText().toString().toLowerCase();


								final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
								ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
										.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
										.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
										.build());


								ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName.getText().toString())
										.build());
								ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber.getText().toString())
										.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
										.build());

								ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.Email.DATA, emailAddress + "@copaco.com.py")
										.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
										.build());
								ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, "COPACO")
										.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, emailAddress.getText().toString())
										.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
										.build());

								ContentProviderResult[] result = null;
								try {
									result = ContactDetails.this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (OperationApplicationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								Intent editIntent = new Intent(Intent.ACTION_EDIT);
								contactUri = result[0].uri;
								editIntent.setDataAndType(contactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
								editIntent.putExtra("finishActivityOnSaveCompleted", true);

								startActivityForResult(editIntent, CONTACT_SAVE_INTENT_REQUEST);


								dialog.dismiss();
							}

							private int queryContactIDByName(String name) {
								return queryRawIDByData(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
							}

							private int queryContactIDByPhone(String phone) {
								return queryRawIDByData(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
							}

							private int queryContactIDByEmail(String email) {
								return queryRawIDByData(ContactsContract.CommonDataKinds.Email.DATA, email);
							}


							private int queryRawIDByData(String dataColumn, String dataValue) {
								ContentResolver resolver = ContactDetails.this.getContentResolver();

								//first find RAW_CONTENTID from DATA
								Cursor cursor = null;
								try {
									cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
											new String[]{ContactsContract.Data.RAW_CONTACT_ID},
											dataColumn + "=?",
											new String[]{dataValue},
											null);
									while (cursor.moveToNext()) {
										int rawID = cursor.getInt(0);
										return queryContactIDByRawID(Integer.toString(rawID));
									}
								} finally {
									if (cursor != null) {
										cursor.close();
									}
								}

								return -1;
							}

							private int queryContactIDByRawID(String rawID) {
								ContentResolver resolver = ContactDetails.this.getContentResolver();

								//first find RAW_CONTENTID from DATA
								Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI,
										new String[]{ContactsContract.RawContacts.CONTACT_ID},
										ContactsContract.RawContacts._ID + "=?",
										new String[]{rawID},
										null);
								while (cursor.moveToNext()) {
									int contactID = cursor.getInt(0);
									return contactID;
								}
								return -1;
							}
						})
						.setNegativeButton(resources.getString(R.string.lable_cancelbtn), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.show();

			}

		});
		///////////agregar fin


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_details, menu);
		return true;
	}

	public void deleteContact(View view)
	{
		try
		{
			PhonebookDB.deleteContacts(rowID);
			finish();
			PhonebookDB.close();

			Intent contactList = new Intent(this,ContactList.class);
			startActivity(contactList);
		}
		catch(Exception e)
		{
			Log.e("Phonebook_TAG","Got an error while deleting the contact",e);
		}
	}

	@Override
	public void onBackPressed() {

		finish();

		PhonebookDB.close();
		startActivity(new Intent(this, ContactList.class));
	}

}
