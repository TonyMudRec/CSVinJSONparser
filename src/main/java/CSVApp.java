import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVApp {

    static String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    static String fileName = "data.csv";
    static List<Employee> list = parseCSV(columnMapping, fileName);
    static String json = listToJson(list);

    public static void main(String[] args) {
//        "1,John,Smith,USA,25"
//        "2,Inav,Petrov,RU,23"
        String[] employee = "2,Inav,Petrov,RU,23".split(",");
        try (CSVWriter csvwriter = new CSVWriter(new FileWriter("data.csv", true))) {
            csvwriter.writeNext(employee);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        writeString(json);
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
//        Type listType = new TypeToken<List<T>>() { (я так и не понял, зачем это)
//        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, Employee.class);
    }

    private static void writeString(String json) {
        JSONObject obj = new JSONObject();
        obj.put("id", (Employee) id); /* как передать сюда значение конструктора из Employee*/
        obj.put("firstName", json.id);
        obj.put("lastName", json.id);
        obj.put("country", json.id);
        obj.put("age", json.id);
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
