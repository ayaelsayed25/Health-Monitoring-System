package Controllers;
import Services.Quering.Query;
import Services.Quering.UserService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;

@CrossOrigin
@RestController
class Controller {
    UserService userService;
    Controller() {
        userService = new UserService();
    }
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/query")
    public String getInfo(@RequestBody Query query) throws ClassNotFoundException, SQLException, InterruptedException, ParseException {
        return userService.processQuery(query);
    }
}