import java.util.Comparator;

public class MinAvScore implements Comparator<Zachetka> {

    @Override
    public int compare(Zachetka o1, Zachetka o2) {
        return Double.compare(o1.get_av_sc_All(), o2.get_av_sc_All());
    }
}
