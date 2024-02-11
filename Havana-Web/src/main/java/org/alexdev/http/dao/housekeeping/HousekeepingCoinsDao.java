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
            preparedStatement = Storage.getStorage().prepare("SELECT v.voucher_code AS voucherCode, v.credits, v.expiry_date, v.is_single_use, v.allow_new_users, vi.catalogue_sale_code " +
                    "FROM vouchers v " +
                    "LEFT JOIN vouchers_items vi ON v.voucher_code = vi.voucher_code", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> Voucher = new HashMap<>();
                Voucher.put("voucherCode", resultSet.getString("voucher_code"));
                Voucher.put("saleCode", resultSet.getString("catalogue_sale_code"));
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

    public static void createVoucher(String voucherCode, String credits, String expiryDate, int isSingleUse, int allowNewUsers, String item, String type) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementItem = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO vouchers (voucher_code, credits, expiry_date, is_single_use, allow_new_users) VALUES (?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, voucherCode);
            preparedStatement.setString(2, credits);
            preparedStatement.setString(3, expiryDate);
            preparedStatement.setInt(4, isSingleUse);
            preparedStatement.setInt(5, allowNewUsers);

            preparedStatement.execute();

            if (type.equals("voucherItem")) {
                sqlConnection = Storage.getStorage().getConnection();
                preparedStatementItem = Storage.getStorage().prepare("INSERT INTO vouchers_items (voucher_code, catalogue_sale_code) VALUES (?, ?)", sqlConnection);
                preparedStatementItem.setString(1, voucherCode);
                preparedStatementItem.setString(2, item);

                preparedStatementItem.execute();
            }
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(preparedStatementItem);
            Storage.closeSilently(sqlConnection);
        }
    }
}
