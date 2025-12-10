package tests;

import domain.EquationBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.Member;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EquationBuilderTests {
    @Test
    void testGetAnswer() {
        List<Member> members = List.of(
                new Member(true, 1.0),
                new Member(true, 2.0),
                new Member(false, 3.0)
        );

        double answer = EquationBuilder.getAnswer(members);

        assertEquals(-1.0, answer);
    }

    @Test
    void testGetAnswer_WithEmptyNonVariableMembers() {
        List<Member> members = List.of(
                new Member(true, 1.0),
                new Member(true, 2.0)
        );

        double answer = EquationBuilder.getAnswer(members);
        assertEquals(0.0, answer);
    }

    @Test
    void testGetAnswer_WithEmptyVariableMembers() {
        List<Member> members = List.of(
                new Member(false, 1.0),
                new Member(false, 2.0)
        );

        assertThrows(
                ArithmeticException.class,
                () -> EquationBuilder.getAnswer(members)
        );
    }
}
