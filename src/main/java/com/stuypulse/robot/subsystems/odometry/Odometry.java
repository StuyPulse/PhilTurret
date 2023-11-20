import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Odometry extends SubsystemBase {
    private static final Odometry instance;

    static {
        instance = new Odometry();
    }

    public static Odometry getInstance() {
        return instance;
    }

    private final FieldObject2d robot;
    private final Field2d field;

    protected Odometry() {
        field = new Field2d();
        robot = field.getObject("Turret Pose 2d");

        SmartDashboard.putData(field);
    }

    public Pose2d getPose() {
        return robot.getPose();
    }

    public Field2d getField() {
        return field;
    }
}