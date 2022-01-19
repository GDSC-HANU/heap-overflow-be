package hanu.gdsc.domains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
/*
This class use the design pattern "Template Method"
 */
public abstract class ActiveRecord {
    protected final JdbcTemplate jdbcTemplate;

    public ActiveRecord(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    Save a domain object to DB and return its ID
     */
    public int insert() {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(makeInsertQuery(), Statement.RETURN_GENERATED_KEYS);
                prepareInsertStmt(stmt);
                return stmt;
            }
        });
        return kh.getKey().intValue();
    }

    protected abstract String makeInsertQuery();

    protected abstract void prepareInsertStmt(PreparedStatement stmt) throws SQLException;

    public ActiveRecord getById(int id) {
        return jdbcTemplate.queryForObject(makeGetByIdQuery(), getClass(), id);
    }

    protected abstract String makeGetByIdQuery();
}
