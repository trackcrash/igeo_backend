package igeo.site.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Index
@Controller
public class IndexController {

    @GetMapping
    public int index(){
        return 1;
    }

}
