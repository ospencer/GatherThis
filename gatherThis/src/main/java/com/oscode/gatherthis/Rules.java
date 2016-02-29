package com.oscode.gatherthis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Rules extends Fragment {

	private static ViewFlipper vf;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		final ListView main = (ListView) getView().findViewById(
				R.id.rulebookmainlist);
		final ListView sub = (ListView) getView().findViewById(
				R.id.rulebooksublist);
		final ListView glossary = (ListView) getView().findViewById(
				R.id.glossary);
		vf = (ViewFlipper) getView().findViewById(R.id.ruleflipper);
		final TextView rbt = (TextView) getView().findViewById(
				R.id.rulebooktext);
		final TextView rsn = (TextView) getView()
				.findViewById(R.id.rulesetname);
		final ScrollView numba3 = (ScrollView) getView().findViewById(
				R.id.scrollView3);

		String[] contents = getResources().getStringArray(R.array.contents);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),
				R.layout.rulelist_item, contents);
		System.out.println(aa.getCount() + " items in adapter");
		main.setAdapter(aa);
		final OnItemClickListener subListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String setName = (String) sub.getAdapter().getItem(position);
				String itemName = setName.replaceAll("\\d+.\\s", "")
						.replaceAll("[\\s\','/\\-]+", "_").toLowerCase();
				if (itemName.equals("general")) {
					itemName += position + 1;
				}
				System.out.println(itemName);
				rbt.setText(Html
						.fromHtml(arrayToString(getStringArrayResourceByName(itemName))));
				rsn.setText(setName);
				vf.showNext();
				numba3.fullScroll(ScrollView.FOCUS_UP);
			}
		};
		main.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemName = (String) main.getAdapter().getItem(position);
				if (!itemName.equals("Glossary")) {
					itemName = itemName.replaceAll("[^a-zA-Z\',']{2,}", "")
							.replaceAll("[\\s\','/\\-]+", "_").toLowerCase();
					if (itemName.equals("general")) {
						itemName += position;
					}
					if (itemName.equals("introduction")) {
						sub.setOnItemClickListener(null);
					} else {
						sub.setOnItemClickListener(subListener);
					}
					System.out.println(itemName);
					sub.setAdapter(new ArrayAdapter<String>(getActivity(),
							R.layout.glossarylist_item,
							getStringArrayResourceByName(itemName)));
					vf.showNext();
					// numba3.fullScroll(ScrollView.FOCUS_UP);
					sub.setSelection(0);
				} else {
					String[] defs = getResources().getStringArray(
							R.array.glossary);
					Spanned[] spans = new Spanned[defs.length];
					for (int n = 0; n < defs.length; n++) {
						spans[n] = Html.fromHtml(defs[n]);
					}
					FastAdapter fa = new FastAdapter((Context) getActivity(),
							R.layout.list_item, spans);

					glossary.setAdapter(fa);
					glossary.setFastScrollEnabled(true);
					vf.setDisplayedChild(3);

				}
			}
		});

		sub.setOnItemClickListener(subListener);
		super.onActivityCreated(savedInstanceState);
	}

	public class FastAdapter extends ArrayAdapter<CharSequence> implements
			SectionIndexer {

		private String sections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public FastAdapter(Context context, int resource, CharSequence[] objects) {

			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object[] getSections() {
			String[] secs = new String[sections.length()];
			for (int n = 0; n < sections.length(); n++) {
				secs[n] = sections.charAt(n) + "";
			}
			return secs;
		}

		@Override
		public int getPositionForSection(int section) {
			for (int n = 0; n < this.getCount(); n++) {
				if (this.getItem(n).charAt(0) == sections.charAt(section)) {
					return n;
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.rulebook, container, false);
	}

	private String[] getStringArrayResourceByName(String aString) {
		String packageName = getActivity().getPackageName();
		int resId = getResources().getIdentifier(aString, "array", packageName);
		return getResources().getStringArray(resId);
	}

	private String arrayToString(String[] a) {
		String string = "";
		for (String s : a) {
			string += s;
		}
		return string;
	}

	public static void backPressed() {
		switch (vf.getDisplayedChild()) {
		case 1:
		case 2:
			vf.showPrevious();
			break;
		case 3:
			vf.setDisplayedChild(0);
			break;
		}
	}
}
