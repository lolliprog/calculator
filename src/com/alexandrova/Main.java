package com.alexandrova;
//import java.lang.reflect.Array;


import java.util.*;

public class Main {
    static ArrayList<String> operands = new ArrayList<>();

    static {
        operands.add("+");
        operands.add("-");
        operands.add("*");
        operands.add("/");
    }

    public static void main(String[] args) {
        System.out.println("Input the expression to calculate:");
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();
        char[] chArray = expression.toCharArray();
        Deque<String> deque = new ArrayDeque<>();
        List<String> outStr = new ArrayList<>();
        for (int i = 0; i < chArray.length; i++) {
            char currentChar = chArray[i];
            if (Character.isDigit(currentChar)) {
                outStr.add(String.valueOf(currentChar));
            }
            if (currentChar == '(') {
                deque.add(String.valueOf(currentChar));
            }
            if (currentChar == ')') {
                if (!deque.isEmpty()) {
                    String popped = deque.pollLast();
                    while (popped != null && !popped.equals("(")) {
                        System.out.println("Add to string: " + popped);
                        outStr.add(popped);
                        popped = deque.pollLast();
                    }
//                        else {
//                            deque.removeLast();
//                            //а если это скобка, то мы удаляем оба элемента
//                        }
                } else {
                    System.out.println("Wrong expression!!");
                }
            }


            //operands.add("(");
            //     operands.add(")");
            if (operands.contains(String.valueOf(currentChar))) {
                if (!deque.isEmpty()) {
                    String popOp = deque.pollLast();
                    int priorityP = getPrior(popOp);
                    int priorityA = getPrior(String.valueOf(currentChar));


                    if (priorityA <= priorityP) {
                        outStr.add(popOp);
                        deque.add(String.valueOf(currentChar));
                    } else {
                        deque.add(popOp);
                        deque.add(String.valueOf(currentChar));
                    }

                } else {
                    deque.add(String.valueOf(currentChar));
                }

            }
            System.out.println(outStr + " " + deque);
        }
        for (int i = 0; i < deque.size(); i++) {
            String elFrS = deque.pollLast();
            outStr.add(elFrS);
        }
        System.out.println(outStr);
        attempt = 0;
        calculateIt(outStr);
    }

    private static final int MAX_ATTEMPTS = 100;

    private static void calculateIt(List<String> outStr) {
        float a;
        float b;
        int aPosition;
        int bPosition;
        for (int i = 0; i < outStr.size(); i++) {
            aPosition = i - 2;
            bPosition = i - 1;
            if (operands.contains(outStr.get(i))) {
                if (bPosition >= 0 && aPosition >= 0) {
                    a = Float.parseFloat(outStr.get(aPosition));
                    b = Float.parseFloat(outStr.get(bPosition));
                    float result = operate(outStr.get(i), a, b);
                    System.out.println("Setting up position: " + i + " value: " + String.valueOf(result));
                    outStr.set(i, String.valueOf(result));
                    outStr.set(aPosition, null);
                    outStr.set(bPosition, null);
                    break;
                } else {
                    System.out.println("Error ");
                }
            } else {
                System.out.println(outStr);
            }
        }
        if (outStr.size() > 3) {
            cleanUpList(outStr);
            calculateIt(outStr);
        }
    }

    private static void cleanUpList(List<String> outStr) {
        for (int idx = 0; idx < outStr.size(); idx++) {
            if (outStr.get(idx) == null || outStr.get(idx).isEmpty()) {
                System.out.println("Remove null on index : " + idx);
                outStr.remove(idx);
                cleanUpList(outStr);
            }
        }
        System.out.println("Clean up complete. Result: " + outStr);
    }

    static int attempt;

    private static float operate(String operand, float a, float b) {
        System.out.println("Execute [" + operand + "] on a: " + a + " and b: " + b);
        switch (operand) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) throw new IllegalStateException("Can not divide by 0");
                return a / b;
            default:
                return 0;
        }
    }

    private static int getPrior(String operand) {
        switch (operand) {
            //  case ")":
            //case "(":
            // return 1;
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
                return 3;
            default:
                return 0;
        }
    }
}
