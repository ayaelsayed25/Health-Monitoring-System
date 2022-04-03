package Services;


import org.springframework.web.bind.annotation.ResponseBody;

public class UserService {

    @ResponseBody
    public Response processQuery(Query query)
    {

        return new Response(query.getStart());
    }
}