package states;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class BotStates {
    private final int startState = 0;
    private final int chooseThemeLetterState = 1;
    private final int chooseThemeState = 2;
    private final int chooseAnekdotState = 3;
    private final int finishAnekdotState = 4;
    private final int helpState = 5;
}
