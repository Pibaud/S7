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
		if (aLine == null) return null;
		aLine = aLine.trim();
		if (aLine.isEmpty()) return null;

		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(";");
		StampedCoord p = null;

		try {
			if (!scanner.hasNext()) return null;
			String xs = scanner.next().trim();
			if (!scanner.hasNext()) return null;
			String ys = scanner.next().trim();
			if (!scanner.hasNext()) return null;
			String third = scanner.next().trim(); // timestamp or label
			String label = null;
			long t;

			// try parse third token as timestamp
			try {
				t = Long.parseLong(third);
				label = scanner.hasNext() ? scanner.next().trim() : "p" + i;
			} catch (NumberFormatException nfe) {
				// third token is label, synthesize timestamp
				label = third;
				t = i * 10L; // 10 ms per point, fixed step
			}

			int xi = Integer.parseInt(xs);
			int yi = Integer.parseInt(ys);

			p = new StampedCoord(xi, yi, t);
			p.setLabel(label);
		} catch (Exception ex) {
			System.err.println("ReadWritePoint: failed to parse line " + i + " in file '" + rf.getPath() + "' -> '" + aLine + "' : " + ex.getMessage());
			p = null;
		} finally {
			scanner.close();
		}

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
