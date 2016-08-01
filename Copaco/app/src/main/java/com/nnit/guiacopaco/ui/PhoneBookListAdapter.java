package com.nnit.guiacopaco.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.nnit.guiacopaco.MainActivity;
import com.nnit.guiacopaco.R;
import com.nnit.guiacopaco.data.FavoriteManager;
import com.nnit.guiacopaco.data.PhoneBookItem;












import com.nnit.guiacopaco.data.PhotoManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneBookListAdapter extends BaseAdapter{
	private List<PhoneBookItem> pbItems = null;
	private LayoutInflater mInflater = null;
	private Context context = null;
	
	public PhoneBookListAdapter(Context c, List<PhoneBookItem> items){
		this.context = c;
		this.pbItems = items;
		mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return pbItems == null?0: pbItems.size();
	}

	@Override
	public Object getItem(int position) {
		return pbItems == null? null : pbItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhoneBookItem pb = pbItems.get(position);
		final Resources resources = context.getResources();
		
		if (MainActivity.isDetailList){
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.listitem_detaillist, parent, false);								
			}
			TextView tv1 = (TextView)convertView.findViewById(R.id.detailList_Initials);
			//nombre y departamento que se muestra en la lista
			tv1.setText(pb == null? null:  pb.getTitle());
			TextView tv2 = (TextView)convertView.findViewById(R.id.detailList_Name);
			tv2.setText(pb == null? null: pb.getLocalName() + "," + pb.getDepartmentNo() + " , " + pb.getPhone());
			ImageView iv = (ImageView)convertView.findViewById(R.id.detailList_Photo);
			
			ImageButton removeFavoriteBtn = (ImageButton)convertView.findViewById(R.id.detaillist_removefromfavorite);
			if(MainActivity.showFavorite){
				removeFavoriteBtn.setVisibility(View.VISIBLE);
			}else{
				removeFavoriteBtn.setVisibility(View.GONE);
			}
			
			final Context context = convertView.getContext();
			final String targetInitials = pb.getInitials();
			final ViewGroup vg = parent;
			removeFavoriteBtn.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View arg0) {
					Dialog dialog = new AlertDialog.Builder(context)
		        	.setIcon(R.drawable.ic_launcher)
		        	.setTitle(resources.getString(R.string.info_removefavorite))
		        	.setPositiveButton(resources.getString(R.string.lable_okbtn),new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(!FavoriteManager.getInstance().removeFromFavoriteList(targetInitials)){
								Toast.makeText(context, resources.getString(R.string.error_remove_favorite), Toast.LENGTH_SHORT).show();
							}
							for(int i = 0; i<pbItems.size(); i++){
								PhoneBookItem pbi = pbItems.get(i);
								if(pbi.getInitials().equalsIgnoreCase(targetInitials)){
									pbItems.remove(i);
									break;
								}
							}
							PhoneBookListAdapter.this.notifyDataSetChanged();
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
			
			FileInputStream fis = null;
			String initials = (pb == null ? null: pb.getInitials().toLowerCase());
			
			try{
				String photoFilename = PhotoManager.getInstance().getPhotoFilenameByInitials(initials);
				if(photoFilename == null){
					iv.setImageResource(R.drawable.photo);
				}
				File f = new File(photoFilename);
			
				if(f.exists() && f.isFile()){
					fis = new FileInputStream(f);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					iv.setImageBitmap(bitmap);
				}else{
					iv.setImageResource(R.drawable.photo); 
				}			
				
			}catch(Exception exp){
				iv.setImageResource(R.drawable.photo); 
			}finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
			
//			int id = context.getResources().getIdentifier(initials, "drawable", "com.nnit.phonebook");
//			if(id == 0){
//				iv.setImageResource(R.drawable.photo); 
//			}else{
//				iv.setImageResource(id);
//			}
			
			
		}else{
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.listitem_brieflist, parent, false);
			}
			TextView tv = (TextView)convertView.findViewById(R.id.briefList_Initial);
			tv.setText(pb == null? null: pb.getInitials() + "(" + pb.getName() +")");
		}
		
		return convertView;
	}

}
