package com.emobile.springtodo.repository;

import com.emobile.springtodo.annotation.DatabaseField;
import com.emobile.springtodo.entity.Entity;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.exception.FieldAccessException;
import com.emobile.springtodo.repository.util.Tables;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class AbstractRepository<T extends Entity> {
    protected final RowMapper<T> rowMapper;
    protected final JdbcTemplate jdbcTemplate;
    protected final Tables tableName;

    public void save(T entity) {
        var fieldsToInsert = extractFields(entity);
        var fieldNames = extractFieldNames(entity);
        var query = buildInsertQuery(fieldNames);
        jdbcTemplate.update(query, fieldsToInsert);

    }

    public void delete(Long id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        jdbcTemplate.update(sql, id);
    }

    public List<T> findAll(int limit, int offset) {
        String sql = String.format("SELECT * FROM %s LIMIT ? OFFSET ?", tableName);
        var foundEntities = jdbcTemplate.query(sql, rowMapper, limit, offset);
        if (foundEntities.isEmpty()) {
            throw new EntityNotFoundException("Entities for your request are not found");
        }
        return foundEntities;
    }

    public T findById(Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Entity for your request is not found");
        }
    }

    public void update(T entity) {
        var fieldsToUpdate = extractFields(entity);
        var fieldsToUpdateAndEntityId = Arrays.copyOf(fieldsToUpdate, fieldsToUpdate.length + 1);
        fieldsToUpdateAndEntityId[fieldsToUpdate.length] = entity.getId();
        var fieldNames = extractFieldNames(entity);
        var query = buildUpdateQuery(fieldNames);
        jdbcTemplate.update(query, fieldsToUpdateAndEntityId);
    }

    public boolean existsById(Long id) {
        try {
            findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }


    private String buildUpdateQuery(List<String> fieldNames) {
        String setExpression = fieldNames.stream()
                .map(col -> col + " = ?")
                .collect(Collectors.joining(", "));
        return String.format("UPDATE %s SET %s WHERE id = ?", tableName, setExpression);
    }

    private String buildInsertQuery(List<String> fieldNames) {
        String columns = String.join(", ", fieldNames);
        String placeholders = String.join(", ", Collections.nCopies(fieldNames.size(), "?"));
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns, placeholders);
    }

    private Object[] extractFields(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> isFieldNotNullAndAnnotated(field, entity))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        var value = field.get(entity);
                        return value instanceof Enum ? value.toString() : value;
                    } catch (IllegalAccessException e) {
                        throw new FieldAccessException(String
                                .format("Cannot access field %s in entity %s", field.getName(), entity));
                    }
                })
                .toArray();
    }

    private List<String> extractFieldNames(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> isFieldNotNullAndAnnotated(field, entity))
                .map(Field::getName)
                .toList();
    }

    private boolean isFieldNotNullAndAnnotated(Field field, Object entity) {
        field.setAccessible(true);
        try {
            return field.get(entity) != null && field.isAnnotationPresent(DatabaseField.class);
        } catch (IllegalAccessException e) {
            throw new FieldAccessException(
                    String.format("Cannot access field %s in entity %s", field.getName(), entity));
        }
    }
}
