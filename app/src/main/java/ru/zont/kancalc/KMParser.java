package ru.zont.kancalc;

import android.content.Context;
import android.content.res.Resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


@SuppressWarnings("unused")
class KMParser {
	static private Resources res;

	static private NodeList kms;
	static private ArrayList<String> kmNodes;

	static private ArrayList<Kanmusu> remodels = new ArrayList<>();

	static void init(Context act) {res = act.getResources();}
	
	static ArrayList<Kanmusu> getKMList() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document kmlistFile = db.parse(res.openRawResource(R.raw.kmlist));
		Node root = kmlistFile.getDocumentElement();
		kmNodes = new ArrayList<>();
		for (int j = 0; j< root.getChildNodes().getLength(); j++)
			if (root.getChildNodes().item(j).getNodeName().equals("ship"))
				kmNodes.add(j+"");
		kms = root.getChildNodes();
		ArrayList<Kanmusu> res = new ArrayList<>();
		for (int i=0; i<kmNodes.size(); i++)
			res.add(getKM(i));
		res.addAll(remodels);
		return res;
	}

	private static Kanmusu getKM(int i) {
		Node km = kms.item(Integer.valueOf(kmNodes.get(i)));
		Element kmE = (Element) km;
		Kanmusu kanmusu = new Kanmusu(kmE.getAttribute("type"));
		kanmusu.cls = kmE.getAttribute("class");
		NodeList kmps = km.getChildNodes();
		for (int j = 0; j < kmps.getLength(); j++) {
			if (kmps.item(j).getNodeType() == Node.ELEMENT_NODE) {
				Element kmp = (Element) kmps.item(j);
				switch (kmp.getNodeName()) {
				case "id":
					kanmusu.id = Integer.valueOf(kmp.getTextContent());
					break;
				case "nid":
					kanmusu.nid = Integer.valueOf(kmp.getTextContent());
					break;
				case "name":
					kanmusu.name = kmp.getTextContent();
					break;
				case "nameJP":
					if (kmp.hasAttribute("original")) {
						kanmusu.oname = kmp.getAttribute("original");
						kanmusu.jpname = kmp.getTextContent();
					} else {
						kanmusu.jpname = kmp.getTextContent();
						kanmusu.oname = kanmusu.jpname;
					}
					break;
				case "craft":
					kanmusu.setCraft(kmp.getTextContent());
					break;
				case "stats":
					for (int k = 0; k<kmp.getChildNodes().getLength(); k++) {
						if (kmp.getChildNodes().item(k).getNodeType() == Node.ELEMENT_NODE) {
							Element kmpst = (Element) kmp.getChildNodes().item(k);
							switch (kmpst.getNodeName()) {
								case "fuel":
									kanmusu.fuel = Integer.valueOf(kmpst.getTextContent());
									break;
								case "ammo":
									kanmusu.ammo = Integer.valueOf(kmpst.getTextContent());
									break;
								case "slots":
									kanmusu.slots = new int[Integer.valueOf(kmpst.getAttribute("count"))];
									for (int l = 0; l < kmpst.getChildNodes().getLength(); l++) {
										if (kmpst.getChildNodes().item(l).getNodeType() == Node.ELEMENT_NODE) {
											Element kmsl = (Element) kmpst.getChildNodes().item(l);
											if (kmsl.getNodeName().equals("slot"))
												kanmusu.slots[Integer.valueOf(kmsl.getAttribute("id"))] = Integer.valueOf(kmsl.getTextContent());
										}
									}
									break;
								case "hp":
									kanmusu.hp.name="hp";
									kanmusu.hp.setMin(kmpst.getTextContent());
									break;
								case "fp":
									kanmusu.fp.name="fp";
									kanmusu.fp.setMax(kmpst.getTextContent());
									break;
								case "tp":
									kanmusu.tp.name="tp";
									kanmusu.tp.setMax(kmpst.getTextContent());
									break;
								case "aa":
									kanmusu.aa.name="aa";
									kanmusu.aa.setMax(kmpst.getTextContent());
									break;
								case "ar":
									kanmusu.ar.name="ar";
									kanmusu.ar.setMax(kmpst.getTextContent());
									break;
								case "eva":
									kanmusu.eva.name="eva";
									kanmusu.eva.setMax(kmpst.getTextContent());
									break;
								case "asw":
									kanmusu.asw.name="asw";
									kanmusu.asw.setMax(kmpst.getTextContent());
									break;
								case "los":
									kanmusu.los.name="los";
									kanmusu.los.setMax(kmpst.getTextContent());
									break;
								case "luk":
									kanmusu.luk.name="luk";
									kanmusu.luk.setMin(kmpst.getTextContent());
									break;
								case "rng":
									kanmusu.rng.name="rng";
									kanmusu.rng.setMin(kmpst.getTextContent());
									break;
								case "speed":
									kanmusu.speed.name="speed";
									kanmusu.speed.setMin(kmpst.getTextContent());
									break;
                                case "hpM":
                                    kanmusu.hp.name="hp";
                                    kanmusu.hp.setMax(kmpst.getTextContent());
                                    break;
                                case "fpB":
                                    kanmusu.fp.name="fp";
                                    kanmusu.fp.setMin(kmpst.getTextContent());
                                    break;
                                case "tpB":
                                    kanmusu.tp.name="tp";
                                    kanmusu.tp.setMin(kmpst.getTextContent());
                                    break;
                                case "aaB":
                                    kanmusu.aa.name="aa";
                                    kanmusu.aa.setMin(kmpst.getTextContent());
                                    break;
                                case "arB":
                                    kanmusu.ar.name="ar";
                                    kanmusu.ar.setMin(kmpst.getTextContent());
                                    break;
                                case "evaB":
                                    kanmusu.eva.name="eva";
                                    kanmusu.eva.setMin(kmpst.getTextContent());
                                    break;
                                case "aswB":
                                    kanmusu.asw.name="asw";
                                    kanmusu.asw.setMin(kmpst.getTextContent());
                                    break;
                                case "losB":
                                    kanmusu.los.name="los";
                                    kanmusu.los.setMin(kmpst.getTextContent());
                                    break;
                                case "lukM":
                                    kanmusu.luk.name="luk";
                                    kanmusu.luk.setMax(kmpst.getTextContent());
                                    break;
								default:
									break;
							}
						}
					}
					break;
				case "remodel":
					String type = kanmusu.type;
					if (kmp.hasAttribute("type"))
						type = kmp.getAttribute("type");
					
					Kanmusu remodel = new Kanmusu(type);
					remodel.name = kanmusu.name;
					remodel.jpname = kanmusu.jpname;
					remodel.oname = kanmusu.oname;
					remodel.type = kanmusu.type;
					remodel.cls = kanmusu.cls;
                    
					if (kmp.hasAttribute("type"))
						remodel.type = kmp.getAttribute("type");
					if (kmp.hasAttribute("class"))
						remodel.cls = kmp.getAttribute("class");
					
					
					NodeList rmps = kmp.getChildNodes();
					for (int k = 0; k < rmps.getLength(); k++) {
						if (rmps.item(k).getNodeType() != Node.ELEMENT_NODE)
							continue;
						Element rmp = (Element) rmps.item(k);
						
						switch (rmp.getNodeName()) {
                            case "id":
                                remodel.id = Integer.valueOf(rmp.getTextContent());
                                break;
                            case "nid":
                                remodel.nid = Integer.valueOf(rmp.getTextContent());
                                break;
                            case "name":
                                remodel.name = rmp.getTextContent();
                                break;
                            case "nameJP":
                                remodel.jpname = rmp.getTextContent();
                                remodel.oname = remodel.jpname;
                                if (rmp.hasAttribute("original"))
                                    remodel.oname = rmp.getAttribute("original");
                                break;
                            case "fuel":
                                remodel.fuel = Integer.valueOf(rmp.getTextContent());
                                break;
                            case "ammo":
                                remodel.ammo = Integer.valueOf(rmp.getTextContent());
                                break;
                            case "suffix":
                                remodel.name = remodel.name+" "+rmp.getTextContent();
                                break;
                            case "suffixJP":
                                remodel.jpname = remodel.jpname+rmp.getTextContent();
                                if (rmp.getTextContent().equals("改"))
                                    remodel.oname = remodel.oname+rmp.getTextContent();
                                else
                                    remodel.oname = remodel.oname+"・"+rmp.getTextContent();
                                break;
                            case "slots":
                                remodel.slots = new int[Integer.valueOf(rmp.getAttribute("count"))];
                                for (int l = 0; l < rmp.getChildNodes().getLength(); l++) {
                                    if (rmp.getChildNodes().item(l).getNodeType() == Node.ELEMENT_NODE) {
                                        Element kmsl = (Element) rmp.getChildNodes().item(l);
                                        if (kmsl.getNodeName().equals("slot"))
                                            remodel.slots[Integer.valueOf(kmsl.getAttribute("id"))] = Integer.valueOf(kmsl.getTextContent());
                                    }
                                }
                                break;
                            case "hp":
                                remodel.hp.name="hp";
                                remodel.hp.setMin(rmp.getTextContent());
                                break;
                            case "fp":
                                remodel.fp.name="fp";
                                remodel.fp.setMax(rmp.getTextContent());
                                break;
                            case "tp":
                                remodel.tp.name="tp";
                                remodel.tp.setMax(rmp.getTextContent());
                                break;
                            case "aa":
                                remodel.aa.name="aa";
                                remodel.aa.setMax(rmp.getTextContent());
                                break;
                            case "ar":
                                remodel.ar.name="ar";
                                remodel.ar.setMax(rmp.getTextContent());
                                break;
                            case "eva":
                                remodel.eva.name="eva";
                                remodel.eva.setMax(rmp.getTextContent());
                                break;
                            case "asw":
                                remodel.asw.name="asw";
                                remodel.asw.setMax(rmp.getTextContent());
                                break;
                            case "los":
                                remodel.los.name="los";
                                remodel.los.setMax(rmp.getTextContent());
                                break;
                            case "luk":
                                remodel.luk.name="luk";
                                remodel.luk.setMin(rmp.getTextContent());
                                break;
                            case "rng":
                                remodel.rng.name="rng";
                                remodel.rng.setMin(rmp.getTextContent());
                                break;
                            case "speed":
                                remodel.speed.name="speed";
                                remodel.speed.setMin(rmp.getTextContent());
                                break;
                            case "hpM":
                                remodel.hp.name="hp";
                                remodel.hp.setMax(rmp.getTextContent());
                                break;
                            case "fpB":
                                remodel.fp.name="fp";
                                remodel.fp.setMin(rmp.getTextContent());
                                break;
                            case "tpB":
                                remodel.tp.name="tp";
                                remodel.tp.setMin(rmp.getTextContent());
                                break;
                            case "aaB":
                                remodel.aa.name="aa";
                                remodel.aa.setMin(rmp.getTextContent());
                                break;
                            case "arB":
                                remodel.ar.name="ar";
                                remodel.ar.setMin(rmp.getTextContent());
                                break;
                            case "evaB":
                                remodel.eva.name="eva";
                                remodel.eva.setMin(rmp.getTextContent());
                                break;
                            case "aswB":
                                remodel.asw.name="asw";
                                remodel.asw.setMin(rmp.getTextContent());
                                break;
                            case "losB":
                                remodel.los.name="los";
                                remodel.los.setMin(rmp.getTextContent());
                                break;
                            case "lukM":
                                remodel.luk.name="luk";
                                remodel.luk.setMax(rmp.getTextContent());
                                break;
						default:
							break;
						}
					}

					kanmusu.remodels.add(Integer.valueOf(kmp.getAttribute("index")), remodel);
					remodel.remodels = kanmusu.remodels;
					remodels.add(remodel);
					break;
				default:
					break;
				}
			}
		}
		return kanmusu;
	}


	static String getConstTime(Kanmusu kanmusu) {
		if (kanmusu.craft.equals("unbuildable"))
			return null;
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(res.openRawResource(R.raw.ctime));
			Node root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			ArrayList<Node> ships = new ArrayList<>();
			ArrayList<Node> classes = new ArrayList<>();
			for (int i=0; i<nodes.getLength(); i++) {
				if (nodes.item(i).getNodeName().equals("ship"))
					ships.add(nodes.item(i));
				else if (nodes.item(i).getNodeName().equals("class"))
					classes.add(nodes.item(i));
			}
			
			for (int i=0; i<ships.size(); i++) {
				Node node = ships.get(i);
				if (node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element e = (Element) node;
				if (e.getAttribute("name").equals(kanmusu.name) || e.getAttribute("id").equals(kanmusu.id+""))
					return e.getTextContent();
			}
			for (int i=0; i<classes.size(); i++) {
				Node node = classes.get(i);
				if (node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element e = (Element) node;
				if (e.getAttribute("name").equals(kanmusu.cls))
					return e.getTextContent();
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {e.printStackTrace();}
		
		return null;
	}

	static String getConstTime(String name) {
		return getConstTime(Core.getKanmusu(name, Core.kmlist));
	}

	static String getConstTime(int id) {
		return getConstTime(Core.getKanmusu(id, Core.kmlist));
	}
}
