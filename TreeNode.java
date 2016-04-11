public class TreeNode {

    private int id;
    private int level;

    public TreeNode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLevel(int level) {
        this.level = level;
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
