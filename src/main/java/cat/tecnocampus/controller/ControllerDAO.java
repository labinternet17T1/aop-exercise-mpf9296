package cat.tecnocampus.controller;

import cat.tecnocampus.domain.Classroom;
import cat.tecnocampus.persintence.ClassroomDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by maria on 16/10/2017.
 */

@Service("controller")
public class ControllerDAO {

    private final ClassroomDAO classroomDAO;

    /* Constructor */
    public ControllerDAO(ClassroomDAO classroomDAO) {
        this.classroomDAO = classroomDAO;
    }

    public Classroom createClassroom (String name, int capacity, String orientation, boolean plugs) {
        Classroom classroom = new Classroom.ClassroomBuilder().name(name).
                capacity(capacity).orientation(orientation).plugs(plugs).build();
        insert(classroom);
        return classroom;
    }

    /* The @Transactional annotation states that saveUser is a transaction. So ,if a unchecked exception is signaled
      (and not cached) during the saveUser method the transaction is going to rollback */
    @Transactional
    public int insert(Classroom classroom) {
        return classroomDAO.insert(classroom);
    }

    public int[] insertBatch(List<Classroom> classrooms) {
        return classroomDAO.insertBatch(classrooms);
    }

    public List<Classroom> findAll() {
        return classroomDAO.findAll();
    }

    public List<Classroom> findCapacityLowerThan(int capacity) {
        return classroomDAO.findCapacityLowerThan(capacity);
    }

    public List<Classroom> findCapacityLargerThan(int capacity) {
        return classroomDAO.findCapacityLargerThan(capacity);
    }

    public List<Classroom> findWithPlugs() {
        return classroomDAO.findWithPlugs();
    }

    public List<Classroom> findWithNoPlugs() {
        return classroomDAO.findWithNoPlugs();
    }
}