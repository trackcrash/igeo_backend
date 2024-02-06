package igeo.site.Service;

import igeo.site.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.Collections;
import java.util.List;

@Service
public class NameCheckService {

    private List<String> bannedWord = getBannedWord();
    public List<String> getBannedWord()
    {
        List<String> bannedWord1;
        try
        {
            bannedWord1 = Files.readAllLines(Paths.get(new ClassPathResource("static/banned_words.txt").getURI()));
        }catch(IOException e)
        {
            bannedWord1 = Collections.emptyList();
        }
        return bannedWord1;
    }

    @Autowired
    private UserService userService;
    public boolean nameDuplicationCheck(String name)
    {
        List<User> userList =  userService.getUserList();
        for (User item : userList)
        {
            if(item.getName().equals(name)) return false;
        }
        return true;
    }
    public boolean nameBannedCheck(String name)
    {
        for (String item : bannedWord)
        {
            if(name.contains(item)) return false;
        }
        return true;
    }
}
