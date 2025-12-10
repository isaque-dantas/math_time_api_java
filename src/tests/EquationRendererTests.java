package tests;

import domain.Member;
import domain.EquationRenderer;
import domain.Equation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.RenderingOptions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class EquationRendererTests {
    List<Member> zeroSide = List.of(new Member(false, 0));

    List<Member> defaultMembers = List.of(
            new Member(true, 2.0),
            new Member(true, 1.0),
            new Member(false, 3.0),
            new Member(false, 4.0)
    );

    Equation defaultEquation = new Equation(defaultMembers, 0);
    RenderingOptions defaultRenderingOptions = new RenderingOptions('x', false, 0);

    @Test
    void testRender_AllPositive() {
        String renderedEquation = EquationRenderer.render(defaultEquation, defaultRenderingOptions);
        assertEquals("2x + 1x + 3 + 4 = 0", renderedEquation);
    }

    @Test
    void testRender_WithNegative() {
        List<Member> members = List.of(
                new Member(true, 2.0),
                new Member(true, 1.0),
                new Member(false, -2.0),
                new Member(false, 3.0)
        );

        Equation equation = new Equation(members, 0);

        String renderedEquation = EquationRenderer.render(equation, defaultRenderingOptions);
        assertEquals("2x + 1x - 2 + 3 = 0", renderedEquation);
    }

    @Test
    void testRender_WithEmptyNonVariableMembers() {
        List<Member> members = List.of(
                new Member(true, 2.0),
                new Member(true, 1.0)
        );

        Equation equation = new Equation(members, 0);
        String renderedEquation = EquationRenderer.render(equation, defaultRenderingOptions);
        assertEquals("2x + 1x = 0", renderedEquation);
    }

    @Test
    void testRender_WithEmptyVariableMembers() {
        List<Member> members = List.of(
                new Member(false, 3.0),
                new Member(false, 4.0)
        );

        Equation equation = new Equation(members, 0);
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
    void testSplitInTwoSides_NoMembersOn2ndSide() {
        // Act
        Map<String, List<Member>> split = EquationRenderer.splitInTwoSides(defaultEquation.members(), 0);
        var firstSide = split.get("firstSide");
        var secondSide = split.get("secondSide");

        // Assert
        assertEquals(defaultEquation.members(), firstSide);
        assertEquals(zeroSide, secondSide);
    }

    @Test
    void testSplitInTwoSides_AllMembersOn2ndSide() {
        // Arrange
        var expectedSecondSide = List.of(
                new Member(true, -2.0),
                new Member(true, -1.0),
                new Member(false, -3.0),
                new Member(false, -4.0)
        );

        // Act
        var split = EquationRenderer.splitInTwoSides(defaultEquation.members(), 10);
        var firstSide = split.get("firstSide");
        var secondSide = split.get("secondSide");

        // Assert
        assertEquals(zeroSide, firstSide);
        assertEquals(expectedSecondSide, secondSide);
    }

    @Test
    void testSplitInTwoSides_SomeMembersOnEitherSide() {
        // Arrange
        var expectedFirstSide = List.of(
                new Member(true, 2.0),
                new Member(true, 1.0),
                new Member(false, 3.0)
        );

        var expectedSecondSide = List.of(new Member(false, -4.0));

        // Act
        var split = EquationRenderer.splitInTwoSides(defaultEquation.members(), 1);
        var firstSide = split.get("firstSide");
        var secondSide = split.get("secondSide");

        // Assert
        assertEquals(expectedFirstSide, firstSide);
        assertEquals(expectedSecondSide, secondSide);
    }
}
