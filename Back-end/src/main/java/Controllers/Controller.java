package Controllers;
import Services.Quering.Query;
import Services.Quering.UserService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;

@CrossOrigin(origins = "http://167.172.39.122:3000")
@RestController
class Controller {
    UserService userService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/query")
    public String getInfo(@RequestBody Query query) throws ClassNotFoundException, SQLException, InterruptedException, ParseException {
        userService = new UserService();
        return userService.processQuery(query);
    }
}

