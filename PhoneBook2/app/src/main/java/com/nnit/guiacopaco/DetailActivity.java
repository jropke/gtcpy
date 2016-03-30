package com.nnit.guiacopaco;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.nnit.guiacopaco.data.FavoriteManager;
import com.nnit.guiacopaco.data.PhoneBookItem;
import com.nnit.guiacopaco.data.PhotoManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity{
	
	public static final String TARGET_INITIALS = "com.nnit.phonebook.TARGET_INITIALS";
	private static final int CONTACT_SAVE_INTENT_REQUEST = 1;
	
	private PhoneBookItem pbItem = null;
	
	private Uri contactUri = null;
	
	private Resources resources = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		resources = getResources();

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_detail_fusion);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_detail);

		pbItem = (PhoneBookItem) getIntent().getSerializableExtra(MainActivity.SELECTED_PBITEM);

		
		TextView localnameTV = (TextView) findViewById(R.id.detail_localname);
		localnameTV.setText(pbItem.getLocalName());

		TextView phoneTV = (TextView) findViewById(R.id.detail_phone);
		phoneTV.setText(pbItem.getPhone());
		
		TextView titleTV = (TextView) findViewById(R.id.detail_title);
		titleTV.setText(pbItem.getTitle());
		
		TextView depNoTV = (TextView) findViewById(R.id.detail_departmentNo);
		depNoTV.setText(pbItem.getDepartmentNo());
		
		TextView depTV = (TextView) findViewById(R.id.detail_department);
		depTV.setText(pbItem.getDepartment());
		
		TextView managerTV = (TextView) findViewById(R.id.detail_manager);
		managerTV.setText(pbItem.getManager());
		
		ImageButton closeBtn = (ImageButton) findViewById(R.id.imagebtn_closedetail);
		closeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}

		});
		
		ImageButton callBtn = (ImageButton) findViewById(R.id.imagebtn_call);
		callBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(DetailActivity.this)
		        	.setIcon(R.drawable.ic_launcher)
		        	.setTitle(resources.getString(R.string.info_call))
		        	.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
						    intent.setAction("android.intent.action.DIAL");
						    intent.setData(Uri.parse("tel:"+pbItem.getPhone()));
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

		ImageView callBtn2 = (ImageView) findViewById(R.id.imagebtn_call2);
		callBtn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(DetailActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(resources.getString(R.string.info_call))
						.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.DIAL");
								intent.setData(Uri.parse("tel:"+pbItem.getPhone()));
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
		
		//ImageButton smsBtn = (ImageButton) findViewById(R.id.imagebtn_sms);
		//smsBtn.setOnClickListener(new OnClickListener(){

		//	@Override
		//	public void onClick(View arg0) {
		//		Dialog dialog = new AlertDialog.Builder(DetailActivity.this)
		//        	.setIcon(R.drawable.ic_launcher)
		 //       	.setTitle(resources.getString(R.string.info_sms))
		 //       	.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
		//				@Override
		//				public void onClick(DialogInterface dialog, int which) {
		//					Uri uri = Uri.parse("smsto:" + pbItem.getPhone());
		//					Intent intent = new Intent(Intent.ACTION_SENDTO,uri);

		//					String enName = pbItem.getName();
		//					StringTokenizer st =  new StringTokenizer(enName, " ");
		//					if(st.hasMoreTokens()){
		//						enName = st.nextToken();
		//					}

		//					intent.putExtra("sms_body", "Hola," + enName + ",");

		//					startActivity(intent);
		//				}
		//			})
		//        	.setNegativeButton(resources.getString(R.string.lable_cancelbtn), new DialogInterface.OnClickListener() {

		//				@Override
		//				public void onClick(DialogInterface dialog, int which) {
		//					dialog.dismiss();
		//				}
		//			})
		//	        	.show();

		//		}
			
	//	});
		
		ImageButton contactBtn = (ImageButton) findViewById(R.id.imagebtn_newcontact);
		contactBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog dialog = new AlertDialog.Builder(DetailActivity.this)
		        	.setIcon(R.drawable.ic_launcher)
		        	.setTitle(resources.getString(R.string.info_addcontact))
		        	.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String initials = pbItem.getInitials().toLowerCase();

							
							final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
							ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
									.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
									.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
									.build());
							Bitmap bitmap = getPhotoBitmap(initials);
							if(bitmap!=null){
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								bitmap.compress(CompressFormat.JPEG, 100, baos);
								byte[] bytes = baos.toByteArray();

								ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
										.withValueBackReference(Data.RAW_CONTACT_ID, 0)
										.withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
										.withValue(Photo.PHOTO, bytes)
										.build());
							}
							
							ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
									.withValueBackReference(Data.RAW_CONTACT_ID, 0)
									.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
									.withValue(StructuredName.GIVEN_NAME, pbItem.getTitle())
									.build());
							ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
									.withValueBackReference(Data.RAW_CONTACT_ID, 0)
									.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
									.withValue(Phone.NUMBER, pbItem.getPhone())
									.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
									.build());
						
							ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
									.withValueBackReference(Data.RAW_CONTACT_ID, 0)
									.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
									.withValue(Email.DATA, initials + "@copaco.com.py")
									.withValue(Email.TYPE, Email.TYPE_WORK)
									.build());
							ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
									.withValueBackReference(Data.RAW_CONTACT_ID, 0)
									.withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
									.withValue(Organization.COMPANY, "COPACO")
									.withValue(Organization.TITLE, pbItem.getTitle())
									.withValue(Organization.TYPE, Organization.TYPE_WORK)
									.build());
							
							ContentProviderResult[] result = null;
							try {
								result = DetailActivity.this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OperationApplicationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Intent editIntent = new Intent(Intent.ACTION_EDIT);
							contactUri = result[0].uri;
							editIntent.setDataAndType(contactUri, Contacts.CONTENT_ITEM_TYPE);
							editIntent.putExtra("finishActivityOnSaveCompleted", true);
							
							startActivityForResult(editIntent, CONTACT_SAVE_INTENT_REQUEST);
							
							/*
							Intent intent = new Intent("android.intent.action.INSERT", ContactsContract.Contacts.CONTENT_URI);
						    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, initials +"@nnit.com");
						    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "NNIT");
						    intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, pbItem.getTitle());
						    intent.putExtra(ContactsContract.Intents.Insert.NAME, pbItem.getLocalName());
						    intent.putExtra(ContactsContract.Intents.Insert.PHONE, pbItem.getPhone());
						    
						    //prepare photo
						    FileInputStream fis = null;
						    ByteArrayOutputStream baos = new ByteArrayOutputStream();
						    try{
								String photoFilename = PhotoManager.getInstance().getPhotoFilenameByInitials(initials);
								if(photoFilename != null){
									File f = new File(photoFilename);
									if(f.exists() && f.isFile()){
										fis = new FileInputStream(f);
										Bitmap bitmap = BitmapFactory.decodeStream(fis);
										bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos);
										
										
										ArrayList<ContentValues> data = new ArrayList<ContentValues>();
										ContentValues row = new ContentValues();
										row.put(ContactsContract.CommonDataKinds.Photo.IS_SUPER_PRIMARY, 1);
										row.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
										row.put(ContactsContract.CommonDataKinds.Photo.PHOTO, baos.toByteArray());
										data.add(row);
										
										intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);
									}
								}
							}catch(Exception exp){
								exp.printStackTrace();
							}finally{
								if(fis != null){
									try {
										fis.close();
									} catch (IOException e) {
									}
								}
								if(baos != null){
									try{
										baos.close();
									} catch (IOException e){
									}
								}
							}
						   
						    //startActivity(intent);
						    startActivityForResult(intent,CONTACT_SAVE_INTENT_REQUEST);
						    */
						    dialog.dismiss();
						}

						private int queryContactIDByName(String name) {
							return queryRawIDByData(StructuredName.GIVEN_NAME, name);
						}
						
						private int queryContactIDByPhone(String phone) {
							return queryRawIDByData(Phone.NUMBER, phone);
						}
						
						private int queryContactIDByEmail(String email) {
							return queryRawIDByData(Email.DATA, email);
						}
						
						
						private int queryRawIDByData(String dataColumn, String dataValue) {
							ContentResolver resolver = DetailActivity.this.getContentResolver();
							
							//first find RAW_CONTENTID from DATA
							Cursor cursor = null;
							try{
								cursor = resolver.query(Data.CONTENT_URI, 
										new String[]{Data.RAW_CONTACT_ID},
										dataColumn + "=?",
										new String[]{dataValue},
										null);
								while(cursor.moveToNext()){
									int rawID = cursor.getInt(0);
									return queryContactIDByRawID(Integer.toString(rawID));
								}
							}finally{
								if(cursor!=null){
									cursor.close();
								}
							}
							
							return -1;
						}
						private int queryContactIDByRawID(String rawID){
							ContentResolver resolver = DetailActivity.this.getContentResolver();
							
							//first find RAW_CONTENTID from DATA
							Cursor cursor = resolver.query(RawContacts.CONTENT_URI, 
									new String[]{ RawContacts.CONTACT_ID },
									RawContacts._ID + "=?",
									new String[]{rawID},
									null);
							while(cursor.moveToNext()){
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
		
		

		
		ImageButton addFavoriteBtn = (ImageButton) findViewById(R.id.imagebtn_addfavorite);
		addFavoriteBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				if(FavoriteManager.getInstance().isInFavoriteList(pbItem.getInitials())){
					Toast.makeText(DetailActivity.this, resources.getString(R.string.error_already_in_favoritelist), Toast.LENGTH_SHORT).show();
				}else{
				
					Dialog dialog = new AlertDialog.Builder(DetailActivity.this)
		        	.setIcon(R.drawable.ic_launcher)
		        	.setTitle(resources.getString(R.string.info_addfavorite))
		        	.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(!FavoriteManager.getInstance().addToFavoriteList(pbItem.getInitials())){
								Toast.makeText(DetailActivity.this, resources.getString(R.string.error_save_favorite), Toast.LENGTH_SHORT).show();
							}
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
				
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    switch (requestCode) {
		    case CONTACT_SAVE_INTENT_REQUEST:
		        if (resultCode != RESULT_OK) {
		        	ContentResolver cr = getContentResolver();
		        	cr.delete(contactUri, null, null);
		        	
		        	/*
		        	Uri rawContactUri = intent.getData();
		        	long rawContactId = ContentUris.parseId(rawContactUri);
		        	
		        	Bitmap bitmap = getPhotoBitmap(pbItem.getInitials().toLowerCase());
		        	if(bitmap != null){
		        		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		        		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		        		ContentValues values = new ContentValues();
		        		values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
		        		values.put(ContactsContract.CommonDataKinds.Photo.IS_SUPER_PRIMARY, 1);
		        		
		        		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bytes.toByteArray());
		        		values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE );
		        		getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.Data.RAW_CONTACT_ID + " = " + rawContactId, null);

		        		Toast.makeText(this, "Hahahahaha", Toast.LENGTH_LONG).show();
		        	}
		        	*/
		        }
		        break;
		    }
	}
	
	

	
	
	private Bitmap getPhotoBitmap(String initials){
		FileInputStream fis = null;
		try{
			String photoFilename = PhotoManager.getInstance().getPhotoFilenameByInitials(initials);
			if(photoFilename != null){
				File f = new File(photoFilename);
		
				if(f.exists() && f.isFile()){
					fis = new FileInputStream(f);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					return bitmap;
				}			
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	


}
