package domain;

public record BuildingOptions (
    int variableMembersMinQuantity,
    int variableMembersMaxQuantity,
    int nonVariableMembersMinQuantity,
    int nonVariableMembersMaxQuantity,
    int coefficientsMinValue,
    int coefficientsMaxValue
) { }
