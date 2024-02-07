package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.ProductData;
import org.alexdev.havana.dao.Storage;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDataDao {

    public static List<ProductData> getProductData() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        var productDataList = new ArrayList<ProductData>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT sale_code, name, description, deal_description FROM productdata", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                var saleCode = resultSet.getString("sale_code");
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");
                var dealDescription = resultSet.getString("deal_description");
                productDataList.add(new ProductData(saleCode, name, description, dealDescription));
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return productDataList;
    }
}
