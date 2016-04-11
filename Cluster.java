import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private int clusterId;
    private int height;
    private int size;
    private List<TreeNode> vertices;

    Cluster(int Id) {
        this.size = 0;
        this.height = 0;
        this.clusterId = Id;
    }

    public boolean contains(TreeNode node) {
        if (vertices.contains(node)) {
            return true;
        }
        return false;
    }

    public int getClusterId() {
        return clusterId;
    }

    public List<TreeNode> getVertices() {
        return vertices;
    }

    public void addNode(TreeNode node) {
        if (size == 0) {
            vertices = new ArrayList<>();
        }
        node.setLevel(height);
        vertices.add(node);
        height += 1;
        size += 1;
    }

    public void addNodes(TreeNode node1, TreeNode node2) {
        if (size == 0) {
            vertices = new ArrayList<>();
        }
        node1.setLevel(height);
        node2.setLevel(height);
        vertices.add(node1);
        vertices.add(node2);
        height += 1;
        size += 2;
    }


}
