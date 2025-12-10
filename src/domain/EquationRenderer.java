package domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EquationRenderer {
    public static String render(Equation equation, RenderingOptions options) {
        StringBuilder rendered = new StringBuilder();

        Map<String, List<Member>> split = splitInTwoSides(equation.members(), options.quantityOfMembersOn2ndSide());
        List<Member> firstSide = split.get("firstSide");
        List<Member> secondSide = split.get("secondSide");

        rendered.append(renderSide(firstSide, options));
        rendered.append(" = ");
        rendered.append(renderSide(secondSide, options));

        return rendered.toString();
    }

    public static Map<String, List<Member>> splitInTwoSides(List<Member> members, int quantityOfMembersOn2ndSide) {
        final List<Member> zeroSide = List.of(new Member(false, 0));

        // CASE A
        if (quantityOfMembersOn2ndSide == 0) {
            return Map.of(
                    "firstSide", members,
                    "secondSide", zeroSide
            );
        }

        // CASE B
        if (quantityOfMembersOn2ndSide >= members.size()) {
            final List<Member> membersWithOppositeSignal = (
                    members
                            .stream()
                            .map(EquationRenderer::getMemberWithOppositeSignal)
                            .toList()
            );

            return Map.of(
                    "firstSide", zeroSide,
                    "secondSide", membersWithOppositeSignal
            );
        }

        // CASE C
        final List<Member> firstSide = members.subList(0, members.size() - quantityOfMembersOn2ndSide);
        final List<Member> secondSide = (
                members
                        .subList(members.size() - quantityOfMembersOn2ndSide, members.size())
                        .stream()
                        .map(EquationRenderer::getMemberWithOppositeSignal)
                        .toList()
        );

        return Map.of(
                "firstSide", firstSide,
                "secondSide", secondSide
        );
    }

    public static Member getMemberWithOppositeSignal(Member member) {
        return new Member(member.has_variable(), member.value() * -1);
    }

    public static String renderSide(List<Member> members, RenderingOptions options) {
        if (members.isEmpty()) return "";

        final String firstRenderedMember = renderMember(
                members.getFirst(),
                getRepresentationForMember(members.getFirst(), options.variableRepresentation()),
                true
        );

        final String otherRenderedMembers = members
                .subList(1, members.size())
                .stream()
                .map(member -> renderMember(
                        member,
                        getRepresentationForMember(member, options.variableRepresentation()),
                        false)
                )
                .collect(Collectors.joining());

        return firstRenderedMember + otherRenderedMembers;
    }

    private static String getRepresentationForMember(Member member, Character representation) {
        return member.has_variable() ? String.valueOf(representation) : "";
    }

    public static String renderMember(Member member, String variableRepresentation, boolean isFirst) {
        if (isFirst) return renderValue(member.value()) + variableRepresentation;

        char signal = member.value() > 0 ? '+' : '-';
        return " " +
                signal +
                " " +
                renderValue(Math.abs(member.value())) +
                variableRepresentation;
    }

    public static String renderValue(double value) {
        if (value == (int) value) return String.valueOf((int) value);
        return String.valueOf(value);
    }
}
