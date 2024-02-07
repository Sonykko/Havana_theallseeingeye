package org.alexdev.havana.dao;

import org.alexdev.havana.dao.mysql.ProductDataDao;

public class ProductData {
    private String saleCode;
    private String name;
    private String description;
    private String dealDescription;

    public ProductData(String saleCode, String name, String description, String dealDescription) {
        this.saleCode = saleCode;
        this.name = name;
        this.description = description;
        this.dealDescription = dealDescription;
    }

    public String getSaleCode() {
        return this.saleCode;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDealDescription() {
        return this.dealDescription;
    }
}
