package domain;

import java.util.Random;
import java.util.ArrayList;

public class EquationBuilder {

    public EquationBuilder() {
        System.out.println("abcde");
    }

    public static Equation build(BuildingOptions options) {
        if (options.variableMembersMinQuantity() <= 0) {
            throw new IllegalArgumentException("The equation must have at least one variable member, so BuildingOptions.variableMembersMinQuantity must be greater than 0.");
        }

        Random random = new Random();

        int variableMembersQuantity = random.nextInt(options.variableMembersMinQuantity(), options.variableMembersMaxQuantity());
        int nonVariableMembersQuantity = random.nextInt(options.nonVariableMembersMinQuantity(), options.nonVariableMembersMaxQuantity());

        ArrayList<Float> variableMembers = new ArrayList<>();
        float variableMembersSum = 0;
        while (true) {
            for (int i = 0; i < variableMembersQuantity; i++) {
                float value = random.nextInt(options.coefficientsMinValue(), options.coefficientsMaxValue());
                variableMembers.add(value);
                variableMembersSum += value;
            }

            if (variableMembersSum != 0) break;
            variableMembersSum = 0;
            variableMembers.clear();
        }

        ArrayList<Float> nonVariableMembers = new ArrayList<>();
        for (int i = 0; i < nonVariableMembersQuantity; i++) {
            float value = random.nextFloat(options.coefficientsMinValue(), options.coefficientsMaxValue());
            nonVariableMembers.add(value);
        }

        float answer = EquationBuilder.getAnswer(variableMembers, nonVariableMembers);
        return new Equation(variableMembers, nonVariableMembers, answer);
    }

    public static float getAnswer(ArrayList<Float> variableMembers, ArrayList<Float> nonVariableMembers) {
        float variableMembersSum = 0;
        for (Float member : variableMembers) variableMembersSum += member;

        if (variableMembersSum == 0) {
            throw new ArithmeticException("Variable members sum is zero, so it is not a first degree equation.");
        }

        float nonVariableMembersSum = 0;
        for (Float member : nonVariableMembers) nonVariableMembersSum += member;

        if (nonVariableMembersSum == 0) return 0F;

        return -1 * nonVariableMembersSum / variableMembersSum;
    }
}

// 10x + 2 = 3 - 4x
// 10x + 4x + 2 - 3 = 0
// [1,   1,   0,  0]

// ax + bx + c + d = 0
// x(a+b) = - c - d

// x = -(c + d) / (a + b)