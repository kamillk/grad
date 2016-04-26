import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class AverageLink extends LinkageClustering{

    private Map<String, Map<String, Integer>> UserItemArray;
    private Set<String> uniqueItems;
    private String metric;
    private int numberOfUsers;
    private List<TreeNode> list;
    private double minimum;
    private Pair pair;
    private StringBuilder stringBuilder;

    AverageLink() {}

    AverageLink(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, String metric, int numberOfUsers) {
        this.UserItemArray = UserItemArray;
        this.uniqueItems = uniqueItems;
        this.metric = metric;
        this.numberOfUsers = numberOfUsers;
        list = new ArrayList<>();
    }

    @Override
    protected Map<String, Map<String, Integer>> getUserItemArray() {
        return UserItemArray;
    }

    @Override
    protected Set<String> getUniqueItems() {
        return uniqueItems;
    }

    @Override
    protected String getMetric() {
        return metric;
    }

    @Override
    protected List<TreeNode> getList() {
        return list;
    }

    @Override
    protected int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void findClustersToMerge(String Id1, String Id2) {

        double distance;

        distance = calculateDistance(Id1, Id2);
        if (distance < minimum) {
            minimum = distance;
            pair.setFirst(Id1);
            pair.setSecond(Id2);
        }
    }

    public void findClustersToMerge(List<String> childrenIds, String Id) {

        double distance, res;
        double sumOfDistances = 0;

        String firstname = Id;
        String secondname = "";
        for (String childId : childrenIds) {
            secondname = childId;
            distance = calculateDistance(firstname, secondname);
            sumOfDistances += distance;
        }
        res = sumOfDistances / childrenIds.size();
        if (res < minimum) {
            minimum = res;
            pair.setFirst(firstname);
            pair.setSecond(secondname);
        }
    }

    public void findClustersToMerge(List<String> childrenIds1, List<String> childrenIds2) {

        double distance, res;
        double sumOfDistances = 0;

        for (String childId1 : childrenIds1) {
            for (String childId2 : childrenIds2) {
                distance = calculateDistance(childId1, childId2);
                sumOfDistances += distance;
            }
        }
        res = sumOfDistances / (childrenIds1.size() * childrenIds2.size());
        if (res < minimum) {
            minimum = res;
            pair.setFirst(childrenIds1.get(0));
            pair.setSecond(childrenIds2.get(0));
        }
    }

    public void doStructure() {

        for (int i = 0; i < numberOfUsers - 1; ++i) {

            minimum = 1000000;

            pair = new Pair();

            Set<Pair> set = new HashSet<>();

            /*for (TreeNode element1 : list) {
                for (TreeNode element2 : list) {
                    if (element1.getId() != element2.getId() && !set.contains(new Pair(element1, element2))) {
                        set.add(new Pair(element1, element2));
                        set.add(new Pair(element2, element1));
                        System.out.println(element1.getId()+" "+element2.getId()+" "+calculateDistance(element1.getId(), element2.getId()));

                    }
                }
            }*/
            for (TreeNode element1 : list) {
                for (TreeNode element2 : list) {
                    if (element1.getId() != element2.getId() && !set.contains(new Pair(element1, element2))) {
                        set.add(new Pair(element1, element2));
                        set.add(new Pair(element2, element1));
                        if (!element1.hasChildren() && !element2.hasChildren()) {
                            findClustersToMerge(element1.getId(), element2.getId());
                        }
                        else if (element1.hasChildren() && !element2.hasChildren()) {
                            findClustersToMerge(element1.getChildren(), element2.getId());
                        }
                        else if (!element1.hasChildren() && element2.hasChildren()) {
                            findClustersToMerge(element2.getChildren(), element1.getId());
                        }
                        else if (element1.hasChildren() && element2.hasChildren()) {
                            findClustersToMerge(element1.getChildren(), element2.getChildren());
                        }
                    }
                }
            }
            TreeNode newNode = createNewNode(pair.getFirst(), pair.getSecond());
            removeNode(pair.getFirst());
            removeNode(pair.getSecond());
            list.add(newNode);
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("/Users/kamilla/Documents/Graduate work/average.txt"), "UTF-8")) {
            for (TreeNode treeNode : list) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(print(treeNode));
                writer.write(stringBuilder.toString());
                writer.append('\n');
            }
            writer.flush();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
