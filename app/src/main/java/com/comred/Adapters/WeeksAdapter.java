package com.comred.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.comred.Controls.CstmTextView;
import com.comred.hangout.R;

import java.util.ArrayList;
import java.util.List;


/*
* Adapter used to display the week horizontal bar in the calendar fragment.
* */
public class WeeksAdapter extends BaseAdapter {


    private final Context mContext;
    List<String> weeks=new ArrayList<String>();
    public WeeksAdapter(Context c,String[]  wks) {

		mContext = c;
        for(int i=0;i<wks.length;i++){
            this.weeks.add(wks[i]);
        }
	}

	public int getCount() {
		return weeks.size();
	}

	public Object getItem(int position) {
		return weeks.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	
	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        CstmTextView weekView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.week_item, null);

		}
        weekView= (CstmTextView) v.findViewById(R.id.week_cal);

        weekView.setText(weeks.get(position));

			return v;
	}





}