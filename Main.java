import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public double[][] createDistanceMatrix(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, int method) {

        int index = 0;
        int n = UserItemArray.size();
        String[] conformityArray = new String[n];
        double distanceMatrix[][] = new double[n][];

        // create the triangular matrix
        for (int i = 0; i < n; i++)
        {
            distanceMatrix[i] = new double[i];
        }

        // create matrix to conform username and id in distanceMatrix
        Iterator it = UserItemArray.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String userName = pair.getKey().toString();
            conformityArray[index++] = userName;
        }
        // estimate measures
        switch(method) {
            case 1:
                distanceMatrix = distanceMeasure(UserItemArray, uniqueItems, conformityArray, distanceMatrix, 1);
                return distanceMatrix;
            case 2:
                distanceMatrix = distanceMeasure(UserItemArray, uniqueItems, conformityArray, distanceMatrix, 2);
                return distanceMatrix;
            case 3:
                distanceMatrix = cosineMeasure(UserItemArray, uniqueItems, conformityArray, distanceMatrix);
                return distanceMatrix;
            default:
                distanceMatrix = distanceMeasure(UserItemArray, uniqueItems, conformityArray, distanceMatrix, 1);
                return distanceMatrix;
        }
    }

    public double[][] distanceMeasure(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, String[] conformityArray, double distanceMatrix[][], int degree) {

        int sum;
        int n = UserItemArray.size();

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                sum = 0;
                String firstUsername = conformityArray[i];
                String secondUsername = conformityArray[j];
                Map<String, Integer> first = UserItemArray.get(firstUsername);
                Map<String, Integer> second = UserItemArray.get(secondUsername);
                Iterator iterator = uniqueItems.iterator();
                while (iterator.hasNext()) {
                    String item = iterator.next().toString();
                    if (first.containsKey(item) && !second.containsKey(item)) {
                        sum += Math.pow(first.get(item), degree);
                    }
                    else if (!first.containsKey(item) && second.containsKey(item)) {
                        sum += Math.pow(second.get(item), degree);
                    }
                    else if (first.containsKey(item) && second.containsKey(item)) {
                        sum += Math.pow(Math.abs(first.get(item) - second.get(item)), degree);
                    }
                }
                distanceMatrix[i][j] = Math.pow(sum, 1/degree);
            }
        }
        return distanceMatrix;
    }

    public double[][] cosineMeasure(Map<String, Map<String, Integer>> UserItemArray, Set<String> uniqueItems, String[] conformityArray, double distanceMatrix[][]) {

        int numerator, denominator1, denominator2;
        int n = UserItemArray.size();

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                numerator = 0; denominator1 = 0; denominator2 = 0;
                String firstUsername = conformityArray[i];
                String secondUsername = conformityArray[j];
                Map<String, Integer> first = UserItemArray.get(firstUsername);
                Map<String, Integer> second = UserItemArray.get(secondUsername);
                Iterator iterator = uniqueItems.iterator();
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
                distanceMatrix[i][j] = numerator / Math.sqrt(denominator1) / Math.sqrt(denominator2);
            }
        }
        return distanceMatrix;
    }

    public static void main(String[] args) {

        int pos, viewNumber = 0, index = 0, count = 0;
        char[] currentUser, currentItem;
        double distanceMatrix[][];

        currentUser = new char[100];
        currentItem = new char[100];

        File file = new File("/Users/kamilla/Documents/Education/Graduate work/data.txt");

        try(FileReader reader = new FileReader(file))
        {
            char[] charData = new char[(int)file.length()];

            Map<String, Map<String, Integer>> UserItemArray = new HashMap<String, Map<String, Integer>>();
            Set<String> uniqueItems = new HashSet<>();

            reader.read(charData);
            // remain one space between user, item and number in each row
            for (int i = 0; i < charData.length; i++) {
                if (charData[i] == '\r' || charData[i] == '\t') {
                    charData[i] = ' ';
                }
            }
            while (charData[index] != '\n' && index < charData.length) {
                // read user
                pos = 0;
                while (charData[index] != ' ' && index < charData.length) {
                    currentUser[pos++] = charData[index++];
                }
                index++;
                // read item
                pos = 0;
                while (charData[index] != ' ' && index < charData.length) {
                    currentItem[pos++] = charData[index++];
                }
                index++;
                // read view number
                viewNumber = Character.getNumericValue(charData[index++]);

                uniqueItems.add(new String(currentItem));

                // create the matrix in the form of
                // user1 : (item1, 1), (item3, 1), ...
                // user2: (item1, 2), ...
                if (UserItemArray.containsKey(new String(currentUser))) {
                    UserItemArray.get(new String(currentUser)).put(new String(currentItem), viewNumber);
                }
                else {
                    Map<String, Integer> rowMap = new HashMap<>();
                    rowMap.put(new String(currentItem), viewNumber);
                    UserItemArray.put(new String(currentUser), rowMap);
                }
                index += 2;
            }

            Main m = new Main();
            // 3d parameter is the number of the selected method - 1 is distance measure with degree = 1, 2 is distance measure with degree = 2,
            // 3 is cosine measure
            distanceMatrix = m.createDistanceMatrix(UserItemArray, uniqueItems, 1);

            for (int i = 0; i < UserItemArray.size(); ++i) {
                for (int j = 0; j < i; ++j) {
                    System.out.print(distanceMatrix[i][j] + " ");
                }
                System.out.println("");
            }

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
