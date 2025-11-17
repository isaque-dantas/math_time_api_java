package tests;

import domain.EquationBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EquationBuilderTests {
    @Test
    void testGetAnswer() {
        ArrayList<Float> variableMembers = new ArrayList<>();
        variableMembers.add(1F);
        variableMembers.add(2F);

        ArrayList<Float> nonVariableMembers = new ArrayList<>();
        nonVariableMembers.add(3F);

        float answer = EquationBuilder.getAnswer(variableMembers, nonVariableMembers);

        assertEquals(-1F, answer);
    }

    @Test
    void testGetAnswer_WithEmptyNonVariableMembers() {
        ArrayList<Float> variableMembers = new ArrayList<>();
        variableMembers.add(1F);
        variableMembers.add(2F);

        ArrayList<Float> nonVariableMembers = new ArrayList<>();

        float answer = EquationBuilder.getAnswer(variableMembers, nonVariableMembers);

        assertEquals(0F, answer);
    }

    @Test
    void testGetAnswer_WithEmptyVariableMembers() {
        ArrayList<Float> variableMembers = new ArrayList<>();

        ArrayList<Float> nonVariableMembers = new ArrayList<>();
        nonVariableMembers.add(1F);
        nonVariableMembers.add(2F);

        assertThrows(
                ArithmeticException.class,
                () -> EquationBuilder.getAnswer(variableMembers, nonVariableMembers)
        );
    }
}
