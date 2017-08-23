package ru.zont.kancalc;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings({"unused", "WeakerAccess"})
class Kanmusu implements Serializable {
	int id = -1;
	String type = "?";
	String name;
	String jpname;
	String oname;
	String cls;
	String craft = "unbuildable";
	double chance;
	private double craftchance = -1;
	boolean gotDrops = false;
	ArrayList<Map> drops = new ArrayList<>();
	int minlevel = 0;
	ArrayList<Kanmusu> remodels = new ArrayList<>();
	ArrayList<Craft> crafts = new ArrayList<>();
	String image = null;

	int hp;
	int fp;
	int tp;
	int aa;
	int ar;
	int eva;
	int asw;
	int los;
	int luk;
	int rng;
	int speed;

	int index;
	int nid;
	int[] ids;
	int fuel;
	int ammo;
	int[] slots;

	ArrayList<Object> getParcingStats() {
		ArrayList<Object> res = new ArrayList<>();
		res.add(name);
		res.add(jpname);
		res.add(craft);
		res.add(fuel);
		res.add(ammo);
		res.add(hp);
		res.add(fp);
		res.add(tp);
		res.add(aa);
		res.add(ar);
		res.add(eva);
		res.add(asw);
		res.add(los);
		res.add(luk);
		res.add(rng);
		res.add(speed);
		return res;
	}

	private int getRemodelIndex() {
		for (int i=0; i<remodels.size(); i++)
			if (remodels.get(i)==this)
				return i;
		return -1;
	}

	boolean isBase() {return getRemodelIndex()==0;}
	
	Kanmusu(String type) {
		this.type = type;
		this.name = "?";
		this.jpname = "?";
		this.oname = "?";
		remodels.add(0, this);
	}
	
	Kanmusu(String name, int id, int nid) {
		this.name = name;
		this.jpname = "?";
		this.oname = "?";
		this.id = id;
		this.nid = nid;
		remodels.add(0, this);
	}
	
	static class Map {
		String id;
		String name;
		ArrayList<Node> nodes = new ArrayList<>();
		
		static class Node {
			String name;
			double chance;

			@Override
			public String toString() {return name;}
		}

		@Override
		public String toString() {return name;}
	}
	
	static class Craft {
		String reciepe = "0/0/0/0";
		double chance = 0;
		int entries = -1;
	}

	@Override
	public String toString() {
		return jpname+" ("+name+")";
	}

	void setCraft(String craft) {
		this.craft = craft;
	}
	
	public Craft getCraft(String craft) {
		for (int i=0; i<crafts.size(); i++)
			if (craft.equals(crafts.get(i).reciepe))
				return crafts.get(i);
		return new Craft();
	}

//	public double getCraftchance() {
//		if (craftchance >= 0)
//			return craftchance;
//		if (craft == null) {
//			craftchance = 0;
//			return 0;
//		}
//		craftchance = KCDB.getCC(this, craft);
//		return craftchance;
//	}
//
//	public ArrayList<String> getMaps() throws IOException {
//		if (!gotDrops) {
//			drops = KCDB.getDrops(this);
//			gotDrops = true;
//		}
//		ArrayList<String> res = new ArrayList<>();
//		for (int i=0; i<drops.size(); i++) {
//			String map = drops.get(i).name;
//			boolean was = false;
//			for (int j=0; j<res.size(); j++)
//				if (res.get(j).equals(map))
//					was = true;
//			if (!was)
//				res.add(map);
//		}
//		return res;
//	}

	public ArrayList<String> getNodes(String map) {
		ArrayList<String> res = new ArrayList<>();
		for (int i=0; i<drops.size(); i++)
			if (drops.get(i).name.equals(map))
				for (int j=0; j<drops.get(i).nodes.size(); j++)
					res.add(drops.get(i).nodes.get(j).name);
		return res;
	}

	public double getDropChance(String map, String node) {
		for (int i=0; i<drops.size(); i++)
			for (int j=0; j<drops.get(i).nodes.size(); j++)
				if (drops.get(i).nodes.get(j).name.equals(node) && drops.get(i).name.equals(map))
					return drops.get(i).nodes.get(j).chance;
		return -1;
	}
}
