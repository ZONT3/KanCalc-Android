package ru.zont.kancalc;

import android.content.Context;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import ru.zont.kancalc.Kanmusu.Map;

class Core {
	private static int[] diff = new int[99];
	
	public static final String version = "Android Port v.1.3";
	
	static ArrayList<Kanmusu> kmlist = new ArrayList<>();
	static ArrayList<Kanmusu> kmlistAM = new ArrayList<>();
	
	static String[] ranks = {"S", "A", "B", "C", "D"};
	static String[] farmMaps = {"1-5", "3-2", "4-3", "5-3"};

	static void init(Context act) throws IOException, SAXException, ParserConfigurationException {
		KMParser.init(act);
		initLevels();
		kmlistAM = KMParser.getKMList();
		kmlist = getBaseList(kmlistAM);
		kmsortNid(kmlist);
		kmsortName(kmlistAM);

	}

	private static ArrayList<Kanmusu> getBaseList(ArrayList<Kanmusu> kmlistAM) {
		ArrayList<Kanmusu> res = new ArrayList<>();

		for (Kanmusu kanmusu : kmlistAM)
			if (kanmusu.isBase())
				res.add(kanmusu);

		return res;
	}


	static void kmsortNid(ArrayList<Kanmusu> list) {
        for (int i=list.size()-1; i>=0; i--) {
            for (int j=0; j<i; j++) {
                if (list.get(j).nid>list.get(j+1).nid) {
                    Kanmusu t = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, t);
                }
            }
        }
	}

	static void kmsortId(ArrayList<Kanmusu> list) {
		for (int i=list.size()-1; i>=0; i--) {
			for (int j=0; j<i; j++) {
				if (list.get(j).id>list.get(j+1).id) {
					Kanmusu t = list.get(j);
					list.set(j, list.get(j+1));
					list.set(j+1, t);
				}
			}
		}
	}
	
	static void kmsortName(ArrayList<Kanmusu> list) {
        for (int i=list.size()-1; i>=0; i--) {
            for (int j=0; j<i; j++) {
                if (list.get(j).name.compareTo(list.get(j+1).name)>0) {
                    Kanmusu t = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, t);
                }
            }
        }
	}

	static void kmsortClass(ArrayList<Kanmusu> list) {
		for (int i=list.size()-1; i>=0; i--) {
			for (int j=0; j<i; j++) {
				if (list.get(j).cls.compareTo(list.get(j+1).cls)>0) {
					Kanmusu t = list.get(j);
					list.set(j, list.get(j+1));
					list.set(j+1, t);
				}
			}
		}
	}



