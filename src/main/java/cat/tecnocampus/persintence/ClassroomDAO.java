package cat.tecnocampus.persintence;

import cat.tecnocampus.domain.Classroom;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ClassroomDAO {
    private JdbcTemplate jdbcTemplate;

    private final String FIND_ALL = "select * from classroom";
    private final String FIND_BY_CAPACITY = FIND_ALL + " where capacity ";
    private final String FIND_BY_PLUGS = FIND_ALL + " where plugs = ";
    private final String INSERT = "insert into classroom (name, capacity, orientation, plugs) values(?, ?, ?, ?)";

    private final RowMapper<Classroom> mapper = (resultSet, i) -> {
        return new Classroom.ClassroomBuilder().capacity(resultSet.getInt("capacity"))
                .name(resultSet.getString("name")).orientation(resultSet.getString("orientation"))
                .plugs(resultSet.getBoolean("plugs")).build();
    };

    public ClassroomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Classroom> findAll() {
        //instead of using the rowMapper it uses the BeanPropertyRowMapper to fo it authomatically
        return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Classroom.class));
    }

    public List<Classroom> findCapacityLargerThan(int capacity) {
        return jdbcTemplate.query(FIND_BY_CAPACITY + "> ?", new Object[]{capacity}, mapper);
    }

    public List<Classroom> findCapacityLowerThan(int capacity) {
        return jdbcTemplate.query(FIND_BY_CAPACITY + "< ?", new Object[]{capacity}, mapper);
    }

    public List<Classroom> findWithPlugs() {
        return jdbcTemplate.query(FIND_BY_PLUGS + "true", mapper);
    }

    public List<Classroom> findWithNoPlugs() {
        return jdbcTemplate.query(FIND_BY_PLUGS + "false", mapper);
    }

    public int insert(Classroom classroom) {
        return jdbcTemplate.update(INSERT, classroom.getName(), classroom.getCapacity(), classroom.getOrientation(),
                classroom.isPlugs());
    }

    public int[] insertBatch(List<Classroom> classrooms) {
        return jdbcTemplate.batchUpdate(INSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Classroom classroom = classrooms.get(i);
                preparedStatement.setString(1, classroom.getName());
                preparedStatement.setInt(2, classroom.getCapacity());
                preparedStatement.setString(3, classroom.getOrientation());
                preparedStatement.setBoolean(4, classroom.isPlugs());
            }
            @Override
            public int getBatchSize() {
                return classrooms.size();
            }
        });
    }
}