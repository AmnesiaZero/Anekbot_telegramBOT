package controller;

import lombok.Data;
import sql.AnekdotDAO;
import sql.BotStateDAO;
import sql.ThemesDAO;
@Data
public class SqlController {
    AnekdotDAO anekdotDAO;
    ThemesDAO themesDAO;
    BotStateDAO botStateDAO;
}
