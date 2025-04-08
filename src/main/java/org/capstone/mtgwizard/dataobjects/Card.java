package org.capstone.mtgwizard.dataobjects;

public class Card {

    private float cardKingdomPrice;
    private String name;
    private float tcgPlayerPrice;
    private String manaCost;
    private String rulesText;
    private String set;
    private String type;
    private String uuid;

    public Card(String name, float cardKingdomprice, float TCGPlayerprice, String manaCost, String rulesText, String set, String type) {
        this.name = name;
        this.cardKingdomPrice = cardKingdomprice;
        this.tcgPlayerPrice = TCGPlayerprice;
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

    public float getCardKingdomPrice() {
        return cardKingdomPrice;
    }

    public void setCardKingdomPrice(float TCGPlayerprice) {
        this.cardKingdomPrice = TCGPlayerprice;
    }

    public float getTCGPlayerPrice() {
        return tcgPlayerPrice;
    }

    public void setTCGPlayerPrice(float TCGPlayerPrice) {
        this.tcgPlayerPrice = TCGPlayerPrice;
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
