import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class DistanceClass {

    public double distanceMatrix[][] = new double[6000][];

    public double[][] createDistanceMatrix(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, List<String> params) {

        int index = 0, degree = 1;
        int n = UserItemArray.size();
        String[] conformityArray = new String[n];

        // create the triangular matrix
        for (int i = 0; i < 5000; i++)
        {
            distanceMatrix[i] = new double[i+1];
        }

        // create matrix to conform username and id in distanceMatrix
        Iterator it = UserItemArray.entrySet().iterator();
        int k = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String userName = pair.getKey().toString();
            conformityArray[index++] = userName;
            k++;
        }

        String metric = params.get(0);
        if (params.size() == 2)
            degree = Integer.parseInt(params.get(1));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j <= i; ++j) {

                String firstUsername = conformityArray[i];
                String secondUsername = conformityArray[j];
                Map<String, Integer> first = UserItemArray.get(firstUsername);
                Map<String, Integer> second = UserItemArray.get(secondUsername);
                Iterator iterator = uniqueItems.iterator();

                switch(metric) {
                    case "distanceMeasure":
                        distanceMatrix[i][j] = distanceMeasure(iterator, first, second, degree);
                        break;
                    case "cosineMeasure":
                        distanceMatrix[i][j] = cosineMeasure(iterator, first, second);
                        if (i == j) {
                            distanceMatrix[i][j] = 0;
                        }
                        break;
                }

            }
        }
        return distanceMatrix;
    }

    public void completeLink(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, List<String> params) {

        int degree = 1, index = 0;
        int newId = 0;

        double distance = 0;
        int n = UserItemArray.size();
        String[] conformityArray = new String[n];
        List<Pairs> maxArray = new ArrayList<>();
        List<Cluster> clusters = new ArrayList<>();

        Iterator it = UserItemArray.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String userName = pair.getKey().toString();
            conformityArray[index++] = userName;
        }

        String metric = params.get(0);
        if (params.size() == 2)
            degree = Integer.parseInt(params.get(1));

        // k is the number of combining iterations
        for (int k = 0; k < 15; ++k) {

            double max;
            int posj = 0;
            List<Pairs> rowMax = new ArrayList<>();
            for (int i = 0; i < 20; ++i) {
                max = -1;
                for (int j = 0; j < i; ++j) {

                    Pairs currentPair = new Pairs(i, j);

                    if (maxArray.isEmpty() || !maxArray.contains(currentPair)) {

                        String firstUsername = conformityArray[i];
                        String secondUsername = conformityArray[j];
                        Map<String, Integer> first = UserItemArray.get(firstUsername);
                        Map<String, Integer> second = UserItemArray.get(secondUsername);
                        Iterator iterator = uniqueItems.iterator();

                        switch (metric) {
                            case "distanceMeasure":
                                distance = distanceMeasure(iterator, first, second, degree);
                                break;
                            case "cosineMeasure":
                                distance = cosineMeasure(iterator, first, second);
                                break;
                        }

                        // find max distance in each row
                        if (distance > max) {
                            max = distance;
                            posj = j;
                        }
                    }
                }
                // put maximum of each row in List
                if (max != -1) {
                    Pairs p = new Pairs(max, i, posj);
                    rowMax.add(p);
                }
            }
            // find overall maximum by searching the List of maximums and remember pair of users - userIndex1, userIndex2
            max = -1;
            int userIndex1 = 0, userIndex2 = 0;
            for (Pairs element : rowMax) {
                if (element.value > max) {
                    max = element.value;
                    userIndex1 = element.x;
                    userIndex2 = element.y;
                }
            }
            // remember the indices of maximum
            maxArray.add(new Pairs(userIndex1, userIndex2));

            // set a flag to check if user indices are in current clusters
            // or we should create new cluster
            boolean flag = false;

            // create tree structure

            // add one value to existing cluster or create the new one and put both values
            if (!clusters.isEmpty()) {
                for (Cluster cluster : clusters) {
                    TreeNode node1 = new TreeNode(userIndex1);
                    TreeNode node2 = new TreeNode(userIndex2);
                    if (cluster.contains(node1) && !cluster.contains(node2)) {
                        cluster.addNode(node2);
                        flag = true;
                        break;
                    }
                    else if (cluster.contains(node2) && !cluster.contains(node1)) {
                        cluster.addNode(node1);
                        flag = true;
                        break;
                    }
                    else if (cluster.contains(node1) && cluster.contains(node2)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    Cluster cluster = new Cluster(newId++);
                    cluster.addNodes(new TreeNode(userIndex1), new TreeNode(userIndex2));
                    clusters.add(cluster);
                }
            }
            // create a new cluster if there are no clusters
            else {
                Cluster cluster = new Cluster(newId++);
                cluster.addNodes(new TreeNode(userIndex1), new TreeNode(userIndex2));
                clusters.add(cluster);
            }
        }

        int i = 1;
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("/Users/kamilla/Documents/Graduate work/clusters.txt"), "UTF-8")) {
            for (Cluster cluster: clusters) {
                writer.write("Cluster # " + i);
                writer.append('\n');
                i++;
                List<TreeNode> vertices = cluster.getVertices();
                for (TreeNode node : vertices) {
                    writer.write(String.valueOf(node.getId()));
                    writer.append('\n');
                }
            }
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public double distanceMeasure(Iterator iterator, Map<String, Integer> first, Map<String, Integer> second, int degree) {

        int sum = 0;
        while (iterator.hasNext()) {
            String item = iterator.next().toString();
            if (first.containsKey(item) && !second.containsKey(item)) {
                sum += Math.pow(first.get(item), degree);
            } else if (!first.containsKey(item) && second.containsKey(item)) {
                sum += Math.pow(second.get(item), degree);
            } else if (first.containsKey(item) && second.containsKey(item)) {
                sum += Math.pow(Math.abs(first.get(item) - second.get(item)), degree);
            }
        }
        return Math.pow(sum, (double) (1 / (double)degree));
    }

    public double cosineMeasure(Iterator iterator, Map<String, Integer> first, Map<String, Integer> second) {

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
