package com.comred.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comred.hangout.R;

import java.util.List;

 

 
 
/*
* SimpleListAdapter is used to fill a combobox dialog that contains items.*/
public class SimpleListAdapter  extends ArrayAdapter<Object> {

	Context context;
	int layoutResourceId;
	List<Object> data = null;

	static class PostsHolder {
		// ImageView imgPoint;
		// ImageView imgPlus ;
		TextView txtTitle;
		Object dataRow;
	}

	public SimpleListAdapter(Context context, int layoutResourceId,
			List<Object> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PostsHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(this.layoutResourceId, parent, false);
			holder = new PostsHolder();			
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);			 
			row.setTag(holder);
		} else {
			holder = (PostsHolder) row.getTag();
		}





        Object p = (Object)  this.getItem(position);//(Object) data.get(position);
		holder.txtTitle.setText(p.toString());
		holder.dataRow=p;
		return row;
	}
	
 
 
	
 
}
