package config;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
@Log4j
public class BotConfig {
    String botName;
    String token;
    public BotConfig(){
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./bot.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            botName = properties.getProperty("bot.name");
            token = properties.getProperty("bot.token");
        }
        catch (IOException e){
            log.error(e);
        }
    }
}
