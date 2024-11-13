package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("C:\\Users\\zolot\\IdeaProjects\\Lab7Mag\\src\\main\\java\\org\\example\\student1.xml"));

        document.getDocumentElement().normalize();

        // Получаем элементы с оценками
        NodeList subjectList = document.getElementsByTagName("subject");
        int totalMarks = 0;
        int numberOfSubjects = subjectList.getLength();

        // Вычисляем общую сумму оценок
        for (int i = 0; i < numberOfSubjects; i++) {
            Element subject = (Element) subjectList.item(i);
            int mark = Integer.parseInt(subject.getAttribute("mark"));
            totalMarks += mark;
        }

        // Вычисляем среднюю оценку
        double average = (double) totalMarks / numberOfSubjects;

        // Получаем элемент average и обновляем его значение
        NodeList averageList = document.getElementsByTagName("average");
        if (averageList.getLength() > 0) {
            Element averageElement = (Element) averageList.item(0);
            averageElement.setTextContent(String.format("%.2f", average));
        } else {
            // Если элемента average нет, создаем его
            Element averageElement = document.createElement("average");
            averageElement.setTextContent(String.format("%.2f", average));
            document.getDocumentElement().appendChild(averageElement);
        }

        // Создаем новый документ для записи с DOCTYPE
        Document newDocument = builder.newDocument();
        DocumentType docType = newDocument.getImplementation().createDocumentType("student", null, "student.dtd");
        newDocument.appendChild(docType);

        // Копируем корневой элемент
        Element root = newDocument.createElement("student");
        root.setAttribute("lastname", document.getDocumentElement().getAttribute("lastname"));
        newDocument.appendChild(root);

        // Копируем элементы subject
        for (int i = 0; i < subjectList.getLength(); i++) {
            Element subject = (Element) subjectList.item(i);
            Element newSubject = newDocument.createElement("subject");
            newSubject.setAttribute("mark", subject.getAttribute("mark"));
            newSubject.setAttribute("title", subject.getAttribute("title"));
            root.appendChild(newSubject);
        }

        // Копируем элемент average
        Element averageElement = newDocument.createElement("average");
        averageElement.setTextContent(String.format("%.2f", average));
        root.appendChild(averageElement);

        // Запись нового документа в файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(newDocument);
        StreamResult result = new StreamResult(new File("C:\\Users\\zolot\\IdeaProjects\\Lab7Mag\\src\\main\\java\\org\\example\\student2.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "student.dtd");

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Средняя оценка обновлена и добавлена в " + "student2.xml");
    }
}