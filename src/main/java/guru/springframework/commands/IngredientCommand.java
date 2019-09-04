package guru.springframework.commands;

import java.math.BigDecimal;

public class IngredientCommand {
    private Long id;
    private Long recipeId;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand unitOfMeasureCommand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UnitOfMeasureCommand getUnitOfMeasure() {
        return unitOfMeasureCommand;
    }

    public void setUnitOfMeasure(UnitOfMeasureCommand unitOfMeasure) {
        this.unitOfMeasureCommand = unitOfMeasure;
    }
}
