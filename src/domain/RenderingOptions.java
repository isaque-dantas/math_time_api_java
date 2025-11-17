package domain;

public record RenderingOptions(
        char variableRepresentation,
        boolean shouldMixAllRandomly,
        int quantityOfMembersOn2ndSide
) { }
