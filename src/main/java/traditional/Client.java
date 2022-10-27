package traditional;

public class Client {
    public static void main(String... args) {
        Math math = new Math();
        long first = 100;
        long second = 50;

        long sum = math.add(first, second);
        System.out.println("Sum of " + first + " and " + second + " is " + sum);

        long diff = math.subtract(first, second);
        System.out.println("Difference of " + first + " and " + second + " is " + diff);
    }
}
