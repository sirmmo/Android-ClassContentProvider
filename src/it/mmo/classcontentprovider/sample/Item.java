package it.mmo.classcontentprovider.sample;

import it.mmo.classcontentprovider.annotations.row.Autoincrement;
import it.mmo.classcontentprovider.annotations.row.PrimaryKey;
import it.mmo.classcontentprovider.annotations.row.Row;
import it.mmo.classcontentprovider.annotations.table.MimeType;
import it.mmo.classcontentprovider.annotations.table.Table;

@Table(name="items")
@MimeType(company="it.mmo")
public class Item {

	@Row(name="name")
	String name;
	
	@Row(name="_id")
	@PrimaryKey
	@Autoincrement
	int id;
	
}
