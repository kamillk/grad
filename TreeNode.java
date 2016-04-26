import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private String id;
    private TreeNode left;
    private TreeNode right;
    private List<String> children;
    private TreeNode parent;

    TreeNode() {}

    TreeNode(String id) {
        this.id = id;
    }

    TreeNode(String id, TreeNode left, TreeNode right) {
        this.id = id;
        this.left = left;
        this.right = right;
        children = new ArrayList<>();
    }

    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren() {
        this.children = new ArrayList<>();
    }

    public void addChildren(List<String> newchildren) {
        for (String child : newchildren) {
            this.children.add(child);
        }
    }

    public boolean hasChildren() {
        if (!children.isEmpty())
            return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() == this.getClass()) {
            TreeNode treeNode = (TreeNode) obj;
            if (treeNode.getId() == this.getId())
                return true;
        }
        return false;
    }


}
