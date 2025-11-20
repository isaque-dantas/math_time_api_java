package domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class EquationRenderer {
    public static String render(Equation equation, RenderingOptions options) {
        StringBuilder rendered = new StringBuilder();

        HashMap<String, ArrayList<Float>> splitted = splitInTwoSides(equation.variableMembers(), equation.nonVariableMembers(), options.quantityOfMembersOn2ndSide());
        ArrayList<Float> firstSideVariableMembers = splitted.get("firstSideVariableMembers");
        ArrayList<Float> firstSideNonVariableMembers = splitted.get("firstSideNonVariableMembers");
        ArrayList<Float> secondSideVariableMembers = splitted.get("secondSideVariableMembers");
        ArrayList<Float> secondSideNonVariableMembers = splitted.get("secondSideNonVariableMembers");

        rendered.append(EquationRenderer.renderSide(firstSideVariableMembers, firstSideNonVariableMembers, options));
        rendered.append(" = ");
        rendered.append(EquationRenderer.renderSide(secondSideVariableMembers, secondSideNonVariableMembers, options));

        return rendered.toString();
    }

    public static HashMap<String, ArrayList<Float>> splitInTwoSides(ArrayList<Float> variableMembers, ArrayList<Float> nonVariableMembers, int quantityOfMembersOn2ndSide) {
        int variableMembersSize = variableMembers.size();
        int nonVariableMembersSize = nonVariableMembers.size();
        int totalSize = variableMembersSize + nonVariableMembersSize;

        // CASE A
        if (quantityOfMembersOn2ndSide == 0) {
            ArrayList<Float> secondSideNonVariableMembers = new ArrayList<>();
            secondSideNonVariableMembers.add(0F);

            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", variableMembers);
                    put("firstSideNonVariableMembers", nonVariableMembers);
                    put("secondSideVariableMembers", new ArrayList<>());
                    put("secondSideNonVariableMembers", secondSideNonVariableMembers);
                }
            };
        }

        // CASE B
        if (quantityOfMembersOn2ndSide >= totalSize) {
            ArrayList<Float> firstSideNonVariableMembers = new ArrayList<>();
            firstSideNonVariableMembers.add(0F);

            ArrayList<Float> secondSideVariableMembers = new ArrayList<>();
            for (float member : variableMembers) {
                secondSideVariableMembers.add(member * -1);
            }

            ArrayList<Float> secondSideNonVariableMembers = new ArrayList<>();
            for (float member : nonVariableMembers) {
                secondSideNonVariableMembers.add(member * -1);
            }

            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", new ArrayList<>());
                    put("firstSideNonVariableMembers", firstSideNonVariableMembers);
                    put("secondSideVariableMembers", secondSideVariableMembers);
                    put("secondSideNonVariableMembers", secondSideNonVariableMembers);
                }
            };
        }

        // CASE C
        if (quantityOfMembersOn2ndSide == nonVariableMembersSize) {
            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", variableMembers);
                    put("firstSideNonVariableMembers", new ArrayList<>());
                    put("secondSideVariableMembers", new ArrayList<>());
                    put("secondSideNonVariableMembers", nonVariableMembers);
                }
            };
        }

        // CASE D
        if (quantityOfMembersOn2ndSide < nonVariableMembersSize) {
            ArrayList<Float> firstSideNonVariableMembers = new ArrayList<>();
            ArrayList<Float> secondSideNonVariableMembers = new ArrayList<>();

            for (int i = 0; i < nonVariableMembersSize - quantityOfMembersOn2ndSide; i++) {
                firstSideNonVariableMembers.add(nonVariableMembers.get(i));
            }

            for (int i = nonVariableMembersSize - quantityOfMembersOn2ndSide; i < nonVariableMembersSize; i++) {
                secondSideNonVariableMembers.add(nonVariableMembers.get(i) * -1);
            }

            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", variableMembers);
                    put("firstSideNonVariableMembers", firstSideNonVariableMembers);
                    put("secondSideVariableMembers", new ArrayList<>());
                    put("secondSideNonVariableMembers", secondSideNonVariableMembers);
                }
            };
        }

        // CASE E
        // The last case: quantitOfMembersOn2ndSize < totalSize

        ArrayList<Float> firstSideVariableMembers = new ArrayList<>();
        ArrayList<Float> secondSideVariableMembers = new ArrayList<>();

        int quantityOfVariableMembersOn2ndSide = quantityOfMembersOn2ndSide - nonVariableMembersSize;

        for (int i = 0; i < variableMembersSize - quantityOfVariableMembersOn2ndSide; i++) {
            firstSideVariableMembers.add(variableMembers.get(i));
        }

        for (int i = variableMembersSize - quantityOfVariableMembersOn2ndSide; i < variableMembersSize; i++) {
            secondSideVariableMembers.add(variableMembers.get(i) * -1);
        }

        return new HashMap<>() {
            {
                put("firstSideVariableMembers", firstSideVariableMembers);
                put("firstSideNonVariableMembers", new ArrayList<>());
                put("secondSideVariableMembers", secondSideVariableMembers);
                put("secondSideNonVariableMembers", nonVariableMembers);
            }
        };
    }

    public static String renderSide(ArrayList<Float> variableMembers, ArrayList<Float> nonVariableMembers, RenderingOptions options) {
        StringBuilder rendered = new StringBuilder();

        for (int i = 0; i < variableMembers.size(); i++) {
            String renderedMember = EquationRenderer.renderMember(
                    variableMembers.get(i),
                    String.valueOf(options.variableRepresentation()),
                    i == 0
            );
            rendered.append(renderedMember);
        }

        for (Float nonVariableMember : nonVariableMembers) {
            String renderedMember = EquationRenderer.renderMember(
                    nonVariableMember,
                    "",
                    rendered.isEmpty()
            );
            rendered.append(renderedMember);
        }

        return rendered.toString();
    }

    public static String renderMember(float member, String variableRepresentation, boolean isFirst) {
        if (isFirst) return EquationRenderer.renderValue(member) + variableRepresentation;

        char signal = member > 0 ? '+' : '-';
        return " " +
                signal +
                " " +
                EquationRenderer.renderValue(Math.abs(member)) +
                variableRepresentation;
    }

    public static String renderValue(float value) {
        if (value == (int) value) return String.valueOf((int) value);
        return String.valueOf(value);
    }
}
