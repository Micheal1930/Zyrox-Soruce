package com.varrock.util;

/**
 * Created by Jonny on 7/28/2019
 **/
public class RandomEvent {

    private static String[] art = {"the", "my", "your", "our", "that", "this", "every", "one", "the only", "his", "her"};
    public static String[] adj = {"glorious", "hairy", "happy", "rotating", "red", "fast", "elastic", "smily", "unbelievable", "infinte", "surprising", "mysterious", "glowing", "green", "blue", "tired", "hard", "soft", "transparent", "long", "short", "excellent", "noisy", "silent", "rare", "normal", "typical", "living", "clean", "glamorous", "fancy", "handsome", "lazy", "scary", "helpless", "skinny", "melodic", "silly", "kind", "brave", "nice", "ancient", "modern", "young", "sweet", "wet", "cold", "dry", "heavy", "industrial", "complex", "accurate", "awesome", "shiny", "cool", "glittering", "fake", "unreal", "naked", "intelligent", "smart", "curious", "strange", "unique", "empty", "gray", "saturated", "blurry"};
    private static String[] nou = {"bush", "computer program", "grandma", "school", "bed", "mouse", "keyboard", "bicycle", "spaghetti", "drink", "cat", "t-shirt", "carpet", "wall", "poster", "airport", "bridge", "road", "river", "beach", "sculpture", "piano", "guitar", "fruit", "banana", "apple", "strawberry", "rubber band", "saxophone", "window", "linux computer", "skate board", "piece of paper", "photograph", "painting", "hat", "space", "fork", "mission", "goal", "project", "tax", "wind mill", "light bulb", "microphone", "cpu", "hard drive", "screwdriver"};
    private static String[] pre = {"under", "in front of", "above", "behind", "near", "following", "inside", "besides", "unlike", "like", "beneath", "against", "into", "beyond", "considering", "without", "with", "towards"};
    private static String[] ver = {"sings", "dances", "was dancing", "runs", "will run", "walks", "flies", "moves", "moved", "will move", "glows", "glowed", "spins", "promised", "hugs", "cheated", "waits", "is waiting", "is studying", "swims", "travels", "traveled", "plays", "played", "enjoys", "will enjoy", "illuminates", "arises", "eats", "drinks", "calculates", "kissed", "faded", "listens", "navigated", "responds", "smiles", "will smile", "will succeed", "is wondering", "is thinking", "is", "was", "will be", "might be", "was never"};

    private static String getRandomWord(String[] words) {
        return words[Misc.random(words.length - 1)];
    }


}
