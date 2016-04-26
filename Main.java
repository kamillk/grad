import java.io.*;
import java.util.*;

public class Main{

    public static void main(String[] args) throws Exception{

        int pos, viewNumber = 0, index = 0;
        char[] currentUser, currentItem;
        Map<String, Map<String, Integer>> UserItemArray = new HashMap<>();
        Set<String> uniqueItems = new HashSet<>();

        currentUser = new char[31];
        currentItem = new char[100];

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

            System.out.println("Please, choose the metric (1 - Manhattan distance, 2 - Euclidean distance, 3 - Cosine similarity distance): ");
            Scanner sc = new Scanner(System.in);
            int metric = sc.nextInt();

            String method = "";
            switch(metric) {
                case 1:
                    method = "Manhattan distance";
                    break;
                case 2:
                    method = "Euclidean distance";
                    break;
                case 3:
                    method = "Cosine similarity distance";
                    break;
            }

            // linkage clustering

            //CompleteLink completeLink = new CompleteLink(UserItemArray, uniqueItems, method, 5);
            //completeLink.makeClusters();

            AverageLink averageLink = new AverageLink(UserItemArray, uniqueItems, method, 1000);
            averageLink.makeClusters();


        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }


    }
}
