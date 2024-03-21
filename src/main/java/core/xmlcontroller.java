/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author keita
 */
public class xmlcontroller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            xmlcontroller xml = new xmlcontroller();

            SQL_OPE sql = xml.getSQL();
            String[][] rot_type = xml.getRotDefinition();
            log.info(rot_type.length);
            for (int i = 0; i < rot_type.length; i++) {
                String[] val2 = rot_type[i];
                log.info(Arrays.toString(val2));
            }

//            log.info(Arrays.toString(xml.getAttachmentType()));
            AttachmentInspection2 ains = new AttachmentInspection2(sql, rot_type);

            InspectionData idatas[] = xml.getInspectionDatas();

            //<editor-fold defaultstate="collapsed" desc="処理プログラム">
            for (int i = 0; i < idatas.length; i++) {
                log.info("Input Inspection at DAY:" + idatas[i].getDay());
                ains.Inspection(idatas[i]);
            }

            if (idatas.length >= 1) {
                log.info("Creating Inspection Report at DAY:" + idatas[idatas.length - 1].getDay());
                ains.OutputOneInspectionResult(
                        xml.getAttachmentType(),
                        idatas[idatas.length - 1].getDay(),
                        "output/");
                log.info("Creating Cumulative Inspection Report at DAY:" + idatas[idatas.length - 1].getDay());

            }

            //すべての金物生産情報docファイルを出力します。
            ains.OoutputAllInspectionResult(
                    xml.getAttachmentType(),
                    "output/allresult.doc"
            );
