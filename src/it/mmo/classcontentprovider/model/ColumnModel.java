package it.mmo.classcontentprovider.model;

import java.lang.reflect.Type;

public class ColumnModel implements SqlObject {
	String name;
	Type datatype;

	boolean pk = false;
	boolean ai = false;

	TableModel parent;

	@Override
	public String toSQL() {
		StringBuilder b = new StringBuilder();
		b.append(name);
		b.append(" ");
		b.append(TToS(datatype));
		b.append(" ");
		if (pk) {
			b.append("primary key");
			b.append(" ");
		}
		if (ai) {
			b.append("autoincrement");
			b.append(" ");
		}
		return b.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getDatatype() {
		return datatype;
	}

	public void setDatatype(Type datatype) {
		this.datatype = datatype;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public boolean isAi() {
		return ai;
	}

	public void setAi(boolean ai) {
		this.ai = ai;
	}

	public TableModel getParent() {
		return parent;
	}

	public void setParent(TableModel parent) {
		this.parent = parent;
	}

	private String TToS(Type t) {
		return "";
	}

}
