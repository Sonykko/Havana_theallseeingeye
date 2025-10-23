package org.alexdev.http.dao.housekeeping;

import org.alexdev.havana.dao.Storage;
import org.alexdev.http.game.housekeeping.HousekeepingVouchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HousekeepingCoinsDao {
    public static List<HousekeepingVouchers> getAllVouchers() {
        List<HousekeepingVouchers> vouchers = new ArrayList<>();

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
                vouchers.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return vouchers;
    }

    public static void createVoucher(String voucherCode, String credits, String expiryDate, boolean isSingleUse, boolean allowNewUsers, String item, String type) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementItem = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO vouchers (voucher_code, credits, expiry_date, is_single_use, allow_new_users) VALUES (?, ?, ?, ?, ?)", sqlConnection);
            preparedStatement.setString(1, voucherCode);
            preparedStatement.setString(2, credits);
            preparedStatement.setString(3, expiryDate);
            preparedStatement.setBoolean(4, isSingleUse);
            preparedStatement.setBoolean(5, allowNewUsers);

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

    public static List<HousekeepingVouchers> searchVouchers(String query) {
        List<HousekeepingVouchers> vouchers = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT v.voucher_code AS voucherCode, v.credits, v.expiry_date, v.is_single_use, v.allow_new_users, vi.catalogue_sale_code " +
                                        "FROM vouchers v " +
                                        "LEFT JOIN vouchers_items vi ON v.voucher_code = vi.voucher_code WHERE v.voucher_code LIKE ? OR vi.catalogue_sale_code = ? LIMIT 20", sqlConnection);
            preparedStatement.setString(1, query + "%");
            preparedStatement.setString(2, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                vouchers.add(fill(resultSet));
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return vouchers;
    }

    public static void deleteVoucher(String voucherCode) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM vouchers WHERE voucher_code = ?", sqlConnection);
            preparedStatement.setString(1, voucherCode);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deleteVoucherItem(String voucherCode) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("DELETE FROM vouchers_items WHERE voucher_code = ?", sqlConnection);
            preparedStatement.setString(1, voucherCode);
            preparedStatement.execute();
        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static HousekeepingVouchers getVoucherByCode(String voucherCode) {
        HousekeepingVouchers voucher = null;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare(
                    "SELECT v.voucher_code, v.credits, v.expiry_date, v.is_single_use, v.allow_new_users, vi.catalogue_sale_code " +
                            "FROM vouchers v " +
                            "LEFT JOIN vouchers_items vi ON v.voucher_code = vi.voucher_code " +
                            "WHERE v.voucher_code = ?", sqlConnection
            );
            preparedStatement.setString(1, voucherCode);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                voucher = fill(resultSet);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return voucher;
    }

    public static HousekeepingVouchers fill(ResultSet resultSet) throws SQLException {
        return new HousekeepingVouchers(
                resultSet.getString("voucher_code"),
                resultSet.getString("catalogue_sale_code"),
                resultSet.getInt("credits"),
                resultSet.getString("expiry_date"),
                resultSet.getBoolean("is_single_use"),
                resultSet.getBoolean("allow_new_users")
        );
    }
}
