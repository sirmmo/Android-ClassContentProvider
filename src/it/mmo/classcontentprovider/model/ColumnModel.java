package it.mmo.classcontentprovider.model;

import it.mmo.classcontentprovider.annotations.table.Table;

import java.lang.reflect.Type;
import java.util.Date;

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
		if (fk(datatype))
			b.append(fk_declaration(datatype));
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
		if (t.equals(Integer.class)) {
			return "INTEGER";
		}
		if (t.equals(String.class)) {
			return "TEXT";
		}
		if (t.equals(Double.class) || t.equals(Float.class))
			return "DOUBLE";
		if (t.equals(Boolean.class) || t.equals(Date.class)
				|| t.equals(Number.class))
			return "NUMERIC";
		return "BLOB";
	}
	
	private boolean fk(Type t){
		return !(t.getClass().getPackage().toString().startsWith("java") || t.getClass().getPackage().toString().startsWith("android") || t.getClass().getPackage().toString().startsWith("dalvik"));
	}
	
	private String fk_declaration(Type t){
		StringBuilder b = new StringBuilder();
		b.append("REFERENCES ");
		b.append(t.getClass().getAnnotation(Table.class).name());
		return b.toString();
	}

}
