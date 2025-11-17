package domain;

import java.util.ArrayList;

public record Equation(
        ArrayList<Float> variableMembers,
        ArrayList<Float> nonVariableMembers,
        float answer
) {
}
