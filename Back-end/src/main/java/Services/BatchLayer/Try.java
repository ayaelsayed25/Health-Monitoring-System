package Services.BatchLayer;

import java.io.IOException;
import java.sql.SQLException;

public class Try {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException, ClassNotFoundException {
        Runner runner = new Runner();
        runner.jobRun();
    }
}
