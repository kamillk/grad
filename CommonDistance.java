import java.util.Iterator;
import java.util.Map;

public abstract class CommonDistance implements Distance{

    protected abstract int getDegree();

    public double computeDistance(Iterator iterator, Map<String, Integer> first, Map<String, Integer> second) {
        int sum = 0;
        while (iterator.hasNext()) {
            String item = iterator.next().toString();
            if (first.containsKey(item) && !second.containsKey(item)) {
                sum += Math.pow(first.get(item), this.getDegree());
            } else if (!first.containsKey(item) && second.containsKey(item)) {
                sum += Math.pow(second.get(item), this.getDegree());
            } else if (first.containsKey(item) && second.containsKey(item)) {
                sum += Math.pow(Math.abs(first.get(item) - second.get(item)), this.getDegree());
            }
        }
        return Math.pow(sum, (double) (1 / (double)this.getDegree()));
    }

}
