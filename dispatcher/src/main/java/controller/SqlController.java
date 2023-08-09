package controller;

import lombok.Data;
import sql.AnekdotDAO;
import sql.BotStateDAO;
import sql.DataSource;
import sql.ThemesDAO;
@Data
public class SqlController {
    DataSource dataSource;
    AnekdotDAO anekdotDAO;
    ThemesDAO themesDAO;
    BotStateDAO botStateDAO;
    public SqlController(DataSource dataSource){
        this.dataSource = dataSource;
        anekdotDAO = new AnekdotDAO(dataSource);
        themesDAO = new ThemesDAO(dataSource);
        botStateDAO = new BotStateDAO(dataSource);
    }
}
