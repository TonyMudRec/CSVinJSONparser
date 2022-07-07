import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CSVApp {
    static List<Employee> list;

    public static void main(String[] args) {
//        "1,John,Smith,USA,25"
//        "2,Ivan,Petrov,RU,23"
        String[] employeeParameters1 = "1,John,Smith,USA,25".split(",");
        String[] employeeParameters2 = "2,Ivan,Petrov,RU,23".split(",");
        try (CSVWriter csvwriter = new CSVWriter(new FileWriter("data.csv", false))) {
            csvwriter.writeNext(employeeParameters1);
            csvwriter.writeNext(employeeParameters2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element staff = document.createElement("staff");
            document.appendChild(staff);
            Element employee1 = document.createElement("employee");
            employee1.setAttribute("id", "1");
            employee1.setAttribute("firstName", "John");
            employee1.setAttribute("lastName", "Smith");
            employee1.setAttribute("country", "USA");
            employee1.setAttribute("age", "25");
            staff.appendChild(employee1);
            Element employee2 = document.createElement("employee");
            employee2.setAttribute("id", "2");
            employee2.setAttribute("firstName", "Ivan");
            employee2.setAttribute("lastName", "Petrov");
            employee2.setAttribute("country", "RU");
            employee2.setAttribute("age", "23");
            staff.appendChild(employee2);

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("data.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }


    }

    private static List<Employee> parseCSV(String[] layout, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(layout);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = csv.parse();
            list.forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
