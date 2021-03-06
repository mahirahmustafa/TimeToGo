package com.example.fragmentpractice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EventInfo extends Activity {

	private String action = null;
	private String eventName = null;
	private Event event;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		Intent intent = getIntent();
		action = intent.getStringExtra("Action");
		if (action == null) {
			action = getIntent().getStringExtra("EventName");
		}
		if (!action.equalsIgnoreCase("Create")) {

			event = EventManager.getEvent(action);
			eventName = event.getName();
			String eventNotes = event.getNotes();
			String eventTime = event.getTime();
			String eventDate = event.getDate();
			String eventStartAddress = event.getStartAddressName();
			String eventEndAddress = event.getEndAddressName();
			String eventContacts = event.getContactsString();
		
			EditText eventNameEdit = (EditText) findViewById(R.id.event_name);
			EditText eventNotesEdit = (EditText) findViewById(R.id.event_notes);
			EditText eventTimeEdit = (EditText) findViewById(R.id.event_time);
			EditText eventDateEdit = (EditText) findViewById(R.id.event_date);
			EditText eventStartAddressEdit = (EditText) findViewById(R.id.start_address);
			EditText eventAddressEdit = (EditText) findViewById(R.id.event_address);
			EditText eventContactsEdit = (EditText) findViewById(R.id.event_contacts);

			eventNameEdit.setText(action);
			eventNotesEdit.setText(eventNotes);
			eventTimeEdit.setText(eventTime);
			eventDateEdit.setText(eventDate);
			eventStartAddressEdit.setText(eventStartAddress);
			eventAddressEdit.setText(eventEndAddress);
			eventContactsEdit.setText(eventContacts);
			
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_info, menu);
		return true;
	}
	
	public void selectStartAddress(View view) {
		Intent intent = new Intent(this, AddressBook.class);
		if (eventName == null) {
			// get data from text boxes
			EditText eventName = (EditText) findViewById(R.id.event_name);
			EditText eventNotes = (EditText) findViewById(R.id.event_notes);
			EditText eventTime = (EditText) findViewById(R.id.event_time);
			EditText eventDate = (EditText) findViewById(R.id.event_date);			
			EditText eventStartAddress = (EditText) findViewById(R.id.start_address);
			EditText eventAddress = (EditText) findViewById(R.id.event_address);
			EditText eventContacts = (EditText) findViewById(R.id.event_contacts);

			ArrayList<Contact> contacts = null;
			if (!eventContacts.getText().toString().equalsIgnoreCase("")
					&& !eventContacts.getText().toString().equalsIgnoreCase("none")) {
				contacts = new ArrayList<Contact>();
				String[] contactStrings = eventContacts.getText().toString()
						.split(";");
				for (int i = 0; i < contactStrings.length; ++i) {
					contacts.add(ContactList.getContact(contactStrings[i]));
				}
			}

			// make a database connection
			EventDbHelper db = new EventDbHelper(this);
			Event event = new Event(eventName.getText().toString(), eventNotes
					.getText().toString(), eventTime.getText().toString(),
					eventDate.getText().toString(),
					AddressBook.getAddress(eventStartAddress.getText().toString()),
					AddressBook.getAddress(eventAddress.getText().toString()),
					ContactList.getContacts(eventContacts.getText().toString()));
						
			
			db.addEvent(event);
			db.close();
			this.eventName = event.getName();
			EventManager.getEvents().add(event);

		}
		intent.putExtra("EditOrSelect", "select");
		intent.putExtra("StartOrEnd", "Start");
		intent.putExtra("EventName", eventName);
		startActivity(intent);
	}

	public void selectEventAddress(View view) {
		Intent intent = new Intent(this, AddressBook.class);
		if (eventName == null) {
			// get data from text boxes
			EditText eventName = (EditText) findViewById(R.id.event_name);
			EditText eventNotes = (EditText) findViewById(R.id.event_notes);
			EditText eventTime = (EditText) findViewById(R.id.event_time);
			EditText eventDate = (EditText) findViewById(R.id.event_date);			
			EditText eventStartAddress = (EditText) findViewById(R.id.start_address);
			EditText eventAddress = (EditText) findViewById(R.id.event_address);
			EditText eventContacts = (EditText) findViewById(R.id.event_contacts);

			ArrayList<Contact> contacts = null;
			if (!eventContacts.getText().toString().equalsIgnoreCase("")
					&& !eventContacts.getText().toString().equalsIgnoreCase("none")) {
				contacts = new ArrayList<Contact>();
				String[] contactStrings = eventContacts.getText().toString()
						.split(";");
				for (int i = 0; i < contactStrings.length; ++i) {
					contacts.add(ContactList.getContact(contactStrings[i]));
				}
			}

			EventDbHelper db = new EventDbHelper(this);
			Event event = new Event(eventName.getText().toString(), eventNotes
					.getText().toString(), eventTime.getText().toString(),
					eventDate.getText().toString(),
					AddressBook.getAddress(eventStartAddress.getText().toString()),
					AddressBook.getAddress(eventAddress.getText().toString()),
					ContactList.getContacts(eventContacts.getText().toString()));
			
			
			
			
			db.addEvent(event);
			db.close();
			this.eventName = event.getName();
			EventManager.getEvents().add(event);

		}
		intent.putExtra("EditOrSelect", "select");
		intent.putExtra("StartOrEnd", "End");
		intent.putExtra("EventName", eventName);
		startActivity(intent);
	}

	public void selectContacts(View view) {
		Intent intent = new Intent(this, ContactList.class);
		intent.putExtra("EditOrSelect", "select");
		intent.putExtra("EventName", eventName);
		startActivity(intent);
	}

	public void createEvent(View view) {
		// get data from text boxes
		EditText eventName = (EditText) findViewById(R.id.event_name);
		EditText eventNotes = (EditText) findViewById(R.id.event_notes);
		EditText eventTime = (EditText) findViewById(R.id.event_time);
		EditText eventDate = (EditText) findViewById(R.id.event_date);			
		EditText eventStartAddress = (EditText) findViewById(R.id.start_address);
		EditText eventAddress = (EditText) findViewById(R.id.event_address);
		EditText eventContacts = (EditText) findViewById(R.id.event_contacts);

		ArrayList<Contact> contacts = null;

		if (!eventContacts.getText().toString().equalsIgnoreCase("")
				&& !eventContacts.getText().toString().equalsIgnoreCase("none")) {
			contacts = new ArrayList<Contact>();
			String[] contactStrings = eventContacts.getText().toString()
					.split("; ");

			for (int i = 0; i < contactStrings.length; ++i) {
				contacts.add(ContactList.getContact(contactStrings[i]));
				
			}
		} else {
			
		}

		// make a database connection
		EventDbHelper db = new EventDbHelper(this);

		// create an event object with the information
		Event event = EventManager.getEvent(eventName.getText().toString());

		if (event != null) {
			int eventID = event.getID();
			event = new Event(eventID, eventName.getText().toString(),
					eventNotes.getText().toString(),
					eventTime.getText().toString(),
					eventDate.getText().toString(),
					AddressBook.getAddress(eventStartAddress.getText().toString()),
					AddressBook.getAddress(eventAddress.getText().toString()), 
					contacts);
		} else {
			event = new Event(eventName.getText().toString(),
					eventNotes.getText().toString(),
					eventTime.getText().toString(),
					eventDate.getText().toString(),
					AddressBook.getAddress(eventStartAddress.getText().toString()),
					AddressBook.getAddress(eventAddress.getText().toString()),
					contacts);
		}

		// add address to database
		if (action.equalsIgnoreCase("Create")) {
			db.addEvent(event);
		} else {
			db.deleteEvent(event);
			db.addEvent(event);

			
		}

		db.close();

		Intent intent = new Intent(this, EventManager.class);
		startActivity(intent);
	}

	public void startTrip(View view) {
		// get data from text boxes
		EditText eventName = (EditText) findViewById(R.id.event_name);
		EditText eventNotes = (EditText) findViewById(R.id.event_notes);
		EditText eventTime = (EditText) findViewById(R.id.event_time);
		EditText eventDate = (EditText) findViewById(R.id.event_date);		
		EditText eventStartAddress = (EditText) findViewById(R.id.start_address);
		EditText eventAddress = (EditText) findViewById(R.id.event_address);
		EditText eventContacts = (EditText) findViewById(R.id.event_contacts);

		Event event = new Event(eventName.getText().toString(), eventNotes
				.getText().toString(), eventTime.getText().toString(),
				eventDate.getText().toString(),
				AddressBook.getAddress(eventStartAddress.getText().toString()),
				AddressBook.getAddress(eventAddress.getText().toString()), 
				//null);
				ContactList.getContacts(eventContacts.getText().toString()));
				
		// make a database connection and add address to it
		EventDbHelper db = new EventDbHelper(this);

		if (action.equalsIgnoreCase("Create")) {
			db.addEvent(event);
		} 
	
		Intent intent = new Intent(this, EnRoute.class);
		intent.putExtra("Event Time", eventTime.getText().toString());
		intent.putExtra("Event Name", eventName.getText().toString());
		startActivity(intent);

	}

	@SuppressWarnings("deprecation")
	public void calculateNow(View view) {
		EditText eventStartAddress = (EditText) findViewById(R.id.start_address);
		EditText eventAddress = (EditText) findViewById(R.id.event_address);
		EditText eventTravelTime = (EditText) findViewById(R.id.travel_time_guess);
		
		
		// Parse the fields for the date and time from the Event.
		String[] monthInfo = event.getDate().split("/");
		String[] timeInfo = event.getTime().split(":");
		int month = Integer.parseInt(monthInfo[0]);
		int day = Integer.parseInt(monthInfo[1]);
		int year = Integer.parseInt(monthInfo[2]);
		int hour = Integer.parseInt(timeInfo[0]);
		int minute = 0;
		if (timeInfo[1].substring(timeInfo.length - 2) != null && timeInfo[1].substring(timeInfo[1].length() - 2).equalsIgnoreCase("pm")) {
			hour += 12;
			minute = Integer.parseInt(timeInfo[1].substring(0, timeInfo[1].length() - 2));
		} else {
			minute = Integer.parseInt(timeInfo[1]);
		}
		
		int tTime = 60*1000*(new Date(year, month, day, hour, minute).getMinutes()
				- new Date().getMinutes());
		
		tTime = tTime + 24*60*60*1000*(day - new Date().getDate());		
		
		// If the user has NOT explicitly entered a guess for the travel time of this event...
		float totalTime = 0;
		if(eventTravelTime.getText().toString().equalsIgnoreCase("")) {
				
			/* USE DATABASE TO GET PREVIOUS TRAVEL TIMES */
			TravelTimeDbHelper db = new TravelTimeDbHelper(this);
			ArrayList<TravelTime> travelTimes = db.getAllTravelTime(eventStartAddress.getText().toString(), eventAddress.getText().toString());
			if (travelTimes.size() != 0) {
				for (int i = 0; i < travelTimes.size(); ++i) {
					totalTime = totalTime + travelTimes.get(i).getTravelTime();
				}
				totalTime = totalTime / travelTimes.size();
			}
			

		} else { // If the user HAS explicitly entered a travel time for this event...
			/* SET ALARM BASED ON THIS TRAVEL TIME */

			totalTime = 1000*60*Integer.parseInt(eventTravelTime.getText().toString());
		}
				
		// Get a Calendar instance.
		Calendar calCalc = Calendar.getInstance();


		
		calCalc.add(Calendar.MILLISECOND, (int) (tTime-totalTime-(60*1000*UserPrefs.getAlarmVal())));

		
		// Set up a pending intent to give the AlarmManager.
		Intent alarmIntent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 12345, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		// Schedule the alarm.
		AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calCalc.getTimeInMillis(), pendingIntent);
		Toast.makeText(getBaseContext(), "Alarm Set", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, EventManager.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, EventManager.class);
		startActivity(intent);
		return;
	}
	
	public void deleteEvent(View view) {
		EventManager.getEvents().remove(event);
		EventDbHelper db = new EventDbHelper(this);
		db.deleteEvent(event);
		Intent intent = new Intent(this, EventManager.class);
		startActivity(intent);
	}

}
