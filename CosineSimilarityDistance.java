import java.util.Iterator;
import java.util.Map;

public class CosineSimilarityDistance implements Distance {

    CosineSimilarityDistance() {}

    @Override
    public double computeDistance(Iterator iterator, Map<String, Integer> first, Map<String, Integer> second) {
        double numerator = 0, denominator1 = 0, denominator2 = 0;
        while (iterator.hasNext()) {
            String item = iterator.next().toString();
            if (first.containsKey(item) && !second.containsKey(item)) {
                denominator1 += Math.pow(first.get(item), 2);
            }
            else if (!first.containsKey(item) && second.containsKey(item)) {
                denominator2 += Math.pow(second.get(item), 2);
            }
            else if (first.containsKey(item) && second.containsKey(item)) {
                numerator += first.get(item) * second.get(item);
                denominator1 += Math.pow(first.get(item), 2);
                denominator2 += Math.pow(second.get(item), 2);
            }
        }
        double denominator = Math.sqrt(denominator1) * Math.sqrt(denominator2);
        return 1 - (double) (numerator / denominator);
    }
}