//</editor-fold>

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }
    private final DocumentBuilderFactory dbf;
    private static final Logger log = LogManager.getLogger(xmlcontroller.class);

    public xmlcontroller() {

        dbf = DocumentBuilderFactory.newInstance();

    }

    public InspectionData[] getInspectionDatas() throws SAXException, IOException, ParserConfigurationException {

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("Inspection.xml");

        // ルート要素を取得
        Element root = doc.getDocumentElement();

        Element[] elem = this.getElements(root, "inspection");
        InspectionData[] id = new InspectionData[elem.length];
        for (int i = 0; i < elem.length; i++) {
            id[i] = this.getInspectionData(elem[i]);
        }
        return id;
    }

    private InspectionData getInspectionData(Element elem) {
        String day = elem.getAttribute("day");
        log.debug("day:" + day);
        String photopath = elem.getAttribute("photo");
        log.debug("photopath;" + photopath);
        Element[] pallet = this.getElements(elem, "pallet");
        InspectionRotInfo iri[] = new InspectionRotInfo[pallet.length];
        for (int i = 0; i < iri.length; i++) {
            int rotno;
            log.debug(pallet[i].getAttribute("rotno"));
//            log.debug(pallet[i].getTextContent());
            if (pallet[i].getAttribute("rotno").contains("PALLET_ID_ORDERED")) {
                rotno = AttachmentInspection2.PALLET_ID_ORDERED;
            } else {
                rotno = Integer.parseInt(pallet[i].getAttribute("rotno"));
            }
            String[] photo = pallet[i].getTextContent().trim().split(",");
            iri[i] = new InspectionRotInfo(rotno, photo);
            log.debug("rotno:" + iri[i].getRotno() + "\tphoto;" + Arrays.toString(iri[i].getRotPicData()));
        }
        Element[] relem = this.getElements(elem, "order");
        Attachment[] order = new Attachment[relem.length];
        for (int i = 0; i < relem.length; i++) {
            int rotno = Integer.parseInt(relem[i].getAttribute("rotno"));
            int pno = Integer.parseInt(relem[i].getAttribute("pno"));
            String direction;
            if (relem[i].getAttribute("lr").toLowerCase().contains("l")) {
                direction = Attachment.L;
            } else {
                direction = Attachment.R;
            }
            String remark = relem[i].getAttribute("remark");
            String[] photo = relem[i].getTextContent().trim().split(",");
            order[i] = new Attachment(rotno, pno, direction, remark, photo);
            log.debug(order[i].getAttachmentName());
        }
        relem = this.getElements(elem, "repair");
        Attachment[] repair = new Attachment[relem.length];
        for (int i = 0; i < relem.length; i++) {
            int rotno = Integer.parseInt(relem[i].getAttribute("rotno"));
            int pno = Integer.parseInt(relem[i].getAttribute("pno"));
            String direction;
            if (relem[i].getAttribute("lr").toLowerCase().contains("l")) {
                direction = Attachment.L;
            } else {
                direction = Attachment.R;
            }
            String remark = "";
            String[] photo = relem[i].getTextContent().trim().split(",");
            repair[i] = new Attachment(rotno, pno, direction, "", photo);
            log.debug(repair[i].getAttachmentName());
        }
        relem = this.getElements(elem, "defective");
        Attachment[] defective = new Attachment[relem.length];
        for (int i = 0; i < relem.length; i++) {
            int rotno = Integer.parseInt(relem[i].getAttribute("rotno"));
            int pno = Integer.parseInt(relem[i].getAttribute("pno"));
            String direction;
            if (relem[i].getAttribute("lr").toLowerCase().contains("l")) {
                direction = Attachment.L;
            } else {
                direction = Attachment.R;
            }
            String remark = relem[i].getAttribute("remark");
            String[] photo = relem[i].getTextContent().trim().split(",");
            defective[i] = new Attachment(rotno, pno, direction, remark, photo);
            log.debug(defective[i].getAttachmentName());
        }
        relem = this.getElements(elem, "defective");
        Attachment[] macro = new Attachment[relem.length];
        for (int i = 0; i < relem.length; i++) {
            int rotno = Integer.parseInt(relem[i].getAttribute("rotno"));
            int pno = Integer.parseInt(relem[i].getAttribute("pno"));
            String direction;
            if (relem[i].getAttribute("lr").toLowerCase().contains("l")) {
                direction = Attachment.L;
            } else {
                direction = Attachment.R;
            }
            String remark = "";
            String[] photo = relem[i].getTextContent().trim().split(",");
            macro[i] = new Attachment(rotno, pno, direction, remark, photo);
            log.debug(macro[i].getAttachmentName());
        }

        //ここから下は、いじらないでください
        return new InspectionData(
                day,
                iri,
                order,
                repair,
                defective,
                macro,
                photopath);
    }

    public String[] getAttachmentType() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("Inspection.xml");

        // ルート要素を取得
        Element root = doc.getDocumentElement();

        Element settings = this.getElement(root, "Setting");
        String val = this.getString(settings, "attachmenttype");

        return val.trim().split(",");
    }

    public SQL_OPE getSQL() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("Inspection.xml");

        // ルート要素を取得
        Element root = doc.getDocumentElement();

        Element settings = this.getElement(root, "Setting");
        Element url = this.getElement(settings, "url");

        String a = this.getString(settings, "url");
        String b = url.getAttribute("uname");
        String c = url.getAttribute("");

        log.info(a + "\t" + b + "\t" + c);
        return new SQL_OPE(a, b, c);
    }

    public String[][] getRotDefinition() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("Inspection.xml");

        // ルート要素を取得
        Element root = doc.getDocumentElement();
        Element[] rotdef = this.getElements(this.getElement(root, "Setting"), "rotdef");
        String[][] val = new String[rotdef.length][];
        for (int i = 0; i < rotdef.length; i++) {
            Element elem = rotdef[i];
            String[] val2 = new String[]{
                elem.getTextContent(),
                elem.getAttribute("attachment"),
                elem.getAttribute("num")
            };
//            log.info(Arrays.toString(val2));
            val[i] = val2;
        }
        return val;
    }

    private Element[] getElements(Element root, String tag) {

        NodeList node = root.getChildNodes();
        ArrayList<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < node.getLength(); i++) {
            Node childNode = node.item(i);
            if (Node.ELEMENT_NODE == childNode.getNodeType()) {
                Element child = (Element) childNode;

                if (Objects.equals(child.getNodeName(), tag)) {

                    NodeList node2 = child.getChildNodes();
                    Element elem2 = (Element) node2;
//                    log.info(elem2.getNodeValue());
                    elements.add((Element) node2);
//                    return (Element) node2;

                }
            }
        }
        return elements.toArray(new Element[elements.size()]);
    }

    private Element getElement(Element root, String tag) {

        NodeList node = root.getChildNodes();
        for (int i = 0; i < node.getLength(); i++) {
            Node childNode = node.item(i);
            if (Node.ELEMENT_NODE == childNode.getNodeType()) {
                Element child = (Element) childNode;

                if (Objects.equals(child.getNodeName(), tag)) {

                    NodeList node2 = child.getChildNodes();
                    return (Element) node2;

                }
            }
        }
        return null;
    }

    private String getString(Element doc, String Tag) {

        NodeList lst = doc.getElementsByTagName(Tag);
        String[] val1 = new String[lst.getLength()];
        for (int i = 0; i < lst.getLength(); i++) {
            Node n = lst.item(i);
            for (Node ch = n.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
                val1[i] = ch.getNodeValue();
            }
        }

        return val1[0];

    }

}
