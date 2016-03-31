import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{

        int pos, viewNumber = 0, index = 0, count = 0, norm = 1;
        char[] currentUser, currentItem;
        double[][] distanceMatrix;
        Map<String, Map<String, Integer>> UserItemArray = new HashMap<String, Map<String, Integer>>();
        Set<String> uniqueItems = new HashSet<>();

        currentUser = new char[100];
        currentItem = new char[100];

        int myCount = 0;

        File file = new File("/Users/kamilla/Documents/Graduate work/data.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String stringData;

            while(br.read() != -1) {
                stringData = br.readLine();
                if (!stringData.isEmpty()) {
                    // remain one space between user, item and number in each row
                    stringData = stringData.trim().replaceAll("(\\s)+", "$1").replaceAll("\\t", " ");
                    char[] charData = stringData.toCharArray();
                    index = 0;
                    // read user
                    pos = 0;
                    while (charData[index] != ' ') {
                        currentUser[pos++] = charData[index++];
                    }
                    index++;
                    // read item
                    pos = 0;
                    while (charData[index] != ' ') {
                        currentItem[pos++] = charData[index++];
                    }
                    index++;
                    // read view number
                    viewNumber = Character.getNumericValue(charData[index++]);
                    //if (viewNumber == 1)
                        myCount++;

                    uniqueItems.add(new String(currentItem));

                    // create the matrix in the form of
                    // user1 : (item1, 1), (item3, 1), ...
                    // user2: (item1, 2), ...
                    if (UserItemArray.containsKey(new String(currentUser))) {
                        UserItemArray.get(new String(currentUser)).put(new String(currentItem), viewNumber);
                    } else {
                        Map<String, Integer> rowMap = new HashMap<>();
                        rowMap.put(new String(currentItem), viewNumber);
                        UserItemArray.put(new String(currentUser), rowMap);
                    }
                }
            }
            System.out.println(myCount);

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
            //Method method = distanceClass.getClass().getMethod("createDistanceMatrix", new Class[] { Map.class, Set.class, List.class });
            Method method = distanceClass.getClass().getMethod("completeLink", new Class[] { Map.class, Set.class, List.class });

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
            //distanceMatrix = (double[][]) method.invoke(distanceClass, params);
            method.invoke(distanceClass, params);

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }


        /*try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("/Users/kamilla/Documents/Graduate work/output2.csv"), "UTF-8")) {

            int n = UserItemArray.size();
            String userName;
            String[] conformityArray = new String[n];
            String[] conformityItemArray = new String[2*n];
            int ind = 0;
            StringBuilder Ids = new StringBuilder();
            Ids.append("Users");
            Iterator it = UserItemArray.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                userName = pair.getKey().toString();
                conformityArray[ind++] = userName;
            }
            int j = 0;
            Iterator iter = uniqueItems.iterator();
            while (iter.hasNext()) {
                String item = iter.next().toString();
                conformityItemArray[j++] = item;
                Ids.append(",").append(item);
            }
            writer.write(Ids.toString());
            writer.append('\n');

            int ccc = 0;
            for (int i = 0; i < n; ++i) {
                Ids = new StringBuilder();
                userName = conformityArray[i];
                Ids.append(userName);
                Map<String, Integer> current = UserItemArray.get(userName);
                iter = uniqueItems.iterator();
                int k = 0;
                while (k < 4) {
                    String item = iter.next().toString();
                    if (!current.containsKey(item)) {
                        Ids.append(",0");
                    }
                    else {
                        Ids.append(",").append(String.valueOf(current.get(item)));
                        if (k == 3)
                            ccc++;
                    }
                    k++;
                }
                writer.write(Ids.toString());
                writer.append('\n');
            }
            System.out.println(ccc);
            writer.flush();

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }*/


    }
}
