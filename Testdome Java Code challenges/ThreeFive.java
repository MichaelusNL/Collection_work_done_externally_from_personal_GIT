import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.List;

class ThreeFive {

    static List<String> doThreeFive(int max) {
        List<String> list_of_digits_except_three_five = new ArrayList();
        for (int i=0; i<max; i++){
            if ((i+1)%5==0 && (i+1)%3==0){
                list_of_digits_except_three_five.add("threefive");
            }
            else if ((i+1)%5==0){
                list_of_digits_except_three_five.add("five");
            }
            else if ((i+1)%3==0){
                list_of_digits_except_three_five.add("three");
            }
            else{
                list_of_digits_except_three_five.add(String.valueOf(i+1));
            }
        }
        return list_of_digits_except_three_five;
    }

    public static void main(String[] args) {
        List <String> output = doThreeFive(16);
        System.out.println(output);


    }
}