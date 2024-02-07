package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.FurniData;
import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FurniDataDao {
    public static List<FurniData> getFurniData() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        var furniData = new ArrayList<FurniData>();

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT item_type, furni_id, sale_code, class_id, groupable, recycleable, tradeable, stuff_data, name, description  FROM furnidata", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                var itemType = resultSet.getString("item_type");
                var furniId = resultSet.getString("furni_id");
                var saleCode = resultSet.getString("sale_code");
                var classId = resultSet.getString("class_id");
                var groupable = resultSet.getBoolean("groupable");
                var recycleable = resultSet.getBoolean("recycleable");
                var tradeable = resultSet.getBoolean("tradeable");
                var stuffData = resultSet.getString("stuff_data");
                var name = resultSet.getString("name");
                var description = resultSet.getString("description");

                furniData.add(new FurniData(itemType, furniId, saleCode, classId, groupable, recycleable, tradeable, stuffData, name, description));
            }

        } catch (Exception e) {
            System.out.println(e);
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return furniData;
    }
}
