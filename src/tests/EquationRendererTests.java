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
}
