package ru.zont.kancalc;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings({"unused", "WeakerAccess"})
class Kanmusu implements Serializable {
	enum AdditionalInf {CLASS, TYPE, NONE}
	AdditionalInf ai = AdditionalInf.NONE;

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
	int level = 1;
	ArrayList<Kanmusu> remodels = new ArrayList<>();
	ArrayList<Craft> crafts = new ArrayList<>();

	Stats hp = new Stats();
	Stats fp = new Stats();
	Stats tp = new Stats();
	Stats aa = new Stats();
	Stats ar = new Stats();
	Stats eva = new Stats();
	Stats asw = new Stats();
	Stats los = new Stats();
	Stats luk = new Stats();
	Stats rng = new Stats();
	Stats speed = new Stats();

	int index;
	int nid;
	int[] ids;
	int fuel;
	int ammo;
	int[] slots;

	ArrayList<Stats> getParcingStats() {
		ArrayList<Stats> res = new ArrayList<>();
		res.add(Stats.getStat(craft, "craft"));
		res.add(Stats.getStat(fuel, "fuel"));
		res.add(Stats.getStat(ammo, "ammo"));
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

	static class Stats implements Serializable {

		String name = null;
		private String minValue = null;
		private String maxValue = null;

		public void setMin(String minValue) {
            if (!minValue.equals(""))
                this.minValue = minValue;
		}

		public void setMax(String maxValue) {
			if (!maxValue.equals(""))
				this.maxValue = maxValue;
		}

		public static Stats getStat(Object stat, String name) {
			Stats res = new Stats();
			res.name = name;
			res.minValue = stat.toString();
			return res;
		}

		@Override
		public String toString() {return maxValue!=null && !maxValue.equals("0") ? minValue+"-"+maxValue : minValue;}
	}


	int getRemodelIndex() {
		for (int i=0; i<remodels.size(); i++)
			if (remodels.get(i)==this)
				return i;
		return -1;
	}

	Kanmusu getRemodel() {
		if (getRemodelIndex()<remodels.size()-1) return remodels.get(getRemodelIndex()+1);
		return null;
	}

	boolean isBase() {return getRemodelIndex()==0;}

    Kanmusu getBase() {return remodels.get(0);}
	
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
	
	static class Map implements Serializable {
		String id;
		String name;
		ArrayList<Node> nodes = new ArrayList<>();
		
		static class Node {
			String name;
			double chance;
			int ents;

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
		switch (ai) {
			case CLASS: return jpname+" ("+name+") ["+cls+"]";
			case TYPE: return jpname+" ("+name+") ["+type+"]";
			default: return jpname+" ("+ name+")";
		}
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
