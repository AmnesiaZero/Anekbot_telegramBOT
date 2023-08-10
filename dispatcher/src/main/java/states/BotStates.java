package states;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class BotStates {
    private final int defaultState = 0;
    private final int startState = 1;
    private final int chooseThemeLetterState = 2;
    private final int chooseThemeState = 3;
    private final int chooseAnekdotState = 4;
    private final int finishAnekdotState = 5;
    private final int helpState = 6;
}
