package Buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class StartButtons implements BotCommands {
    static ButtonsInitializer buttonsInitializer = new ButtonsInitializer();
    public static InlineKeyboardMarkup inlineMarkup() {
        List<InlineKeyboardButton> rowInline = List.of(buttonsInitializer.themeButton,buttonsInitializer.helpButton);
        return buttonsInitializer.getInlineMarkup(rowInline);
    }
}

