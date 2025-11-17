package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EquationRenderer {
    public static String render(Equation equation, RenderingOptions options) {
        StringBuilder rendered = new StringBuilder();


        rendered.append(EquationRenderer.renderSide(equation.variableMembers(), equation.nonVariableMembers(), options));
        rendered.append(" = ");
        rendered.append(EquationRenderer.renderSide(equation.variableMembers(), equation.nonVariableMembers(), options));

        return rendered.toString();
    }

    public static HashMap<String, ArrayList<Float>> splitInTwoSides(ArrayList<Float> variableMembers, ArrayList<Float> nonVariableMembers, int quantityOfMembersOn2ndSide) {
        int variableMembersSize = variableMembers.size();
        int nonVariableMembersSize = nonVariableMembers.size();
        int totalSize = variableMembersSize + nonVariableMembersSize;

        if (quantityOfMembersOn2ndSide == 0) {
            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", variableMembers);
                    put("firstSideNonVariableMembers", nonVariableMembers);
                    put("secondSideVariableMembers", new ArrayList<>());
                    put("secondSideNonVariableMembers", new ArrayList<>());
                }
            };
        }

        if (quantityOfMembersOn2ndSide >= totalSize) {
            return new HashMap<>() {
                {
                    put("firstSideVariableMembers", new ArrayList<>());
                    put("firstSideNonVariableMembers", new ArrayList<>());
                    put("secondSideVariableMembers", variableMembers);
                    put("secondSideNonVariableMembers", nonVariableMembers);
                }
            };
        }

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

        if (quantityOfMembersOn2ndSide < nonVariableMembersSize) {
            ArrayList<Float> firstSideNonVariableMembers = new ArrayList<>();
            ArrayList<Float> secondSideNonVariableMembers = new ArrayList<>();
            for (int i = nonVariableMembersSize - 1; i >= nonVariableMembersSize - quantityOfMembersOn2ndSide; i--) {

            }
        }
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
