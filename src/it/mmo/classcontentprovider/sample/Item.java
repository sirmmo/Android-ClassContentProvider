package it.mmo.classcontentprovider.sample;

import it.mmo.classcontentprovider.annotations.column.Autoincrement;
import it.mmo.classcontentprovider.annotations.column.PrimaryKey;
import it.mmo.classcontentprovider.annotations.column.Column;
import it.mmo.classcontentprovider.annotations.table.MimeType;
import it.mmo.classcontentprovider.annotations.table.Table;

@Table(name="items")
@MimeType(company="it.mmo")
public class Item {

	@Column(name="name")
	String name;
	
	@Column(name="_id")
	@PrimaryKey
	@Autoincrement
	int id;
	
}
