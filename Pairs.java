
public class Pairs {
    public double value;
    public int x;
    public int y;

    public Pairs(double val, int x, int y)
    {
        this.value = val;
        this.x = x;
        this.y = y;
    }
    public Pairs(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Pairs)
        {
            sameSame = (this.x == ((Pairs) object).x && this.y == ((Pairs) object).y);
        }

        return sameSame;
    }


}
