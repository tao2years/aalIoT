package testLearnLib;

import de.learnlib.api.oracle.SingleQueryOracle;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

public class queryOracle implements SingleQueryOracle<Word<String>, Word<String>> {

    @Override
    public Word<String> answerQuery(Word<Word<String>> prefix, Word<Word<String>> suffix) {
        final WordBuilder<String> wb = new WordBuilder();
        for (int i = 0; i < suffix.size(); ++i) {
            wb.append(handleEvent(handleEvent(String.valueOf(suffix.getSymbol(i)))));
        }
        return wb.toWord();
    }

    private String handleEvent(String event) {
        switch (event) {
            case "on":
                return "on";
            case "off":
                return "off";
            default:
                return "error";
        }
    }


}


