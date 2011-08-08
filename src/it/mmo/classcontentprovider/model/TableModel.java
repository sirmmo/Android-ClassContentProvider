package it.mmo.classcontentprovider.model;

import java.util.LinkedList;
import java.util.List;

public class TableModel implements SqlObject {
	List<RowModel> rows = new LinkedList<RowModel>();
	String name = "";

	String all_rows_mime = "";
	String single_row_mime = "";

	@Override
	public String toSQL() {
		StringBuilder b = new StringBuilder();
		b.append("create table ");
		b.append(name);
		b.append(' ');
		b.append(" ( ");
		int rc = rows.size();
		for (RowModel r : rows) {
			rc--;
			b.append(r.toSQL());
			if (rc > 0)
				b.append(", ");
		}
		b.append(");");
		return b.toString();
	}

	public List<RowModel> getRows() {
		return rows;
	}

	public void addRow(RowModel row){
		rows.add(row);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAll_rows_mime() {
		return all_rows_mime;
	}

	public void setAll_rows_mime(String company, String contenttype) {
		this.all_rows_mime = "vnc."+company+".cursor.dir/"+contenttype;
	}

	public String getSingle_row_mime() {
		return single_row_mime;
	}

	public void setSingle_row_mime(String company, String contenttype) {
		this.single_row_mime = "vnc."+company+".cursor.item/"+contenttype;
	}
	
	public String getPK(){
		for (RowModel r : getRows()) {
			if (r.isPk())
				return r.name;
		}
		return null;
	}

}
