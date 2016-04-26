import java.util.*;

public abstract class LinkageClustering {

    protected abstract Map<String, Map<String, Integer>> getUserItemArray();
    protected abstract Set<String> getUniqueItems();
    protected abstract String getMetric();
    protected abstract List<TreeNode> getList();
    protected abstract int getNumberOfUsers();

    protected abstract void doStructure();

    public void makeClusters() {
        int i = 0;
        for (Map.Entry<String, Map<String, Integer>> entry : this.getUserItemArray().entrySet())
        {
            if (i < this.getNumberOfUsers()) {
                String name = entry.getKey();
                TreeNode treeNode = new TreeNode(name, null, null);
                this.getList().add(treeNode);
            }
            i++;
        }
        doStructure();
    }


    public double calculateDistance(String firstname, String secondname) {

        double distance = 0;

        Map<String, Integer> first = this.getUserItemArray().get(firstname);
        Map<String, Integer> second = this.getUserItemArray().get(secondname);
        Iterator iterator = this.getUniqueItems().iterator();

        switch (this.getMetric()) {
            case "Manhattan distance":
                ManhattanDistance manhattanDistance = new ManhattanDistance();
                distance = manhattanDistance.computeDistance(iterator, first, second);
                break;
            case "Euclidean distance":
                EuclideanDistance euclideanDistance = new EuclideanDistance();
                distance = euclideanDistance.computeDistance(iterator, first, second);
                break;
            case "Cosine similarity distance":
                CosineSimilarityDistance cosineSimilarityDistance = new CosineSimilarityDistance();
                distance = cosineSimilarityDistance.computeDistance(iterator, first, second);
                break;
        }
        return distance;
    }

    public TreeNode findCurrentRoot(String userId) {
        for (TreeNode treeNode : this.getList()) {
            List<String> children = treeNode.getChildren();
            if (children.size() == 0 && treeNode.getId().equals(userId))
                return treeNode;
            else if (children.contains(userId)) {
                return treeNode;
            }
        }
        return null;
    }

    public TreeNode createNewNode(Object name1, Object name2) {
        TreeNode newNode = new TreeNode();
        TreeNode root1 = findCurrentRoot(name1.toString());
        TreeNode root2 = findCurrentRoot(name2.toString());
        newNode.setRight(root1);
        newNode.setLeft(root2);
        newNode.setId(String.valueOf(-1));
        newNode.setChildren();
        if (root1.hasChildren())
            newNode.addChildren(root1.getChildren());
        else
            newNode.addChildren(Arrays.asList(name1.toString()));
        if (root2.hasChildren())
            newNode.addChildren(root2.getChildren());
        else
            newNode.addChildren(Arrays.asList(name2.toString()));
        return newNode;
    }

    public void removeNode(Object name) {
        for (TreeNode object : this.getList()) {
            if (object.hasChildren()) {
                List<String> childrenOfCurrentObject = object.getChildren();
                if (childrenOfCurrentObject.contains(name.toString())) {
                    this.getList().remove(object);
                    break;
                }
            }
            else if (object.getId().equals(name)) {
                this.getList().remove(object);
                break;
            }
        }
    }

    public String print(TreeNode root) {

        if (!root.getId().equals("-1")) {
            return root.getId();
        }
        else {
            TreeNode left = null, right = null;
            if (root.getLeft() != null) {
                left = root.getLeft();
            }
            if (root.getRight() != null) {
                right = root.getRight();
            }
            return "(" + print(left) + "," + print(right) + ")";
        }
    }

}
