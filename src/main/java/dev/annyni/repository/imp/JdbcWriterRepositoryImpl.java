package dev.annyni.repository.imp;

import dev.annyni.DriverJDBC;
import dev.annyni.mapper.WriterMapper;
import dev.annyni.model.Status;
import dev.annyni.model.Writer;
import dev.annyni.repository.WriterRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo Document type JdbcWriterRepositoryImpl
 */
public class JdbcWriterRepositoryImpl implements WriterRepository {

    private static final String SQL_FIND_BY_ID = """
                                                    SELECT *
                                                    FROM module2_2.writer
                                                    WHERE id = ?
                                                 """;
    private static final String SQL_FIND_ALL = """
                                                    SELECT *
                                                    FROM module2_2.writer
                                               """;
    private static final String SQL_INSERT = """ 
                                                INSERT INTO module2_2.writer(firstname, lastname, writer_status)
                                                VALUES (?, ?, ?)
                                             """;
    private static final String SQL_UPDATE_BY_ID = """
                                                       UPDATE module2_2.writer
                                                       SET
                                                       firstname = ?, lastname = ?, writer_status = ?
                                                       WHERE id = ?
                                                   """;
    private static final String SQL_DELETE_BY_ID = """
                                                       UPDATE module2_2.writer
                                                       SET
                                                       writer_status = ?
                                                       WHERE id = ?
                                                   """;

    @Override
    public Writer findById(Long writerId) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, writerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return WriterMapper.mapResultSetToWriter(resultSet);
            } else {
                throw new RuntimeException("Автор не найден по идентификатору: " + writerId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }

    @Override
    public List<Writer> findAll() {
        List<Writer>  writers = new ArrayList<>();

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                writers.add(WriterMapper.mapResultSetToWriter(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return writers;
    }

    @Override
    public Writer save(Writer saveWriter) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_INSERT)) {
            preparedStatement.setString(1,saveWriter.getFirstName());
            preparedStatement.setString(2,saveWriter.getLastName());
            preparedStatement.setString(3, saveWriter.getStatus().name());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Создание автора не удалось, ни одна запись не была изменена.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                saveWriter.setId(resultSet.getLong(1));
            } else {
                throw new RuntimeException("Создание автора не удалось, не удалось получить идентификатор.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return saveWriter;
    }

    @Override
    public Writer update(Writer updateWriter) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_UPDATE_BY_ID)){
            preparedStatement.setString(1, updateWriter.getFirstName());
            preparedStatement.setString(2, updateWriter.getLastName());
            preparedStatement.setString(3, updateWriter.getStatus().name());
            preparedStatement.setLong(4, updateWriter.getId());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Обновление автора не удалось, ни одна запись не была изменена.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return updateWriter;
    }

    @Override
    public void deleteById(Long writerId) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setString(1, Status.DELETED.name());
            preparedStatement.setLong(2, writerId);

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Метка с ID " + writerId + " не найден.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