//	private static void checkCrafts() {
//		Ui.CCCi();
//		for (int i=0; i<kmlist.size(); i++) {
//			double ch = kmlist.get(i).getCraftchance();
//			Ui.CCCc(kmlist.size(), i, "N:\""+kmlist.get(i).name+"\" C:"+kmlist.get(i).craft+" V:"+ch);
//		}
//		Ui.CCCe();
//	}
	
	static Kanmusu getKanmusu(String name, ArrayList<Kanmusu> list) {
		// Хуйня какая-то, не робит
		// O, заробила, магия
		for (int i = 0; i<list.size(); i++)
			if (list.get(i).name.equals(name) || list.get(i).jpname.equals(name) || list.get(i).oname.equals(name))
				return list.get(i);
		return null;
	}
	
	static Kanmusu getKanmusu(int id, ArrayList<Kanmusu> list) {
		for (int i = 0; i<list.size(); i++) {
			if (list.get(i).id == id)
				return list.get(i);
		}
		return null;
	}

	private static void initLevels() {
		for (int i=0; i<51; i++)
			diff[i] = i*100;
		int c = 5200;
		for (int i=51; i<60; i++) {
			diff[i] = c;
			c+=200;
		}
		for (int i=60; i<70; i++) {
			diff[i] = c;
			c+=300;
		}
		for (int i=70; i<80; i++) {
			diff[i] = c;
			c+=400;
		}
		for (int i=80; i<90; i++) {
			diff[i] = c;
			c+=500;
		}
		diff[90] = c;
		diff[91] = 20000;
		diff[92] = 22000;
		diff[93] = 25000;
		diff[94] = 30000;
		diff[95] = 40000;
		diff[96] = 60000;
		diff[97] = 90000;
		diff[98] = 148500;
	}

	static double getSumChance(double chance, int tries) {
		chance /= 100;
		double mtch = chance;
		for (int i=0; i<tries-1; i++)
			chance = (mtch+chance-mtch*chance);
		chance*=100;
		chance = Math.rint(chance * 1000.0) / 1000.0;
		return chance;
	}

	static int getBattlesLeft(String lvls, String map, String rank) {
		double basexp = 0;
		switch (map) {
		case "3-2":
			basexp = 320;
			break;
		case "1-5":
			basexp = 150;
			break;
		case "4-3":
			basexp = 330;
			break;
		case "5-3":
			basexp = 400;
			break;
		default:
			break;
		}
		double rankmult = 1;
		switch (rank) {
		case "S":
			rankmult = 1.2;
			break;
		case "A":
			rankmult = 1;
			break;
		case "B":
			rankmult = 1;
			break;
		case "C":
			rankmult = 0.8;
			break;
		case "D":
			rankmult = 0.7;
			break;
		default:
			break;
		}
		
		int slvl;
		int llvl;
		int i = 0;
		while (true) {
			if(lvls.charAt(i) == '-')
				break;
			i++;
		}
		slvl = Integer.parseInt(lvls.substring(0, i));
		llvl = Integer.parseInt(lvls.substring(i+1));
		
		double cxp = 0;
		double rxp = 0;
		for (int ii=0; ii<slvl; ii++)
			cxp+=diff[ii];
		for (int ii=0; ii<llvl; ii++)
			rxp+=diff[ii];
		return (int) ((rxp-cxp)/(basexp*rankmult*3.0))+1;
	}

	@SuppressWarnings("unused")
	public static String getPrice(int tries, Kanmusu kanmusu) {
		if (kanmusu.craft.equals("unbuildable"))
			return kanmusu.craft;
		int i = 0;
		while (kanmusu.craft.charAt(i) != '/')
				i++;
		int fuel = Integer.parseInt(kanmusu.craft.substring(0, i));
		int ii = i+1;
		while (kanmusu.craft.charAt(ii) != '/')
			ii++;
		int ammo = Integer.parseInt(kanmusu.craft.substring(i+1, ii));
		i = ii+1;
		while (kanmusu.craft.charAt(i) != '/')
			i++;
		int steel = Integer.parseInt(kanmusu.craft.substring(ii+1, i));
		int boux = Integer.parseInt(kanmusu.craft.substring(i+1));
		return fuel*tries+"/"+ammo*tries+"/"+steel*tries+"/"+boux*tries;
	}


	static int mapPresent(Kanmusu.Map map, ArrayList<Map> list) {
		for (int i=0; i<list.size(); i++) {
			if (map.id.equals(list.get(i).id))
				return i;
		}
		return -1;
	}
	
	static class Consumption {
		int fuel = 0;
		int ammo = 0;

		@Override
		public String toString() {
			return fuel+"/"+ammo;
		}
	}

	@SuppressWarnings("unused")
	public static Consumption getConsumption(Kanmusu[] setup) {return getConsumption(setup, 1);}

	static Consumption getConsumption(Kanmusu[] setup, int bs) {
		Consumption res = new Consumption();
        for (Kanmusu aSetup : setup) {
            res.fuel += aSetup.fuel * 0.2;
            res.ammo += aSetup.ammo * 0.2;
        }
        res.fuel *=bs;
		res.ammo *=bs;
		return res;
	}
	
}
