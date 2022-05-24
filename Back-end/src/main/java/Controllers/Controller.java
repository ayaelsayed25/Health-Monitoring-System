package Controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
class Controller {

    @CrossOrigin
    @GetMapping("/query")
    public String getInfo() {
        System.out.println("dateTime");
        return "hi";
    }

}