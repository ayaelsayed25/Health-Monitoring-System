//package Controllers;
//
//import Services.Query;
//
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//class Controller {
//    UserService userService;
//    public Controller() {
//        userService = new UserService();
//    }
//
//    @CrossOrigin
//    @GetMapping("/query")
//    public Response getInfo(@RequestParam(value = "startDate")@RequestBody Query query) {
//        System.out.println("hi");
//        return userService.processQuery(query);
//    }
//
//}