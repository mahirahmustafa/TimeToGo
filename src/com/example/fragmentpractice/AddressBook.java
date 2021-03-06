package com.example.fragmentpractice;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddressBook extends ListActivity {

	private static ArrayList<Address> addresses = new ArrayList<Address>();
	private static AddressBook addressBook = new AddressBook();
	//Address ID for database use
	public static int nextAddressID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show address book
		setContentView(R.layout.activity_address_book);
		// make new database connection
		AddressDbHelper db = new AddressDbHelper(this);
		// get all addresses from address database
		addresses = db.getAllAdresses();
		db.close();
		// displays addresses or says that no addresses exist
		if (addresses == null) {
			String[] addressStrings = new String[] { "No Addresses" };
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,	addressStrings));

		} else {
			String[] addressStrings = new String[addresses.size()];
			for (int i = 0; i < addresses.size(); ++i) {
				addressStrings[i] = addresses.get(i).getName();
			}
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,	addressStrings));

			// handles what happens when you click on an address
			Intent handle = getIntent();
			String editOrSelect = handle.getStringExtra("EditOrSelect");
			String startOrEnd = handle.getStringExtra("StartOrEnd");
			
			if (editOrSelect.equalsIgnoreCase("edit")) {
				ListView addressBook = (ListView) findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_layout, addressStrings);
				addressBook.setAdapter(adapter);
				addressBook.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(AddressBook.this, AddressInfo.class);
						intent.putExtra("Action", ((TextView) view).getText().toString());
						startActivity(intent);
					}
				});
			} else if (editOrSelect.equalsIgnoreCase("select") && startOrEnd.equalsIgnoreCase("start")) {
				ListView addressBook = (ListView) findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_layout, addressStrings);
				addressBook.setAdapter(adapter);
				addressBook.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent eventIntent = getIntent();
						String eventName = eventIntent.getStringExtra("EventName");
						Event event = EventManager.getEvent(eventName);
						Address address = AddressBook.getAddress(((TextView) view).getText().toString());
						
						if(event != null) {
							event.editStartAddess(address);
						}
						
						Intent intent = new Intent(AddressBook.this, EventInfo.class);
						intent.putExtra("AddressName", ((TextView) view).getText().toString());
						intent.putExtra("EventName", eventName);
						startActivity(intent);
					}
				
				
				});
			} else if (editOrSelect.equalsIgnoreCase("select") && startOrEnd.equalsIgnoreCase("end")) {
				ListView addressBook = (ListView) findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_layout, addressStrings);
				addressBook.setAdapter(adapter);
				addressBook.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent eventIntent = getIntent();
						String eventName = eventIntent.getStringExtra("EventName");
						Event event = EventManager.getEvent(eventName);
						Address address = AddressBook.getAddress(((TextView) view).getText().toString());
						
						if(event != null) {
							event.editEndAddess(address);
						}
						
						Intent intent = new Intent(AddressBook.this, EventInfo.class);
						intent.putExtra("AddressName", ((TextView) view).getText().toString());
						intent.putExtra("EventName", eventName);
						startActivity(intent);
					}
				});
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		// this menu says "Add Address"
		getMenuInflater().inflate(R.menu.activity_address_book, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// on selecting "Add Address"
		// control is transfer to activity where address info can be entered
		int choice = item.getItemId();
		if (choice == R.id.create_address_button) {
			Intent intent = new Intent(this, AddressInfo.class);
			intent.putExtra("Action", "Create");
			startActivity(intent);
			// return true;
		} else if (choice == R.id.clear_addresses_database) {
			AddressDbHelper db = new AddressDbHelper(this);
			this.deleteDatabase(db.getName());
			addresses = db.getAllAdresses();
			this.resetNextAddressID();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		return true;

	}

	public static ArrayList<Address> getAddresses() {
		return addresses;
	}

	public static AddressBook getAddressBook() {
		return addressBook;
	}

	public void removeAddress(Address address) {
		addresses.remove(address);
	}

	public static Address getAddress(String addressName) {
		for (int i = 0; i < addresses.size(); ++i) {
			if (addresses.get(i).getName().equalsIgnoreCase(addressName)) {
				return addresses.get(i);
			}
		}
		return null;
	}

	public void addAddress(Address address) {
		addresses.add(address);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		return;
	}

	public static int getNextAddressID() {
		return nextAddressID++;
	}

	public void resetNextAddressID() {
		nextAddressID = 1;
	}

}
