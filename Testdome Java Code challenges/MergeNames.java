import java.util.HashSet;
public class MergeNames {

    public static String[] uniqueNames(String[] names1, String[] names2) {
        HashSet<String> merged= new HashSet<String>();
        for (int i=0; i<names1.length;i++){
            merged.add(names1[i]);
        }
        for (int i=0; i<names2.length;i++){
            merged.add(names2[i]);
        }
        return merged.toArray(new String[merged.size()]);
    }

    public static void main(String[] args) {
        String[] names1 = new String[] {"Ava", "Emma", "Olivia"};
        String[] names2 = new String[] {"Olivia", "Sophia", "Emma"};
        System.out.println(String.join(", ", MergeNames.uniqueNames(names1, names2))); // should print Ava, Emma, Olivia, Sophia
    }
}