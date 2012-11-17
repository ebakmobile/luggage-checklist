package com.lugcheck;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuggestedItemActivity extends ExpandableListActivity {
	
	private float density;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_suggested_item);
      //  TextView topMessage= (TextView)findViewById(R.id.enter_item_text);
        
        setListAdapter(new ExampleAdapter(this)); 
        density = this.getResources().getDisplayMetrics().density;
    }
    
    private class ExampleAdapter extends BaseExpandableListAdapter {
        private Context context;
    	public ExampleAdapter(Context context) {
    		this.context = context;
    	}
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}
		

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LinearLayout linear = new LinearLayout(this.context);
			
			
			if(groupPosition == 0) {
				CheckBox checkbox = new CheckBox(this.context);	
				TextView txtView=new TextView(this.context);
				txtView.setText("Belt");
				int txtPadding = (int) (20 * density);
				txtView.setPadding(0, txtPadding, 0, 10);
				txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				linear.addView(checkbox);
				linear.addView(txtView);
										
			}
			if(groupPosition == 1) {
				
				CheckBox checkbox = new CheckBox(this.context);	
				TextView txtView=new TextView(this.context);
				txtView.setText("Gloves");
				int txtPadding = (int) (20 * density);
				txtView.setPadding(0, txtPadding, 0, 10);
				txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				linear.addView(checkbox);
				linear.addView(txtView);
				
						
			}
			return linear;
		}
		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}
		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}
		@Override
		public int getGroupCount() {
			return 2;
		}
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LinearLayout linear = new LinearLayout(this.context);
			
			if(groupPosition == 0) {
				TextView txtView = new TextView(this.context);
				txtView.setText("Clothing:");
				txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
				ImageView im = new ImageView(this.context);
				im.setImageResource(R.drawable.white);
				// FROM STACKOVERFLOW!
				int width = (int) (35 * density);
				int height = (int) (50 * density);
				im.setLayoutParams(new LayoutParams(width, height));
				int pad = (int) (5 * density);
				im.setPadding(pad, pad, 0, 0);
				int txtPadding = (int) (20 * density);
				txtView.setPadding(0, txtPadding, 0, 10);
				linear.addView(im);
				linear.addView(txtView);
							
			}
			if(groupPosition == 1) {
				TextView txtView = new TextView(this.context);
				txtView.setText("Clothing for Cold Weather");
				txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
				ImageView im = new ImageView(this.context);
				im.setImageResource(R.drawable.white);
				// FROM STACKOVERFLOW!
				int width = (int) (35 * density);
				int height = (int) (50 * density);
				im.setLayoutParams(new LayoutParams(width, height));
				int pad = (int) (5 * density);
				im.setPadding(pad, pad, 0, 0);
				int txtPadding = (int) (20 * density);
				txtView.setPadding(0, txtPadding, 0, 10);
				linear.addView(im);
				linear.addView(txtView);
			}

			return linear;
		}
		
		
		
		
		@Override
		public boolean hasStableIds() {
			return false;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, 
				                         int childPosition) {
			return true;
		}
    }
}