package testLearnLib;

import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;

import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.drivers.reflect.MethodInput;
import de.learnlib.drivers.reflect.MethodOutput;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

public class testQO {
    public static void main(String[] args) {


    }

    private static CompactDFA<Character> constructSUL(){
        // input alphabet contains characters 'a'..'b'
        Alphabet<Character> sigma = Alphabets.characters('a', 'b');

        // create automata
        return AutomatonBuilders.newDFA(sigma)
                .withInitial("q0")
                .from("q0")
                .on('a').to("q1")
                .on('b').to("q2")
                .from("q1")
                .on('a').to("q0")
                .on('b').to("q3")
                .from("q2")
                .on('a').to("q3")
                .on('b').to("q0")
                .from("q3")
                .on('a').to("q2")
                .on('b').to("q1")
                .withAccepting("q0")
                .create();
    }

}
