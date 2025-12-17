package org.alexdev.havana.dao.mysql;

import org.alexdev.havana.dao.Storage;
import org.alexdev.havana.game.moderation.cfh.CallForHelp;
import org.alexdev.havana.game.moderation.cfh.enums.CFHAction;

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
            preparedStatement = Storage.getStorage().prepare("INSERT INTO cfh_logs (user_id, user, reason, room_id, room, category, created_time, expire_time, is_deleted, cry_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    sqlConnection
            );

            preparedStatement.setInt(1, cfh.getCaller());
            preparedStatement.setString(2, PlayerDao.getName(cfh.getCaller()));
            preparedStatement.setString(3, cfh.getMessage());
            preparedStatement.setInt(4, cfh.getRoom().getData().getId());
            preparedStatement.setString(5, cfh.getRoom().getData().getName());
            preparedStatement.setInt(6, cfh.getCategory());
            preparedStatement.setString(7, cfh.getFormattedRequestTime());
            preparedStatement.setLong(8, cfh.getExpireTime());
            preparedStatement.setBoolean(9, cfh.isDeleted());
            preparedStatement.setInt(10, cfh.getCryId());

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void updateReplyType(CallForHelp cfh, CFHAction type, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm d/MM/yyyy");
        String formattedTimestamp = sdf.format(new Date());

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String moderator = "";

        //if (PlayerDao.getName(cfh.getPickedUpBy() ) != NULL)

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cfh_logs SET is_deleted = 1, moderator = ?, picked_time = ?, action = ?, message_to_user = ? WHERE cry_id = ?", sqlConnection);

            preparedStatement.setString(1, PlayerDao.getName(cfh.getPickedUpBy()));
            preparedStatement.setString(2, formattedTimestamp);
            preparedStatement.setString(3, type.toString());
            preparedStatement.setString(4, message);
            preparedStatement.setInt(5, cfh.getCryId());

            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }
    }

    public static void deletePurgedCfh(CallForHelp cfh) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("UPDATE cfh_logs SET is_deleted = 1 WHERE cry_id = ?", sqlConnection);

            preparedStatement.setInt(1, cfh.getCryId());

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
}