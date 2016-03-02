import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{

        int pos, viewNumber = 0, index = 0, count = 0, norm = 1;
        char[] currentUser, currentItem;
        double[][] distanceMatrix;

        currentUser = new char[100];
        currentItem = new char[100];

        File file = new File("/Users/kamilla/Documents/Graduate work/data.txt");

        try(FileReader reader = new FileReader(file))
        {
            char[] charData = new char[(int)file.length()];

            Map<String, Map<String, Integer>> UserItemArray = new HashMap<String, Map<String, Integer>>();
            Set<String> uniqueItems = new HashSet<>();

            reader.read(charData);

            // remain one space between user, item and number in each row
            String stringData = new String(charData);
            stringData = stringData.trim().replaceAll("(\\s)+", "$1").replaceAll("\\t", " ");
            charData = stringData.toCharArray();

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
                index ++;
            }

            //check
            /*int ff = 0, ind = 0;
            String item = "";
            int n = UserItemArray.size();
            String[] conformityArray = new String[n];
            Iterator it = UserItemArray.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userName = pair.getKey().toString();
                conformityArray[ind++] = userName;
            }
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j <= i; ++j) {

                    ff = 0;

                    if (i != j) {

                        String firstUsername = conformityArray[i];
                        String secondUsername = conformityArray[j];
                        Map<String, Integer> first = UserItemArray.get(firstUsername);
                        Map<String, Integer> second = UserItemArray.get(secondUsername);
                        Iterator iterator = uniqueItems.iterator();
                        while (iterator.hasNext() && ff < 1) {
                            item = iterator.next().toString();
                            if (first.containsKey(item) && second.containsKey(item)) {
                                ff++;
                            }
                        }
                        if (ff >= 1) {
                            System.out.println("i " + i);
                            System.out.println("j " + j);
                            System.out.println("item " + item);
                        }
                    }
                }
            }*/

            System.out.println("Please, choose the metric (1 - Manhattan metric, 2 - Euclidean metric, 3 - Cosine measure): ");
            Scanner sc = new Scanner(System.in);
            int metric = sc.nextInt();
            norm = metric;

            DistanceClass distanceClass = new DistanceClass();
            Method method = distanceClass.getClass().getMethod("createDistanceMatrix", new Class[] { Map.class, Set.class, List.class });

            List<String> list = new ArrayList<>();
            switch(metric) {
                case 1:
                case 2:
                    list.add(new String("distanceMeasure"));
                    list.add(String.valueOf(norm));
                    break;
                case 3:
                    list.add(new String("cosineMeasure"));
                    break;
            }
            Object[] params = new Object[] { UserItemArray, uniqueItems, list };
            distanceMatrix = (double[][]) method.invoke(distanceClass, params);

            int n = UserItemArray.size();
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j <= i; ++j) {
                    System.out.print(distanceMatrix[i][j] + " ");
                }
                System.out.println("");
            }
            //System.out.println(distanceMatrix[391][32]);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
