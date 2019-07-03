import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavan Kanekal on 6/6/16.
 */
public class Person {
    List<Person> littles = new ArrayList<>();
    int generation;
    int captain;
    String name;

    public Person (String name) {
        this.name = name;
        this.captain = 0;
    }
}
