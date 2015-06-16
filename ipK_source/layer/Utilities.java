/*
 * Copyright (c) 2000-2001 ipKangaroo
 *
 */

package layer;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Utilities {
	
	public static Image getImage(String path) {
		return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(path));
	}
	
	public static ImageIcon getImageIcon(String path) {
	    //return new ImageIcon(ClassLoader.getSystemResource(path));
	    return new ImageIcon();
	}
	
	public static String getLongestString(Object[] a) {
		String longest = new String();
		for (int i = 0; i < a.length; i++)
			if (a[i].toString().length() > longest.length())
				longest = a[i].toString();
		return longest;
	}

	public static void sort(Comparable[] a) {
		sort(a, 0, a.length - 1);
	}

	private final static void sort(Comparable[] a, int lo0, int hi0) {
		int lo = lo0;
		int hi = hi0;
		if (lo >= hi)
			return;
		else if (lo == hi - 1) {
			if (a[lo].compareTo(a[hi]) > 0) {
				Comparable temp = a[lo];
				a[lo] = a[hi];
				a[hi] = temp;
			}
			return;
		}
	
		Comparable pivot = a[(lo + hi) / 2];
		a[(lo + hi) / 2] = a[hi];
		a[hi] = pivot;
	
		while (lo < hi) {
			while (a[lo].compareTo(pivot) <= 0  && lo < hi)
				lo++;
			while (pivot.compareTo(a[hi]) <= 0 && lo < hi )
				hi--;
			if (lo < hi) {
				Comparable temp = a[lo];
				a[lo] = a[hi];
				a[hi] = temp;
			}
		}

		a[hi0] = a[hi];
		a[hi] = pivot;

		sort(a, lo0, lo - 1);
		sort(a, hi + 1, hi0);
	}
}
