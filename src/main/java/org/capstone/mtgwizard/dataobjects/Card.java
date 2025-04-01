package org.capstone.mtgwizard.dataobjects;

public class Card {

    private String name;
    private float price;
    private String manaCost;
    private String rulesText;
    private String set;
    private String type;

    public Card(String name, float price, String manaCost, String rulesText, String set, String type) {
        this.name = name;
        this.price = price;
        this.manaCost = manaCost;
        this.rulesText = rulesText;
        this.set = set;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public String getRulesText() {
        return rulesText;
    }

    public void setRulesText(String rulesText) {
        this.rulesText = rulesText;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
