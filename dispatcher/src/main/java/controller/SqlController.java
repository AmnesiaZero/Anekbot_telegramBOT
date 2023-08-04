package controller;

import lombok.Data;
import sql.AnekdotDAO;
import sql.ThemesDAO;
@Data
public class SqlController {
    AnekdotDAO anekdotDAO;
    ThemesDAO themesDAO;
}
