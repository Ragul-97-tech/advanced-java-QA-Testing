import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<Integer> list  = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        Integer[] ints = list.toArray(new Integer[0]);
        System.out.println();

        ArrayList<String> strings = new ArrayList<>();
        strings.trimToSize();

        System.out.println(new Random().nextInt(1));
    }
}