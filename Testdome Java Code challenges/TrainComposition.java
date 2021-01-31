import java.util.LinkedList;

public class TrainComposition {
    private LinkedList<Integer> wagons = new LinkedList<Integer>();

    public void attachWagonFromLeft(int wagonId) {
        wagons.addFirst(wagonId);
    }

    public void attachWagonFromRight(int wagonId) {
        wagons.addLast(wagonId);
    }

    public int detachWagonFromLeft() {
        Integer wagondetached=wagons.getFirst();
        wagons.removeFirst();
        return wagondetached;
    }

    public int detachWagonFromRight() {
        Integer wagondetached=wagons.getLast();
        wagons.removeLast();
        return wagondetached;
    }

    public static void main(String[] args) {
        TrainComposition train = new TrainComposition();
        train.attachWagonFromLeft(7);
        train.attachWagonFromLeft(13);
        System.out.println(train.detachWagonFromRight()); // 7
        System.out.println(train.detachWagonFromLeft()); // 13
    }
}