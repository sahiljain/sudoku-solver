import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    static Random rand = new Random();

    public static void main(String[] args) {
//        int[][] emptyBoard = {
//                {8,0,1,5,0,0,0,0,0},
//                {0,0,0,9,0,0,1,2,0},
//                {0,0,0,0,8,0,0,0,4},
//                {0,0,4,0,0,1,0,7,0},
//                {0,1,0,0,5,0,0,9,0},
//                {0,6,0,2,0,0,3,0,0},
//                {9,0,0,0,6,0,0,0,0},
//                {0,7,3,0,0,8,0,0,0},
//                {0,0,0,0,0,5,9,0,3}
//        };
        int[][] emptyBoard = {
            {1,0,0,0,0,6,5,0,0},
            {2,0,0,0,0,4,6,0,8},
            {0,0,0,0,8,0,0,0,1},
            {0,2,4,0,1,0,0,0,0},
            {0,0,0,8,0,3,0,0,0},
            {0,0,0,0,7,0,1,3,0},
            {8,0,0,0,9,0,0,0,0},
            {6,0,9,5,0,0,0,0,7},
            {0,0,7,2,0,0,0,0,5}
        };
        printSolved(emptyBoard);
    }

    private static void printSolved(int[][] emptyBoard) {
        // start with a random guess
        int[][] currentGuess = mutate(mutate(getGuess(emptyBoard), emptyBoard), emptyBoard);
        int currentCost = cost(currentGuess);

        printArr(emptyBoard);

        double temp = 0.5;
        int numIter = 0;

        while (currentCost > 0) {
            int[][] nextGuess = mutate(currentGuess, emptyBoard);
            int nextCost = cost(nextGuess);
            int costIncrease = nextCost - currentCost;
            if (costIncrease <= 0) {
                currentCost = nextCost;
                currentGuess = nextGuess;
            } else {
                double acceptanceProb = Math.exp(-costIncrease/temp);
                if (rand.nextDouble() < acceptanceProb) {
                    currentCost = nextCost;
                    currentGuess = nextGuess;
                }
            }
            if (numIter % 1000 == 0) {
                if (temp > 0.001) {
                    temp *= 0.999;
                } else {
                    System.out.println("Could not find solution.");
                    break;
                }

            }
            numIter++;
            if (numIter % 10000 == 0) {
                System.out.println(numIter + ",\t" + currentCost + ",\t " + temp);
            }
        }

        System.out.println(numIter);

        printArr(currentGuess);
    }

    private static int[][] mutate(int[][] guess, int[][] emptyBoard) {
        // pick a random 3x3 block
        int blockI = rand.nextInt(3)*3;
        int blockJ = rand.nextInt(3)*3;

        ArrayList<Point> pointArr = new ArrayList<>();

        for (int i = blockI; i < blockI + 3; i++) {
            for (int j = blockJ; j < blockJ + 3; j++) {
                if (emptyBoard[i][j] == 0) {
                    pointArr.add(new Point(i, j));
                }
            }
        }

        int firstIndex = rand.nextInt(pointArr.size());
        int secondIndex = firstIndex;
        while (secondIndex == firstIndex) {
            secondIndex = rand.nextInt(pointArr.size());
        }

        int firstX = pointArr.get(firstIndex).x;
        int firstY = pointArr.get(firstIndex).y;

        int secondX = pointArr.get(secondIndex).x;
        int secondY = pointArr.get(secondIndex).y;


        int[][] out = new int[guess.length][];
        for (int i = 0; i < guess.length; i++) {
            out[i] = guess[i].clone();
        }
        int temp = out[firstX][firstY];
        out[firstX][firstY] = out[secondX][secondY];
        out[secondX][secondY] = temp;
        return out;
    }

    private static int cost(int[][] guess) {
        int cost = 162;
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[9];
            boolean[] isDuplicate = new boolean[9];
            boolean[] isDuplicateCol = new boolean[9];
            boolean[] seenCol = new boolean[9];
            for (int col = 0; col < 9; col++) {
                if (!seen[guess[row][col] - 1]) {
                    seen[guess[row][col] - 1] = true;
                } else {
                    isDuplicate[guess[row][col] - 1] = true;
                }
                if (!seenCol[guess[col][row] - 1]) {
                    seenCol[guess[col][row] - 1] = true;
                } else {
                    isDuplicateCol[guess[col][row] - 1] = true;
                }
            }
            for (boolean b : isDuplicate) {
                if (!b) {
                    cost--;
                }
            }
            for (boolean b : isDuplicateCol) {
                if (!b) {
                    cost--;
                }
            }
        }
        return cost;
    }

    private static void printArr(int[][] arr) {
        for (int[] line : arr) {
            for (int x : line) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }

    private static int[][] getGuess(int[][] emptyBoard) {
        int[][] guess = new int[emptyBoard.length][];
        for (int i = 0; i < emptyBoard.length; i++) {
            guess[i] = emptyBoard[i].clone();
        }

        for (int bigI = 0; bigI < 9; bigI+=3) {
            for (int bigJ = 0; bigJ < 9; bigJ+=3) {
                // populate top left 3x3 block
                boolean[] numUsed = new boolean[10];
                for (int i = bigI; i < bigI + 3; i++) {
                    for (int j = bigJ; j < bigJ + 3; j++) {
                        numUsed[emptyBoard[i][j]] = true;
                    }
                }

                for (int i = bigI; i < bigI + 3; i++) {
                    for (int j = bigJ; j < bigJ + 3; j++) {
                        for (int num = 1; num < 10; num++) {
                            if (!numUsed[num] && guess[i][j] == 0) {
                                guess[i][j] = num;
                                numUsed[num] = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return guess;
    }
}
