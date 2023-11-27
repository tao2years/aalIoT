package testLearnLib;

import de.learnlib.api.oracle.SingleQueryOracle.*;
import net.automatalib.words.Word;
import java.util.stream.StreamSupport;

public class BlackBoxSystem implements SingleQueryOracleMealy<String, String> {

    @Override
    public Word<String> answerQuery(Word<String> prefix, Word<String> suffix) {
        CMExample.CoffeeExample coffee = new CMExample.CoffeeExample();
        prefix.forEach(input -> handleInput(input, coffee));
        return StreamSupport.stream(suffix.spliterator(), false).map(i->handleInput(i, coffee)).collect(Word.collector());
    }

    private String handleInput(String input, CMExample.CoffeeExample coffee) {
        switch (input) {
            case "water": return coffee.water();
            case "pod": return coffee.pod();
            case "clean": return coffee.clean();
            case "button": return coffee.button();
            default: return "Invalid";
        }
    }
}
