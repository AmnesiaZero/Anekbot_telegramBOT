package utils;

import java.util.Random;

public class RandomGenerator {
    static Random rnd = new Random();
    public static int generateRandomInteger(){
        return rnd.nextInt();
    }
}
