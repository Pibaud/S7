package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import geste.StampedCoord;


/** Assumes UTF-8 encoding. JDK 7+. */
public class ReadWritePoint {
	File rf;
	ArrayList<String> textToWrite;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public ReadWritePoint(File importFile) {
		rf = importFile;
		textToWrite = new ArrayList<String>();
	}

	public ArrayList<StampedCoord> read()  {
		ArrayList<StampedCoord> points = new ArrayList<StampedCoord>();
		try (Scanner scanner = new Scanner(rf, ENCODING.name())) {
			int i = 0;
			while (scanner.hasNextLine()) {
				points.add(readLine(scanner.nextLine(), i++));
			}
			//System.out.println(points.size() + " points lus");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return points;
	}

	StampedCoord readLine(String aLine, int i) {
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(";");
		StampedCoord p = null;
		String x,y,t, label;
		
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			x = scanner.next();
			y = scanner.next();
			t = scanner.next();
			label = scanner.hasNext() ? scanner.next():"p"+i;

			p = new StampedCoord(Integer.parseInt(x), Integer.parseInt(y), Long.parseLong(t));
		}
		scanner.close();
		return p;
	}

	public void write() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(rf);
			for (String s : textToWrite) {
				pw.println(s);
				pw.flush();
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void add(String s) {
		textToWrite.add(s);
	}

}
