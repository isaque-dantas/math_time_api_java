package tests;

import domain.EquationRenderer;
import domain.Equation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.RenderingOptions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EquationRendererTests {
    ArrayList<Float> variableMembers = new ArrayList<>() {
        {
            add(2F);
            add(1F);
        }
    };

    ArrayList<Float> nonVariableMembers = new ArrayList<>() {
        {
            add(3F);
            add(4F);
        }
    };
    ArrayList<Float> membersWithZero = new ArrayList<>() {
        {
            add(0F);
        }
    };
    ArrayList<Float> emptyArray = new ArrayList<>();

    Equation defaultEquation = new Equation(variableMembers, nonVariableMembers, 0);
    RenderingOptions defaultRenderingOptions = new RenderingOptions('x', false, 0);

    @Test
    void testRender_AllPositive() {
        String renderedEquation = EquationRenderer.render(defaultEquation, defaultRenderingOptions);
        assertEquals("2x + 1x + 3 + 4 = 0", renderedEquation);
    }

    @Test
    void testRender_WithNegative() {
        ArrayList<Float> nonVariableMembers = new ArrayList<>();
        nonVariableMembers.add(-2F);
        nonVariableMembers.add(3F);

        Equation equation = new Equation(variableMembers, nonVariableMembers, 0);

        String renderedEquation = EquationRenderer.render(equation, defaultRenderingOptions);
        assertEquals("2x + 1x - 2 + 3 = 0", renderedEquation);
    }

    @Test
    void testRender_WithEmptyNonVariableMembers() {
        Equation equation = new Equation(variableMembers, new ArrayList<>(), 0);
        String renderedEquation = EquationRenderer.render(equation, defaultRenderingOptions);
        assertEquals("2x + 1x = 0", renderedEquation);
    }

    @Test
    void testRender_WithEmptyVariableMembers() {
        Equation equation = new Equation(new ArrayList<>(), nonVariableMembers, 0);
        String renderedEquation = EquationRenderer.render(equation, defaultRenderingOptions);
        assertEquals("3 + 4 = 0", renderedEquation);
    }

    @Test
    void testRender_WithManySides() {
        RenderingOptions options = new RenderingOptions('x', false, 1);
        String renderedEquation = EquationRenderer.render(defaultEquation, options);
        assertEquals("2x + 1x + 3 = -4", renderedEquation);
    }

    @Test
    void testSplitInTwoSides_CaseA() {
        // Act
        var split = EquationRenderer.splitInTwoSides(defaultEquation.variableMembers(), defaultEquation.nonVariableMembers(), 0);
        var firstSideVariableMembers = split.get("firstSideVariableMembers");
        var firstSideNonVariableMembers = split.get("firstSideNonVariableMembers");
        var secondSideVariableMembers = split.get("secondSideVariableMembers");
        var secondSideNonVariableMembers = split.get("secondSideNonVariableMembers");

        // Assert
        assertEquals(defaultEquation.variableMembers(), firstSideVariableMembers);
        assertEquals(defaultEquation.nonVariableMembers(), firstSideNonVariableMembers);
        assertEquals(emptyArray, secondSideVariableMembers);
        assertEquals(membersWithZero, secondSideNonVariableMembers);
    }

    @Test
    void testSplitInTwoSides_CaseB() {
        // Arrange
        var expectedSecondSideVariableMembers = new ArrayList<Float>();
        expectedSecondSideVariableMembers.add(-2F);
        expectedSecondSideVariableMembers.add(-1F);

        var expectedSecondSideNonVariableMembers = new ArrayList<Float>();
        expectedSecondSideNonVariableMembers.add(-3F);
        expectedSecondSideNonVariableMembers.add(-4F);

        // Act
        var split = EquationRenderer.splitInTwoSides(defaultEquation.variableMembers(), defaultEquation.nonVariableMembers(), 10);
        var firstSideVariableMembers = split.get("firstSideVariableMembers");
        var firstSideNonVariableMembers = split.get("firstSideNonVariableMembers");
        var secondSideVariableMembers = split.get("secondSideVariableMembers");
        var secondSideNonVariableMembers = split.get("secondSideNonVariableMembers");

        // Assert
        assertEquals(emptyArray, firstSideVariableMembers);
        assertEquals(membersWithZero, firstSideNonVariableMembers);
        assertEquals(expectedSecondSideVariableMembers, secondSideVariableMembers);
        assertEquals(expectedSecondSideNonVariableMembers, secondSideNonVariableMembers);
    }

    @Test
    void testSplitInTwoSides_CaseD() {
        // Arrange
        var expectedFirstSideVariableMembers = new ArrayList<Float>();
        expectedFirstSideVariableMembers.add(2F);
        expectedFirstSideVariableMembers.add(1F);

        var expectedFirstSideNonVariableMembers = new ArrayList<Float>();
        expectedFirstSideNonVariableMembers.add(3F);

        var expectedSecondSideNonVariableMembers = new ArrayList<Float>();
        expectedSecondSideNonVariableMembers.add(-4F);

        // Act
        var split = EquationRenderer.splitInTwoSides(defaultEquation.variableMembers(), defaultEquation.nonVariableMembers(), 1);
        var firstSideVariableMembers = split.get("firstSideVariableMembers");
        var firstSideNonVariableMembers = split.get("firstSideNonVariableMembers");
        var secondSideVariableMembers = split.get("secondSideVariableMembers");
        var secondSideNonVariableMembers = split.get("secondSideNonVariableMembers");

        // Assert
        assertEquals(expectedFirstSideVariableMembers, firstSideVariableMembers);
        assertEquals(expectedFirstSideNonVariableMembers, firstSideNonVariableMembers);
        assertEquals(emptyArray, secondSideVariableMembers);
        assertEquals(expectedSecondSideNonVariableMembers, secondSideNonVariableMembers);
    }
}
