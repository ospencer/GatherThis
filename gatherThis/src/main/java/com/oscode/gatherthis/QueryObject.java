package com.oscode.gatherthis;

import java.util.ArrayList;

public class QueryObject {
	public static final int AND = 0;
	public static final int OR = 1;
	public static final int NOT = 2;
	private String field;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public ArrayList<VM> getVms() {
		return vms;
	}
	private ArrayList<VM> vms;
	public void addVM(VM vm) {
		vms.add(vm);
	}
	public QueryObject(String f) {
		vms = new ArrayList<VM>();
		field = f;
	}
	public class VM {
		public String value;
		public int mode;
		public VM(String v, int m) {
			value = v;
			mode = m;
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof VM) {
				if (((VM)o).value.equals(value) && ((VM)o).mode == mode) {
					return true;
				}
			}
			return false;
		}
		
		
	}
}
