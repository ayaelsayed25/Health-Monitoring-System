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
    @RequestMapping(method = RequestMethod.GET, value = "/query/{Start}/{End}/{startDay}/{endDay}")

    public String getInfo(@PathVariable(value = "Start") int Start ,@PathVariable(value = "End") int End,@PathVariable(value = "startDay") String startDay,@PathVariable(value = "endDay") String endDay) throws ClassNotFoundException, SQLException, InterruptedException, ParseException {
        System.out.println(Start +"  "+End);
        Query query = new Query(End,Start,startDay,endDay);
        String result=userService.processQuery(query);
        return result;
    }
}
