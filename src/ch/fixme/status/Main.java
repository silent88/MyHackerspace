package ch.fixme.status;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class Main extends Activity {

	private static final int DIALOG_LOADING = 0;
	private static final String API_KEY = "apiurl";
	private static final String API_DEFAULT = "https://fixme.ch/cgi-bin/spaceapi.py";
	public static final String API_NAME = "space";
	public static final String API_STATUS = "open";
	public static final String API_STATUS_TXT = "status";
	public static final String API_ICON = "icon";
	public static final String API_ICON_OPEN = "open";
	public static final String API_ICON_CLOSED = "closed";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String apiUrl = PreferenceManager
				.getDefaultSharedPreferences(Main.this).getString(API_KEY,
						API_DEFAULT);
		new GetApiTask().execute(apiUrl);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		switch (id) {
		case DIALOG_LOADING:
			dialog = new ProgressDialog(this);
			dialog.setCancelable(false);
			dialog.setMessage("Loading...");
			((ProgressDialog) dialog).setIndeterminate(true);
			break;
		}
		return dialog;
	}

	private class GetApiTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_LOADING);
		}

		@Override
		protected String doInBackground(String... url) {
			return Net.get(url[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject api = new JSONObject(result);
				((TextView) findViewById(R.id.name)).setText(api
						.getString(API_NAME));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dismissDialog(DIALOG_LOADING);
		}

	}

}