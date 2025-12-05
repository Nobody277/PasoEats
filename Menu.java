import java.util.*;
import java.io.IOException;
import java.nio.file.Path;

public final class Menu {
    private final HashMap<String, MenuItem> items = new HashMap();

    public void add(MenuItem item) {
        items.put(item.getName(), item);
    }

    public boolean remove(String name) {
        return items.remove(name) != null; // the item does not equal null then its true
    }

    public MenuItem get(String name) {
        return items.get(name);
    }

    public Collection<MenuItem> all() {
        return items.values();
    }

    public void save(Path path) throws IOException {
        List<MenuItem> snapshop = new ArrayList<>(items.values());
    }
}