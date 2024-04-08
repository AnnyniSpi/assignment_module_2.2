package dev.annyni.mapper;

import dev.annyni.model.Label;
import dev.annyni.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * todo Document type LabelMapper
 */
public class LabelMapper {
    public static Label mapResultSetToLabel(ResultSet resultSet) throws SQLException {
        return  Label.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .status(Status.valueOf(resultSet.getString("status")))
            .build();


    }
}
