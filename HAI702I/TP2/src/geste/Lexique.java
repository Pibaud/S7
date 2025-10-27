package geste;

import java.io.File;
import java.util.ArrayList;

public class Lexique {
	private ArrayList<Geste> gestes;

	public Lexique() {
		this.gestes = new ArrayList<Geste>();
	}

	public Geste get(int currentGesture) {
		return gestes.get(currentGesture);
	}

	public void add(Geste g) {
		gestes.add(g);
	}

	public void initData(String split) {
		String name;
		File racine = new File(Parameters.defaultFolder + "/" + split + "/");

		Trace model, t;
		Geste geste;

		if (racine.exists() && racine.isDirectory()) {
			for (File dataX : racine.listFiles()) {
				if (!dataX.isDirectory()) continue;
				File rawData = new File(dataX, Parameters.rawData);
				if (!rawData.exists() || !rawData.isDirectory()) continue;
				for (File traceDir : rawData.listFiles()) {
					if (!traceDir.isDirectory()) continue;
					name = traceDir.getName();
					File premierModel = null;
					for (File f : traceDir.listFiles()) {
						if (f.getName().endsWith("-" + Parameters.baseModelName + ".csv")) {
							premierModel = f;
							break;
						}
					}
					if (premierModel == null) continue;
					model = new Trace(true, premierModel.getPath());
					geste = new Geste(name, model);
					add(geste);
					for (File trace : traceDir.listFiles()) {
						String fname = trace.getName();
						if (!fname.toLowerCase().endsWith(".csv")) continue;
						if (trace.equals(premierModel)) continue;
						t = new Trace(false, trace.getPath());
						if (t.size() > 2) geste.addTrace(t);
					}
				}
			}
		}
	}

	public int size() {
		return gestes.size();
	}

	public ArrayList<Geste> getGestes() {
		return this.gestes;
	}
}