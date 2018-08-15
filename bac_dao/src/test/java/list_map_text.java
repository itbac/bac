import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class list_map_text {
    public static void main(String[] args) {


        Map map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("text", "联想");

        Map map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("text", "华为");

        List<Map> list = new ArrayList<>();

        list.add(map1);
        list.add(map2);

        System.out.println(list);
    }
}