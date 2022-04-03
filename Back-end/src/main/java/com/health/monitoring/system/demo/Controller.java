import demo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {
    UserService userService;
    Controller() {
        userService = new UserService()
    }
    @RequestMapping(method = RequestMethod.POST, value = "/query")
    public Response getInfo(@RequestBody Job job) {
        return userService.processQuery(job);
    }
}