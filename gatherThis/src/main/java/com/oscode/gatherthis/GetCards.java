package com.oscode.gatherthis;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import com.oscode.gatherthis.Database.Card;

class GetCards extends AsyncTask<Void, Void, Void> {

		
		Database database;
		Context context = null;

		public GetCards(Database database, Context c) {
			super();
			this.database = database;
			context = c;
		}

		@Override
		protected Void doInBackground(Void... params) {
			FileOutputStream outputStream = null;
			JSONArray blocks = null;
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh
					.makeServiceCall("http://mtgjson.com/json/SetCodes.json",
							ServiceHandler.GET);
			if (jsonStr != null) {
				try {
					outputStream = context.openFileOutput("sets", Context.MODE_PRIVATE);
					blocks = new JSONArray(jsonStr);
					for (int j = 0; j < blocks.length(); j++) {
						jsonStr = sh.makeServiceCall("http://mtgjson.com/json/"
								+ blocks.getString(j) + "-x.json",
								ServiceHandler.GET);
						if (jsonStr != null) {

							JSONObject json = new JSONObject(jsonStr);
							this.database.set = json.getString("name");// .replaceAll("�[^a-z].",
																		// "�");
//							getActivity().runOnUiThread(new Runnable() {
//
//								@Override
//								public void run() {
//									diag.setText("Archiving Gatherer database...\n"
//											+ GetCards.this.database.set);
//								}
//
//							});
							Intent localIntent =
						            new Intent(DatabaseDownloadService.Constants.BROADCAST_ACTION)
						            // Puts the status into the Intent
						            .putExtra(DatabaseDownloadService.Constants.EXTENDED_DATA_STATUS, this.database.set);
						    // Broadcasts the Intent to receivers in this app.
						    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
							this.database.mastercardlist = json
									.getJSONArray(Database.TAG_CARDS);

							for (int n = 0; n < this.database.mastercardlist
									.length(); n++) {
								JSONObject o = this.database.mastercardlist
										.getJSONObject(n);
								Card c = this.database.new Card();
								if (o.has(Database.NAME)) {
									c.name = o.getString(Database.NAME);
								}
								if (o.has(Database.MANA_COST)) {
									c.manacost = o
											.getString(Database.MANA_COST);
								}
								if (o.has(Database.CONVERTED_MANA_COST)) {
									c.cmc = o
											.getInt(Database.CONVERTED_MANA_COST);
								}
								if (o.has(Database.COLORS)) {
									c.colors = o.getJSONArray(Database.COLORS)
											.toString();

								}
								if (o.has(Database.TYPE)) {
									c.type = o.getString(Database.TYPE);

								}
								if (o.has(Database.SUPERTYPES)) {
									c.supertypes = o.getJSONArray(
											Database.SUPERTYPES).toString();

								}
								if (o.has(Database.TYPES)) {
									c.types = o.getJSONArray(Database.TYPES)
											.toString();

								}
								if (o.has(Database.SUBTYPES)) {
									c.subtypes = o.getJSONArray(
											Database.SUBTYPES).toString();

								}
								if (o.has(Database.RARITY)) {
									c.rarity = o.getString(Database.RARITY);

								}
								if (o.has(Database.TEXT)) {
									c.text = o.getString(Database.TEXT);

								}
								if (o.has(Database.FLAVOR)) {
									c.flavor = o.getString(Database.FLAVOR);

								}
								if (o.has(Database.ARTIST)) {
									c.artist = o.getString(Database.ARTIST);

								}
								if (o.has(Database.NUMBER)) {
									c.number = o.getString(Database.NUMBER);
								}
								if (o.has(Database.POWER)) {
									c.power = o.getString(Database.POWER);
								}
								if (o.has(Database.TOUGHNESS)) {
									c.toughness = o
											.getString(Database.TOUGHNESS);
								}
								if (o.has(Database.LAYOUT)) {
									c.layout = o.getString(Database.LAYOUT);

								}
								if (o.has(Database.MULTIVERSE_ID)) {
									c.multiverseid = o
											.getInt(Database.MULTIVERSE_ID);
								}
								if (o.has(Database.IMAGE_NAME)) {
									c.imageName = o
											.getString(Database.IMAGE_NAME);

								}
								if (o.has(Database.RULINGS)) {
									JSONArray rules = o
											.getJSONArray(Database.RULINGS);
									String rulestext = "";
									for (int m = 0; m < rules.length(); m++) {
										JSONObject obj = rules.getJSONObject(m);
										String daterule = "";
										if (obj.has("date")) {
											daterule = "<b>"
													+ obj.getString("date")
													+ "</b> ";
										}
										if (obj.has("text")) {
											daterule += obj.getString("text")
													+ "<br>";
										}
										rulestext += daterule;
									}

									c.rulings = Html.fromHtml(
											escapeHtml4(rulestext)).toString();
								}
								if (o.has(Database.LEGALITIES)) {
									JSONObject legals = o
											.getJSONObject(Database.LEGALITIES);
									String legality = "";
									Iterator<?> keys = legals.keys();
									while (keys.hasNext()) {
										String temp = (String) keys.next();
										if (legals.has(temp)) {
											legality += legals.getString(temp)
													+ " in " + temp + "<br>";
										}
									}
									c.legality = Html.fromHtml(
											escapeHtml4(legality)).toString();
								}
								
								if (o.has(Database.PRINTINGS)) {
									JSONArray prints = o
											.getJSONArray(Database.PRINTINGS);
									String printings = "";
									for (int m = 0; m < prints.length(); m++) {
										if (m > 0) printings += ", ";
										printings += prints.getString(m);
									}
									c.printings = printings;
								}
								c.save();
							}
						}

					}
					outputStream.write((this.database.set + "\n").getBytes());

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent localIntent =
		            new Intent(DatabaseDownloadService.Constants.BROADCAST_ACTION)
		            // Puts the status into the Intent
		            .putExtra(DatabaseDownloadService.Constants.EXTENDED_DATA_STATUS, "doneDownloadingSets");
		    // Broadcasts the Intent to receivers in this app.
		    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
			return null;
		}

//		@Override
//		protected void onPostExecute(Void result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			et.setHint("Search...");
//			et.setEnabled(true);
//			dialog.setVisibility(View.GONE);
//		}

	}