package org.capstone.mtgwizard.domain.model;

public class Card implements Comparable<Card> {

    // Name of card
    private String name;

    // Price on cardkingdom.com
    private float cardKingdomPrice;
    // Price on tcgplayer.com
    private float tcgPlayerPrice;

    // Cost of casting a card
    private String manaCost;
    // Rules text for playing card
    private String rulesText;
    // Set that card was printed in (Cards in a set share a setting/theme and are released together every few months)
    private String set;
    // Type of card
    private String type;
    // Uuid used to identify card in database
    private String uuid;

    public Card(String name, float cardKingdomprice, float TCGPlayerprice, String manaCost, String rulesText, String set, String type, String uuid) {
        this.name = name;
        this.cardKingdomPrice = cardKingdomprice;
        this.tcgPlayerPrice = TCGPlayerprice;
        this.manaCost = manaCost;
        this.rulesText = rulesText;
        this.set = set;
        this.type = type;
        this.uuid = uuid;
    }

    // Adding compare method to sort by name
    @Override
    public int compareTo(Card o) {
        return this.name.compareTo(o.name);
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
