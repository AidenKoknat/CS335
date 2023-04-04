public class Dice_Koknat {
    static int RollDice() {
        int roll = (int)(Math.random()*6) + 1;
        return roll;
    }
    static int snakeEyesCounter(int attempts) {
        int sum = 0;
        for (int i = 0; i < attempts; i++) {
            if (RollDice() + RollDice() + RollDice() + RollDice() + RollDice() + RollDice() == 6) {
                sum++;
            }
        }
        return sum;
    }
    public static void main(String[] args) {
        System.out.printf("%d\n", snakeEyesCounter(100));
        System.out.printf("%d\n", snakeEyesCounter(1000));
        System.out.printf("%d\n", snakeEyesCounter(10000));
        System.out.printf("%d\n", snakeEyesCounter(100000));
    }
}
