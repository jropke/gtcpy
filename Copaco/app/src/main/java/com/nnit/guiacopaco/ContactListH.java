package com.nnit.guiacopaco;

import java.util.*;


import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnKeyListener;
import android.view.Menu;
import android.view.View;
import android.database.Cursor;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

import com.nnit.guiacopaco.ui.MenuView;

public class ContactListH extends Activity {


	private LayoutInflater inflater = null;
	private MenuView menuListView = null;
	private TextView titleTextView = null;
	public static boolean showFavorite = true;

	private EditText updateDataPackageFilenameET = null;
	private ToggleButton detailListBtn = null;
	private ToggleButton favoriteListBtn = null;


	private Resources resources = null;

	private EditText contactName;
	private Cursor CursorList;
	private ListView ContactsListView;
	private String rowID;
	int count;
	private HashMap<Integer, String> getRowID=new HashMap<Integer, String>();
	private List<HashMap<String, String>> listContact=new ArrayList<HashMap<String, String>>();
	ContactDBH PhonebookDB = new ContactDBH(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		resources = getResources();

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_contact_list_h);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main_hotel);

		inflater = getLayoutInflater();

		titleTextView = (TextView)findViewById(R.id.textview_title);

		favoriteListBtn = (ToggleButton) findViewById(R.id.imagebtn_favoritelist);

		contactName=(EditText)findViewById(R.id.search_contact);
		ContactsListView=(ListView)findViewById(R.id.lstViewContacts);



		PhonebookDB.open();
		CursorList = PhonebookDB.getAllContacts();
		count=0;
		//insertContacts(int ContactID, String Nombre, String Dep, String Ciudad, String Telefono)
		PhonebookDB.insertContacts(5,"Hotel Cecilia", "","Asunción","210365");
		PhonebookDB.insertContacts(6,"Hotel Cecilia 2", "","Asunción","210366");
		PhonebookDB.insertContacts(7,"Hotel Internacional de Asunción", "","Asunción","494113");
		PhonebookDB.insertContacts(8,"Hotel Internacional de Asunción 2", "","Asunción","494383");
		PhonebookDB.insertContacts(9,"Hotel El Lapacho", "Rca. Dominicana, 543 con Avda. España","Asunción","210662");

		if (CursorList.moveToFirst())
		{
			do
			{
				HashMap<String, String> contactDet=new HashMap<String, String>();
				String rowID=CursorList.getString(0).toString();
				String contactNombre=CursorList.getString(1).toString();
				String contactDep=CursorList.getString(2).toString();
				contactDet.put("name",contactNombre+" "+contactDep);
				listContact.add(contactDet);
				getRowID.put(count, rowID);
				count++;
			}while (CursorList.moveToNext());

		}



		String[] itemControl = {"name"};
		int[] itemLayout={R.id.name};
		listContact=sortContact(listContact);
		SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(),listContact,R.layout.list_contact_layout,itemControl,itemLayout);
		ContactsListView.setAdapter(adapter);

		//To view the contact details
		try
		{
			ContactsListView.setOnItemClickListener(new OnItemClickListener()
			{
				@SuppressWarnings("rawtypes")
				public void onItemClick(AdapterView parent, View v, int position, long id)
				{

					Intent contactDetails = new Intent(ContactListH.this, ContactDetailsH.class);
					contactDetails.putExtra("posit",getRowID.get(position));
					finish();
					startActivity(contactDetails);

				}
			});
		}
		catch(Exception e)
		{
			Log.e("Phonebook_TAG","I got an error on clicking the contact name",e);
		}

		//Will be called when we search for a contact
		try
		{
			contactName.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					SimpleAdapter adapter=searchViewAdapter(contactName.getText().toString(),CursorList);
					ContactsListView.setAdapter(null);
					ContactsListView.setAdapter(adapter);

					return false;
				}
			});
		}

		catch(Exception e)
		{
			Log.e("Phonebook_TAG","I got an error while searching",e);
		}
		PhonebookDB.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);
		return true;

	}


	//Return updated search list view adapter after search
	public SimpleAdapter searchViewAdapter(String search,Cursor crList)
	{
		count=0;
		listContact=new ArrayList<HashMap<String,String>>();
		if (crList.moveToFirst()) 
		{
			do
			{
				HashMap<String, String> contactDet=new HashMap<String, String>();
				String rowID=crList.getString(0).toString();
				String fullName=crList.getString(1).toString()+" "+crList.getString(2).toString();
				String emailAdd=crList.getString(3).toString();
				String phoneNumber=crList.getString(4).toString();

				if(fullName.toLowerCase().contains(search.toLowerCase()) && search!="")
				{
					contactDet.put("name",fullName);
					listContact.add(contactDet);
					getRowID.put(count, rowID);

					count++;
				}
				else if(phoneNumber.toLowerCase().contains(search.toLowerCase()) && search!="")
				{
					contactDet.put("name",fullName);
					listContact.add(contactDet);
					getRowID.put(count, rowID);

					count++;
				}
				else if(emailAdd.toLowerCase().contains(search.toLowerCase()) && search!="")
				{
					contactDet.put("name",fullName);
					listContact.add(contactDet);
					getRowID.put(count, rowID);

					count++;
				}
				else if(search=="")
				{
					contactDet.put("name",fullName);
					listContact.add(contactDet);
					getRowID.put(count, rowID);

					count++;
				}

			}while (crList.moveToNext());
		}

		String[] itemControl = {"name"};
		int[] itemLayout={R.id.name};
		listContact=sortContact(listContact);
		SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(),listContact,R.layout.list_contact_layout,itemControl,itemLayout);
		return adapter;
	}

	//To sort the contacts
	public List<HashMap<String, String>> sortContact(List<HashMap<String, String>> contacts)
	{
		
		List<String> lst=new ArrayList<String>();
		List<HashMap<String, String>> sortContacts=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<contacts.size();i++)
		{
			lst.add(contacts.get(i).get("name")+","+getRowID.get(i));
		}
		Collections.sort(lst);
		getRowID=new HashMap<Integer, String>();
		for(int i=0;i<lst.size();i++)
		{
			HashMap<String, String> hashContacts=new HashMap<String, String>();
			String splitData[]=lst.get(i).split(",");
			hashContacts.put("name",splitData[0]);
			sortContacts.add(hashContacts);
			getRowID.put(i, splitData[splitData.length-1]);
		}
		return sortContacts;
	}
	public void persona(View view) {
		finish();
	}
}
