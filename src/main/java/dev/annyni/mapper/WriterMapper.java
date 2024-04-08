package dev.annyni.mapper;

import dev.annyni.DriverJDBC;
import dev.annyni.model.Post;
import dev.annyni.model.Status;
import dev.annyni.model.Writer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo Document type WriterMapper
 */
public class WriterMapper {

    public static Writer mapResultSetToWriter(ResultSet resultSet) throws SQLException {
        Long writerId = resultSet.getLong("id");
        List<Post> posts = getPostsForWriter(writerId);

        return Writer.builder()
            .id(resultSet.getLong("id"))
            .firstName(resultSet.getString("firstname"))
            .lastName(resultSet.getString("lastname"))
            .status(Status.valueOf(resultSet.getString("status")))
            .posts(posts)
            .build();
    }

    private static List<Post> getPostsForWriter(Long writerId) {
        String sql = """
                      SELECT *
                      FROM module2_2.post
                      WHERE id = ?
                     """;

        List<Post> posts = new ArrayList<>();

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(sql)){
            preparedStatement.setLong(1, writerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = PostMapper.mapResultSetToPost(resultSet);
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return posts;
    }
}
