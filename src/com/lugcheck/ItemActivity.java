package com.lugcheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemActivity extends Activity {

	SQLiteDatabase db; 
	int suitcase_id;
	int limit;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        db=openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		limit=0;
	
    	Bundle extras = getIntent().getExtras();
		suitcase_id = extras.getInt("suitcase_id"); // receiving trip_id from previous activity 
		String bah= "In Item Class, suitcase_id is " + suitcase_id;
		Log.w("SDFDS", bah);
		
		/*code below is to set the activity title to the trip_name*/
		String GET_TRIP_NAME= "select * from suitcase_table where suitcase_id = '" + suitcase_id + "'";
		Cursor c = db.rawQuery(GET_TRIP_NAME, null);
		c.moveToFirst();		
		String suitcase_name= c.getString(c.getColumnIndex("suitcase_name")); 
		setTitle("Displaying items for "+ suitcase_name );
        
		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container);
		View ruler = new View(this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler,
		new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();
		c.close();
        
    }//end onCreate


    public void createLayoutsFromDB()
	{
    	/* Code Below fetches trips from item_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from item_table where suitcase_id = '" + suitcase_id + "'", null);
		c.moveToFirst();
		while (c.isAfterLast() == false)
		{  

			TextView hw= new TextView(this);
			String text=c.getString(c.getColumnIndex("item_name")); 
			int ITEM_ID=c.getInt(c.getColumnIndex("item_id")); 
			String quant=c.getString(c.getColumnIndex("quantity"));
			hw.setText(" x " +text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			TextView qtext= new TextView(this);
			qtext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
			qtext.setText(quant);
				
			ImageView im=new ImageView(this);
			im.setImageResource(R.drawable.tshirt);
			// FROM STACKOVERFLOW!
			float d = this.getResources().getDisplayMetrics().density;
			int width = (int)(58 * d);
			int height = (int)(50 * d);
			im.setLayoutParams(new LayoutParams(width, height));
			int pad = (int)(5*d);
			im.setPadding(pad, pad, 0, 0);
			// END
			int txtPadding=(int)(20*d);
			hw.setPadding(0, txtPadding, 0, 0);


			LinearLayout newTab =new LinearLayout(this);
			newTab.setOrientation(LinearLayout.HORIZONTAL);
			newTab.addView(im);
			newTab.addView(qtext);
			newTab.addView(hw);  
			newTab.setBackgroundColor(Color.WHITE);
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container);

			tripContainer.addView(newTab);
			View ruler = new View(this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			tripContainer.addView(ruler,
			new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
			c.moveToNext();



			/*Code Below handles the delete situation*/
			final String text2=text;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
					builder.setMessage("Are you sure you want delete?")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							deleteFromDB(text2,suitcase_id);	 
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

					AlertDialog alert = builder.create();
					alert.show();
					return true;	
				}
			});


			
			/* Code below handles the situation where u click a item */
						
			newTab.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View view) {
					
					LinearLayout ll= (LinearLayout) view;
					TextView textBox = (TextView) ll.getChildAt(2);
				
					ImageView im=new ImageView(ItemActivity.this);
					im.setImageResource(R.drawable.checkmark); // creating a checkmark
					// FROM STACKOVERFLOW!
					float d = ItemActivity.this.getResources().getDisplayMetrics().density;
					int width = (int)(58 * d);
					int height = (int)(50 * d);
					im.setLayoutParams(new LayoutParams(width, height));
					int pad = (int)(5*d);
					im.setPadding(pad, pad, 0, 0);
					
									
					if ((textBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) //removes slash
					{	textBox.setPaintFlags( textBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
						ll.removeViewAt(3); // removes the checkmark icon
						
					}
					
					else  //adds the slash
						{textBox.setPaintFlags(textBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); 
						ll.addView(im);
						
						}
					
				}});
		}//end while*/
		c.close();     
	}   //end method 
    
    public void deleteFromDB(String i, int suitcase_id)
	{

		String deleteFromDB= "delete from item_table where item_name = '" + i+ "' and suitcase_id = '"+suitcase_id + "'";
		db.execSQL(deleteFromDB);	

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container); 
		LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_item); 
		tripContainer.removeAllViews();
		tripContainer.addView(addTrip);
		View ruler = new View(this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler,
		new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();	

	}
    
    
    
    public void addItem(View view) {


		if (limit==0)//checking to make sure there is no open layouts 
		{  
		limit =1;
		LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		final LinearLayout newTab = new LinearLayout(this);
		newTab.setOrientation(LinearLayout.VERTICAL);
		final EditText hw = new EditText(this); 
		hw.setHint("Enter item name");	
		final EditText quantity = new EditText(this);
		quantity.setHint("Enter Quantity");
		final Button okButton=new Button(this);
		okButton.setText("Add");
		final Button cancelButton=new Button(this);
		cancelButton.setText("Cancel");
		LinearLayout ll=new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(hw);
		ll.addView(quantity);
		ll.addView(okButton);
		ll.addView(cancelButton);

		newTab.addView(ll);
		
		View ruler = new View(this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		newTab.addView(ruler,
		new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
		
		
		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container);
		tripContainer.addView(newTab, 0,lp);
		

		//Code below is for adding a item to the database
		okButton.setOnClickListener(new View.OnClickListener() { 
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				

				LinearLayout buttonParent = (LinearLayout) okButton.getParent();
				EditText textBoxItemName = (EditText) buttonParent.getChildAt(0);//gets value of textbox 
				EditText textBoxQuantity= (EditText) buttonParent.getChildAt(1);
				
				String itemName = textBoxItemName.getText().toString();
				String quantity = textBoxQuantity.getText().toString();

				 if (quantity.equals("") && itemName.equals(""))
				{
					AlertDialog dupe = new AlertDialog.Builder(ItemActivity.this).create();
					dupe.setMessage("Please enter a valid name and quantity");
					dupe.setButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {			 
						}
					} );

					dupe.show();

				}
				
				 else if(itemName.equals("")) //if they try to add a null trip to database
				{
					AlertDialog dupe = new AlertDialog.Builder(ItemActivity.this).create();
					dupe.setMessage("You cannot enter a blank item name. Please enter a item name");
					dupe.setButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {			 
						}
					} );

					dupe.show();

				}
				
				else if (quantity.equals(""))
				{
					AlertDialog dupe = new AlertDialog.Builder(ItemActivity.this).create();
					dupe.setMessage("You cannot enter a blank quantity. Please enter a quantity");
					dupe.setButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {			 
						}
					} );

					dupe.show();

				}
				 
				 
				else if (!quantity.matches("\\d+"))
				{
					AlertDialog dupe = new AlertDialog.Builder(ItemActivity.this).create();
					dupe.setMessage("Please enter a numeric value for 'Quantity'");
					dupe.setButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {			 
						}
					} );

					dupe.show();

					
				}
				//code below checks for duplicates in database
				else {
					
					Cursor c = db.rawQuery("SELECT * from item_table where suitcase_id='"+suitcase_id+"'", null);
					c.moveToFirst();

					boolean isDupe=false;
					while (c.isAfterLast() == false)
					{  
						String text=c.getString(c.getColumnIndex("item_name")); 
						c.moveToNext();
						if(text.equals(itemName))
						{isDupe=true;
						break;
						}
					}//end while

					c.close();

					if (isDupe==false)
					{String INSERT_STATEMENT= "INSERT INTO item_table (item_name, quantity, suitcase_id) Values ('"+ itemName+ "', '" +quantity + "','"+ suitcase_id+ "')";
					db.execSQL(INSERT_STATEMENT); // insert into item_table db
					limit =0; // allow to recreate a new trip

					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container); 
					LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_item); 
					tripContainer.removeAllViews();
					tripContainer.addView(addTrip);
					View ruler = new View(ItemActivity.this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
					tripContainer.addView(ruler,
					new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
					createLayoutsFromDB();
					}

					else
					{

						AlertDialog dupe = new AlertDialog.Builder(ItemActivity.this).create();
						dupe.setTitle("Duplicate Found");
						dupe.setMessage("Item name already exists. Please use that item instead");
						dupe.setButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {			 
							}
						} );

						dupe.show();



					}//end else


				}
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				limit=0;
				LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container); 	
				LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_item); 
				tripContainer.removeAllViews();
				tripContainer.addView(addTrip);
				View ruler = new View(ItemActivity.this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
				tripContainer.addView(ruler,
				new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
				createLayoutsFromDB();

			}
		});



		}

	}//end method
    
    
/*method below determines if a string is a integer. Used for Quanity*/    
    public boolean isInteger(String s) {
    	  boolean result = false;
    	  try {
    	    Integer.parseInt("-1234");
    	    result = true;
    	  } catch (NumberFormatException nfe) {
    	    // no need to handle the exception
    	  }
    	  return result;
    	}


} //end activity
