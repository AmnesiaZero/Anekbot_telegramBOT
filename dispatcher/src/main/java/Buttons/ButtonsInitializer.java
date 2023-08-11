package Buttons;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ButtonsInitializer {
    public InlineKeyboardButton themeButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":abcd:theme"));
    public InlineKeyboardButton backButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":arrow_left:back"));
    public InlineKeyboardButton helpButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":spiral_note_pad:help"));
    public InlineKeyboardButton startButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":rocket:start"));
    public InlineKeyboardButton againButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":arrows_counterclockwise:again"));
    public InlineKeyboardButton stopButton = new InlineKeyboardButton(EmojiParser.parseToUnicode(":black_square_for_stop:stop"));
    public ButtonsInitializer(){
        themeButton.setCallbackData("/theme");
        backButton.setCallbackData("/back");
        helpButton.setCallbackData("/help");
        startButton.setCallbackData("/start");
        againButton.setCallbackData("/start");
        stopButton.setCallbackData("/stop");
    }
    public InlineKeyboardMarkup getInlineMarkup(List<InlineKeyboardButton> rowInline){
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }
}
