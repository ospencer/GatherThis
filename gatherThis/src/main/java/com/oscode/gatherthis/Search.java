package com.oscode.gatherthis;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class Search extends Fragment {

	DB openHelper;
	private SQLiteDatabase database;
	Cursor c;
	SimpleCursorAdapter mAdapter;
	final int URL_LOADER = 0;
	TextView cardname;
	TextView manacost;
	TextView manacostlabel;
	TextView cmc;
	TextView flavortext;
	TextView flavortextlabel;
	TextView types;
	TextView cardtext;
	TextView cardtextlabel;
	TextView expansions;
	TextView rarity;
	TextView artist;
	TextView pt;
	TextView ptlabel;
	private static ViewFlipper vf;
	InputMethodManager imm;
	ScrollView sv;
	ImageView cardimage;
	private boolean expansionsshown;
	private TextView expansionslabel;
	private boolean legalityshown;
	private boolean advancedmode = false;
	private TextView legalitylabel;
	private TextView legality;
	private TextView rulings;
	private boolean rulingsshown;
	private TextView rulingslabel;
	private TextView diag;
	Context searchcontext = null;
	private EditText et;
	private boolean buildingDatabase = false;
	private LinearLayout dialog;
	private Button advancedbutton;
	private ArrayList<QueryObject> advanced_search_queries = new ArrayList<QueryObject>();
	private ArrayList<QueryText> advanced_search_items = new ArrayList<QueryText>();
	float scale;
	String[] searchfields = { "Name", "Rules Text", "Expansion", "Colors",
			"Types", "Subtypes", "Converted Mana Cost", "Power", "Toughness",
			"Flavor Text", "Artist", "Rarity" };
	String[] sortoptionsStrings = { "Name", "Converted Mana Cost", "Color",
			"Power", "Toughness", "Rarity" };
	String[] sortordersStrings = { "Asc", "Desc" };
	private LinearLayout queryitems;
	private LinearLayout cardresults;
	static int lastAction = 0;
	private static SlidingUpPanelLayout supl;
	private int ITEMS_PER_PAGE = 20;
	private int numPages;
	private int cardIndex = 0;
	private int page = 1;
	private int itemsOnPage = 0;
	private Cursor advancedSearchResults;
	private Button backPage;
	private Button forwardPage;
	private TextView currentPage;
	private ScrollView resultsscrollview;
	private Spinner sortoptions;
	private Spinner sortorders;

	public Search() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		searchcontext = container.getContext();

		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(getActivity()
					.getApplicationContext().getDatabasePath("gatherer.db")
					.toString(), null, SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
		}
		if (checkDB != null ? false : true) {
			buildingDatabase = true;
			Intent mServiceIntent = new Intent(getActivity(),
					DatabaseDownloadService.class);
			getActivity().startService(mServiceIntent);
			// new GetCards(new Database(database), searchcontext).execute();
		} else {
			openHelper = new DB(getActivity(), Database.DATABASE_NAME, null,
					Database.DATABASE_VERSION);
			database = openHelper.getWritableDatabase();
		}
		return inflater.inflate(R.layout.gather, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		scale = getResources().getDisplayMetrics().density;
		IntentFilter mStatusIntentFilter = new IntentFilter(
				DatabaseDownloadService.Constants.BROADCAST_ACTION);
		// Instantiates a new DownloadStateReceiver
		ResponseReceiver mDownloadStateReceiver = new ResponseReceiver();
		// Registers the DownloadStateReceiver and its intent filters
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mDownloadStateReceiver, mStatusIntentFilter);
		vf = (ViewFlipper) getView().findViewById(R.id.viewFlipper1);
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		sv = (ScrollView) getView().findViewById(R.id.scrollView1);
		cardimage = (ImageView) getView().findViewById(R.id.cardimage);
		diag = (TextView) getView().findViewById(R.id.dialogText1);
		et = (EditText) getView().findViewById(R.id.editText1);
		final EditText advancedvalue = (EditText) getView().findViewById(
				R.id.advancedvalue);
		final ListView lv = (ListView) getView().findViewById(
				R.id.rulebookmainlist);
		resultsscrollview = (ScrollView) getView().findViewById(
				R.id.scrollView3);
		et.setSelectAllOnFocus(true);
		dialog = (LinearLayout) getView().findViewById(R.id.dialog1);
		advancedbutton = (Button) getView().findViewById(R.id.advancedbutton);
		final Spinner spinner = (Spinner) getView().findViewById(R.id.spinner1);
		sortoptions = (Spinner) getView().findViewById(R.id.sortoptions);
		ArrayAdapter<String> spinneradapter1 = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				R.layout.spinnertitle_item, sortoptionsStrings);
		spinneradapter1.setDropDownViewResource(R.layout.sortoptionlist_item);
		sortoptions.setAdapter(spinneradapter1);
		OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				queryAdvancedSearch();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		};
		sortoptions.setOnItemSelectedListener(spinnerListener);
		ArrayAdapter<String> spinneradapter2 = new ArrayAdapter<String>(
				getActivity().getApplicationContext(),
				R.layout.spinnertitle_item, sortordersStrings);
		spinneradapter2.setDropDownViewResource(R.layout.sortoptionlist_item);
		sortorders = (Spinner) getView().findViewById(R.id.sortorder);
		sortorders.setAdapter(spinneradapter2);
		sortorders.setOnItemSelectedListener(spinnerListener);
		spinner.setAdapter(new ArrayAdapter<String>(getActivity()
				.getApplicationContext(), R.layout.rulelist_item, searchfields));
		spinner.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				supl.invalidate();
				return false;
			}
		});
		queryitems = (LinearLayout) getView().findViewById(R.id.queryitems);
		cardresults = (LinearLayout) getView().findViewById(R.id.cardresults);
		final ImageView expandarrow = (ImageView) getView().findViewById(
				R.id.expandarrow);
		supl = (SlidingUpPanelLayout) getView().findViewById(R.id.slidingpanel);
		supl.setPanelHeight((int) (40 * scale + +0.5f));
		supl.setOverlayed(true);
		supl.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPanelHidden(View panel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPanelExpanded(View panel) {
				expandarrow.setImageResource(R.drawable.ic_action_expand);

			}

			@Override
			public void onPanelCollapsed(View panel) {
				expandarrow.setImageResource(R.drawable.ic_action_collapse);

			}

			@Override
			public void onPanelAnchored(View panel) {
				// TODO Auto-generated method stub

			}
		});
		supl.hidePanel();

		final Button advancedadd = (Button) getView().findViewById(
				R.id.advancedadd);
		Button advancedclear = (Button) getView().findViewById(
				R.id.advancedclear);
		Button advancedsearch = (Button) getView().findViewById(
				R.id.advancedsearch);
		currentPage = (TextView) getView().findViewById(R.id.currentPage);
		backPage = (Button) getView().findViewById(R.id.backPage);
		backPage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				previousPage();
			}
		});
		forwardPage = (Button) getView().findViewById(R.id.forwardPage);
		forwardPage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				advancePage();
			}
		});
		final RadioGroup advancedmodes = (RadioGroup) getView().findViewById(
				R.id.radioGroup1);
		if (buildingDatabase) {
			et.setHint("Search currently unavailable.");
			et.setEnabled(false);
		} else {
			dialog.setVisibility(View.GONE);
		}
		advancedvalue.setMaxLines(1);
		advancedvalue.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				advancedadd.performClick();
				return false;
			}
		});
		advancedclear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				queryitems.removeAllViews();
				advanced_search_queries.clear();
				advanced_search_items.clear();
			}
		});
		advancedsearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				advancedadd.performClick();
				queryAdvancedSearch();
				lastAction = 2;
				resultsscrollview.scrollTo(0, 0);
			}
		});
		advancedbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				supl.hidePanel();
				if (advancedmode) {
					advancedmode = false;
					vf.setDisplayedChild(0);
					advancedbutton.setText("Advanced");
				} else {
					advancedmode = true;
					vf.setDisplayedChild(2);
					advancedbutton.setText("Simple");
					advancedvalue.requestFocus();
				}

			}
		});
		advancedadd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String field = searchfields[spinner.getSelectedItemPosition()];
				int mode;
				switch (advancedmodes.getCheckedRadioButtonId()) {

				case R.id.radio0:
					mode = QueryObject.AND;
					break;
				case R.id.radio1:
					mode = QueryObject.OR;
					break;
				case R.id.radio2:
					mode = QueryObject.NOT;
					break;
				default:
					mode = QueryObject.AND;
					break;
				}
				String origvalue = advancedvalue.getText().toString().trim();
				String[] multiplevalues;
				if (field.equals("Expansion")) {
					multiplevalues = new String[1];
					multiplevalues[0] = origvalue;
				} else {
					multiplevalues = origvalue.split("\\s");
				}
				advancedvalue.setText("");
				if (!origvalue.equals(""))
					for (String value : multiplevalues) {
						boolean hadit = false;
						for (QueryObject qo : advanced_search_queries) {
							if (qo.getField().equals(field)) {
								hadit = true;
								Log.d("QueryText", "I hadit!");
								QueryObject.VM vm = qo.new VM(value, mode);
								qo.addVM(vm);
								for (QueryText qt : advanced_search_items) {
									if (qt.getField().equals(field)) {
										qt.addVM(vm);
										Log.d("QueryText", "Made a new VM!");
									}
									break;
								}
								break;
							}
						}
						if (!hadit) {
							QueryObject qo = new QueryObject(field);
							QueryObject.VM vm = qo.new VM(value, mode);
							qo.addVM(vm);
							advanced_search_queries.add(qo);
							QueryText qt = new QueryText(getActivity()
									.getApplicationContext());
							qt.setField(field);
							qt.addVM(vm);
							advanced_search_items.add(qt);
							queryitems.addView(qt);
						}
					}
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				loadCard(getMultiverseid((String) lv.getAdapter().getItem(
						position)));
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				et.setText("");
				lastAction = 0;
			}

		});
		et.setMaxLines(1);
		et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					supl.hidePanel();
					vf.setDisplayedChild(0);
					if (expansionsshown) {
						expansionslabel.performClick();
					}
					if (advancedmode) {
						advancedmode = false;
						advancedbutton.setText("Advanced");
					}
				}

			}

		});
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				System.out.println("Key event fired");
				if (!(s.length() == 0)) {
					ArrayAdapter<String> aa = new ArrayAdapter<String>(
						getActivity(), R.layout.list_item, getNames(et
								.getText().toString()));
				lv.setAdapter(aa);
				}
				else {
					ArrayAdapter<String> aa = new ArrayAdapter<String>(
							getActivity(), R.layout.list_item, new String[0]);
					lv.setAdapter(aa);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		legalityshown = false;
		legalitylabel = (TextView) getView().findViewById(R.id.legalitylabel);
		legalitylabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (legalityshown) {
					legalityshown = false;
					legality.setVisibility(View.GONE);
					legalitylabel.setText("Legality: (tap to show)");
				} else {
					legalityshown = true;
					legality.setVisibility(View.VISIBLE);
					legalitylabel.setText("Legality: (tap to hide)");
				}

			}
		});
		rulingsshown = false;
		rulingslabel = (TextView) getView().findViewById(R.id.rulingslabel);
		rulingslabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rulingsshown) {
					rulingsshown = false;
					rulings.setVisibility(View.GONE);
					rulingslabel.setText("Rulings: (tap to show)");
				} else {
					rulingsshown = true;
					rulings.setVisibility(View.VISIBLE);
					rulingslabel.setText("Rulings: (tap to hide)");
				}

			}
		});
		expansionsshown = false;
		expansionslabel = (TextView) getView().findViewById(R.id.textView7);
		expansionslabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (expansionsshown) {
					expansionsshown = false;
					expansions.setVisibility(View.GONE);
					expansionslabel.setText("Expansions: (tap to show)");
				} else {
					expansionsshown = true;
					expansions.setVisibility(View.VISIBLE);
					expansionslabel.setText("Expansions: (tap to hide)");
				}

			}
		});

		cardname = (TextView) getView().findViewById(R.id.cardname);
		manacost = (TextView) getView().findViewById(R.id.manacost);
		manacostlabel = (TextView) getView().findViewById(R.id.textView2);
		cmc = (TextView) getView().findViewById(R.id.cmc);
		types = (TextView) getView().findViewById(R.id.types);
		cardtext = (TextView) getView().findViewById(R.id.cardtext);
		cardtextlabel = (TextView) getView().findViewById(R.id.textView9);
		expansions = (TextView) getView().findViewById(R.id.expansions);
		rarity = (TextView) getView().findViewById(R.id.rarity);
		artist = (TextView) getView().findViewById(R.id.artist);
		flavortext = (TextView) getView().findViewById(R.id.flavortext);
		flavortextlabel = (TextView) getView().findViewById(R.id.textView8);
		pt = (TextView) getView().findViewById(R.id.pt);
		ptlabel = (TextView) getView().findViewById(R.id.textView10);
		legality = (TextView) getView().findViewById(R.id.legality);
		rulings = (TextView) getView().findViewById(R.id.rulings);
		super.onCreate(savedInstanceState);
	}

	public String getMultiverseid(String cn) {
		cn = cn.replaceAll("'", "''");
		String cn2 = cn.toLowerCase(Locale.US).replaceAll("ae", "æ");
		Cursor c = database
				.rawQuery(
						"SELECT * FROM gatherertb WHERE ((name LIKE '"
								+ cn
								+ "' OR name LIKE '"
								+ cn2
								+ "') AND setblock NOT LIKE 'Vanguard') ORDER BY multiverseid * 1 DESC",
						new String[] {});
		if (c.moveToFirst()) {
			return c.getString(16);
		} else
			return "-1";
	}

	public void loadCard(String cn) {
		vf.setDisplayedChild(1);
		sv.requestFocus();
		Cursor c = database.rawQuery(
				"SELECT * FROM gatherertb WHERE multiverseid='" + cn + "'",
				new String[] {});
		if (c.moveToFirst()) {
			if (c.getString(1).equals("n")) {
				manacost.setVisibility(View.GONE);
				manacostlabel.setVisibility(View.GONE);
			} else {
				manacost.setVisibility(View.VISIBLE);
				// manacostlabel.setVisibility(View.VISIBLE);
			}
			if (c.getString(9).equals("n")) {
				cardtext.setVisibility(View.GONE);
				cardtextlabel.setVisibility(View.GONE);
			} else {
				cardtext.setVisibility(View.VISIBLE);
				// cardtextlabel.setVisibility(View.VISIBLE);
			}
			if (c.getString(13).equals("n")) {
				pt.setVisibility(View.GONE);
				ptlabel.setVisibility(View.GONE);
			} else {
				pt.setVisibility(View.VISIBLE);
				// ptlabel.setVisibility(View.VISIBLE);
			}
			if (c.getString(10).equals("n")) {
				flavortext.setVisibility(View.GONE);
				flavortextlabel.setVisibility(View.GONE);
			} else {
				flavortext.setVisibility(View.VISIBLE);
				// flavortextlabel.setVisibility(View.VISIBLE);
			}
			if (c.getString(19).equals("n")) {
				rulings.setVisibility(View.GONE);
				rulingslabel.setVisibility(View.GONE);
			} else {
				// rulings.setVisibility(View.VISIBLE);
				rulingslabel.setVisibility(View.VISIBLE);
			}
			if (c.getString(20).equals("n")) {
				legality.setVisibility(View.GONE);
				legalitylabel.setVisibility(View.GONE);
			} else {
				// legality.setVisibility(View.VISIBLE);
				legalitylabel.setVisibility(View.VISIBLE);
			}
			cardname.setText(c.getString(0));
			manacost.setText(Html.fromHtml(
					c.getString(1).replace("{", "<img src=\"{")
							.replace("}", "}\"/>"), new ImageGetter(100,
							getActivity()), null));
			cmc.setText(c.getString(2));
			types.setText(c.getString(4));
			cardtext.setText(Html.fromHtml(
					c.getString(9).replace("{", "<img src=\"{")
							.replace("}", "}\"/>").replace("(", "(<i>")
							.replace(")", ")</i>").replace("\n", "<br>"),
					new ImageGetter(60, getActivity()), null));
			pt.setText(c.getString(13) + "/" + c.getString(14));
			flavortext.setText(c.getString(10));
			expansions.setText(c.getString(21));
			rarity.setText(c.getString(8));
			artist.setText(c.getString(11));
			rulings.setText(Html.fromHtml(c.getString(19)/*
														 * .replaceAll("''",
														 * "'").replaceAll("’",
														 * "'").replaceAll("�.",
														 * "\"")
														 */));
			legality.setText(Html.fromHtml(c.getString(20)/*
														 * .replaceAll("''",
														 * "'").replaceAll("’",
														 * "'").replaceAll("�.",
														 * "\"")
														 */));
			Log.d("GatherThis", c.getString(19) + "what do???");
			new DownloadImageTask(cardimage)
//					.execute("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid="
//							+ c.getString(16) + "&type=card");
                    .execute("http://magiccards.info/scans/en/"
                            + c.getString(16) + "&type=card");
			Log.d("DnlImgTask",
					"http://mtgimage.com/multiverseid/" + c.getString(16)
							+ ".jpg");
			// while (true) {
			// if (!c.moveToNext()) {
			// break;
			// }
			// expansions.setText(expansions.getText() + ", "
			// + c.getString(18));
			// }

		}

	}

	public ArrayList<String> getNames(String s) {
		// s = s.replaceAll("�", "ae").replaceAll("�", "�_").replaceAll("'",
		// "''''");
		String s2 = s.toLowerCase(Locale.US).replaceAll("ae", "æ");
		Cursor c = database.rawQuery(
				"SELECT DISTINCT name FROM gatherertb WHERE (name LIKE '" + s
						+ "%' OR name LIKE '" + s2 + "%') LIMIT 12",
				new String[] {});
		ArrayList<String> a = new ArrayList<String>();
		Log.d("GatherThis", c.getCount() + "");

		if (c.moveToFirst()) {
			while (true) {

				a.add(c.getString(0)//
				// .replaceAll("''", "'"));
				);
				System.out.println("In while loop");
				if (!c.moveToNext()) {
					break;
				}
			}
		}
		Collections.sort(a);
		System.out.println(a);
		return a;
	}

	public void queryAdvancedSearch() {
		if (!advanced_search_queries.isEmpty()) {
			cardresults.removeAllViews();
			ArrayList<String> selectionArgs = new ArrayList<String>();
			String queryWhere = "(";
			boolean firstarg = true;
			for (QueryObject qo : advanced_search_queries) {
				if (firstarg) {
					firstarg = false;
				} else {
					queryWhere += " AND ";
				}
				String field = null;
				switch (qo.getField()) {
				case "Name":
					field = Database.NAME;
					break;
				case "Rules Text":
					field = Database.TEXT;
					break;
				case "Expansion":
					field = Database.SET;
					break;
				case "Colors":
					field = Database.COLORS;
					break;
				case "Types":
					field = Database.TYPES;
					break;
				case "Subtypes":
					field = Database.SUBTYPES;
					break;
				case "Converted Mana Cost":
					field = Database.CONVERTED_MANA_COST;
					break;
				case "Power":
					field = Database.POWER;
					break;
				case "Toughness":
					field = Database.TOUGHNESS;
					break;
				case "Flavor Text":
					field = Database.FLAVOR;
					break;
				case "Artist":
					field = Database.ARTIST;
					break;
				case "Rarity":
					field = Database.RARITY;
					break;
				}
				boolean first = true;
				for (QueryObject.VM vm : qo.getVms()) {
					if (vm.mode == QueryObject.OR) {
						if (first) {
							queryWhere += "(";
							first = false;
							queryWhere += field;
							queryWhere += " LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						} else {
							queryWhere += " OR " + field + " LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						}
					}
				}
				if (!first)
					queryWhere += ")";
				for (QueryObject.VM vm : qo.getVms()) {
					if (vm.mode == QueryObject.AND) {
						if (first) {
							first = false;
							queryWhere += field;
							queryWhere += " LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						} else {
							queryWhere += " AND " + field + " LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						}
					}
				}
				for (QueryObject.VM vm : qo.getVms()) {
					if (vm.mode == QueryObject.NOT) {
						if (first) {
							first = false;
							queryWhere += field;
							queryWhere += " NOT LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						} else {
							queryWhere += " AND " + field + " NOT LIKE ?";
							selectionArgs.add("%" + vm.value + "%");
						}
					}
				}
			}
			queryWhere += ") GROUP BY name";
			String order;
			switch (sortoptions.getSelectedItemPosition()) {
			case 0:
				order = Database.NAME;
				break;
			case 1:
				order = Database.CONVERTED_MANA_COST;
				break;
			case 2:
				order = Database.COLORS;
				break;
			case 3:
				order = Database.POWER;
				break;
			case 4:
				order = Database.TOUGHNESS;
				break;
			case 5:
				order = "CASE WHEN " + Database.RARITY + "='Common' THEN 1 "
						+ "WHEN " + Database.RARITY + "='Uncommon' THEN 2 "
						+ "WHEN " + Database.RARITY + "='Rare' THEN 3 "
						+ "WHEN " + Database.RARITY
						+ "='Mythic Rare' THEN 4 END";
				break;
			default:
				order = "";
			}
			String direction;
			switch (sortorders.getSelectedItemPosition()) {
			case 1:
				direction = " DESC";
				break;
			case 0:
			default:
				direction = "";
			}
			queryWhere += " ORDER BY " + order + direction;
			vf.setDisplayedChild(3);
			supl.showPanel();
			String[] sArgs = new String[1];
			Log.d("WHERE Query", queryWhere);
			Log.d("WHERE Query", selectionArgs.toString());
			advancedSearchResults = database.rawQuery(
					"SELECT * FROM gatherertb WHERE " + queryWhere,
					selectionArgs.toArray(sArgs));
			numPages = (int) Math.ceil(((float) advancedSearchResults
					.getCount()) / ITEMS_PER_PAGE);
			if (advancedSearchResults.moveToFirst()) {
				cardIndex = 0;
				page = 1;
				if (numPages == 1)
					forwardPage.setEnabled(false);
				else
					forwardPage.setEnabled(true);
				loadCardResults();
			}
		}
	}

	public void loadCardResults() {
		itemsOnPage = 0;
		currentPage.setText("Page " + page + " of " + numPages);
		for (; cardIndex < ITEMS_PER_PAGE * page; cardIndex++) {
			itemsOnPage++;
			CardResult cr = new CardResult(getActivity()
					.getApplicationContext());
			cr.setMultiverseid(advancedSearchResults.getString(16));
			if (!advancedSearchResults.getString(1).equals("n"))
				cr.setName(Html.fromHtml(
						advancedSearchResults.getString(0)
								+ " "
								+ advancedSearchResults.getString(1)
										.replace("{", "<img src=\"{")
										.replace("}", "}\"/>"),
						new ImageGetter(70, getActivity()), null));
			else
				cr.setName(Html.fromHtml(advancedSearchResults.getString(0)));
			if (advancedSearchResults.getString(13).equals("n")) {
				cr.setTypes(advancedSearchResults.getString(4));
			} else {
				cr.setTypes(advancedSearchResults.getString(4) + " ("
						+ advancedSearchResults.getString(13) + "/"
						+ advancedSearchResults.getString(14) + ")");
			}
			if (!advancedSearchResults.getString(9).equals("n")) {
				cr.setRulesText(Html.fromHtml(
						advancedSearchResults.getString(9)
								.replace("{", "<img src=\"{")
								.replace("}", "}\"/>").replace("(", "(<i>")
								.replace(")", ")</i>").replace("\n", "<br>"),
						new ImageGetter(50, getActivity()), null));
			} else {
				cr.noRulesText();
			}
			cardresults.addView(cr);
			if (!advancedSearchResults.moveToNext())
				break;
		}
	}

	public void advancePage() {
		if (page < numPages) {
			cardresults.removeAllViews();
			page++;
			loadCardResults();
			if (page == numPages)
				forwardPage.setEnabled(false);
			if (page > 1)
				backPage.setEnabled(true);
			currentPage.setText("Page " + page + " of " + numPages);
			resultsscrollview.scrollTo(0, 0);
		}
	}

	public void previousPage() {
		if (page > 1) {
			cardresults.removeAllViews();
			if (page == numPages) {
				cardIndex -= (ITEMS_PER_PAGE + itemsOnPage - 1);
				advancedSearchResults.moveToPosition(cardIndex);
			} else {
				cardIndex -= ITEMS_PER_PAGE * 2;
				advancedSearchResults.moveToPosition(cardIndex);
			}
			page--;
			loadCardResults();
			if (page == 1)
				backPage.setEnabled(false);
			if (page < numPages)
				forwardPage.setEnabled(true);
			currentPage.setText("Page " + page + " of " + numPages);
			resultsscrollview.scrollTo(0, 0);
		}
	}

	class QueryText extends LinearLayout {

		int qCount = 0;
		TextView field = new TextView(getActivity().getApplicationContext());
		String fieldName = null;

		public QueryText(Context context) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);
			setBackgroundResource(R.drawable.shadow);
			int dpAsPixels = (int) (5 * scale + 0.5f);
			setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
			field.setTextColor(getResources().getColor(R.color.black));
			field.setTextSize((int) (7 * scale + 0.5f));
			LayoutParams qtLayoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			qtLayoutParams.setMargins(0, dpAsPixels, 0, 0);
			setLayoutParams(qtLayoutParams);
			addView(field);
		}

		public void setField(String f) {
			field.setText(f);
			fieldName = f;
		}

		public String getField() {
			return fieldName;
		}

		public void addVM(QueryObject.VM vm) {
			addView(new VMObject(getActivity().getApplicationContext(), vm));
			qCount++;
		}

		class VMObject extends LinearLayout {

			QueryObject.VM valuemode;

			public VMObject(Context context, final QueryObject.VM valuemode) {
				super(context);
				this.valuemode = valuemode;
				setOrientation(LinearLayout.HORIZONTAL);
				Button x = new Button(context);
				x.setText("x");
				x.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d("VMObject", "I got touched.");
						for (QueryObject qo : advanced_search_queries) {
							if (qo.getField().equals(fieldName)) {
								ArrayList<QueryObject.VM> qovms = qo.getVms();
								for (int n = 0; n < qo.getVms().size(); n++) {
									QueryObject.VM vm = qovms.get(n);
									if (valuemode.equals(vm)) {
										Log.d("VMObject", "It equaled!");
										qovms.remove(vm);
										remove();
										break;
									}
								}
								break;
							}
						}

					}
				});
				addView(x);
				TextView value = new TextView(context);

				if (valuemode.mode == QueryObject.AND) {
					value.setText("AND contains \"" + valuemode.value + "\"");
				}
				if (valuemode.mode == QueryObject.OR) {
					value.setText("OR contains \"" + valuemode.value + "\"");
				}
				if (valuemode.mode == QueryObject.NOT) {
					value.setText("Does NOT contain \"" + valuemode.value
							+ "\"");
				}
				value.setTextColor(getResources().getColor(R.color.black));
				value.setTextSize((int) (5 * scale + 0.5f));
				value.setPadding((int) (5 * scale + 0.5f), 0, 0, 0);
				addView(value);
			}

			public void remove() {
				for (QueryText qt : advanced_search_items) {
					if (qt.getField().equals(fieldName)) {
						Log.d("VMObject", "It equaled again!");
						qCount--;
						qt.removeView(this);
						if (qCount == 0) {
							if (advanced_search_items.remove(qt))
								Log.d("Remove", "Successful delete!");
							queryitems.removeView(qt);

							for (QueryObject qo : advanced_search_queries) {
								if (qo.getVms().isEmpty()) {
									Log.d("Remove", "VMS empty!");
									advanced_search_queries.remove(qo);
									Log.d("Remove",
											advanced_search_queries.size() + "");
									Log.d("Remove", "size of views "
											+ advanced_search_items.size() + "");
									break;
								}
							}
						}
						break;
					}

				}
			}

		}
	}

	class CardResult extends LinearLayout {
		String multiverseid = null;
		TextView name;
		TextView types;
		TextView rulestext;

		public CardResult(Context context) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);
			int dpAsPixels = (int) (15 * scale + 0.5f);
			int dpAsPixels2 = (int) (10 * scale + 0.5f);
			setBackgroundResource(R.drawable.shadow);
			LayoutParams crLayoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			crLayoutParams.setMargins(0, dpAsPixels2, 0, 0);
			setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
			setLayoutParams(crLayoutParams);
			name = new TextView(context);
			types = new TextView(context);
			rulestext = new TextView(context);
			int black = getResources().getColor(R.color.black);
			name.setTextColor(black);
			types.setTextColor(black);
			rulestext.setTextColor(black);
			name.setTextSize((int) (6 * scale + 0.5f));
			LayoutParams rtLayoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			rtLayoutParams.gravity = Gravity.TOP;
			rulestext.setLayoutParams(rtLayoutParams);
			addView(name);
			addView(types);
			addView(rulestext);
			setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loadCard(multiverseid);
					lastAction = 3;
					supl.hidePanel();
				}
			});
		}

		public void setName(Spanned spanned) {
			name.setText(spanned);
		}

		public void setTypes(String t) {
			types.setText(t);
		}

		public void setRulesText(Spanned spanned) {
			rulestext.setText(spanned);
		}

		public void setMultiverseid(String m) {
			multiverseid = m;
		}

		public void noRulesText() {
			rulestext.setVisibility(View.GONE);
		}
	}

	public static void backPressed() {
		switch (vf.getDisplayedChild()) {
		case 1:
			vf.setDisplayedChild(lastAction);
			if (lastAction == 3)
				supl.showPanel();
			break;
		case 3:
			vf.setDisplayedChild(2);
			supl.hidePanel();
			break;
		default:

		}
	}

	private class ResponseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String setName = intent
					.getStringExtra(DatabaseDownloadService.Constants.EXTENDED_DATA_STATUS);
			if (!setName.equals("doneDownloadingSets")) {
				if (getActivity() != null) 
					getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						diag.setText("Archiving Gatherer database...\n" + setName);
					}

				});
			} else {
				et.setHint("Search...");
				et.setEnabled(true);
				dialog.setVisibility(View.GONE);
			}
		}

	}

}
