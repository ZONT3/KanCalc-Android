package ru.zont.kancalc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


@SuppressWarnings("unused")
class KCDB {

	private static org.jsoup.nodes.Document inf;
	private static String craft;
	private static Kanmusu kanmusu;

	static double getCC(Kanmusu kanmusu, String craft) throws IOException {
		KCDB.craft = craft;
		KCDB.kanmusu = kanmusu;
		double res = -1;
		System.out.println("Getting CC for "+kanmusu+"\tID:"+kanmusu.id+"\tC:"+craft);
		inf = Jsoup.connect("http://kancolle-db.net/ship/"+kanmusu.id+".html").get();
		org.jsoup.select.Elements tr = inf.getElementsByTag("tr");
		for (int i=0; i<tr.size(); i++) {
			if (tr.get(i).getElementsByAttributeValue("class", "ship").size()>0) {
				if (tr.get(i).getElementsByAttributeValue("class", "ship").get(0).text().equals(craft))
					for (int j=0; j<tr.get(i).childNodeSize(); j++)
						if (tr.get(i).getElementsContainingText("%").size()>0)
							res = Double.valueOf(tr.get(i).getElementsContainingText("%").get(1).text().substring
									(0, tr.get(i).getElementsContainingText("%").get(1).text().length()-1));
			} else if (craft.equals("unbuildable")) {
				return 0;
			}
		}

		System.out.println(res+"%\n");
		return res;
	}

	static int getCraftEnts() {
		int res = -1;
		System.out.println("Getting CE for "+kanmusu+"\tID:"+kanmusu.id+"\tC:"+craft);
		if (inf==null) return -1;
		org.jsoup.select.Elements tr = inf.getElementsByTag("tr");
		for (int i=0; i<tr.size(); i++) {
			if (tr.get(i).getElementsByAttributeValue("class", "ship").size()>0) {
				if (tr.get(i).getElementsByAttributeValue("class", "ship").get(0).text().equals(craft))
					res = Integer.valueOf(tr.get(i).getElementsByTag("td").get(1).text());
			} else if (craft.equals("unbuildable")) {
				return 0;
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
				node.ents = Integer.valueOf(tr.get(i).getElementsByTag("td").get(2).text());
				System.out.println("MID:"+drop.id+"\tM:\""+drop.name+"\"\tN:\""+node.name+"\"\tC:"+node.chance+"\tE:"+node.ents);
				res.get(Core.mapPresent(drop, res)).nodes.add(node);
			}
		}
		System.out.println();
		return res;
	}
}
