import java.util.Iterator;
import java.util.Map;

public interface Distance {

    double computeDistance(Iterator iterator, Map<String, Integer> first, Map<String, Integer> second);
}
