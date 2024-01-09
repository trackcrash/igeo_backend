package igeo.site.Model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class Answer {
    private Map<String, Category> categories;

    public Answer() {
        this.categories = new HashMap<>();
    }

    public void addCategory(String name, Category category) {
        this.categories.put(name, category);
    }



}
