package it.mmo.classcontentprovider.sample;

import it.mmo.classcontentprovider.ClassContentProvider;

public class ItemContentProvider extends ClassContentProvider<Item> {
	public ItemContentProvider() {
		provideClass(Item.class);
	}
}
