package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SBookMeta extends SItemMeta implements Serializable {

	private static final long serialVersionUID = 5142127163683413370L;
	private String author;
	private List<String> pages;
	private String title;

	public SBookMeta() {
		super();
	}

	public SBookMeta(ItemStack stack) {
		super(stack);
		if(stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if(meta instanceof BookMeta) {
				BookMeta book = (BookMeta) meta;
				author = book.getAuthor();
				pages = book.getPages();
				title = book.getTitle();
			}
		}
	}

	public String getAuthor() {
		return author;
	}

	public List<String> getPages() {
		return pages;
	}

	public String getTitle() {
		return title;
	}

	public boolean hasAuthor() {
		return author != null;
	}

	public boolean hasPages() {
		return pages != null;
	}

	public boolean hasTitle() {
		return title != null;
	}


	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPages(List<String> pages) {
		this.pages = pages;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
