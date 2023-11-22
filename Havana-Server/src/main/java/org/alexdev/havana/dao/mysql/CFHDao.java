package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CFHDao {
    private static CFHDao instance;

    public static CFHDao getInstance() {
        if (instance == null) {
            instance = new CFHDao();
        }
        return instance;
    }

    public void insertCall(CallForHelp cfh) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cfh_logs (user, message, room_id, room, category, created_time, expire_time, is_deleted, cry_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    sqlConnection
            );

            preparedStatement.setString(1, PlayerDao.getName(cfh.getCaller()));
            preparedStatement.setString(2, cfh.getMessage());
            preparedStatement.setInt(3, cfh.getRoom().getData().getId());
            preparedStatement.setString(4, cfh.getRoom().getData().getName());
            preparedStatement.setInt(5, cfh.getCategory());
            preparedStatement.setString(6, cfh.getFormattedRequestTime());
            preparedStatement.setLong(7, cfh.getExpireTime());
            preparedStatement.setBoolean(8, cfh.isDeleted());
            preparedStatement.setInt(9, cfh.getCryId());

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void updateIsDeletedInDatabase(CallForHelp cfh) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm d/MM/yyyy");
        String formattedTimestamp = sdf.format(new Date());

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cfh_logs SET is_deleted = 1, moderator = ?, picked_time = ? WHERE cry_id = ?", sqlConnection);

            preparedStatement.setString(1, PlayerDao.getName(cfh.getPickedUpBy()));
            preparedStatement.setString(2, formattedTimestamp);
            preparedStatement.setInt(3, cfh.getCryId());

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    private static final String ALLOWED_CHARACTERS = "0123456789";
    private static final int CALL_ID_LENGTH = 6;

    public static String generateRandomCallId() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CALL_ID_LENGTH);

        for (int i = 0; i < CALL_ID_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    // Agregar métodos para actualizar, eliminar y consultar llamadas CFH
}