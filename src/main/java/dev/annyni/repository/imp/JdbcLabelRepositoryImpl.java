package dev.annyni.repository.imp;

import dev.annyni.DriverJDBC;
import dev.annyni.mapper.LabelMapper;
import dev.annyni.model.Label;
import dev.annyni.model.Status;
import dev.annyni.repository.LabelRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo Document type JdbcLabelRepositoryImpl
 */
public class JdbcLabelRepositoryImpl implements LabelRepository {
    private static final String SQL_FIND_BY_ID = """
                                                    SELECT *
                                                    FROM module2_2.label
                                                    WHERE id = ?
                                                 """;
    private static final String SQL_FIND_ALL = """
                                                    SELECT *
                                                    FROM module2_2.label
                                               """;
    private static final String SQL_INSERT = """ 
                                                INSERT INTO module2_2.label(name, label_status)
                                                VALUES (?, ?)
                                             """;
    private static final String SQL_UPDATE_BY_ID = """
                                                       UPDATE module2_2.label
                                                       SET
                                                       name = ?, label_status = ?
                                                       WHERE id = ?
                                                   """;
    private static final String SQL_DELETE_BY_ID = """
                                                       UPDATE module2_2.label
                                                       SET
                                                       label_status = ?
                                                       WHERE id = ?
                                                   """;


    @Override
    public Label findById(Long labelId) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, labelId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return LabelMapper.mapResultSetToLabel(resultSet);
            } else {
                throw new RuntimeException("Метка не найдена по идентификатору: " + labelId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }

    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();

        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_FIND_ALL)){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                labels.add(LabelMapper.mapResultSetToLabel(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return labels;
    }

    @Override
    public Label save(Label saveLabel) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_INSERT)) {
            preparedStatement.setString(1, saveLabel.getName());
            preparedStatement.setLong(2, saveLabel.getId());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Создание метки не удалось, ни одна запись не была изменена.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                saveLabel.setId(resultSet.getLong(1));
            } else {
                throw new RuntimeException("Создание метки не удалось, не удалось получить идентификатор.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return saveLabel;
    }

    @Override
    public Label update(Label updateLabel) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_UPDATE_BY_ID)) {
            preparedStatement.setString(1, updateLabel.getName());
            preparedStatement.setString(2, updateLabel.getStatus().name());
            preparedStatement.setLong(3, updateLabel.getId());

            int count = preparedStatement.executeUpdate();

            if (count == 0){
                throw new RuntimeException("Обновление метки не удалось, ни одна запись не была изменена.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }

        return updateLabel;
    }

    @Override
    public void deleteById(Long idLabel) {
        try(PreparedStatement preparedStatement = DriverJDBC.getPreparedStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setString(1, Status.DELETED.name());
            preparedStatement.setLong(2, idLabel);

            int count = preparedStatement.executeUpdate();

            if (count ==  0){
                throw new RuntimeException("Метка с ID " + idLabel + " не найден.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL-запроса", e);
        }
    }
}
