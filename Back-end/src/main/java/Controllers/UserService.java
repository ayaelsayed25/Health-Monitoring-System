package Controllers;

import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.text.ParseException;

public class UserService {



    @ResponseBody
    public String processQuery(Query query) throws SQLException, InterruptedException, ClassNotFoundException, ParseException {
        Response response=new Response();
        return  response.getResponse(query.getStart(), query.getEnd(),query.getStartDay(),query.endDay,query.getStatment());
    }
}