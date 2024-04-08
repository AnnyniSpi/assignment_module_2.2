package dev.annyni.mapper;

import dev.annyni.DriverJDBC;
import dev.annyni.model.Label;
import dev.annyni.model.Post;
import dev.annyni.model.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo Document type PostMapper
 */
public class PostMapper {
    public static Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        Long postId = resultSet.getLong("id");
        List<Label> labels = getLabelsForPost(postId);

        return Post.builder()
            .id(resultSet.getLong("id"))
            .content(resultSet.getString("content"))
            .created(resultSet.getTimestamp("created"))
            .updated(resultSet.getTimestamp("updated"))
            .labels(labels)
            .status(Status.valueOf(resultSet.getString("status")))
            .build();
    }

    private static List<Label> getLabelsForPost(Long postId) {
        String sql = """
                        SELECT l.*
                        FROM module2_2.label as l
                        JOIN module2_2.post_label pl ON l.id = pl.label_id
                        WHERE pl.post_id = ?
                     """;

        List<Label> labels = new ArrayList<>();

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(sql)) {
            preparedStatement.setLong(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Label label = LabelMapper.mapResultSetToLabel(resultSet);
                labels.add(label);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return labels;
    }
}
