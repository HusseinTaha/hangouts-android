package com.comred.Controls;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comred.Adapters.SimpleListAdapter;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonObject;
import com.comred.hangout.R;

import java.util.List;


/*
* Combo is the spinner Custom Control
* it shows a dialog contain a filter edittext and a list of items.
* the edittext is filtrable.*/

public class Combo extends EditText implements
		View.OnClickListener {
	private EditText inputSearch;
	public String Title;
	public String Description;
	private JsonObject SelectedValue = null;
    private String hint;
	Dialog alert ;
    private Context context;

	public void SetSelectedValue(JsonObject value) {
		SelectedValue = value;
		String displaString = SelectedValue.JsonDisplayMember();
		this.setText(SelectedValue.JsonDisplayMember());
		this.setTag(SelectedValue);
		
	}
    public void SetSelectedValue(int id){
        JsonObject object= ((List<JsonObject>)DataSource).get(id);
        SetSelectedValue(object);

    }

    public void SET_HINT(String msg){
        this.hint= msg;
    }

	public JsonObject getSelectedValue() {
		return SelectedValue;
	}

	public Object DataSource = null;

	public Combo(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
		this.setClickable(true);
		this.setFocusableInTouchMode(false);
		this.setOnClickListener(this);
        this.context=context;
	}




	@Override
	public void onClick(View v) {
		if (DataSource == null)
			return;
		showDialog(DataSource);
	}

	public void DoDropDown() {
		if (DataSource == null)
			return;
		showDialog(DataSource);
	}

	private void showDialog(Object data) {
		final LinearLayout layout = new LinearLayout(this.getContext());
		
		layout.setOrientation(LinearLayout.VERTICAL);
		final ListView lv;
		final SimpleListAdapter adapter;
		lv = new ListView(this.getContext());
		inputSearch = new EditText(this.getContext());
		inputSearch.setHint(hint);
        LinearLayout.LayoutParams pp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pp.setMargins(0,0,0,20);

		inputSearch.setBackgroundResource(R.drawable.rounded_edittext);
		adapter = new SimpleListAdapter(this.getContext(),
				R.layout.simple_list, (List<Object>) data);
		lv.setAdapter(adapter);
		layout.addView(inputSearch,pp);


        int[] colors = {getResources().getColor(R.color.dg_line1), getResources().getColor(R.color.dg_line2)}; // red for the example


        lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors));
        lv.setDividerHeight(5);
        lv.setBackgroundResource(R.drawable.rounded_listview);
    	// set of on item click in the list


		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SelectedValue = (JsonObject) adapter.getItem(arg2);
				inputSearch.setText(SelectedValue.JsonDisplayMember());
				inputSearch.setTag(SelectedValue);
				if(SelectedValue!=null)
					setText(SelectedValue.JsonDisplayMember());
			//	Utility.hideKeyboard(getContext(), inputSearch);
				alert.dismiss();
			}
		});

		layout.addView(lv);
		/**
		 * Enabling Search Filter
		 * */
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

		});

		alert= new Dialog(context);
		alert.setTitle(Title);
		alert.setContentView(layout);// layout set here
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(alert.getWindow().getAttributes());
	    lp.width = Global.getwidth()*70/100;
		alert.getWindow().setAttributes(lp);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alert.show(); // show this dialog
	}

}
