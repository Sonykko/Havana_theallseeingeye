package org.alexdev.havana;

import org.alexdev.havana.dao.FurniData;
import org.alexdev.havana.dao.ProductData;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class FileFormatter {
    public static String formatForExternalTexts(List<Pair<String, String>> externalTexts) {
        var stringBuilder = new StringBuilder();

        for(var pair : externalTexts) {
            var entry = pair.getKey();
            var text = pair.getValue();

            if(entry.contains("\n")) {
                entry = entry.replace("\n", "\\n");
            }

            if(text.contains("\n")) {
                text = text.replace("\n", "\\n");
            }

            if(entry.contains("\r")) {
                entry = entry.replace("\r", "\\r");
            }

            if(text.contains("\r")) {
                text = text.replace("\r", "\\r");
            }

            stringBuilder.append(entry);
            stringBuilder.append("=");
            stringBuilder.append(text);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static String formatForProductData(List<ProductData> productData) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("[[");
        var count = 1;
        var total = 0;
        for(var product  : productData) {
            stringBuilder.append("\"" + product.getSaleCode() + "\",");
            stringBuilder.append("\"" + product.getName() + "\",");
            stringBuilder.append("\"" + product.getDescription() + "\",");
            stringBuilder.append("\"" + product.getDealDescription() + "\"");

            if(count == 101) {
                stringBuilder.append("]]");
                stringBuilder.append("\n");
                stringBuilder.append("[[");
                count = 0;
            } else {
                if(total == productData.size() - 1) {
                    stringBuilder.append("]]");
                    break;
                }
                stringBuilder.append("],");
                stringBuilder.append("[");
            }

            count++;
            total++;
        }

        return stringBuilder.toString();
    }

    public static String formatForFurniData(List<FurniData> furniData) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        var count = 0;
        for(var furni : furniData) {
            stringBuilder.append("[");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getItemType().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getFurniId().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getSaleCode().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getClassId().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getIsGroupable() == true ? "1" : "0");
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append(furni.getIsRecycleable() == true ? "1" : "0");
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getIsTradeable() == true ? "1" : "0");
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getStuffData().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getName().replace("\n", "\\n"));
            stringBuilder.append("\"");
            stringBuilder.append(",");
            stringBuilder.append("\"");
            stringBuilder.append(furni.getDescription().replace("\n", "\\n"));
            stringBuilder.append("\"");

            if(count != furniData.size()-1) {
                stringBuilder.append("],");
            } else {
                stringBuilder.append("]");
            }
            count++;
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
