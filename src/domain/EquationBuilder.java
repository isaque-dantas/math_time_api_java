package domain;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EquationBuilder {

    public EquationBuilder() {
        System.out.println("abcde");
    }

    public static Equation build(BuildingOptions options) {
        if (options.variableMembersMinQuantity() <= 0) {
            throw new IllegalArgumentException("The equation must have at least one variable member, so BuildingOptions.variableMembersMinQuantity must be greater than 0.");
        }

        Random random = new Random();

        final int variableMembersQuantity = random.nextInt(
                options.variableMembersMinQuantity(),
                options.variableMembersMaxQuantity()
        );

        final int nonVariableMembersQuantity = random.nextInt(
                options.nonVariableMembersMinQuantity(),
                options.nonVariableMembersMaxQuantity()
        );

        final int totalSize = variableMembersQuantity + nonVariableMembersQuantity;

        List<Boolean> membersHaveVariable = EquationBuilder.getHasVariableForMembersSize(variableMembersQuantity, nonVariableMembersQuantity);

        List<Member> members = new ArrayList<>();
        float variableMembersSum = 0;
        while (true) {
            for (int i = 0; i < totalSize; i++) {
                float value = random.nextInt(options.coefficientsMinValue(), options.coefficientsMaxValue());
                boolean hasVariable = membersHaveVariable.get(i);

                members.add(new Member(hasVariable, value));
                if (hasVariable) variableMembersSum += value;
            }

            if (variableMembersSum != 0) break;
            variableMembersSum = 0;
            members.clear();
        }

        double answer = EquationBuilder.getAnswer(members);
        return new Equation(members, answer);
    }

    private static List<Boolean> getHasVariableForMembersSize(int variableMembersQuantity, int nonVariableMembersQuantity) {
        final int hasVariableCount = variableMembersQuantity + 1;
        final int hasNoVariableCount = nonVariableMembersQuantity + 1;
        final int totalSize = hasVariableCount + hasNoVariableCount;

        List<Boolean> hasVariable = (
                IntStream
                        .range(0, totalSize)
                        .mapToObj(i -> i < hasVariableCount)
                        .collect(Collectors.toList())
        );

        Collections.shuffle(hasVariable);
        return hasVariable;
    }

    public static double getAnswer(List<Member> members) {
        final double variableMembersSum = (
                members
                        .stream()
                        .filter(Member::has_variable)
                        .mapToDouble(Member::value)
                        .sum()
        );

        if (variableMembersSum == 0) {
            throw new ArithmeticException("Variable members sum is zero, so it is not a first degree equation.");
        }

        final double nonVariableMembersSum = (
                members
                        .stream()
                        .filter(member -> !member.has_variable())
                        .mapToDouble(Member::value)
                        .sum()
        );

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