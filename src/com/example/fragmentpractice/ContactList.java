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

public class ContactList extends ListActivity {

	private static ArrayList<Contact> contacts = new ArrayList<Contact>();
	private static ContactList contactList = new ContactList();
	private static int nextContactID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// display contact list
		setContentView(R.layout.activity_contact_list);
		// make database connection
		ContactDbHelper db = new ContactDbHelper(this);
		// get all contacts
		contacts = db.getAllContacts();
		// display contacts or say that none exist
		if (contacts == null) {
			String[] contactStrings = new String[] { "No Contacts" };
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,
					contactStrings));
		} else {
			String[] contactStrings = new String[contacts.size()];
			for (int i = 0; i < contacts.size(); ++i) {
				contactStrings[i] = contacts.get(i).getName();
			}
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,
					contactStrings));

			// handles what happens when you click on an contact
			Intent handle = getIntent();
			String editOrSelect = handle.getStringExtra("EditOrSelect");

			if (editOrSelect.equalsIgnoreCase("edit")) {
				ListView contactList = (ListView) findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						R.layout.list_layout, contactStrings);
				contactList.setAdapter(adapter);
				contactList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(ContactList.this,
								ContactInfo.class);
						intent.putExtra("Action", ((TextView) view).getText()
								.toString());
						startActivity(intent);
					}
				});
			} else if (editOrSelect.equalsIgnoreCase("select")) {
				ListView contactList = (ListView) findViewById(android.R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_layout, contactStrings);
				contactList.setAdapter(adapter);
				contactList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent eventIntent = getIntent();
						String eventName = eventIntent.getStringExtra("EventName");
						Event event = EventManager.getEvent(eventName);
						event.addContact(ContactList
								.getContact(((TextView) view).getText()
										.toString()));
						EventDbHelper dbEvent = new EventDbHelper(ContactList.this);
						//dbEvent.updateEvent(event);
						dbEvent.deleteEvent(event);
						dbEvent.addEvent(event);
						dbEvent.close();
						Intent intent = new Intent(ContactList.this,
								EventInfo.class);
						intent.putExtra("ContactName", ((TextView) view)
								.getText().toString());
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
		// this menu says "Create Contact"
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// on clicking "Create Contact"
		// control is transfered to screen to enter contact info
		int choice = item.getItemId();
		if (choice == R.id.create_contact_button) {
			Intent intent = new Intent(this, ContactInfo.class);
			intent.putExtra("Action", "Create");
			startActivity(intent);
		} else if (choice == R.id.clear_contacts_database) {
			ContactDbHelper db = new ContactDbHelper(this);
			this.deleteDatabase(db.getName());
			contacts = db.getAllContacts();
			this.resetNextContactID();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			// return true;
		}

		return true;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public static ContactList getContactList() {
		return contactList;
	}

	public void removeContact(Contact contact) {
		contacts.remove(contact);
	}

	public void addContact(Contact contact) {
		contacts.add(contact);
	}

	public static Contact getContact(String contactName) {
		for (int i = 0; i < contacts.size(); ++i) {
			if (contacts.get(i).getName().equalsIgnoreCase(contactName)) {
				return contacts.get(i);
			}
		}
		return null;
	}

	public static ArrayList<Contact> getContacts(String string) {
		if (string != null) {
			String[] contacts = string.split(";");
			ArrayList<Contact> contactArrayList = new ArrayList<Contact>();
			if (contacts == null) {
				contactArrayList.add(ContactList.getContact(string));
				return contactArrayList;
			} else {
				for (int i = 0; i < contacts.length; ++i) {
					contactArrayList.add(ContactList.getContact(contacts[i]));
				}
				return contactArrayList;
			}
		}
		return null;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		return;
	}

	public static int getNextContactID() {
		return nextContactID++;
	}

	public void resetNextContactID() {
		nextContactID = 1;
	}

}
