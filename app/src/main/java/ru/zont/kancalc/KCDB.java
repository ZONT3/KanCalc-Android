package ru.zont.kancalc;

import android.annotation.SuppressLint;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


@SuppressWarnings("unused")
class KCDB {
	@SuppressLint("StaticFieldLeak")
	private static Context act;

	public static void init(Context act) {KCDB.act = act;}

	public static double getCC(Kanmusu kanmusu, String craft) throws IOException {
		double res = -1;
		System.out.println("Getting CC for "+kanmusu+"\tID:"+kanmusu.id+"\tC:"+craft);
		org.jsoup.nodes.Document inf = Jsoup.connect("http://kancolle-db.net/ship/"+kanmusu.id+".html").get();
		org.jsoup.select.Elements tr = inf.getElementsByTag("tr");
		for (int i=0; i<tr.size(); i++) {
			if (tr.get(i).getElementsByAttributeValue("class", "ship").size()>0) {
				if (tr.get(i).getElementsByAttributeValue("class", "ship").get(0).text().equals(craft)) {
					for (int j=0; j<tr.get(i).childNodeSize(); j++)
						if (tr.get(i).getElementsContainingText("%").size()>0)
							res = Double.valueOf(tr.get(i).getElementsContainingText("%").get(1).text().substring
									(0, tr.get(i).getElementsContainingText("%").get(1).text().length()-1));
				}
			} else if (craft.equals("unbuildable")) {
				res = 0;
			}
		}

		System.out.println(res+"%\n");
		return res;
	}

	static ArrayList<Kanmusu.Map> getDrops(Kanmusu kanmusu) throws IOException {
		ArrayList<Kanmusu.Map> res = new ArrayList<>();
		System.out.println("Getting Drops for "+kanmusu+"\tID:"+kanmusu.id);
		Document inf = Jsoup.connect("http://kancolle-db.net/ship/"+kanmusu.id+".html").get();
		Elements tr = inf.getElementsByTag("tr");
		for (int i=0; i<tr.size(); i++) {
			if (tr.get(i).getElementsByAttributeValue("class", "area").size()>0) {
				Kanmusu.Map drop = new Kanmusu.Map();
				drop.id = tr.get(i).getElementsByAttributeValue("class", "area").get(0).attr("id");
				drop.name = tr.get(i).getElementsByAttributeValue("class", "area").get(0).text();
				if (Core.mapPresent(drop, res) == -1)
					res.add(drop);
				Kanmusu.Map.Node node = new Kanmusu.Map.Node();
				node.name = tr.get(i).getElementsByTag("td").get(1).text();
				node.chance = -1;
				if (tr.get(i).getElementsContainingText("%").size()>0)
					node.chance = Double.valueOf(tr.get(i).getElementsContainingText("%").get(1).text().substring
							(0, tr.get(i).getElementsContainingText("%").get(1).text().length()-1));
				System.out.println("MID:"+drop.id+"\tM:\""+drop.name+"\"\tN:\""+node.name+"\"\tC:"+node.chance);
				res.get(Core.mapPresent(drop, res)).nodes.add(node);
			}
		}
		System.out.println();
		return res;
	}

	public static ArrayList<Kanmusu> getCraftDrops(String craft) throws IOException {
		ArrayList<Kanmusu> res = new ArrayList<>();
		System.out.println("Getting CraftDrops for "+craft);
		Document doc;
		doc = Jsoup.connect("http://kancolle-db.net/ship/"+craft.replace('/', '-')+".html").get();
		Elements tr = doc.getElementsByTag("tr");
		for (int i=0; i<tr.size(); i++) {
			if (tr.get(i).getElementsByAttributeValue("class", "ship").size()==1) {
				String name = tr.get(i).getElementsByAttributeValue("class", "ship").text();
				int id = Integer.valueOf(tr.get(i).getElementsByAttributeValue("class", "ship").attr("id"));
				Kanmusu kanmusu = Core.getKanmusu(id, Core.kmlist);
				if (kanmusu == null) {
					kanmusu = new Kanmusu("??");
					kanmusu.jpname = name;
					kanmusu.oname = name;
					kanmusu.name = "ID"+id;
					kanmusu.id = id;
				}
				Kanmusu.Craft ncr = new Kanmusu.Craft();
				if (tr.get(i).getElementsContainingText("%").size()>=1) {
					ncr.reciepe = craft;
					ncr.chance = Double.valueOf(tr.get(i).getElementsContainingText("%").get(1).text().substring
							(0, tr.get(i).getElementsContainingText("%").get(1).text().length()-1));
				}
				if (tr.get(i).getElementsByTag("td").size()>0)
					ncr.entries = Integer.valueOf(tr.get(i).getElementsByTag("td").get(1).text());
				System.out.println("SID:"+id+"\tSN:\""+name+"\"\tC:"+ncr.chance+"\tE:"+ncr.entries);
				kanmusu.crafts.add(ncr);
				res.add(kanmusu);
			}
		}
		System.out.println();
		return res;
	}
}
