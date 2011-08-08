package it.mmo.classcontentprovider.sample;

import android.net.Uri;
import it.mmo.classcontentprovider.ClassContentProvider;

public class ItemContentProvider extends ClassContentProvider {
	public static final Uri CONTENT_URI = Uri.parse("content://it.mmo.classcontentprovider.sample/items");
	public ItemContentProvider() {
		provideClass(Item.class);
	}
}
