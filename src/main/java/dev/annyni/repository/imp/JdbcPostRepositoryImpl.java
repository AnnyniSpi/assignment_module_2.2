package dev.annyni.repository.imp;

import dev.annyni.DriverJDBC;
import dev.annyni.mapper.PostMapper;
import dev.annyni.model.Label;
import dev.annyni.model.Post;
import dev.annyni.model.Status;
import dev.annyni.repository.PostRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * todo Document type JdbcPostRepositoryImpl
 */
public class JdbcPostRepositoryImpl implements PostRepository {
    private static final String SQL_FIND_BY_ID = """
                                                    SELECT p.id, p.content, p.created, p.updated, p.post_status, p.writer_id,
                                                    w.firstname, w.lastname, w.writer_status, pl.label_id, l.name, label_status 
                                                    FROM module2_2.post as p
                                                    JOIN module2_2.post_label as pl ON p.id = pl.post_id
                                                    JOIN module2_2.writer as w ON p.writer_id = w.id
                                                    JOIN module2_2.label as l ON pl.label_id = l.id
                                                    WHERE p.id = ?
                                                  """;
    private static final String SQL_FIND_ALL = """
                                                   SELECT p.id, p.content, p.created, p.updated, p.post_status, p.writer_id,
                                                    w.firstname, w.lastname, w.writer_status, pl.label_id, l.name, label_status 
                                                    FROM module2_2.post as p
                                                    JOIN module2_2.post_label as pl ON p.id = pl.post_id
                                                    JOIN module2_2.writer as w ON p.writer_id = w.id
                                                    JOIN module2_2.label as l ON pl.label_id = l.id        
                                               """;
    private static final String SQL_INSERT = """
                                                INSERT INTO module2_2.post(content, created, updated, post_status, writer_id)
                                                VALUES (?, ?, ?, ?, ?)
                                             """;
    private static final String SQL_UPDATE_BY_ID = """
                                                      UPDATE module2_2.post 
                                                      SET 
                                                      content = ?, updated = ?, post_status = ?, writer_id = ?
                                                      WHERE id = ?
                                                  """;
    private static final String SQL_DELETE_BY_ID = """
                                                    UPDATE module2_2.post 
                                                    SET 
                                                    post_status = ?
                                                    WHERE id = ?
                                                  """;

    private static final String SQL_INSERT_POST_LABEL = """
                                                            INSERT INTO module2_2.post_label(post_id, label_id) 
                                                            VALUES (?, ?)
                                                        """;
    private static final String SQL_DELETE_POST_LABEL = """
                                                            DELETE FROM module2_2.post_label
                                                            WHERE post_id = ?
                                                        """;

    @Override
    public Post findById(Long postId) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_BY_ID)){
            preparedStatement.setLong(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return PostMapper.mapResultSetToPost(resultSet);
            } else {
                throw new RuntimeException("Пост не найдена по идентификатору: " + postId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_ALL)){
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                posts.add(PostMapper.mapResultSetToPost(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return posts;
    }

    @Override
    public Post save(Post savePost) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_INSERT)) {
            preparedStatement.setString(1, savePost.getContent());
            preparedStatement.setTimestamp(2, new Timestamp(savePost.getCreated().getTime()));
            preparedStatement.setTimestamp(3, new Timestamp(savePost.getUpdated().getTime()));
            preparedStatement.setString(4, savePost.getStatus().name());
            preparedStatement.setLong(5, savePost.getWriter().getId());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Создание поста не удалось, ни одна запись не была изменена.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                savePost.setId(resultSet.getLong(1));
            } else {
                throw new RuntimeException("Создание поста не удалось, не удалось получить идентификатор.");
            }

            addLabelToPost(savePost.getId(), savePost.getLabels());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return savePost;
    }

    @Override
    public Post update(Post updatePost) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_UPDATE_BY_ID)){
            preparedStatement.setString(1, updatePost.getContent());
            preparedStatement.setTimestamp(2, new Timestamp(updatePost.getUpdated().getTime()));
            preparedStatement.setString(3,updatePost.getStatus().name());
            preparedStatement.setLong(4, updatePost.getWriter().getId());
            preparedStatement.setLong(5, updatePost.getId());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Обновление поста не удалось, ни одна запись не была изменена.");
            }

            deleteLabelsForPost(updatePost.getId());
            addLabelToPost(updatePost.getId(), updatePost.getLabels());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return updatePost;
    }

    @Override
    public void deleteById(Long postId) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_DELETE_BY_ID)){
            preparedStatement.setString(1, Status.DELETED.name());
            preparedStatement.setLong(2, postId);

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Метка с ID " + postId + " не найден.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }

    private static void addLabelToPost(Long postId, List<Label> labels){
        if (labels == null){
            return;
        }

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_INSERT_POST_LABEL)) {
            for (Label label : labels) {
                if (label.getId() != 0){
                    preparedStatement.setLong(1, postId);
                    preparedStatement.setLong(2, label.getId());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }

    private static void deleteLabelsForPost(Long postId){
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_DELETE_POST_LABEL)){
            preparedStatement.setLong(1, postId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }
}
