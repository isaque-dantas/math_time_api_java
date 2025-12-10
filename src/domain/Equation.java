package domain;

import java.util.List;

public record Equation(
        List<Member> members,
        double answer
) {
}
