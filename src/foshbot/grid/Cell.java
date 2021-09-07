package foshbot.grid;

import foshbot.Entity;

import java.util.Collection;
import java.util.HashSet;

class Cell {

    boolean[] walls = new boolean[4];

    Collection<Entity> entities = new HashSet<>();
}