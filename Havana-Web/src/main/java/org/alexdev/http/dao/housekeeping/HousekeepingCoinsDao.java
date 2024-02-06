package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingCoinsDao {
    public static List<Map<String, Object>> getAllVouchers() {
        List<Map<String, Object>> VouchersList = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM vouchers", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> Voucher = new HashMap<>();
                Voucher.put("voucherCode", resultSet.getString("voucher_code"));
                Voucher.put("credits", resultSet.getInt("credits"));
                Voucher.put("expiryDate", resultSet.getString("expiry_date"));
                Voucher.put("isSingleUse", resultSet.getInt("is_single_use"));
                Voucher.put("allowNewUsers", resultSet.getInt("allow_new_users"));

                VouchersList.add(Voucher);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return VouchersList;
    }

    public static void createVoucher(String voucherCode, String credits, String expiryDate, int isSingleUse, int allowNewUsers) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO vouchers (voucher_code, credits, expiry_date, is_single_use, allow_new_users) VALUES (?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, voucherCode);
            preparedStatement.setString(2, credits);
            preparedStatement.setString(3, expiryDate);
            preparedStatement.setInt(4, isSingleUse);
            preparedStatement.setInt(5, allowNewUsers);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }
}
