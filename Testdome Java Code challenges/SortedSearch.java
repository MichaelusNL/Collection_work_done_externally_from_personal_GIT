public class SortedSearch {
    public static int countNumbers(int[] sortedArray, int lessThan) {
        int amount=0;
        for (int i=0;i<sortedArray.length;i++){
            if (sortedArray[i]<lessThan){
                amount++;
            }

        }            return amount;}


    public static void main(String[] args) {
        System.out.println(SortedSearch.countNumbers(new int[] { 1, 3, 5, 7 }, 4));
    }
}