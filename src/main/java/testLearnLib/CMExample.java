package testLearnLib;

import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.SUL;
import de.learnlib.api.algorithm.LearningAlgorithm.*;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.statistic.StatisticSUL;
import de.learnlib.drivers.reflect.MethodInput;
import de.learnlib.drivers.reflect.MethodOutput;
import de.learnlib.drivers.reflect.SimplePOJOTestDriver;
import de.learnlib.filter.cache.sul.SULCaches;
import de.learnlib.filter.statistic.sul.ResetCounterSUL;
import de.learnlib.oracle.equivalence.mealy.RandomWalkEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Word;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CMExample {
    private static final double RESET_PROBABILITY = 0.05;
    private static final int MAX_STEPS = 10_000;
    private static final int RANDOM_SEED = 46_346_293;
    private static final String dotFilePath = "src/main/java/testLearnLib/dotFile/";

    private CMExample() {}


    public static void main(String[] args) throws NoSuchMethodException, IOException {
        SimplePOJOTestDriver driver = new SimplePOJOTestDriver(CoffeeExample.class);
        // create learning alphabet
        Method mWater = CoffeeExample.class.getMethod("water");
        Method mPod = CoffeeExample.class.getMethod("pod");
        Method mButton = CoffeeExample.class.getMethod("button");
        Method mClean = CoffeeExample.class.getMethod("clean");

        // construct input alphabet
        MethodInput water = driver.addInput("water", mWater);
        MethodInput pod = driver.addInput("pod", mPod);
        MethodInput button = driver.addInput("button", mButton);
        MethodInput clean = driver.addInput("clean", mClean);

        // oracle for counting queries wraps sul
        StatisticSUL<MethodInput, MethodOutput> statisticSul =
                new ResetCounterSUL<>("membership queries", driver);

        SUL<MethodInput, MethodOutput> effectiveSul = statisticSul;
        // use caching in order to avoid duplicate queries
        effectiveSul = SULCaches.createCache(driver.getInputs(), effectiveSul);

        SULOracle<MethodInput, MethodOutput> mqOracle = new SULOracle<>(effectiveSul);

        // create initial set of suffixes
        List<Word<MethodInput>> suffixes = new ArrayList<>();
        suffixes.add(Word.fromSymbols(water));
        suffixes.add(Word.fromSymbols(pod));
        suffixes.add(Word.fromSymbols(button));
        suffixes.add(Word.fromSymbols(clean));

        // construct L* instance (almost classic Mealy version)
        // almost: we use words (Word<String>) in cells of the table
        // instead of single outputs.
        MealyLearner<MethodInput, MethodOutput> lstar =
                new ExtensibleLStarMealyBuilder<MethodInput, MethodOutput>().withAlphabet(driver.getInputs()) // input alphabet
                        .withOracle(mqOracle) // membership oracle
                        .withInitialSuffixes(suffixes) // initial suffixes
                        .create();


        // create random walks equivalence test
        EquivalenceOracle.MealyEquivalenceOracle<MethodInput, MethodOutput> randomWalks =
                new RandomWalkEQOracle<>(driver, // system under learning
                        RESET_PROBABILITY, // reset SUL w/ this probability before a step
                        MAX_STEPS, // max steps (overall)
                        false, // reset step count after counterexample
                        new Random(RANDOM_SEED) // make results reproducible
                );

        // construct a learning experiment from
        // the learning algorithm and the random walks test.
        // The experiment will execute the main loop of
        // active learning
        Experiment.MealyExperiment<MethodInput, MethodOutput> experiment =
                new Experiment.MealyExperiment<>(lstar, randomWalks, driver.getInputs());

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        MealyMachine<?, MethodInput, ?, MethodOutput> result = experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        SimpleProfiler.logResults();

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println(statisticSul.getStatisticalData().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + driver.getInputs().size());

        // show model
        System.out.println();
        System.out.println("Model: ");

        GraphDOT.write(result, driver.getInputs(), System.out);
        Visualization.visualize(result, driver.getInputs());

        String savedDotfilePath = dotFilePath + "CoffeeMachine.dot";
        try (FileWriter fileWriter = new FileWriter(savedDotfilePath)) {
            GraphDOT.write(result, driver.getInputs(), fileWriter);
            System.out.println("Save dot file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------------------------------------------");

    }

    public static class CoffeeExample {
        private boolean waterReady;
        private boolean podReady;

        public CoffeeExample(){}

        public String water(){
            this.waterReady = true;
            return "*";
        }

        public String pod(){
            this.podReady = true;
            return "*";
        }

        public String button(){
            if (this.waterReady && this.podReady)
                return "OK";
            else
                return "Error";
        }

        public String clean(){
            this.podReady = false;
            this.waterReady = false;
            return "*";
        }
    }
}
