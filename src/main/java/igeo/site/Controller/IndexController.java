package igeo.site.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Index
@Controller
public class IndexController {

    @GetMapping
    public String index(){
        return "index";
    }

}
